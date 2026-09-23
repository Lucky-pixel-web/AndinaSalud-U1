package pe.edu.upeu.andinasalud.presentation.citas

import kotlinx.datetime.LocalDate
import pe.edu.upeu.andinasalud.domain.model.Cita
import pe.edu.upeu.andinasalud.domain.model.EstadoCita

data class CitaUi(
    val id: Int,
    val especialidad: String,
    val medico: String,
    val sede: String,
    val fecha: LocalDate,
    val fechaTexto: String,
    val horaTexto: String,
    val estadoTexto: String,
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
        fechaTexto = "${fecha.dayOfMonth.toString().padStart(2, '0')}/${fecha.monthNumber.toString().padStart(2, '0')}/${fecha.year}",
        horaTexto = "${hora.hour.toString().padStart(2, '0')}:${hora.minute.toString().padStart(2, '0')}",
        estadoTexto = estadoTexto,
        estado = estado
    )
}