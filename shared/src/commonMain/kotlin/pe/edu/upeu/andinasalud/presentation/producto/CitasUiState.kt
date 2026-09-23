package pe.edu.upeu.andinasalud.presentation.citas

sealed interface FaseCitas {
    data object Cargando : FaseCitas
    data object SinCitas : FaseCitas
    data class ConCitas(val citas: List<CitaUi>) : FaseCitas
    data class Error(val mensaje: String) : FaseCitas
}

enum class FiltroEstado(val etiqueta: String) {
    TODAS("Todas"),
    PROGRAMADA("Programada"),
    ATENDIDA("Atendida"),
    CANCELADA("Cancelada")
}

data class CitasUiState(
    val fase: FaseCitas = FaseCitas.Cargando,
    val todasLasCitas: List<CitaUi> = emptyList(),
    val filtroEstado: FiltroEstado = FiltroEstado.TODAS,
    val textoBusqueda: String = ""
)