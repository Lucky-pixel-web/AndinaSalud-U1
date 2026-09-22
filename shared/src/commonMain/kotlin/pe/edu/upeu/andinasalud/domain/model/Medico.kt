package pe.edu.upeu.andinasalud.domain.model

data class Medico(
    val id: Int,
    val nombre: String,
    val especialidad: String,
    val sedes: List<Sede>
) {
    init {
        require(nombre.isNotBlank()) { "El nombre del médico es obligatorio" }
        require(especialidad.isNotBlank()) { "La especialidad es obligatoria" }
        require(sedes.isNotEmpty()) { "El médico debe estar asignado al menos a una sede" }
    }
}