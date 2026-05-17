package com.example.app_lista_tareas.ui.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.app_lista_tareas.data.entidades.TareaConEtiquetas
import com.example.app_lista_tareas.ui.componentes.DialogoFiltros
import com.example.app_lista_tareas.ui.componentes.TarjetaTarea
import com.example.app_lista_tareas.ui.estados.OpcionOrdenamiento
import com.example.app_lista_tareas.ui.viewmodels.InicioViewModel
import java.util.Locale
import androidx.compose.ui.tooling.preview.Preview
import com.example.app_lista_tareas.data.entidades.Tarea
import com.example.app_lista_tareas.data.entidades.Prioridad
import java.util.Date

@Preview(showBackground = true)
@Composable
fun InicioScreenPreview() {
    MaterialTheme {
        InicioScreen(
            onNuevaTarea = {},
            onTareaClick = {},
            onGestionarEtiquetas = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(
    onNuevaTarea: () -> Unit,
    onTareaClick: (Int) -> Unit,
    onGestionarEtiquetas: () -> Unit,
    viewModel: InicioViewModel = hiltViewModel()
) {
    val estadoUi by viewModel.estadoUi.collectAsStateWithLifecycle()
    var mostrarBusqueda by remember { mutableStateOf(false) }
    var mostrarFiltros by remember { mutableStateOf(false) }
    var mostrarMenuOrden by remember { mutableStateOf(false) }
    var mostrarMenuOpciones by remember { mutableStateOf(false) }

    // Sincronizar tab con filtro de estado del ViewModel
    // 0: Todas (filtroEstado = null o true), 1: Pendientes (filtroEstado = false)
    val indexTab = when (estadoUi.filtroEstado) {
        false -> 1
        else -> 0
    }
    
    val manejarTabClick = { index: Int ->
        viewModel.actualizarFiltroEstado(if (index == 0) null else false)
    }

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = { Text("App Tareas") },
                    actions = {
                        IconButton(onClick = { mostrarBusqueda = !mostrarBusqueda }) {
                            Icon(Icons.Default.Search, contentDescription = "Buscar")
                        }
                        Box {
                            IconButton(onClick = { mostrarMenuOrden = true }) {
                                Icon(Icons.Default.Sort, contentDescription = "Ordenar")
                            }
                            DropdownMenu(
                                expanded = mostrarMenuOrden,
                                onDismissRequest = { mostrarMenuOrden = false }
                            ) {
                                OpcionOrdenamiento.entries.forEach { opcion ->
                                    DropdownMenuItem(
                                        text = { 
                                            Text(opcion.name.replace("_", " ")
                                                .lowercase()
                                                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }) 
                                        },
                                        onClick = {
                                            viewModel.actualizarOrdenamiento(opcion)
                                            mostrarMenuOrden = false
                                        }
                                    )
                                }
                            }
                        }
                        IconButton(onClick = { mostrarFiltros = true }) {
                            Icon(Icons.Default.FilterList, contentDescription = "Filtros")
                        }
                        Box {
                            IconButton(onClick = { mostrarMenuOpciones = true }) {
                                Icon(Icons.Default.MoreVert, contentDescription = "Más opciones")
                            }
                            DropdownMenu(
                                expanded = mostrarMenuOpciones,
                                onDismissRequest = { mostrarMenuOpciones = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Gestionar Etiquetas") },
                                    onClick = {
                                        onGestionarEtiquetas()
                                        mostrarMenuOpciones = false
                                    }
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )

                if (mostrarBusqueda) {
                    SearchBar(
                        query = estadoUi.textoBusqueda,
                        onQueryChange = { viewModel.actualizarBusqueda(it) },
                        onSearch = { mostrarBusqueda = false },
                        active = false,
                        onActiveChange = { if (!it) mostrarBusqueda = false },
                        placeholder = { Text("Buscar por título...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {}
                }

                TabRow(selectedTabIndex = indexTab) {
                    Tab(
                        selected = indexTab == 0,
                        onClick = { manejarTabClick(0) },
                        text = { Text("Todas") }
                    )
                    Tab(
                        selected = indexTab == 1,
                        onClick = { manejarTabClick(1) },
                        text = { Text("Pendientes") }
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNuevaTarea,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva tarea")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (estadoUi.listaTareas.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    val mensaje = if (estadoUi.textoBusqueda.isNotEmpty() || estadoUi.filtroPrioridad != null || estadoUi.filtroEtiqueta != null) 
                        "No hay resultados para los filtros aplicados" 
                    else "No tienes tareas registradas"
                    
                    Text(
                        text = mensaje,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = estadoUi.listaTareas,
                        key = { it.tarea.id }
                    ) { tareaConEtiquetas: TareaConEtiquetas ->
                        TarjetaTarea(
                            tareaConEtiquetas = tareaConEtiquetas,
                            onClick = { onTareaClick(tareaConEtiquetas.tarea.id) },
                            onAlternarEstado = { viewModel.alternarEstadoTarea(tareaConEtiquetas) }
                        )
                    }
                }
            }

            if (mostrarFiltros) {
                DialogoFiltros(
                    estadoActual = estadoUi.filtroEstado,
                    prioridadActual = estadoUi.filtroPrioridad,
                    etiquetaActual = estadoUi.filtroEtiqueta,
                    etiquetasDisponibles = estadoUi.etiquetasDisponibles,
                    onAplicar = { estado, prioridad, etiqueta ->
                        viewModel.actualizarFiltroEstado(estado)
                        viewModel.actualizarFiltroPrioridad(prioridad)
                        viewModel.actualizarFiltroEtiqueta(etiqueta)
                        mostrarFiltros = false
                    },
                    onDismiss = { mostrarFiltros = false }
                )
            }
        }
    }
}
