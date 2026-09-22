package pe.edu.upeu.andinasalud.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun EstadoVacio(
    icono: ImageVector,
    titulo: String,
    descripcion: String,
    modifier: Modifier = Modifier,
    esError: Boolean = false,
    onReintentar: (() -> Unit)? = null
) {
    val color = if (esError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(icono, contentDescription = null, tint = color, modifier = Modifier.size(64.dp))
        Spacer(Modifier.height(16.dp))
        Text(titulo, style = MaterialTheme.typography.titleMedium, color = color)
        Spacer(Modifier.height(8.dp))
        Text(descripcion, style = MaterialTheme.typography.bodyMedium, color = color)
        if (onReintentar != null) {
            Spacer(Modifier.height(16.dp))
            Button(onClick = onReintentar) { Text("Reintentar") }
        }
    }
}