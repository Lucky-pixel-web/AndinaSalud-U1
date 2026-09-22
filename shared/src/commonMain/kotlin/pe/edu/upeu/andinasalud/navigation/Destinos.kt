package pe.edu.upeu.andinasalud.navigation

sealed class Screen {
    data object Inicio : Screen()
    data object Citas : Screen()
    data class Detalle(val citaId: Int) : Screen()
    data object Solicitud : Screen()
    data object Perfil : Screen()
}

data class Destino(val screen: Screen, val etiqueta: String)

val DESTINOS = listOf(
    Destino(Screen.Inicio, "Inicio"),
    Destino(Screen.Citas, "Citas"),
    Destino(Screen.Perfil, "Perfil")
)