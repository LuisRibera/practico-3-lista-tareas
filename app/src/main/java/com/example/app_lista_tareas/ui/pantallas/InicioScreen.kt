package com.example.app_lista_tareas.ui.pantallas

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.app_lista_tareas.ui.viewmodels.InicioViewModel

@Composable
fun InicioScreen(
    onNuevaTarea: () -> Unit,
    onTareaClick: (Int) -> Unit,
    viewModel: InicioViewModel = hiltViewModel()
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Pantalla de Inicio — Fase 5")
    }
}
