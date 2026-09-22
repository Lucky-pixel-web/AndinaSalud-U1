package pe.edu.upeu.andinasalud.presentation.libro

data class FormularioLibro(
    val titulo: String = "",
    val autor: String = "",
    val anio: String = "",
    val ejemplares: String = "",
    val tituloError: String? = null,
    val autorError: String? = null,
    val anioError: String? = null,
    val ejemplaresError: String? = null
)