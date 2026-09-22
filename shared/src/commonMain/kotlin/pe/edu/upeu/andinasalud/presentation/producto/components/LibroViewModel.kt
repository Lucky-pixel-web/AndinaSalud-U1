package pe.edu.upeu.andinasalud.presentation.libro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upeu.andinasalud.domain.usecase.LibroInvalidoException
import pe.edu.upeu.andinasalud.domain.usecase.ListarLibrosUseCase
import pe.edu.upeu.andinasalud.domain.usecase.RegistrarLibroUseCase

class LibroViewModel(
    private val registrarLibro: RegistrarLibroUseCase,
    private val listarLibros: ListarLibrosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LibroUiState())
    val uiState: StateFlow<LibroUiState> = _uiState.asStateFlow()

    init {
        cargarLibros()
    }

    fun cargarLibros() {
        _uiState.value = _uiState.value.copy(fase = Fase.Cargando)
        viewModelScope.launch {
            listarLibros().fold(
                onSuccess = { libros ->
                    val fase = if (libros.isEmpty()) Fase.SinLibros
                    else Fase.ConLibros(libros.map { it.aUi() })
                    _uiState.value = _uiState.value.copy(fase = fase)
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(
                        fase = Fase.Error("No se pudo cargar el catálogo")
                    )
                }
            )
        }
    }

    fun onTituloChange(valor: String) {
        _uiState.value = _uiState.value.copy(
            formulario = _uiState.value.formulario.copy(titulo = valor, tituloError = null)
        )
    }

    fun onAutorChange(valor: String) {
        _uiState.value = _uiState.value.copy(
            formulario = _uiState.value.formulario.copy(autor = valor, autorError = null)
        )
    }

    fun onAnioChange(valor: String) {
        _uiState.value = _uiState.value.copy(
            formulario = _uiState.value.formulario.copy(anio = valor, anioError = null)
        )
    }

    fun onEjemplaresChange(valor: String) {
        _uiState.value = _uiState.value.copy(
            formulario = _uiState.value.formulario.copy(ejemplares = valor, ejemplaresError = null)
        )
    }

    fun registrar() {
        if (_uiState.value.registrando) return
        _uiState.value = _uiState.value.copy(registrando = true)

        viewModelScope.launch {
            val formulario = _uiState.value.formulario
            registrarLibro(
                titulo = formulario.titulo,
                autor = formulario.autor,
                anio = formulario.anio,
                ejemplares = formulario.ejemplares
            ).fold(
                onSuccess = { libro ->
                    _uiState.value = _uiState.value.copy(
                        registrando = false,
                        formulario = FormularioLibro(),
                        mensajeExito = "Libro \"${libro.titulo}\" registrado correctamente"
                    )
                    cargarLibros()
                },
                onFailure = { error ->
                    val errores = (error as? LibroInvalidoException)?.errores
                    _uiState.value = _uiState.value.copy(
                        registrando = false,
                        formulario = _uiState.value.formulario.copy(
                            tituloError = errores?.titulo,
                            autorError = errores?.autor,
                            anioError = errores?.anio,
                            ejemplaresError = errores?.ejemplares
                        )
                    )
                }
            )
        }
    }
}