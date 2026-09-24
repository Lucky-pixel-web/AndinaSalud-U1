package pe.edu.upeu.andinasalud.domain.usecase

import kotlinx.datetime.TimeZone
import pe.edu.upeu.andinasalud.domain.model.Cita
import pe.edu.upeu.andinasalud.domain.model.EstadoCita
import pe.edu.upeu.andinasalud.domain.model.ModalidadAtencion
import pe.edu.upeu.andinasalud.domain.repository.CitaRepository
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

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

        val validado = ValidacionesCita.validarFechaHora(fecha, hora, ahora, zonaHoraria)

        val errorMotivo = when {
            motivo.isBlank() -> "El motivo es obligatorio"
            motivo.trim().length !in Cita.MOTIVO_MIN..Cita.MOTIVO_MAX ->
                "El motivo debe tener entre ${Cita.MOTIVO_MIN} y ${Cita.MOTIVO_MAX} caracteres"
            else -> null
        }

        val errorModalidad = if (modalidad == null) "Selecciona una modalidad de atención" else null

        val errores = ErroresSolicitudCita(
            especialidad = errorEspecialidad,
            sede = errorSede,
            fecha = validado.errorFecha,
            hora = validado.errorHora,
            motivo = errorMotivo,
            modalidad = errorModalidad
        )
        if (errores.tieneErrores) throw SolicitudCitaInvalidaException(errores)

        val citasActuales = citaRepository.obtenerCitas()
        val programadas = citasActuales.filter { it.estado is EstadoCita.Programada }

        // RN-02: máximo 3 citas Programadas simultáneas.
        if (programadas.size >= LIMITE_PROGRAMADAS) {
            throw SolicitudCitaInvalidaException(
                ErroresSolicitudCita(fecha = "No puedes tener más de $LIMITE_PROGRAMADAS citas programadas a la vez")
            )
        }

        val yaExisteEnEseHorario = programadas.any {
            it.fecha == validado.fecha && it.hora == validado.hora
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
            fecha = validado.fecha!!,
            hora = validado.hora!!,
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