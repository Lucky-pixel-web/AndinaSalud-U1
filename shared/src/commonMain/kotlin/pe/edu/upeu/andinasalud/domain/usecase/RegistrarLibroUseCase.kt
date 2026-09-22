package pe.edu.upeu.andinasalud.domain.usecase

import pe.edu.upeu.andinasalud.domain.model.Libro
import pe.edu.upeu.andinasalud.domain.repository.LibroRepository

class RegistrarLibroUseCase(private val libroRepository: LibroRepository) {

    suspend operator fun invoke(
        titulo: String,
        autor: String,
        anio: String,
        ejemplares: String
    ): Result<Libro> = resultadoDe {
        val errorTitulo = if (titulo.isBlank()) "El título es obligatorio" else null
        val errorAutor = if (autor.isBlank()) "El autor es obligatorio" else null

        val errorAnio = when {
            anio.isBlank() -> "El año es obligatorio"
            anio.trim().toIntOrNull() == null -> "El año debe ser un número entero"
            anio.trim().toInt() !in Libro.ANIO_MINIMO..Libro.ANIO_MAXIMO ->
                "El año debe estar entre 1450 y 2026"
            else -> null
        }

        val errorEjemplares = when {
            ejemplares.isBlank() -> "Los ejemplares son obligatorios"
            ejemplares.trim().toIntOrNull() == null -> "Los ejemplares deben ser un número entero"
            ejemplares.trim().toInt() < 0 -> "Los ejemplares no pueden ser negativos"
            else -> null
        }

        val errores = ErroresDeLibro(errorTitulo, errorAutor, errorAnio, errorEjemplares)
        if (errores.tieneErrores) throw LibroInvalidoException(errores)

        val libro = Libro(
            id = 0L,
            titulo = titulo.trim(),
            autor = autor.trim(),
            anio = anio.trim().toInt(),
            ejemplares = ejemplares.trim().toInt()
        )
        libroRepository.registrar(libro)
    }
}