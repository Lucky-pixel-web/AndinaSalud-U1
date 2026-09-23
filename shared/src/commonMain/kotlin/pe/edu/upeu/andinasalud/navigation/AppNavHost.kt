package pe.edu.upeu.andinasalud.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import pe.edu.upeu.andinasalud.presentation.citas.CitasScreen
import pe.edu.upeu.andinasalud.presentation.citas.CitasViewModel
import pe.edu.upeu.andinasalud.presentation.detalle.DetalleCitaScreen
import pe.edu.upeu.andinasalud.presentation.detalle.DetalleCitaViewModel
import pe.edu.upeu.andinasalud.presentation.inicio.InicioScreen
import pe.edu.upeu.andinasalud.presentation.inicio.InicioViewModel
import pe.edu.upeu.andinasalud.presentation.perfil.PerfilScreen
import pe.edu.upeu.andinasalud.presentation.solicitud.SolicitudScreen
import pe.edu.upeu.andinasalud.presentation.solicitud.SolicitudViewModel

@Composable
fun AppNavHost(
    actual: Screen,
    padding: PaddingValues,
    darkTheme: Boolean,
    onDarkThemeChange: (Boolean) -> Unit,
    navegar: (Screen) -> Unit,
    irATab: (Screen) -> Unit,
    atras: () -> Unit
) {
    when (actual) {
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
                parameters = { parametersOf(actual.citaId) }
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
            onDarkThemeChange = onDarkThemeChange,
            modifier = Modifier.padding(padding)
        )
    }
}