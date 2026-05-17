package com.example.app_lista_tareas.ui.estados

import com.example.app_lista_tareas.data.entidades.Etiqueta

data class EtiquetasUiState(
    val listaEtiquetas: List<Etiqueta> = emptyList(),
    val nombreNuevaEtiqueta: String = "",
    val mensajeError: String? = null,
    val estaGuardando: Boolean = false
)
