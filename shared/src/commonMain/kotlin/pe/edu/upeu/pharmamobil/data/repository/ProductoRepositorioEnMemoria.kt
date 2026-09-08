package pe.edu.upeu.pharmamobil.data.repository

import kotlinx.coroutines.delay
import pe.edu.upeu.pharmamobil.domain.model.Producto
import pe.edu.upeu.pharmamobil.domain.repository.ProductoRepository
import kotlin.random.Random

class ProductoRepositorioEnMemoria : ProductoRepository {

    private val productos = mutableListOf(
        Producto(
            id = 1L,
            nombre = "Paracetamol",
            precio = 8.50,
            stock = 100
        ),
        Producto(
            id = 2L,
            nombre = "Ibuprofeno",
            precio = 12.00,
            stock = 50
        ),
        Producto(
            id = 3L,
            nombre = "Amoxicilina",
            precio = 18.50,
            stock = 20
        )
    )

    override suspend fun registrar(producto: Producto): Producto {
        delay(Random.nextLong(300, 800))

        val productoConId = producto.copy(
            id = Random.nextLong(1, Long.MAX_VALUE)
        )

        productos.add(0, productoConId)
        return productoConId
    }

    override suspend fun listar(): List<Producto> {
        delay(Random.nextLong(300, 800))
        return productos.toList()
    }
}