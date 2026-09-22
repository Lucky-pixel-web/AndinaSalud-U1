package pe.edu.upeu.andinasalud.presentation.lector

import pe.edu.upeu.andinasalud.domain.model.Lector

data class LectorUi(
    val id: Long,
    val nombre: String,
    val correo: String,
    val telefono: String
)

fun Lector.aUi(): LectorUi = LectorUi(
    id = id,
    nombre = nombre,
    correo = correo,
    telefono = telefono ?: "No registrado"
)