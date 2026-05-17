package com.example.app_lista_tareas.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.app_lista_tareas.data.entidades.TareaConEtiquetas
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TarjetaTarea(
    tareaConEtiquetas: TareaConEtiquetas,
    onClick: () -> Unit,
    onAlternarEstado: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tarea = tareaConEtiquetas.tarea
    val colorPrioridad = colorDePrioridad(tarea.prioridad)

    ElevatedCard(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(colorPrioridad)
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 10.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = tarea.estaCompletada,
                        onCheckedChange = { onAlternarEstado() }
                    )
                    Text(
                        text = tarea.titulo,
                        style = MaterialTheme.typography.bodyLarge,
                        textDecoration = if (tarea.estaCompletada) TextDecoration.LineThrough else null,
                        color = if (tarea.estaCompletada)
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        else
                            MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }
                tarea.fechaVencimiento?.let { fecha ->
                    Text(
                        text = "Vence: ${formatearFecha(fecha)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
                    )
                }
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(start = 4.dp)
                ) {
                    ChipPrioridad(prioridad = tarea.prioridad)
                    tareaConEtiquetas.etiquetas.forEach { etiqueta ->
                        ChipEtiqueta(etiqueta = etiqueta)
                    }
                }
            }
        }
    }
}

private fun formatearFecha(timestamp: Long): String {
    val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formato.format(Date(timestamp))
}
