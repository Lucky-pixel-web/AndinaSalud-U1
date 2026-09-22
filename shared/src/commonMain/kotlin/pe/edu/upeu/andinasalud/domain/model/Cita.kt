package pe.edu.upeu.andinasalud.domain.model

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.time.Duration.Companion.hours

data class Cita(
    val id: Int,
    val especialidad: String,
    val medico: String,
    val sede: String,
    val fecha: LocalDate,
    val hora: LocalTime,
    val motivo: String,
    val estado: EstadoCita
) {
    init {
        require(especialidad.isNotBlank()) { "La especialidad es obligatoria" }
        require(medico.isNotBlank()) { "El médico es obligatorio" }
        require(sede.isNotBlank()) { "La sede es obligatoria" }
        require(motivo.length in MOTIVO_MIN..MOTIVO_MAX) {
            "El motivo debe tener entre $MOTIVO_MIN y $MOTIVO_MAX caracteres"
        }
    }

    val fechaHora: LocalDateTime
        get() = LocalDateTime(fecha, hora)

    fun puedeCancelarse(
        ahora: Instant,
        zonaHoraria: TimeZone = TimeZone.currentSystemDefault()
    ): Boolean {
        if (estado !is EstadoCita.Programada) return false
        val instanteCita = fechaHora.toInstant(zonaHoraria)
        val tiempoRestante = instanteCita - ahora
        return tiempoRestante > HORAS_MINIMAS_CANCELACION.hours
    }

    companion object {
        const val MOTIVO_MIN = 10
        const val MOTIVO_MAX = 200
        const val HORAS_MINIMAS_CANCELACION = 24
    }
}