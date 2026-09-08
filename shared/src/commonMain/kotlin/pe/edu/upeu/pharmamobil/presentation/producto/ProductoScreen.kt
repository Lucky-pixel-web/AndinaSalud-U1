package pe.edu.upeu.pharmamobil.presentation.producto

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pe.edu.upeu.pharmamobil.presentation.components.ValidatedTextField

@Composable
fun ProductoScreen(
    viewModel: ProductoViewModel,
    modifier: Modifier = Modifier
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var tabSeleccionada by remember { mutableStateOf(0) }
    val titulosTabs = listOf("Activos", "Inactivos", "Bajo stock")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            "PharmaMobil",
            color = MaterialTheme.colorScheme.primary
        )
        Text("Registro de Producto")

        ValidatedTextField(
            value = uiState.formulario.nombre,
            onValueChange = viewModel::onNombreChange,
            label = "Nombre",
            error = uiState.formulario.nombreError
        )

        ValidatedTextField(
            value = uiState.formulario.precio,
            onValueChange = viewModel::onPrecioChange,
            label = "Precio",
            error = uiState.formulario.precioError
        )

        ValidatedTextField(
            value = uiState.formulario.stock,
            onValueChange = viewModel::onStockChange,
            label = "Stock",
            error = uiState.formulario.stockError
        )

        Button(
            onClick = viewModel::registrar,
            enabled = !uiState.registrando,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (uiState.registrando) "Registrando…" else "Registrar")
        }

        uiState.mensajeExito?.let { Text(it) }

        Text("Inventario")

        TabRow(selectedTabIndex = tabSeleccionada) {
            titulosTabs.forEachIndexed { index, titulo ->
                Tab(
                    selected = tabSeleccionada == index,
                    onClick = { tabSeleccionada = index },
                    text = { Text(titulo) }
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {

            when (val fase = uiState.fase) {

                ProductoUiState.Fase.Cargando ->
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CircularProgressIndicator()
                        Text("Cargando inventario…")
                    }

                ProductoUiState.Fase.SinProductos ->
                    Text(
                        text = "Todavía no hay productos registrados.",
                        modifier = Modifier.align(Alignment.Center)
                    )

                is ProductoUiState.Fase.ConProductos -> {

                    val productosFiltrados = when (tabSeleccionada) {
                        0 -> fase.productos.filter { it.activo && !it.esBajoStock }
                        1 -> fase.productos.filter { !it.activo }
                        2 -> fase.productos.filter { it.esBajoStock }
                        else -> emptyList()
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().height(240.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(productosFiltrados, key = { it.id }) { p ->
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                ),
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                            ) {
                                Column(Modifier.padding(12.dp)) {
                                    Text(p.nombre)
                                    Text("${p.precio}  ·  Stock: ${p.stock}")
                                }
                            }
                        }
                    }
                }

                is ProductoUiState.Fase.Error ->
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = fase.mensaje,
                            color = MaterialTheme.colorScheme.error
                        )
                        FilledTonalButton(onClick = viewModel::cargarProductos) {
                            Text("Reintentar")
                        }
                    }
            }
        }
    }
}