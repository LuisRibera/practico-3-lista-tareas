package com.example.app_lista_tareas.ui.estados

import com.example.app_lista_tareas.data.entidades.Etiqueta
import com.example.app_lista_tareas.data.entidades.Prioridad
import com.example.app_lista_tareas.data.entidades.TareaConEtiquetas

enum class OpcionOrdenamiento {
    POR_VENCIMIENTO,
    POR_CREACION,
    POR_PRIORIDAD,
    POR_TITULO
}

data class InicioUiState(
    val listaTareas: List<TareaConEtiquetas> = emptyList(),
    val etiquetasDisponibles: List<Etiqueta> = emptyList(),
    val textoBusqueda: String = "",
    val filtroEstado: Boolean? = null,
    val filtroPrioridad: Prioridad? = null,
    val filtroEtiqueta: Etiqueta? = null,
    val opcionOrdenamiento: OpcionOrdenamiento = OpcionOrdenamiento.POR_CREACION,
    val estaCargando: Boolean = true,
    val mensajeError: String? = null
)
