package pe.edu.upeu.andinasalud.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = AzulPrimario,
    onPrimary = Color.White,
    primaryContainer = AzulPrimarioClaro,
    onPrimaryContainer = Color(0xFF001E2E),
    secondary = CianSecundario,
    onSecondary = Color.White,
    background = FondoClaro,
    onBackground = TextoClaro,
    surface = SuperficieClara,
    onSurface = TextoClaro,
    surfaceContainer = Color(0xFFEDF3F5),
    surfaceContainerLow = Color(0xFFF3F8F9),
    surfaceContainerHigh = Color(0xFFE7EEF0),
    surfaceContainerHighest = Color(0xFFE1E9EB),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    error = ErrorClaro,
    onError = Color.White
)

private val DarkColors = darkColorScheme(
    primary = AzulPrimarioOscuro,
    onPrimary = Color(0xFF00344A),
    primaryContainer = AzulPrimarioOscuroContainer,
    onPrimaryContainer = AzulPrimarioClaro,
    secondary = CianSecundarioOscuro,
    onSecondary = Color(0xFF00363A),
    background = FondoOscuro,
    onBackground = TextoOscuro,
    surface = SuperficieOscura,
    onSurface = TextoOscuro,
    surfaceContainer = Color(0xFF1E282D),
    surfaceContainerLow = Color(0xFF161F23),
    surfaceContainerHigh = Color(0xFF283237),
    surfaceContainerHighest = Color(0xFF333D42),
    surfaceContainerLowest = Color(0xFF0B1215),
    error = ErrorOscuro,
    onError = Color(0xFF690005)
)

@Composable
fun AndinaSaludTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = AndinaSaludTypography,
        content = content
    )
}