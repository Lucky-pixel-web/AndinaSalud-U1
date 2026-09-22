package pe.edu.upeu.andinasalud.presentation.libro

sealed interface Fase {
    data object Cargando : Fase
    data object SinLibros : Fase
    data class ConLibros(val libros: List<LibroUi>) : Fase
    data class Error(val mensaje: String) : Fase
}

data class LibroUiState(
    val fase: Fase = Fase.Cargando,
    val formulario: FormularioLibro = FormularioLibro(),
    val registrando: Boolean = false,
    val mensajeExito: String? = null
)