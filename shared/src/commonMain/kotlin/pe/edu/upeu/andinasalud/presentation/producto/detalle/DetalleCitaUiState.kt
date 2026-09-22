package pe.edu.upeu.andinasalud.presentation.detalle

import pe.edu.upeu.andinasalud.presentation.citas.CitaUi

sealed interface FaseDetalle {
    data object Cargando : FaseDetalle
    data class Contenido(val cita: CitaUi) : FaseDetalle
    data class Error(val mensaje: String) : FaseDetalle
}

data class DetalleCitaUiState(
    val fase: FaseDetalle = FaseDetalle.Cargando,
    val mostrarDialogoConfirmacion: Boolean = false,
    val cancelando: Boolean = false,
    val mensajeError: String? = null
)