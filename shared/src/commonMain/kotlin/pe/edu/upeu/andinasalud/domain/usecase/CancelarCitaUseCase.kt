package pe.edu.upeu.andinasalud.domain.usecase

import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import pe.edu.upeu.andinasalud.domain.model.Cita
import pe.edu.upeu.andinasalud.domain.model.EstadoCita
import pe.edu.upeu.andinasalud.domain.repository.CitaRepository

class CitaNoCancelableException(mensaje: String) : Exception(mensaje)

class CancelarCitaUseCase(private val citaRepository: CitaRepository) {

    suspend operator fun invoke(
        citaId: Int,
        ahora: Instant = Clock.System.now()
    ): Result<Cita> = resultadoDe {
        val cita = citaRepository.obtenerCitas().firstOrNull { it.id == citaId }
            ?: throw NoSuchElementException("La cita no existe")

        if (!cita.puedeCancelarse(ahora)) {
            throw CitaNoCancelableException(
                "La cita solo puede cancelarse si está programada y faltan más de 24 horas"
            )
        }

        val citaCancelada = cita.copy(
            estado = EstadoCita.Cancelada(
                motivo = "Cancelada por el paciente",
                canceladaPorPaciente = true
            )
        )
        citaRepository.actualizarCita(citaCancelada)
    }
}