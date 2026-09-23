package pe.edu.upeu.andinasalud.domain.usecase

import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import pe.edu.upeu.andinasalud.domain.model.Cita
import pe.edu.upeu.andinasalud.domain.model.EstadoCita
import pe.edu.upeu.andinasalud.domain.repository.CitaRepository
import pe.edu.upeu.andinasalud.domain.model.ModalidadAtencion

@OptIn(ExperimentalTime::class)
class SolicitarCitaUseCase(private val citaRepository: CitaRepository) {

    suspend operator fun invoke(
        especialidad: String,
        sede: String,
        fecha: String,
        hora: String,
        motivo: String,
        modalidad: ModalidadAtencion?,
        ahora: Instant = Clock.System.now(),
        zonaHoraria: TimeZone = TimeZone.currentSystemDefault()
    ): Result<Cita> = resultadoDe {

        val errorEspecialidad = if (especialidad.isBlank()) "La especialidad es obligatoria" else null
        val errorSede = if (sede.isBlank()) "La sede es obligatoria" else null

        val fechaParseada = runCatching { LocalDate.parse(fecha.trim()) }.getOrNull()
        val horaParseada = runCatching { LocalTime.parse(hora.trim()) }.getOrNull()

        val errorFecha = when {
            fecha.isBlank() -> "La fecha es obligatoria"
            fechaParseada == null -> "La fecha no tiene un formato válido"
            else -> null
        }
        val errorHora = when {
            hora.isBlank() -> "La hora es obligatoria"
            horaParseada == null -> "La hora no tiene un formato válido"
            else -> null
        }

        val errorMotivo = when {
            motivo.isBlank() -> "El motivo es obligatorio"
            motivo.trim().length !in Cita.MOTIVO_MIN..Cita.MOTIVO_MAX ->
                "El motivo debe tener entre ${Cita.MOTIVO_MIN} y ${Cita.MOTIVO_MAX} caracteres"
            else -> null
        }

        var errorRN01: String? = null
        if (fechaParseada != null && horaParseada != null) {
            val instanteSolicitado = LocalDateTime(fechaParseada, horaParseada).toInstant(zonaHoraria)
            if (instanteSolicitado <= ahora) {
                errorRN01 = "No se puede solicitar una cita en una fecha u hora anterior al momento actual"
            }
        }

        val errorModalidad = if (modalidad == null) "Selecciona una modalidad de atención" else null

        val errores = ErroresSolicitudCita(
            especialidad = errorEspecialidad,
            sede = errorSede,
            fecha = errorFecha ?: errorRN01,
            hora = errorHora,
            motivo = errorMotivo,
            modalidad = errorModalidad
        )
        if (errores.tieneErrores) throw SolicitudCitaInvalidaException(errores)

        val citasActuales = citaRepository.obtenerCitas()
        val programadas = citasActuales.filter { it.estado is EstadoCita.Programada }

        if (programadas.size >= LIMITE_PROGRAMADAS) {
            throw SolicitudCitaInvalidaException(
                ErroresSolicitudCita(
                    fecha = "No puedes tener más de $LIMITE_PROGRAMADAS citas programadas a la vez"
                )
            )
        }

        val yaExisteEnEseHorario = programadas.any {
            it.fecha == fechaParseada && it.hora == horaParseada
        }
        if (yaExisteEnEseHorario) {
            throw SolicitudCitaInvalidaException(
                ErroresSolicitudCita(hora = "Ya tienes una cita programada en esa fecha y hora")
            )
        }

        val medicoAsignado = citaRepository.obtenerMedicos()
            .firstOrNull { it.especialidad.equals(especialidad.trim(), ignoreCase = true) }
            ?.nombre ?: "Por asignar"

        val nuevaCita = Cita(
            id = 0,
            especialidad = especialidad.trim(),
            medico = medicoAsignado,
            sede = sede.trim(),
            fecha = fechaParseada!!,
            hora = horaParseada!!,
            motivo = motivo.trim(),
            modalidad = modalidad!!,
            estado = EstadoCita.Programada(recordatorioActivo = true)
        )
        citaRepository.agregarCita(nuevaCita)
    }

    companion object {
        const val LIMITE_PROGRAMADAS = 3
    }
}