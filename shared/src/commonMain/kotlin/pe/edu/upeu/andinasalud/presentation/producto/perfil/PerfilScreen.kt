package pe.edu.upeu.andinasalud.presentation.perfil

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import pe.edu.upeu.andinasalud.domain.usecase.ObtenerPacienteUseCase

@Composable
fun PerfilScreen(
    darkTheme: Boolean,
    onDarkThemeChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    obtenerPaciente: ObtenerPacienteUseCase = koinInject()
) {
    var nombre by remember { mutableStateOf("") }
    var documento by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            obtenerPaciente().onSuccess {
                nombre = it.nombre; documento = it.documento; correo = it.correo
            }
        }
    }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text("Perfil", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))
        Text("Nombre: $nombre")
        Text("Documento: $documento")
        Text("Correo: $correo")
        Spacer(Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Modo oscuro")
            Switch(checked = darkTheme, onCheckedChange = onDarkThemeChange)
        }
    }
}