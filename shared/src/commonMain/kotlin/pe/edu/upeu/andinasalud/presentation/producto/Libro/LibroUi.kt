package pe.edu.upeu.andinasalud.presentation.libro

import pe.edu.upeu.andinasalud.domain.model.Libro

data class LibroUi(
    val id: Long,
    val titulo: String,
    val autor: String,
    val lineaSecundaria: String,
    val requiereReposicion: Boolean
)

fun Libro.aUi(): LibroUi {
    val sufijo = if (ejemplares == 1) "ejemplar" else "ejemplares"
    return LibroUi(
        id = id,
        titulo = titulo,
        autor = autor,
        lineaSecundaria = "$anio · $ejemplares $sufijo",
        requiereReposicion = requiereReposicion
    )
}