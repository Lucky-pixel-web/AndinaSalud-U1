package pe.edu.upeu.andinasalud.presentation.citas

import kotlinx.datetime.LocalDate
import pe.edu.upeu.andinasalud.domain.model.Cita
import pe.edu.upeu.andinasalud.domain.model.EstadoCita
import pe.edu.upeu.andinasalud.domain.model.ModalidadAtencion

data class CitaUi(
    val id: Int,
    val especialidad: String,
    val medico: String,
    val sede: String,
    val fecha: LocalDate,
    val fechaTexto: String,
    val horaTexto: String,
    val estadoTexto: String,
    val modalidad: ModalidadAtencion,
    val estado: EstadoCita
)

fun Cita.aUi(): CitaUi {
    val estadoTexto = when (estado) {
        is EstadoCita.Programada -> "Programada"
        is EstadoCita.Atendida -> "Atendida"
        is EstadoCita.Cancelada -> "Cancelada"
    }
    return CitaUi(
        id = id, especialidad = especialidad, medico = medico, sede = sede,
        fecha = fecha,
        fechaTexto = "...", horaTexto = "...",
        estadoTexto = estadoTexto,
        modalidad = modalidad,
        estado = estado
    )
}