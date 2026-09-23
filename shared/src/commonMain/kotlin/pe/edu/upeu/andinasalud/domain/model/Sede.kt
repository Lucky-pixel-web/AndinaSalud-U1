package pe.edu.upeu.andinasalud.domain.model

data class Sede(
    val id: Int,
    val nombre: String
) {
    init {
        require(nombre.isNotBlank()) { "El nombre de la sede es obligatorio" }
    }
}