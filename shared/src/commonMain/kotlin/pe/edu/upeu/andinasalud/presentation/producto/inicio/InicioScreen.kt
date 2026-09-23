package pe.edu.upeu.andinasalud.presentation.inicio

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun InicioScreen(
    viewModel: InicioViewModel,
    onVerCitas: () -> Unit,
    onSolicitarCita: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text("Hola, ${estado.nombrePaciente}", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Próxima cita", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                val cita = estado.proximaCita
                if (cita != null) {
                    Text("${cita.especialidad} · ${cita.medico}")
                    Text("${cita.fechaTexto} ${cita.horaTexto} · ${cita.sede}")
                } else {
                    Text("No tienes citas programadas")
                }
            }
        }

        Spacer(Modifier.height(24.dp))
        Text("Qué puedes hacer", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = onVerCitas) { Text("Mis citas") }
            Button(onClick = onSolicitarCita) { Text("Solicitar cita") }
        }
    }
}