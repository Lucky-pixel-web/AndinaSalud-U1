package pe.edu.upeu.andinasalud.domain.usecase

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.time.Instant

data class FechaHoraValidada(
    val fecha: LocalDate?,
    val hora: LocalTime?,
    val errorFecha: String?,
    val errorHora: String?
)

object ValidacionesCita {
    fun validarFechaHora(
        fechaTexto: String,
        horaTexto: String,
        ahora: Instant,
        zonaHoraria: TimeZone
    ): FechaHoraValidada {
        val fechaParseada = runCatching { LocalDate.parse(fechaTexto.trim()) }.getOrNull()
        val horaParseada = runCatching { LocalTime.parse(horaTexto.trim()) }.getOrNull()

        val errorFechaFormato = when {
            fechaTexto.isBlank() -> "La fecha es obligatoria"
            fechaParseada == null -> "La fecha no tiene un formato válido"
            else -> null
        }
        val errorHora = when {
            horaTexto.isBlank() -> "La hora es obligatoria"
            horaParseada == null -> "La hora no tiene un formato válido"
            else -> null
        }

        var errorRN01: String? = null
        if (fechaParseada != null && horaParseada != null) {
            val instanteSolicitado = LocalDateTime(fechaParseada, horaParseada).toInstant(zonaHoraria)
            if (instanteSolicitado <= ahora) {
                errorRN01 = "No se puede programar una cita en una fecha u hora anterior al momento actual"
            }
        }

        return FechaHoraValidada(
            fecha = fechaParseada,
            hora = horaParseada,
            errorFecha = errorFechaFormato ?: errorRN01,
            errorHora = errorHora
        )
    }
}