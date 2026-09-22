package pe.edu.upeu.andinasalud.navigation

sealed class Screen {

    data object Inicio : Screen()

    data object Productos : Screen()

    data object Clientes : Screen()

    data object Pedidos : Screen()

}