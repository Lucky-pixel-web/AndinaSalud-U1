package pe.edu.upeu.bibliomobil.domain.repository

import pe.edu.upeu.bibliomobil.domain.model.Libro

/**Contrato para el registro y consulta de libros del catálogo de la biblioteca */
interface LibroRepository {
    suspend fun registrar(libro: Libro): Libro
    suspend fun listar(): List<Libro>
}