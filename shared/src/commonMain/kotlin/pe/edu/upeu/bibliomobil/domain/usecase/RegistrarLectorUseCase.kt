package pe.edu.upeu.bibliomobil.domain.usecase

import pe.edu.upeu.bibliomobil.domain.model.Lector
import pe.edu.upeu.bibliomobil.domain.repository.LectorRepository

class RegistrarLectorUseCase(private val lectorRepository: LectorRepository) {

    suspend operator fun invoke(
        nombre: String,
        correo: String,
        telefono: String
    ): Result<Lector> = resultadoDe {
        val errorNombre = if (nombre.isBlank()) "El nombre es obligatorio" else null

        val errorCorreo = when {
            correo.isBlank() -> "El correo es obligatorio"
            !CORREO_REGEX.matches(correo.trim()) -> "El correo no tiene un formato válido"
            else -> null
        }

        val telefonoLimpio = telefono.trim().ifBlank { null }
        val errorTelefono = if (telefonoLimpio != null && !TELEFONO_REGEX.matches(telefonoLimpio)) {
            "El teléfono debe tener entre 6 y 9 dígitos"
        } else null

        val errores = ErroresDeLector(errorNombre, errorCorreo, errorTelefono)
        if (errores.tieneErrores) throw LectorInvalidoException(errores)

        val lector = Lector(
            id = 0L,
            nombre = nombre.trim(),
            correo = correo.trim(),
            telefono = telefonoLimpio
        )
        lectorRepository.registrar(lector)
    }

    companion object {
        private val CORREO_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        private val TELEFONO_REGEX = Regex("^\\d{6,9}$")
    }
}