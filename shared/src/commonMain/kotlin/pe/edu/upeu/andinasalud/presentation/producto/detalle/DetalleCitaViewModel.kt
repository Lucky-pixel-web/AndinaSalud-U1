package pe.edu.upeu.andinasalud.presentation.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upeu.andinasalud.domain.usecase.CancelarCitaUseCase
import pe.edu.upeu.andinasalud.domain.usecase.ObtenerCitasUseCase
import pe.edu.upeu.andinasalud.presentation.citas.aUi
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.parameter.parametersOf
import pe.edu.upeu.andinasalud.presentation.detalle.DetalleCitaViewModel

class DetalleCitaViewModel(
    private val citaId: Int,
    private val obtenerCitas: ObtenerCitasUseCase,
    private val cancelarCita: CancelarCitaUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetalleCitaUiState())
    val uiState: StateFlow<DetalleCitaUiState> = _uiState.asStateFlow()

    val presentationModule = module {
        factoryOf(::CitasViewModel)
        factory { (citaId: Int) -> DetalleCitaViewModel(citaId, get(), get()) }
    }

    init { cargar() }

    fun cargar() {
        _uiState.value = _uiState.value.copy(fase = FaseDetalle.Cargando)
        viewModelScope.launch {
            obtenerCitas().fold(
                onSuccess = { citas ->
                    val cita = citas.firstOrNull { it.id == citaId }
                    _uiState.value = _uiState.value.copy(
                        fase = if (cita != null) FaseDetalle.Contenido(cita.aUi())
                        else FaseDetalle.Error("La cita no existe")
                    )
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(fase = FaseDetalle.Error("No se pudo cargar la cita"))
                }
            )
        }
    }

    fun onCancelarClick() {
        _uiState.value = _uiState.value.copy(mostrarDialogoConfirmacion = true)
    }

    fun onDescartarDialogo() {
        _uiState.value = _uiState.value.copy(mostrarDialogoConfirmacion = false)
    }

    fun onConfirmarCancelacion() {
        if (_uiState.value.cancelando) return
        _uiState.value = _uiState.value.copy(cancelando = true, mostrarDialogoConfirmacion = false)
        viewModelScope.launch {
            cancelarCita(citaId).fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(cancelando = false)
                    cargar()
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        cancelando = false,
                        mensajeError = error.message ?: "No se pudo cancelar la cita"
                    )
                }
            )
        }
    }
}