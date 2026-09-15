package pe.edu.upeu.bibliomobil.presentation.cliente

data class ClienteUi(
    val id: Long,
    val nombre: String,
    val correo: String,
    val telefono: String
)

fun Cliente.aUi(): ClienteUi = ClienteUi(
    id = id,
    nombre = nombre,
    correo = correo,
    telefono = telefono ?: TELEFONO_AUSENTE
)

const val TELEFONO_AUSENTE = "No registrado"