package com.example.app_lista_tareas.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_lista_tareas.data.entidades.Etiqueta
import com.example.app_lista_tareas.repositorios.RepositorioEtiquetas
import com.example.app_lista_tareas.ui.estados.EtiquetasUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EtiquetasViewModel @Inject constructor(
    private val repositorioEtiquetas: RepositorioEtiquetas
) : ViewModel() {

    private val _estadoUi = MutableStateFlow(EtiquetasUiState())
    val estadoUi = _estadoUi.asStateFlow()

    init {
        viewModelScope.launch {
            repositorioEtiquetas.obtenerTodasLasEtiquetas().collect { etiquetas ->
                _estadoUi.update { it.copy(listaEtiquetas = etiquetas) }
            }
        }
    }

    fun actualizarNombreNuevaEtiqueta(nombre: String) {
        _estadoUi.update { it.copy(nombreNuevaEtiqueta = nombre, mensajeError = null) }
    }

    fun agregarEtiqueta() {
        val nombre = _estadoUi.value.nombreNuevaEtiqueta.trim()
        if (nombre.isBlank()) {
            _estadoUi.update { it.copy(mensajeError = "El nombre de la etiqueta es obligatorio") }
            return
        }
        viewModelScope.launch {
            _estadoUi.update { it.copy(estaGuardando = true) }
            repositorioEtiquetas.insertarEtiqueta(Etiqueta(nombre = nombre))
            _estadoUi.update { it.copy(estaGuardando = false, nombreNuevaEtiqueta = "") }
        }
    }

    fun eliminarEtiqueta(etiqueta: Etiqueta) {
        viewModelScope.launch {
            repositorioEtiquetas.eliminarEtiqueta(etiqueta)
        }
    }
}
