package pe.edu.upeu.bibliomobil.presentation.libro

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pe.edu.upeu.bibliomobil.presentation.components.EstadoVacio
import pe.edu.upeu.bibliomobil.presentation.components.MensajeExito
import pe.edu.upeu.bibliomobil.presentation.components.ValidatedTextField

@Composable
fun LibroScreen(viewModel: LibroViewModel, modifier: Modifier = Modifier) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()
    val formulario = estado.formulario

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ValidatedTextField(
                    value = formulario.titulo,
                    onValueChange = viewModel::onTituloChange,
                    label = "Título",
                    error = formulario.tituloError
                )
                ValidatedTextField(
                    value = formulario.autor,
                    onValueChange = viewModel::onAutorChange,
                    label = "Autor",
                    error = formulario.autorError
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ValidatedTextField(
                        value = formulario.anio,
                        onValueChange = viewModel::onAnioChange,
                        label = "Año",
                        error = formulario.anioError,
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f)
                    )
                    ValidatedTextField(
                        value = formulario.ejemplares,
                        onValueChange = viewModel::onEjemplaresChange,
                        label = "Ejemplares",
                        error = formulario.ejemplaresError,
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f)
                    )
                }
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

        val conteo = (estado.fase as? Fase.ConLibros)?.libros?.size ?: 0
        Text(
            text = if (conteo == 1) "1 libro" else "$conteo libros",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(8.dp))

        Box(modifier = Modifier.weight(1f)) {
            when (val fase = estado.fase) {
                is Fase.Cargando -> Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    Spacer(Modifier.height(8.dp))
                    Text("Cargando catálogo…")
                }

                is Fase.SinLibros -> EstadoVacio(
                    icono = Icons.Default.MenuBook,
                    titulo = "Sin libros",
                    descripcion = "Todavía no hay libros registrados en el catálogo."
                )

                is Fase.ConLibros -> LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(fase.libros, key = { it.id }) { libro ->
                        Card(Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(12.dp)) {
                                Text(libro.titulo, style = MaterialTheme.typography.titleMedium)
                                Text(libro.autor, style = MaterialTheme.typography.bodyMedium)
                                Text(libro.lineaSecundaria, style = MaterialTheme.typography.bodySmall)
                                if (libro.requiereReposicion) {
                                    Spacer(Modifier.height(4.dp))
                                    AssistChip(onClick = {}, label = { Text("Pocos ejemplares") })
                                }
                            }
                        }
                    }
                }

                is Fase.Error -> EstadoVacio(
                    icono = Icons.Default.ErrorOutline,
                    titulo = "Error",
                    descripcion = fase.mensaje,
                    esError = true,
                    onReintentar = viewModel::cargarLibros
                )
            }
        }
    }
}