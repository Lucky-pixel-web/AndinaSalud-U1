package pe.edu.upeu.andinasalud

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import pe.edu.upeu.andinasalud.navigation.Screen
import pe.edu.upeu.andinasalud.presentation.citas.CitasScreen
import pe.edu.upeu.andinasalud.presentation.citas.CitasViewModel
import pe.edu.upeu.andinasalud.presentation.detalle.DetalleCitaScreen
import pe.edu.upeu.andinasalud.presentation.detalle.DetalleCitaViewModel
import pe.edu.upeu.andinasalud.presentation.inicio.InicioScreen
import pe.edu.upeu.andinasalud.presentation.inicio.InicioViewModel
import pe.edu.upeu.andinasalud.presentation.perfil.PerfilScreen
import pe.edu.upeu.andinasalud.presentation.solicitud.SolicitudScreen
import pe.edu.upeu.andinasalud.presentation.solicitud.SolicitudViewModel
import pe.edu.upeu.andinasalud.presentation.theme.AndinaSaludTheme

@Composable
fun App() = KoinContext {
    var darkTheme by rememberSaveable { mutableStateOf(false) }
    // Pila simple: el tope es la pantalla actual; back saca el tope.
    val pila = remember { mutableStateListOf<Screen>(Screen.Inicio) }
    val actual = pila.last()

    fun navegar(destino: Screen) { pila.add(destino) }
    fun atras() { if (pila.size > 1) pila.removeAt(pila.lastIndex) }
    fun irATab(destino: Screen) { pila.clear(); pila.add(destino) }

    AndinaSaludTheme(darkTheme = darkTheme) {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        selected = actual is Screen.Inicio,
                        onClick = { irATab(Screen.Inicio) },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                        label = { Text("Inicio") }
                    )
                    NavigationBarItem(
                        selected = actual is Screen.Citas,
                        onClick = { irATab(Screen.Citas) },
                        icon = { Icon(Icons.Default.CalendarMonth, contentDescription = "Citas") },
                        label = { Text("Citas") }
                    )
                    NavigationBarItem(
                        selected = actual is Screen.Perfil,
                        onClick = { irATab(Screen.Perfil) },
                        icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                        label = { Text("Perfil") }
                    )
                }
            }
        ) { padding ->
            when (val pantalla = actual) {
                is Screen.Inicio -> InicioScreen(
                    viewModel = koinViewModel<InicioViewModel>(),
                    onVerCitas = { irATab(Screen.Citas) },
                    onSolicitarCita = { navegar(Screen.Solicitud) },
                    modifier = Modifier.padding(padding)
                )
                is Screen.Citas -> CitasScreen(
                    viewModel = koinViewModel<CitasViewModel>(),
                    onCitaClick = { id -> navegar(Screen.Detalle(id)) },
                    modifier = Modifier.padding(padding)
                )
                is Screen.Detalle -> DetalleCitaScreen(
                    viewModel = koinViewModel<DetalleCitaViewModel>(
                        parameters = { parametersOf(pantalla.citaId) }
                    ),
                    modifier = Modifier.padding(padding)
                )
                is Screen.Solicitud -> SolicitudScreen(
                    viewModel = koinViewModel<SolicitudViewModel>(),
                    onSolicitudExitosa = { atras() },
                    modifier = Modifier.padding(padding)
                )
                is Screen.Perfil -> PerfilScreen(
                    darkTheme = darkTheme,
                    onDarkThemeChange = { darkTheme = it },
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}