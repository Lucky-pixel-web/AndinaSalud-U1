package pe.edu.upeu.andinasalud.domain.repository

import pe.edu.upeu.andinasalud.domain.model.Cita
import pe.edu.upeu.andinasalud.domain.model.Medico
import pe.edu.upeu.andinasalud.domain.model.Paciente
import pe.edu.upeu.andinasalud.domain.model.Sede

interface CitaRepository {
    suspend fun obtenerPaciente(): Paciente
    suspend fun obtenerSedes(): List<Sede>
    suspend fun obtenerEspecialidades(): List<String>
    suspend fun obtenerMedicos(): List<Medico>
    suspend fun obtenerCitas(): List<Cita>
    suspend fun agregarCita(cita: Cita): Cita
    suspend fun actualizarCita(cita: Cita): Cita
}