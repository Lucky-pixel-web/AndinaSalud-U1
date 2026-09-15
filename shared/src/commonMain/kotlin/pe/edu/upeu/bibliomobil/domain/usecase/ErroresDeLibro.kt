package pe.edu.upeu.bibliomobil.domain.usecase

data class ErroresDeLibro(
    val titulo: String? = null,
    val autor: String? = null,
    val anio: String? = null,
    val ejemplares: String? = null
) {
    val tieneErrores: Boolean
        get() = titulo != null || autor != null || anio != null || ejemplares != null
}

class LibroInvalidoException(val errores: ErroresDeLibro) : Exception("Datos del libro inválidos")