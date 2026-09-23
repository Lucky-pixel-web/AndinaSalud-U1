package pe.edu.upeu.andinasalud.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import pe.edu.upeu.andinasalud.data.local.CitasSimuladas
import pe.edu.upeu.andinasalud.domain.model.Cita
import pe.edu.upeu.andinasalud.domain.model.Medico
import pe.edu.upeu.andinasalud.domain.model.Paciente
import pe.edu.upeu.andinasalud.domain.model.Sede
import pe.edu.upeu.andinasalud.domain.repository.CitaRepository
import kotlin.random.Random

class CitaRepositoryFake : CitaRepository {

    private val mutex = Mutex()
    private val citas = CitasSimuladas.citasIniciales.toMutableList()
    private var siguienteId = citas.maxOf { it.id } + 1

    override suspend fun obtenerPaciente(): Paciente {
        simularRetardo()
        return CitasSimuladas.paciente
    }

    override suspend fun obtenerSedes(): List<Sede> {
        simularRetardo()
        return CitasSimuladas.sedes
    }

    override suspend fun obtenerEspecialidades(): List<String> {
        simularRetardo()
        return CitasSimuladas.especialidades
    }

    override suspend fun obtenerMedicos(): List<Medico> {
        simularRetardo()
        return CitasSimuladas.medicos
    }

    override suspend fun obtenerCitas(): List<Cita> {
        simularRetardo()
        return mutex.withLock { citas.toList() }
    }

    override suspend fun agregarCita(cita: Cita): Cita {
        simularRetardo()
        return mutex.withLock {
            val citaConId = cita.copy(id = siguienteId)
            siguienteId += 1
            citas.add(citaConId)
            citaConId
        }
    }

    override suspend fun actualizarCita(cita: Cita): Cita {
        simularRetardo()
        return mutex.withLock {
            val indice = citas.indexOfFirst { it.id == cita.id }
            if (indice == -1) throw NoSuchElementException("La cita ${cita.id} no existe")
            citas[indice] = cita
            cita
        }
    }

    /** RF-08: retardo simulado de 800 ms para mostrar el estado de carga. */
    private suspend fun simularRetardo() {
        delay(RETARDO_MS + Random.nextLong(-50, 50))
    }

    companion object {
        private const val RETARDO_MS = 800L
    }
}