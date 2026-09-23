package pe.edu.upeu.andinasalud.domain.model

data class Paciente(
    val id: String,
    val nombre: String,
    val documento: String,
    val correo: String,
    val telefono: String? = null
) {
    init {
        require(nombre.isNotBlank()) { "El nombre del paciente es obligatorio" }
        require(documento.isNotBlank()) { "El documento es obligatorio" }
        require(correo.isNotBlank()) { "El correo es obligatorio" }
    }
}