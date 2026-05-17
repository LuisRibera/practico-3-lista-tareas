package com.example.app_lista_tareas.ui.estados

import com.example.app_lista_tareas.data.entidades.Etiqueta
import com.example.app_lista_tareas.data.entidades.Prioridad

data class FormularioTareaUiState(
    val tareaId: Int? = null,
    val titulo: String = "",
    val descripcion: String = "",
    val prioridadSeleccionada: Prioridad = Prioridad.MEDIA,
    val fechaVencimiento: Long? = null,
    val etiquetasDisponibles: List<Etiqueta> = emptyList(),
    val etiquetasSeleccionadas: Set<Int> = emptySet(),
    val mensajeErrorTitulo: String? = null,
    val estaGuardando: Boolean = false,
    val modoEdicion: Boolean = false,
    val guardadoExitoso: Boolean = false
)
