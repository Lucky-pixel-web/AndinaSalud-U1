package pe.edu.upeu.andinasalud.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
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
    limiteAlcanzado: Boolean,
    navegar: (Screen) -> Unit,
    irATab: (Screen) -> Unit,
    atras: () -> Unit
) {
    AnimatedContent(
        targetState = actual,
        transitionSpec = {
            (slideInHorizontally(animationSpec = tween(280)) { ancho -> ancho / 4 } + fadeIn(tween(280)))
                .togetherWith(slideOutHorizontally(animationSpec = tween(200)) { ancho -> -ancho / 4 } + fadeOut(tween(200)))
        },
        label = "navegacion-pantallas"
    ) { pantalla ->
        when (pantalla) {
            is Screen.Inicio -> InicioScreen(
                viewModel = koinViewModel<InicioViewModel>(),
                onVerCitas = { irATab(Screen.Citas) },
                onSolicitarCita = { navegar(Screen.Solicitud) },
                limiteAlcanzado = limiteAlcanzado,
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
                onDarkThemeChange = onDarkThemeChange,
                modifier = Modifier.padding(padding)
            )
        }
    }
}