package pe.edu.upeu.andinasalud.presentation.citas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pe.edu.upeu.andinasalud.domain.model.ModalidadAtencion
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.scale

@Composable
fun CitasScreen(
    viewModel: CitasViewModel,
    onCitaClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {

        OutlinedTextField(
            value = estado.textoBusqueda,
            onValueChange = viewModel::onBusquedaChange,
            label = { Text("Buscar por especialidad o médico") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            FiltroEstado.entries.forEach { filtro ->
                FilterChip(
                    selected = estado.filtroEstado == filtro,
                    onClick = { viewModel.onFiltroEstadoChange(filtro) },
                    label = { Text(filtro.etiqueta) }
                )
            }
            FilterChip(
                selected = estado.soloHoy,
                onClick = { viewModel.onSoloHoyChange(!estado.soloHoy) },
                label = { Text("Hoy") }
            )
        }

        Spacer(Modifier.height(16.dp))

        Box(modifier = Modifier.weight(1f)) {
            when (val fase = estado.fase) {
                is FaseCitas.Cargando -> Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    Spacer(Modifier.height(8.dp))
                    Text("Cargando citas…")
                }

                is FaseCitas.SinCitas -> Column(
                    Modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(64.dp))
                    Spacer(Modifier.height(16.dp))
                    Text("No hay citas para este filtro", style = MaterialTheme.typography.titleMedium)
                }

                is FaseCitas.ConCitas -> LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(fase.citas, key = { it.id }) { cita ->
                        val interactionSource = remember { MutableInteractionSource() }
                        val presionado by interactionSource.collectIsPressedAsState()
                        val escala by animateFloatAsState(
                            targetValue = if (presionado) 0.97f else 1f,
                            label = "escala-tarjeta"
                        )
                        Card(
                            onClick = { onCitaClick(cita.id) },
                            interactionSource = interactionSource,
                            modifier = Modifier.fillMaxWidth().scale(escala)
                        ) {
                            Column(Modifier.padding(12.dp)) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = if (cita.modalidad == ModalidadAtencion.PRESENCIAL)
                                                Icons.Default.LocalHospital
                                            else
                                                Icons.Default.Videocam,
                                            contentDescription = cita.modalidad.etiqueta,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(Modifier.width(6.dp))
                                        Text(cita.especialidad, style = MaterialTheme.typography.titleMedium)
                                    }
                                    AssistChip(onClick = {}, label = { Text(cita.estadoTexto) })
                                }
                                Text(cita.medico, style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    "${cita.fechaTexto} · ${cita.horaTexto} · ${cita.sede}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }

                is FaseCitas.Error -> Column(
                    Modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.ErrorOutline,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(fase.mensaje, color = MaterialTheme.colorScheme.error)
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = viewModel::cargarCitas) { Text("Reintentar") }
                }
            }
        }
    }
}