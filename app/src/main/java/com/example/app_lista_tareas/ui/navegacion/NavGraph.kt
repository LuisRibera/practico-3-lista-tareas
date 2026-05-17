package com.example.app_lista_tareas.ui.navegacion

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.app_lista_tareas.ui.pantallas.DetalleTareaScreen
import com.example.app_lista_tareas.ui.pantallas.FormularioTareaScreen
import com.example.app_lista_tareas.ui.pantallas.GestionEtiquetasScreen
import com.example.app_lista_tareas.ui.pantallas.InicioScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Rutas.Inicio.ruta
    ) {
        composable(route = Rutas.Inicio.ruta) {
            InicioScreen(
                onNuevaTarea = {
                    navController.navigate(Rutas.FormularioTarea.crearRuta())
                },
                onTareaClick = { tareaId ->
                    navController.navigate(Rutas.DetalleTarea.crearRuta(tareaId))
                }
            )
        }

        composable(
            route = Rutas.DetalleTarea.ruta,
            arguments = listOf(
                navArgument("tareaId") { type = NavType.IntType }
            )
        ) {
            DetalleTareaScreen(
                onEditarTarea = { tareaId ->
                    navController.navigate(Rutas.FormularioTarea.crearRuta(tareaId))
                }
            )
        }

        composable(
            route = Rutas.FormularioTarea.ruta,
            arguments = listOf(
                navArgument("tareaId") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) {
            FormularioTareaScreen(
                onNavigateBack = { navController.popBackStack() },
                onGestionarEtiquetas = {
                    navController.navigate(Rutas.GestionEtiquetas.ruta)
                }
            )
        }

        composable(route = Rutas.GestionEtiquetas.ruta) {
            GestionEtiquetasScreen()
        }
    }
}
