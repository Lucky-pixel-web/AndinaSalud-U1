package pe.edu.upeu.andinasalud.domain.usecase

import pe.edu.upeu.andinasalud.domain.model.Lector
import pe.edu.upeu.andinasalud.domain.repository.LectorRepository

class ListarLectoresUseCase(private val lectorRepository: LectorRepository) {
    suspend operator fun invoke(): Result<List<Lector>> = resultadoDe {
        lectorRepository.listar()
    }
}