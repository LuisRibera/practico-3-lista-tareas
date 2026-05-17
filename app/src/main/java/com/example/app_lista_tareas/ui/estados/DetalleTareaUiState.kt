package com.example.app_lista_tareas.ui.estados

import com.example.app_lista_tareas.data.entidades.TareaConEtiquetas

data class DetalleTareaUiState(
    val tarea: TareaConEtiquetas? = null,
    val estaCargando: Boolean = true,
    val mensajeError: String? = null
)
