package pe.edu.upeu.bibliomobil.domain.usecase

import pe.edu.upeu.bibliomobil.domain.model.Libro
import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository

class ListarLibrosUseCase(private val libroRepository: LibroRepository) {
    suspend operator fun invoke(): Result<List<Libro>> = resultadoDe {
        libroRepository.listar()
    }
}