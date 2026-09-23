package pe.edu.upeu.andinasalud.domain.usecase

import pe.edu.upeu.andinasalud.domain.model.EstadoCita
import pe.edu.upeu.andinasalud.domain.repository.CitaRepository

class ContarCitasProgramadasUseCase(private val citaRepository: CitaRepository) {
    suspend operator fun invoke(): Result<Int> = resultadoDe {
        citaRepository.obtenerCitas().count { it.estado is EstadoCita.Programada }
    }
}