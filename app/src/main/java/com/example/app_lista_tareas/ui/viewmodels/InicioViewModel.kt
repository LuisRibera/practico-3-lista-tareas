package com.example.app_lista_tareas.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_lista_tareas.data.entidades.Etiqueta
import com.example.app_lista_tareas.data.entidades.Prioridad
import com.example.app_lista_tareas.data.entidades.TareaConEtiquetas
import com.example.app_lista_tareas.repositorios.RepositorioEtiquetas
import com.example.app_lista_tareas.repositorios.RepositorioTareas
import com.example.app_lista_tareas.ui.estados.InicioUiState
import com.example.app_lista_tareas.ui.estados.OpcionOrdenamiento
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InicioViewModel @Inject constructor(
    private val repositorioTareas: RepositorioTareas,
    private val repositorioEtiquetas: RepositorioEtiquetas
) : ViewModel() {

    private val _filtros = MutableStateFlow(FiltrosEstado())

    val estadoUi = combine(
        repositorioTareas.obtenerTodasLasTareasConEtiquetas(),
        repositorioEtiquetas.obtenerTodasLasEtiquetas(),
        _filtros
    ) { tareas, etiquetas, filtros ->
        val tareasFiltradas = tareas
            .filter { tc -> coincideConFiltros(tc, filtros) }
            .let { lista -> aplicarOrdenamiento(lista, filtros.opcionOrdenamiento) }

        InicioUiState(
            listaTareas = tareasFiltradas,
            etiquetasDisponibles = etiquetas,
            textoBusqueda = filtros.textoBusqueda,
            filtroEstado = filtros.filtroEstado,
            filtroPrioridad = filtros.filtroPrioridad,
            filtroEtiqueta = filtros.filtroEtiqueta,
            opcionOrdenamiento = filtros.opcionOrdenamiento,
            estaCargando = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = InicioUiState()
    )

    fun actualizarBusqueda(texto: String) {
        _filtros.update { it.copy(textoBusqueda = texto) }
    }

    fun actualizarFiltroEstado(estado: Boolean?) {
        _filtros.update { it.copy(filtroEstado = estado) }
    }

    fun actualizarFiltroPrioridad(prioridad: Prioridad?) {
        _filtros.update { it.copy(filtroPrioridad = prioridad) }
    }

    fun actualizarFiltroEtiqueta(etiqueta: Etiqueta?) {
        _filtros.update { it.copy(filtroEtiqueta = etiqueta) }
    }

    fun actualizarOrdenamiento(opcion: OpcionOrdenamiento) {
        _filtros.update { it.copy(opcionOrdenamiento = opcion) }
    }

    fun alternarEstadoTarea(tareaConEtiquetas: TareaConEtiquetas) {
        viewModelScope.launch {
            repositorioTareas.actualizarTarea(
                tareaConEtiquetas.tarea.copy(estaCompletada = !tareaConEtiquetas.tarea.estaCompletada)
            )
        }
    }

    private fun coincideConFiltros(tc: TareaConEtiquetas, filtros: FiltrosEstado): Boolean {
        val coincideBusqueda = filtros.textoBusqueda.isBlank() ||
            tc.tarea.titulo.contains(filtros.textoBusqueda, ignoreCase = true)
        val coincideEstado = filtros.filtroEstado == null ||
            tc.tarea.estaCompletada == filtros.filtroEstado
        val coincidePrioridad = filtros.filtroPrioridad == null ||
            tc.tarea.prioridad == filtros.filtroPrioridad
        val coincideEtiqueta = filtros.filtroEtiqueta == null ||
            tc.etiquetas.any { it.id == filtros.filtroEtiqueta.id }
        return coincideBusqueda && coincideEstado && coincidePrioridad && coincideEtiqueta
    }

    private fun aplicarOrdenamiento(
        lista: List<TareaConEtiquetas>,
        opcion: OpcionOrdenamiento
    ): List<TareaConEtiquetas> = when (opcion) {
        OpcionOrdenamiento.POR_VENCIMIENTO ->
            lista.sortedWith(compareBy(nullsLast()) { it.tarea.fechaVencimiento })
        OpcionOrdenamiento.POR_CREACION ->
            lista.sortedByDescending { it.tarea.fechaCreacion }
        OpcionOrdenamiento.POR_PRIORIDAD ->
            lista.sortedByDescending { it.tarea.prioridad.nivel }
        OpcionOrdenamiento.POR_TITULO ->
            lista.sortedBy { it.tarea.titulo }
    }
}

private data class FiltrosEstado(
    val textoBusqueda: String = "",
    val filtroEstado: Boolean? = null,
    val filtroPrioridad: Prioridad? = null,
    val filtroEtiqueta: Etiqueta? = null,
    val opcionOrdenamiento: OpcionOrdenamiento = OpcionOrdenamiento.POR_CREACION
)
