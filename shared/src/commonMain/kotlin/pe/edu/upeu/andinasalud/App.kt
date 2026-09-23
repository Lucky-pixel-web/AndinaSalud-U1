package pe.edu.upeu.andinasalud

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import org.koin.compose.KoinContext
import org.koin.compose.koinInject
import pe.edu.upeu.andinasalud.domain.usecase.ContarCitasProgramadasUseCase
import pe.edu.upeu.andinasalud.domain.usecase.SolicitarCitaUseCase
import pe.edu.upeu.andinasalud.navigation.AppNavHost
import pe.edu.upeu.andinasalud.navigation.Screen
import pe.edu.upeu.andinasalud.presentation.theme.AndinaSaludTheme

@Composable
fun App() = KoinContext {
    var darkTheme by rememberSaveable { mutableStateOf(false) }
    val pila = remember { mutableStateListOf<Screen>(Screen.Inicio) }
    val actual = pila.last()

    fun navegar(destino: Screen) { pila.add(destino) }
    fun atras() { if (pila.size > 1) pila.removeAt(pila.lastIndex) }
    fun irATab(destino: Screen) { pila.clear(); pila.add(destino) }

    val contarProgramadas: ContarCitasProgramadasUseCase = koinInject()
    var citasProgramadas by remember { mutableStateOf(0) }
    var trigger by remember { mutableStateOf(0) }
    LaunchedEffect(trigger) { contarProgramadas().onSuccess { citasProgramadas = it } }
    val limiteAlcanzado = citasProgramadas >= SolicitarCitaUseCase.LIMITE_PROGRAMADAS

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
                        icon = {
                            BadgedBox(badge = {
                                if (citasProgramadas > 0) Badge { Text("$citasProgramadas") }
                            }) {
                                Icon(Icons.Default.CalendarMonth, contentDescription = "Citas")
                            }
                        },
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
            AppNavHost(
                actual = actual,
                padding = padding,
                darkTheme = darkTheme,
                onDarkThemeChange = { darkTheme = it },
                limiteAlcanzado = limiteAlcanzado,
                navegar = ::navegar,
                irATab = { destino -> irATab(destino); trigger++ },
                atras = { atras(); trigger++ }
            )
        }
    }
}