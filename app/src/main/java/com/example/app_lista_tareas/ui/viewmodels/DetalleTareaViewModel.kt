package com.example.app_lista_tareas.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_lista_tareas.repositorios.RepositorioTareas
import com.example.app_lista_tareas.ui.estados.DetalleTareaUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DetalleTareaViewModel @Inject constructor(
    private val repositorioTareas: RepositorioTareas,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val tareaId: Int = checkNotNull(savedStateHandle["tareaId"])

    val estadoUi = repositorioTareas
        .obtenerTareaConEtiquetasPorId(tareaId)
        .map { tarea -> DetalleTareaUiState(tarea = tarea, estaCargando = false) }
        .catch { e -> emit(DetalleTareaUiState(estaCargando = false, mensajeError = e.message)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DetalleTareaUiState()
        )
}
