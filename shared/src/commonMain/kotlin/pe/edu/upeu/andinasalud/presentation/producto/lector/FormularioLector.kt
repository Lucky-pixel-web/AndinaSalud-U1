package pe.edu.upeu.andinasalud.presentation.lector

data class FormularioLector(
    val nombre: String = "",
    val correo: String = "",
    val telefono: String = "",
    val nombreError: String? = null,
    val correoError: String? = null,
    val telefonoError: String? = null
)