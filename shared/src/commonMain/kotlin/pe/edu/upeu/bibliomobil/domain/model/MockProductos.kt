package pe.edu.upeu.bibliomobil.domain

import pe.edu.upeu.bibliomobil.domain.model.Producto

val productosMock = listOf(
    Producto(1, "Paracetamol", 15.50, 100, activo = true),
    Producto(2, "Ibuprofeno", 18.90, 50, activo = true),
    Producto(3, "Amoxicilina", 25.00, 5, activo = true),
    Producto(4, "Loratadina", 12.50, 0, activo = false),
    Producto(5, "Diclofenaco", 20.00, 3, activo = true)
)