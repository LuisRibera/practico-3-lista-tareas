package com.example.app_lista_tareas.ui.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.app_lista_tareas.ui.viewmodels.EtiquetasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestionEtiquetasScreen(
    onNavigateBack: () -> Unit,
    viewModel: EtiquetasViewModel = hiltViewModel()
) {
    val estadoUi by viewModel.estadoUi.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Gestionar Etiquetas") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top
            ) {
                OutlinedTextField(
                    value = estadoUi.nombreNuevaEtiqueta,
                    onValueChange = { viewModel.actualizarNombreNuevaEtiqueta(it) },
                    label = { Text("Nueva etiqueta") },
                    modifier = Modifier.weight(1f),
                    isError = estadoUi.mensajeError != null,
                    supportingText = {
                        estadoUi.mensajeError?.let {
                            Text(text = it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    singleLine = true
                )
                Button(
                    onClick = { viewModel.agregarEtiqueta() },
                    modifier = Modifier.padding(top = 8.dp),
                    enabled = !estadoUi.estaGuardando && estadoUi.nombreNuevaEtiqueta.isNotBlank()
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar")
                }
            }

            HorizontalDivider()

            Text(
                text = "Etiquetas existentes",
                style = MaterialTheme.typography.titleMedium
            )

            if (estadoUi.listaEtiquetas.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay etiquetas creadas",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = estadoUi.listaEtiquetas,
                        key = { it.id }
                    ) { etiqueta ->
                        ListItem(
                            headlineContent = { Text(etiqueta.nombre) },
                            trailingContent = {
                                IconButton(onClick = { viewModel.eliminarEtiqueta(etiqueta) }) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Eliminar",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        )
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    }
                }
            }
        }
    }
}

