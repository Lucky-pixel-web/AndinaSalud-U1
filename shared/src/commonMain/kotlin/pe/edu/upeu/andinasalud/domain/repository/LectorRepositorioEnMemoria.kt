package pe.edu.upeu.andinasalud.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import pe.edu.upeu.andinasalud.domain.model.Lector
import pe.edu.upeu.andinasalud.domain.repository.LectorRepository
import kotlin.random.Random

class LectorRepositorioEnMemoria : LectorRepository {

    private val mutex = Mutex()
    private val lectores = mutableListOf<Lector>()
    private var siguienteId = 1L

    override suspend fun registrar(lector: Lector): Lector {
        delay(Random.nextLong(300, 800))
        return mutex.withLock {
            val lectorConId = lector.copy(id = siguienteId)
            siguienteId += 1
            lectores.add(lectorConId)
            lectorConId
        }
    }

    override suspend fun listar(): List<Lector> {
        delay(Random.nextLong(300, 800))
        return mutex.withLock { lectores.toList() }
    }
}