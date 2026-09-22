package pe.edu.upeu.andinasalud

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.koin.compose.KoinContext

@Composable
fun App() = KoinContext {
    MaterialTheme {
        Surface {
            Text("AndinaSalud en construcción")
        }
    }
}