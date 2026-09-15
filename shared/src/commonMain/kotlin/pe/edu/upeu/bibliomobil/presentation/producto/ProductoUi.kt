package pe.edu.upeu.bibliomobil.presentation.producto

import kotlin.math.roundToLong
import pe.edu.upeu.bibliomobil.domain.model.Producto

data class ProductoUi(
    val id: Long,
    val nombre: String,
    val precio: String,
    val stock: String,
    val activo: Boolean,
    val esBajoStock: Boolean,
    val requiereReposicion: Boolean
)

fun Producto.aUi(): ProductoUi = ProductoUi(
    id = id,
    nombre = nombre,
    precio = precio.enSoles(),
    stock = "$stock u.",
    activo = activo,
    esBajoStock = esBajoStock(),
    requiereReposicion = requiereReposicion()
)

/** Kotlin que no trae String.format */
private fun Double.enSoles(): String {
    val centavos = (this * 100).roundToLong()
    val enteros = centavos / 100
    val decimales = (centavos % 100).toString().padStart(2, '0')
    return "S/ $enteros.$decimales"
}