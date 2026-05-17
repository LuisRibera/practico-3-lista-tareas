package com.example.app_lista_tareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.app_lista_tareas.ui.navegacion.NavGraph
import com.example.app_lista_tareas.ui.theme.AppListaTareasTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppListaTareasTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}