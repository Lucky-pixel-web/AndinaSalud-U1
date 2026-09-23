package pe.edu.upeu.andinasalud.presentation.solicitud

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@Composable
fun SolicitudScreen(
    viewModel: SolicitudViewModel,
    onSolicitudExitosa: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()
    val f = estado.formulario

    LaunchedEffect(estado.exito) {
        if (estado.exito) {
            onSolicitudExitosa()
            viewModel.onExitoMostrado()
        }
    }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Solicitar cita", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = f.especialidad, onValueChange = viewModel::onEspecialidadChange,
            label = { Text("Especialidad") }, isError = f.errorEspecialidad != null,
            supportingText = { f.errorEspecialidad?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = f.sede, onValueChange = viewModel::onSedeChange,
            label = { Text("Sede") }, isError = f.errorSede != null,
            supportingText = { f.errorSede?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = f.fecha, onValueChange = viewModel::onFechaChange,
            label = { Text("Fecha (AAAA-MM-DD)") }, isError = f.errorFecha != null,
            supportingText = { f.errorFecha?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = f.hora, onValueChange = viewModel::onHoraChange,
            label = { Text("Hora (HH:MM)") }, isError = f.errorHora != null,
            supportingText = { f.errorHora?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = f.motivo, onValueChange = viewModel::onMotivoChange,
            label = { Text("Motivo") }, isError = f.errorMotivo != null,
            supportingText = { f.errorMotivo?.let { Text(it) } },
            minLines = 3, modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = viewModel::enviar,
            enabled = !estado.enviando,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (estado.enviando) "Enviando…" else "Solicitar cita")
        }
    }
}