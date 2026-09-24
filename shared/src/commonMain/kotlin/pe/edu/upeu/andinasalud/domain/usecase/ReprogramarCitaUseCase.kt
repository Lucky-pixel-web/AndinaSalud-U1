package pe.edu.upeu.andinasalud.domain.usecase

import kotlinx.datetime.TimeZone
import pe.edu.upeu.andinasalud.domain.model.Cita
import pe.edu.upeu.andinasalud.domain.model.EstadoCita
import pe.edu.upeu.andinasalud.domain.repository.CitaRepository
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class ReprogramarCitaInvalidaException(
    val errorFecha: String?,
    val errorHora: String?
) : Exception("Fecha u hora inválida para reprogramar")

@OptIn(ExperimentalTime::class)
class ReprogramarCitaUseCase(private val citaRepository: CitaRepository) {

    suspend operator fun invoke(
        citaId: Int,
        nuevaFecha: String,
        nuevaHora: String,
        ahora: Instant = Clock.System.now(),
        zonaHoraria: TimeZone = TimeZone.currentSystemDefault()
    ): Result<Cita> = resultadoDe {

        val validado = ValidacionesCita.validarFechaHora(nuevaFecha, nuevaHora, ahora, zonaHoraria)
        if (validado.errorFecha != null || validado.errorHora != null) {
            throw ReprogramarCitaInvalidaException(validado.errorFecha, validado.errorHora)
        }

        val todasLasCitas = citaRepository.obtenerCitas()
        val cita = todasLasCitas.firstOrNull { it.id == citaId }
            ?: throw NoSuchElementException("La cita no existe")

        if (cita.estado !is EstadoCita.Programada) {
            throw ReprogramarCitaInvalidaException("Solo puede reprogramarse una cita Programada", null)
        }

        val choqueDeHorario = todasLasCitas.any {
            it.id != citaId &&
                    it.estado is EstadoCita.Programada &&
                    it.fecha == validado.fecha &&
                    it.hora == validado.hora
        }
        if (choqueDeHorario) {
            throw ReprogramarCitaInvalidaException(null, "Ya tienes una cita programada en esa fecha y hora")
        }

        val citaActualizada = cita.copy(fecha = validado.fecha!!, hora = validado.hora!!)
        citaRepository.actualizarCita(citaActualizada)
    }
}