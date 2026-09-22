package pe.edu.upeu.andinasalud.data.local

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import pe.edu.upeu.andinasalud.domain.model.Cita
import pe.edu.upeu.andinasalud.domain.model.EstadoCita
import pe.edu.upeu.andinasalud.domain.model.Medico
import pe.edu.upeu.andinasalud.domain.model.Paciente
import pe.edu.upeu.andinasalud.domain.model.Sede

object CitasSimuladas {

    val sedes = listOf(
        Sede(1, "Ñaña"),
        Sede(2, "Chosica"),
        Sede(3, "Chaclacayo"),
        Sede(4, "Santa Anita")
    )

    val especialidades = listOf(
        "Medicina General", "Odontología", "Pediatría", "Nutrición", "Psicología"
    )

    val paciente = Paciente(
        id = "P-0417",
        nombre = "Lucía Quispe Mamani",
        documento = "70154823",
        correo = "lucia.quispe@correo.pe"
    )

    val medicos = listOf(
        Medico(1, "Dr. Iván Rojas", "Medicina General", listOf(sedes[0], sedes[1])),
        Medico(2, "Dra. Elena Castro", "Medicina General", listOf(sedes[2], sedes[3])),
        Medico(3, "Dra. Rosa Flores", "Odontología", listOf(sedes[1])),
        Medico(4, "Dr. Marco Salas", "Odontología", listOf(sedes[0], sedes[3])),
        Medico(5, "Dra. Carla Núñez", "Pediatría", listOf(sedes[2])),
        Medico(6, "Dr. Hugo Ramírez", "Pediatría", listOf(sedes[0])),
        Medico(7, "Lic. Ana Bermúdez", "Nutrición", listOf(sedes[3])),
        Medico(8, "Lic. Jorge Paredes", "Nutrición", listOf(sedes[1])),
        Medico(9, "Ps. Luis Tapia", "Psicología", listOf(sedes[0])),
        Medico(10, "Ps. Diana Vega", "Psicología", listOf(sedes[2]))
    )

    // Nota: ajusta estas fechas para que las "Programadas" queden siempre en el
    // futuro respecto al día real en que sustentes el examen (así lo pide el PDF).
    val citasIniciales = listOf(
        Cita(
            id = 1,
            especialidad = "Medicina General",
            medico = "Dr. Iván Rojas",
            sede = "Ñaña",
            fecha = LocalDate(2026, 10, 5),
            hora = LocalTime(9, 0),
            motivo = "Control general de presión arterial y chequeo de rutina",
            estado = EstadoCita.Programada(recordatorioActivo = true)
        ),
        Cita(
            id = 2,
            especialidad = "Odontología",
            medico = "Dra. Rosa Flores",
            sede = "Chosica",
            fecha = LocalDate(2026, 10, 8),
            hora = LocalTime(16, 30),
            motivo = "Revisión y limpieza dental programada",
            estado = EstadoCita.Programada(recordatorioActivo = false)
        ),
        Cita(
            id = 3,
            especialidad = "Nutrición",
            medico = "Lic. Ana Bermúdez",
            sede = "Santa Anita",
            fecha = LocalDate(2026, 10, 12),
            hora = LocalTime(11, 15),
            motivo = "Seguimiento de plan nutricional mensual",
            estado = EstadoCita.Programada(recordatorioActivo = true)
        ),
        Cita(
            id = 4,
            especialidad = "Pediatría",
            medico = "Dra. Carla Núñez",
            sede = "Chaclacayo",
            fecha = LocalDate(2026, 8, 30),
            hora = LocalTime(8, 45),
            motivo = "Control de crecimiento y desarrollo del niño",
            estado = EstadoCita.Atendida("Control en tres meses")
        ),
        Cita(
            id = 5,
            especialidad = "Psicología",
            medico = "Ps. Luis Tapia",
            sede = "Ñaña",
            fecha = LocalDate(2026, 9, 2),
            hora = LocalTime(15, 0),
            motivo = "Sesión de seguimiento terapéutico quincenal",
            estado = EstadoCita.Atendida("Continuar sesiones quincenales")
        ),
        Cita(
            id = 6,
            especialidad = "Medicina General",
            medico = "Dr. Iván Rojas",
            sede = "Chosica",
            fecha = LocalDate(2026, 9, 5),
            hora = LocalTime(10, 30),
            motivo = "Consulta general por síntomas gripales leves",
            estado = EstadoCita.Cancelada("Viaje del paciente", canceladaPorPaciente = true)
        )
    )
}