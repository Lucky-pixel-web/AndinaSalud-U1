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
        // Reutiliza la MISMA validación de fecha/hora que usa SolicitarCitaUseCase.
        val validado = ValidacionesCita.validarFechaHora(nuevaFecha, nuevaHora, ahora, zonaHoraria)
        if (validado.errorFecha != null || validado.errorHora != null) {
            throw ReprogramarCitaInvalidaException(validado.errorFecha, validado.errorHora)
        }

        val cita = citaRepository.obtenerCitas().firstOrNull { it.id == citaId }
            ?: throw NoSuchElementException("La cita no existe")

        if (cita.estado !is EstadoCita.Programada) {
            throw ReprogramarCitaInvalidaException("Solo puede reprogramarse una cita Programada", null)
        }

        val citaActualizada = cita.copy(fecha = validado.fecha!!, hora = validado.hora!!)
        citaRepository.actualizarCita(citaActualizada)
    }
}