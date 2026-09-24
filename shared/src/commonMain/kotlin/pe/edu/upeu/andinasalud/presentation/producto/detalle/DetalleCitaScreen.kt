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
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) { CircularProgressIndicator() }

            is FaseDetalle.Error -> Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) { Text(fase.mensaje, color = MaterialTheme.colorScheme.error) }

            is FaseDetalle.Contenido -> {
                val cita = fase.cita
                Column(Modifier.fillMaxSize().padding(16.dp)) {
                    Text(cita.especialidad, style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(8.dp))
                    Text("Médico: ${cita.medico}")
                    Text("Sede: ${cita.sede}")
                    Text("Fecha: ${cita.fechaTexto}  ·  Hora: ${cita.horaTexto}")
                    Text("Modalidad: ${cita.modalidad.etiqueta}")
                    Text("Estado: ${cita.estadoTexto}")
                    Spacer(Modifier.height(24.dp))

                    if (cita.estadoTexto == "Programada") {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedButton(
                                onClick = viewModel::onReprogramarClick,
                                enabled = !estado.reprogramando
                            ) { Text("Reprogramar") }

                            Button(
                                onClick = viewModel::onCancelarClick,
                                enabled = !estado.cancelando,
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                            ) {
                                Text(if (estado.cancelando) "Cancelando…" else "Cancelar cita")
                            }
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
                confirmButton = { TextButton(onClick = viewModel::onConfirmarCancelacion) { Text("Sí, cancelar") } },
                dismissButton = { TextButton(onClick = viewModel::onDescartarDialogo) { Text("Volver") } }
            )
        }

        if (estado.mostrarDialogoReprogramar) {
            val f = estado.formularioReprogramar
            AlertDialog(
                onDismissRequest = viewModel::onDescartarReprogramar,
                title = { Text("Reprogramar cita") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = f.fecha, onValueChange = viewModel::onFechaReprogramarChange,
                            label = { Text("Nueva fecha (AAAA-MM-DD)") },
                            isError = f.errorFecha != null,
                            supportingText = { f.errorFecha?.let { Text(it) } }
                        )
                        OutlinedTextField(
                            value = f.hora, onValueChange = viewModel::onHoraReprogramarChange,
                            label = { Text("Nueva hora (HH:MM)") },
                            isError = f.errorHora != null,
                            supportingText = { f.errorHora?.let { Text(it) } }
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = viewModel::onConfirmarReprogramar, enabled = !estado.reprogramando) {
                        Text(if (estado.reprogramando) "Guardando…" else "Confirmar")
                    }
                },
                dismissButton = { TextButton(onClick = viewModel::onDescartarReprogramar) { Text("Cancelar") } }
            )
        }
    }
}