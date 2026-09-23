package pe.edu.upeu.andinasalud.domain.usecase

data class ErroresSolicitudCita(
    val especialidad: String? = null,
    val sede: String? = null,
    val fecha: String? = null,
    val hora: String? = null,
    val motivo: String? = null,
    val modalidad: String? = null
) {
    val tieneErrores: Boolean
        get() = listOfNotNull(especialidad, sede, fecha, hora, motivo, modalidad).isNotEmpty()
}

class SolicitudCitaInvalidaException(val errores: ErroresSolicitudCita) :
    Exception("Datos de la solicitud de cita inválidos")