package pe.edu.upeu.andinasalud.domain.usecase

import pe.edu.upeu.andinasalud.domain.model.Cita
import pe.edu.upeu.andinasalud.domain.repository.CitaRepository

class ObtenerCitasUseCase(private val citaRepository: CitaRepository) {
    suspend operator fun invoke(): Result<List<Cita>> = resultadoDe {
        citaRepository.obtenerCitas().sortedBy { it.fechaHora }
    }
}