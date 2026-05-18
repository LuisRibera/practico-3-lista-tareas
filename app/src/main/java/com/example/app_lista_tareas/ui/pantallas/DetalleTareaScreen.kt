package com.example.app_lista_tareas.ui.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.app_lista_tareas.ui.componentes.ChipEtiqueta
import com.example.app_lista_tareas.ui.componentes.ChipPrioridad
import com.example.app_lista_tareas.ui.viewmodels.DetalleTareaViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DetalleTareaScreen(
    onNavigateBack: () -> Unit,
    onEditarTarea: (Int) -> Unit,
    viewModel: DetalleTareaViewModel = hiltViewModel()
) {
    val estadoUi by viewModel.estadoUi.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detalle de Tarea") },
                actions = {
                    estadoUi.tarea?.let { tareaConEtiquetas ->
                        IconButton(onClick = { onEditarTarea(tareaConEtiquetas.tarea.id) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            estadoUi.estaCargando -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            estadoUi.mensajeError != null -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error: ${estadoUi.mensajeError}",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            estadoUi.tarea != null -> {
                val tarea = estadoUi.tarea!!.tarea
                val etiquetas = estadoUi.tarea!!.etiquetas

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Título y Estado
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = if (tarea.estaCompletada) "COMPLETADA" else "PENDIENTE",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (tarea.estaCompletada) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = tarea.titulo,
                                style = MaterialTheme.typography.headlineMedium,
                                textDecoration = if (tarea.estaCompletada) TextDecoration.LineThrough else null,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Descripción
                    if (!tarea.descripcion.isNullOrBlank()) {
                        Column {
                            Text(
                                text = "Descripción",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = tarea.descripcion,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                    // Prioridad
                    Column {
                        Text(
                            text = "Prioridad",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                        ChipPrioridad(prioridad = tarea.prioridad)
                    }

                    // Etiquetas
                    if (etiquetas.isNotEmpty()) {
                        Column {
                            Text(
                                text = "Etiquetas",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(8.dp))
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                etiquetas.forEach { etiqueta ->
                                    ChipEtiqueta(etiqueta = etiqueta)
                                }
                            }
                        }
                    }

                    // Fecha de Vencimiento
                    if (tarea.fechaVencimiento != null) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Column {
                                Text(
                                    text = "Vence el",
                                    style = MaterialTheme.typography.labelMedium
                                )
                                Text(
                                    text = formatearFecha(tarea.fechaVencimiento),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Fecha de Creación
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Column {
                            Text(
                                text = "Creada el",
                                style = MaterialTheme.typography.labelMedium
                            )
                            Text(
                                text = formatearFecha(tarea.fechaCreacion),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                }
            }
        }
    }
}

private fun formatearFecha(timestamp: Long): String {
    val formato = SimpleDateFormat("EEEE, d 'de' MMMM 'de' yyyy", Locale.getDefault())
    return formato.format(Date(timestamp))
}

