package pe.edu.upeu.bibliomobil.presentation.lector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upeu.bibliomobil.domain.usecase.LectorInvalidoException
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLectoresUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLectorUseCase

class LectorViewModel(
    private val registrarLector: RegistrarLectorUseCase,
    private val listarLectores: ListarLectoresUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LectorUiState())
    val uiState: StateFlow<LectorUiState> = _uiState.asStateFlow()

    init {
        cargarLectores()
    }

    fun cargarLectores() {
        _uiState.value = _uiState.value.copy(fase = FaseLector.Cargando)
        viewModelScope.launch {
            listarLectores().fold(
                onSuccess = { lectores ->
                    val fase = if (lectores.isEmpty()) FaseLector.SinLectores
                    else FaseLector.ConLectores(lectores.map { it.aUi() })
                    _uiState.value = _uiState.value.copy(fase = fase)
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(
                        fase = FaseLector.Error("No se pudo cargar la cartera de lectores")
                    )
                }
            )
        }
    }

    fun onNombreChange(valor: String) {
        _uiState.value = _uiState.value.copy(
            formulario = _uiState.value.formulario.copy(nombre = valor, nombreError = null)
        )
    }

    fun onCorreoChange(valor: String) {
        _uiState.value = _uiState.value.copy(
            formulario = _uiState.value.formulario.copy(correo = valor, correoError = null)
        )
    }

    fun onTelefonoChange(valor: String) {
        _uiState.value = _uiState.value.copy(
            formulario = _uiState.value.formulario.copy(telefono = valor, telefonoError = null)
        )
    }

    fun registrar() {
        if (_uiState.value.registrando) return
        _uiState.value = _uiState.value.copy(registrando = true)

        viewModelScope.launch {
            val formulario = _uiState.value.formulario
            registrarLector(
                nombre = formulario.nombre,
                correo = formulario.correo,
                telefono = formulario.telefono
            ).fold(
                onSuccess = { lector ->
                    _uiState.value = _uiState.value.copy(
                        registrando = false,
                        formulario = FormularioLector(),
                        mensajeExito = "Lector \"${lector.nombre}\" registrado correctamente"
                    )
                    cargarLectores()
                },
                onFailure = { error ->
                    val errores = (error as? LectorInvalidoException)?.errores
                    _uiState.value = _uiState.value.copy(
                        registrando = false,
                        formulario = _uiState.value.formulario.copy(
                            nombreError = errores?.nombre,
                            correoError = errores?.correo,
                            telefonoError = errores?.telefono
                        )
                    )
                }
            )
        }
    }
}