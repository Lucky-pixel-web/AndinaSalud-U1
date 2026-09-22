package pe.edu.upeu.andinasalud.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import pe.edu.upeu.andinasalud.domain.model.Libro
import pe.edu.upeu.andinasalud.domain.repository.LibroRepository
import kotlin.random.Random

class LibroRepositorioEnMemoria : LibroRepository {

    private val mutex = Mutex()
    private val libros = mutableListOf<Libro>()
    private var siguienteId = 1L

    override suspend fun registrar(libro: Libro): Libro {
        delay(Random.nextLong(300, 800))
        return mutex.withLock {
            val libroConId = libro.copy(id = siguienteId)
            siguienteId += 1
            libros.add(libroConId)
            libroConId
        }
    }

    override suspend fun listar(): List<Libro> {
        delay(Random.nextLong(300, 800))
        return mutex.withLock { libros.toList() }
    }
}