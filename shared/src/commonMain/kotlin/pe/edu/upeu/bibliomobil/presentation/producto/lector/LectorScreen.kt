package pe.edu.upeu.bibliomobil.presentation.lector

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pe.edu.upeu.bibliomobil.presentation.components.EstadoVacio
import pe.edu.upeu.bibliomobil.presentation.components.MensajeExito
import pe.edu.upeu.bibliomobil.presentation.components.ValidatedTextField

@Composable
fun LectorScreen(viewModel: LectorViewModel, modifier: Modifier = Modifier) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()
    val formulario = estado.formulario

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ValidatedTextField(
                    value = formulario.nombre,
                    onValueChange = viewModel::onNombreChange,
                    label = "Nombre",
                    error = formulario.nombreError
                )
                ValidatedTextField(
                    value = formulario.correo,
                    onValueChange = viewModel::onCorreoChange,
                    label = "Correo",
                    error = formulario.correoError,
                    keyboardType = KeyboardType.Email
                )
                ValidatedTextField(
                    value = formulario.telefono,
                    onValueChange = viewModel::onTelefonoChange,
                    label = "Teléfono (opcional)",
                    error = formulario.telefonoError,
                    keyboardType = KeyboardType.Phone
                )
                Button(
                    onClick = viewModel::registrar,
                    enabled = !estado.registrando,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (estado.registrando) "Registrando…" else "Registrar")
                }
            }
        }

        estado.mensajeExito?.let {
            Spacer(Modifier.height(12.dp))
            MensajeExito(mensaje = it)
        }

        Spacer(Modifier.height(16.dp))

        val conteo = (estado.fase as? FaseLector.ConLectores)?.lectores?.size ?: 0
        Text(
            text = if (conteo == 1) "1 lector" else "$conteo lectores",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(8.dp))

        Box(modifier = Modifier.weight(1f)) {
            when (val fase = estado.fase) {
                is FaseLector.Cargando -> Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    Spacer(Modifier.height(8.dp))
                    Text("Cargando cartera de lectores…")
                }

                is FaseLector.SinLectores -> EstadoVacio(
                    icono = Icons.Default.People,
                    titulo = "Sin lectores",
                    descripcion = "Todavía no hay lectores registrados."
                )

                is FaseLector.ConLectores -> LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(fase.lectores, key = { it.id }) { lector ->
                        Card(Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(12.dp)) {
                                Text(lector.nombre, style = MaterialTheme.typography.titleMedium)
                                Text(lector.correo, style = MaterialTheme.typography.bodyMedium)
                                Text(lector.telefono, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }

                is FaseLector.Error -> EstadoVacio(
                    icono = Icons.Default.ErrorOutline,
                    titulo = "Error",
                    descripcion = fase.mensaje,
                    esError = true,
                    onReintentar = viewModel::cargarLectores
                )
            }
        }
    }
}