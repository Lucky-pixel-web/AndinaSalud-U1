package pe.edu.upeu.andinasalud.domain.usecase

import pe.edu.upeu.andinasalud.domain.model.Libro
import pe.edu.upeu.andinasalud.domain.repository.LibroRepository

class ListarLibrosUseCase(private val libroRepository: LibroRepository) {
    suspend operator fun invoke(): Result<List<Libro>> = resultadoDe {
        libroRepository.listar()
    }
}