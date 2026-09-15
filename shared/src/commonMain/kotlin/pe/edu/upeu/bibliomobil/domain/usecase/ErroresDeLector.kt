package pe.edu.upeu.bibliomobil.domain.usecase

data class ErroresDeLector(
    val nombre: String? = null,
    val correo: String? = null,
    val telefono: String? = null
) {
    val tieneErrores: Boolean
        get() = nombre != null || correo != null || telefono != null
}

class LectorInvalidoException(val errores: ErroresDeLector) : Exception("Datos del lector inválidos")