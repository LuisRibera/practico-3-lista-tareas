package com.example.app_lista_tareas.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_lista_tareas.data.entidades.Prioridad
import com.example.app_lista_tareas.data.entidades.Tarea
import com.example.app_lista_tareas.repositorios.RepositorioEtiquetas
import com.example.app_lista_tareas.repositorios.RepositorioTareas
import com.example.app_lista_tareas.ui.estados.FormularioTareaUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FormularioTareaViewModel @Inject constructor(
    private val repositorioTareas: RepositorioTareas,
    private val repositorioEtiquetas: RepositorioEtiquetas,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val tareaId: Int = savedStateHandle.get<Int>("tareaId") ?: 0

    private val _estadoUi = MutableStateFlow(FormularioTareaUiState())
    val estadoUi = _estadoUi.asStateFlow()

    init {
        observarEtiquetasDisponibles()
        if (tareaId != 0) cargarTareaExistente(tareaId)
    }

    private fun observarEtiquetasDisponibles() {
        viewModelScope.launch {
            repositorioEtiquetas.obtenerTodasLasEtiquetas().collect { etiquetas ->
                _estadoUi.update { it.copy(etiquetasDisponibles = etiquetas) }
            }
        }
    }

    private fun cargarTareaExistente(id: Int) {
        viewModelScope.launch {
            val tareaConEtiquetas = repositorioTareas.obtenerTareaConEtiquetasPorId(id).first()
                ?: return@launch
            _estadoUi.update {
                it.copy(
                    tareaId = tareaConEtiquetas.tarea.id,
                    titulo = tareaConEtiquetas.tarea.titulo,
                    descripcion = tareaConEtiquetas.tarea.descripcion ?: "",
                    prioridadSeleccionada = tareaConEtiquetas.tarea.prioridad,
                    fechaVencimiento = tareaConEtiquetas.tarea.fechaVencimiento,
                    etiquetasSeleccionadas = tareaConEtiquetas.etiquetas.map { e -> e.id }.toSet(),
                    modoEdicion = true
                )
            }
        }
    }

    fun actualizarTitulo(titulo: String) {
        _estadoUi.update { it.copy(titulo = titulo, mensajeErrorTitulo = null) }
    }

    fun actualizarDescripcion(descripcion: String) {
        _estadoUi.update { it.copy(descripcion = descripcion) }
    }

    fun actualizarPrioridad(prioridad: Prioridad) {
        _estadoUi.update { it.copy(prioridadSeleccionada = prioridad) }
    }

    fun actualizarFechaVencimiento(fecha: Long?) {
        _estadoUi.update { it.copy(fechaVencimiento = fecha) }
    }

    fun alternarEtiqueta(etiquetaId: Int) {
        _estadoUi.update { estado ->
            val nuevasSeleccionadas = if (etiquetaId in estado.etiquetasSeleccionadas) {
                estado.etiquetasSeleccionadas - etiquetaId
            } else {
                estado.etiquetasSeleccionadas + etiquetaId
            }
            estado.copy(etiquetasSeleccionadas = nuevasSeleccionadas)
        }
    }

    fun guardarTarea() {
        val estado = _estadoUi.value
        if (estado.titulo.isBlank()) {
            _estadoUi.update { it.copy(mensajeErrorTitulo = "El título es obligatorio") }
            return
        }

        viewModelScope.launch {
            _estadoUi.update { it.copy(estaGuardando = true) }

            if (estado.modoEdicion && estado.tareaId != null) {
                val tareaActual = repositorioTareas
                    .obtenerTareaConEtiquetasPorId(estado.tareaId).first()?.tarea
                    ?: return@launch
                repositorioTareas.actualizarTarea(
                    tareaActual.copy(
                        titulo = estado.titulo.trim(),
                        descripcion = estado.descripcion.trim().ifBlank { null },
                        fechaVencimiento = estado.fechaVencimiento,
                        prioridad = estado.prioridadSeleccionada
                    )
                )
                repositorioTareas.desasociarEtiquetasDeTarea(estado.tareaId)
                estado.etiquetasSeleccionadas.forEach { etiquetaId ->
                    repositorioTareas.asociarEtiquetaATarea(estado.tareaId, etiquetaId)
                }
            } else {
                val nuevoId = repositorioTareas.insertarTarea(
                    Tarea(
                        titulo = estado.titulo.trim(),
                        descripcion = estado.descripcion.trim().ifBlank { null },
                        fechaVencimiento = estado.fechaVencimiento,
                        prioridad = estado.prioridadSeleccionada
                    )
                ).toInt()
                estado.etiquetasSeleccionadas.forEach { etiquetaId ->
                    repositorioTareas.asociarEtiquetaATarea(nuevoId, etiquetaId)
                }
            }

            _estadoUi.update { it.copy(estaGuardando = false, guardadoExitoso = true) }
        }
    }

    fun eliminarTarea() {
        val estado = _estadoUi.value
        if (!estado.modoEdicion || estado.tareaId == null) return

        viewModelScope.launch {
            val tareaConEtiquetas = repositorioTareas
                .obtenerTareaConEtiquetasPorId(estado.tareaId).first()
                ?: return@launch
            repositorioTareas.eliminarTarea(tareaConEtiquetas.tarea)
            _estadoUi.update { it.copy(guardadoExitoso = true) }
        }
    }
}
