package pe.edu.upeu.bibliomobil.presentation.lector

sealed interface FaseLector {
    data object Cargando : FaseLector
    data object SinLectores : FaseLector
    data class ConLectores(val lectores: List<LectorUi>) : FaseLector
    data class Error(val mensaje: String) : FaseLector
}

data class LectorUiState(
    val fase: FaseLector = FaseLector.Cargando,
    val formulario: FormularioLector = FormularioLector(),
    val registrando: Boolean = false,
    val mensajeExito: String? = null
)