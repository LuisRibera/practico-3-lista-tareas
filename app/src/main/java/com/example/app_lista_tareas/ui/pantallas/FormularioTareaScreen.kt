package com.example.app_lista_tareas.ui.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.app_lista_tareas.data.entidades.Prioridad
import com.example.app_lista_tareas.ui.viewmodels.FormularioTareaViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FormularioTareaScreen(
    onNavigateBack: () -> Unit,
    onGestionarEtiquetas: () -> Unit,
    viewModel: FormularioTareaViewModel = hiltViewModel()
) {
    val estadoUi by viewModel.estadoUi.collectAsStateWithLifecycle()
    var mostrarDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = estadoUi.guardadoExitoso) {
        if (estadoUi.guardadoExitoso) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(if (estadoUi.modoEdicion) "Editar Tarea" else "Nueva Tarea")
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = estadoUi.titulo,
                onValueChange = { viewModel.actualizarTitulo(it) },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth(),
                isError = estadoUi.mensajeErrorTitulo != null,
                supportingText = {
                    estadoUi.mensajeErrorTitulo?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                },
                singleLine = true
            )

            OutlinedTextField(
                value = estadoUi.descripcion,
                onValueChange = { viewModel.actualizarDescripcion(it) },
                label = { Text("Descripción") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                minLines = 3
            )

            // Selector de Fecha
            OutlinedButton(
                onClick = { mostrarDatePicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (estadoUi.fechaVencimiento != null) {
                        "Vencimiento: ${formatearFecha(estadoUi.fechaVencimiento)}"
                    } else {
                        "Seleccionar fecha de vencimiento"
                    }
                )
            }

            // Selector de Prioridad
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Prioridad", style = MaterialTheme.typography.labelLarge)
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    Prioridad.entries.forEachIndexed { index, prioridad ->
                        SegmentedButton(
                            selected = estadoUi.prioridadSeleccionada == prioridad,
                            onClick = { viewModel.actualizarPrioridad(prioridad) },
                            shape = SegmentedButtonDefaults.itemShape(index = index, count = Prioridad.entries.size)
                        ) {
                            Text(prioridad.name.lowercase().capitalize(Locale.ROOT))
                        }
                    }
                }
            }

            // Selector de Etiquetas
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Etiquetas", style = MaterialTheme.typography.labelLarge)
                    TextButton(onClick = onGestionarEtiquetas) {
                        Text("Gestionar")
                    }
                }
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    estadoUi.etiquetasDisponibles.forEach { etiqueta ->
                        FilterChip(
                            selected = etiqueta.id in estadoUi.etiquetasSeleccionadas,
                            onClick = { viewModel.alternarEtiqueta(etiqueta.id) },
                            label = { Text(etiqueta.nombre) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (estadoUi.modoEdicion) {
                    OutlinedButton(
                        onClick = { viewModel.eliminarTarea() },
                        modifier = Modifier.weight(0.4f),
                        enabled = !estadoUi.estaGuardando
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null)
                        Spacer(Modifier.width(4.dp))
                        Text("Eliminar")
                    }
                }
                
                Button(
                    onClick = { viewModel.guardarTarea() },
                    modifier = Modifier.weight(1f),
                    enabled = !estadoUi.estaGuardando
                ) {
                    Text(if (estadoUi.modoEdicion) "Actualizar" else "Guardar Tarea")
                }
            }
        }
    }

    if (mostrarDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = estadoUi.fechaVencimiento
        )
        DatePickerDialog(
            onDismissRequest = { mostrarDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            viewModel.actualizarFechaVencimiento(it)
                        }
                        mostrarDatePicker = false
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                Row {
                    if (estadoUi.fechaVencimiento != null) {
                        TextButton(onClick = {
                            viewModel.actualizarFechaVencimiento(null)
                            mostrarDatePicker = false
                        }) {
                            Text("Sin fecha")
                        }
                    }
                    TextButton(onClick = { mostrarDatePicker = false }) {
                        Text("Cancelar")
                    }
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

private fun formatearFecha(timestamp: Long?): String {
    if (timestamp == null) return "Sin fecha"
    val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formato.format(Date(timestamp))
}

// removed custom width extension; use Modifier.width from foundation.layout when needed
