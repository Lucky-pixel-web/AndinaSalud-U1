package pe.edu.upeu.andinasalud.presentation.detalle

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun DetalleCitaScreen(viewModel: DetalleCitaViewModel, modifier: Modifier = Modifier) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize()) {
        when (val fase = estado.fase) {
            is FaseDetalle.Cargando -> Column(
                Modifier.fillMaxSize(), Alignment.CenterHorizontally, Arrangement.Center
            ) { CircularProgressIndicator() }

            is FaseDetalle.Error -> Column(
                Modifier.fillMaxSize().padding(24.dp), Alignment.CenterHorizontally, Arrangement.Center
            ) { Text(fase.mensaje, color = MaterialTheme.colorScheme.error) }

            is FaseDetalle.Contenido -> {
                val cita = fase.cita
                Column(Modifier.fillMaxSize().padding(16.dp)) {
                    Text(cita.especialidad, style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(8.dp))
                    Text("Médico: ${cita.medico}")
                    Text("Sede: ${cita.sede}")
                    Text("Fecha: ${cita.fechaTexto}  ·  Hora: ${cita.horaTexto}")
                    Text("Estado: ${cita.estadoTexto}")
                    Spacer(Modifier.height(24.dp))
                    if (cita.estadoTexto == "Programada") {
                        Button(
                            onClick = viewModel::onCancelarClick,
                            enabled = !estado.cancelando,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text(if (estado.cancelando) "Cancelando…" else "Cancelar cita")
                        }
                    }
                    estado.mensajeError?.let {
                        Spacer(Modifier.height(8.dp))
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }

        if (estado.mostrarDialogoConfirmacion) {
            AlertDialog(
                onDismissRequest = viewModel::onDescartarDialogo,
                title = { Text("Cancelar cita") },
                text = { Text("¿Seguro que deseas cancelar esta cita? Esta acción no se puede deshacer.") },
                confirmButton = {
                    TextButton(onClick = viewModel::onConfirmarCancelacion) { Text("Sí, cancelar") }
                },
                dismissButton = {
                    TextButton(onClick = viewModel::onDescartarDialogo) { Text("Volver") }
                }
            )
        }
    }
}