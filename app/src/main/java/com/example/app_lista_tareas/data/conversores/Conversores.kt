package com.example.app_lista_tareas.data.conversores

import androidx.room.TypeConverter
import com.example.app_lista_tareas.data.entidades.Prioridad

class Conversores {
    @TypeConverter
    fun dePrioridad(prioridad: Prioridad): Int {
        return prioridad.nivel
    }

    @TypeConverter
    fun aPrioridad(nivel: Int): Prioridad {
        return when (nivel) {
            3 -> Prioridad.ALTA
            2 -> Prioridad.MEDIA
            1 -> Prioridad.BAJA
            else -> Prioridad.BAJA
        }
    }
}