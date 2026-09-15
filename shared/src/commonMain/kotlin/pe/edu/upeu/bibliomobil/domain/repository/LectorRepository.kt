package pe.edu.upeu.bibliomobil.domain.repository

import pe.edu.upeu.bibliomobil.domain.model.Lector

/* Contrato para el registro y consulta de lectores de la biblioteca */
interface LectorRepository {
    suspend fun registrar(lector: Lector): Lector
    suspend fun listar(): List<Lector>
}