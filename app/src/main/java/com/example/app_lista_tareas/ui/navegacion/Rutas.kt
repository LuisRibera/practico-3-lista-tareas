package com.example.app_lista_tareas.ui.navegacion

sealed class Rutas(val ruta: String) {

    data object Inicio : Rutas("inicio")

    data object DetalleTarea : Rutas("detalle_tarea/{tareaId}") {
        fun crearRuta(tareaId: Int) = "detalle_tarea/$tareaId"
    }

    data object FormularioTarea : Rutas("formulario_tarea?tareaId={tareaId}") {
        fun crearRuta(tareaId: Int = 0) = "formulario_tarea?tareaId=$tareaId"
    }

    data object GestionEtiquetas : Rutas("gestion_etiquetas")
}
