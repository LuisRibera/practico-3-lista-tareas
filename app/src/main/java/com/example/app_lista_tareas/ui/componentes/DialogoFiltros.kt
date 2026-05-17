package com.example.app_lista_tareas.ui.componentes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.app_lista_tareas.data.entidades.Etiqueta
import com.example.app_lista_tareas.data.entidades.Prioridad

@Composable
fun DialogoFiltros(
    estadoActual: Boolean?,
    prioridadActual: Prioridad?,
    etiquetaActual: Etiqueta?,
    etiquetasDisponibles: List<Etiqueta>,
    onAplicar: (Boolean?, Prioridad?, Etiqueta?) -> Unit,
    onDismiss: () -> Unit
) {
    var estadoSeleccionado by remember { mutableStateOf(estadoActual) }
    var prioridadSeleccionada by remember { mutableStateOf(prioridadActual) }
    var etiquetaSeleccionada by remember { mutableStateOf(etiquetaActual) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filtros") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text("Estado", style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(4.dp))
                FilaRadio(seleccionado = estadoSeleccionado == null, etiqueta = "Todas") {
                    estadoSeleccionado = null
                }
                FilaRadio(seleccionado = estadoSeleccionado == false, etiqueta = "Pendientes") {
                    estadoSeleccionado = false
                }
                FilaRadio(seleccionado = estadoSeleccionado == true, etiqueta = "Completadas") {
                    estadoSeleccionado = true
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                Text("Prioridad", style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(4.dp))
                FilaRadio(seleccionado = prioridadSeleccionada == null, etiqueta = "Todas") {
                    prioridadSeleccionada = null
                }
                Prioridad.entries.forEach { prioridad ->
                    val nombre = when (prioridad) {
                        Prioridad.ALTA -> "Alta"
                        Prioridad.MEDIA -> "Media"
                        Prioridad.BAJA -> "Baja"
                    }
                    FilaRadio(
                        seleccionado = prioridadSeleccionada == prioridad,
                        etiqueta = nombre
                    ) { prioridadSeleccionada = prioridad }
                }

                if (etiquetasDisponibles.isNotEmpty()) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Text("Etiqueta", style = MaterialTheme.typography.labelLarge)
                    Spacer(Modifier.height(4.dp))
                    FilaRadio(seleccionado = etiquetaSeleccionada == null, etiqueta = "Todas") {
                        etiquetaSeleccionada = null
                    }
                    etiquetasDisponibles.forEach { etiqueta ->
                        FilaRadio(
                            seleccionado = etiquetaSeleccionada?.id == etiqueta.id,
                            etiqueta = etiqueta.nombre
                        ) { etiquetaSeleccionada = etiqueta }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onAplicar(estadoSeleccionado, prioridadSeleccionada, etiquetaSeleccionada) }) {
                Text("Aplicar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun FilaRadio(seleccionado: Boolean, etiqueta: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        RadioButton(selected = seleccionado, onClick = onClick)
        Text(text = etiqueta, modifier = Modifier.padding(start = 4.dp))
    }
}
