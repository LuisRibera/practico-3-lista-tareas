package com.example.app_lista_tareas.data.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tareas")
data class Tarea(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val descripcion: String,
    val fechaVencimiento: Long?,
    val prioridad: Prioridad,
    val estaCompletada: Boolean = false,
    val fechaCreacion: Long = System.currentTimeMillis()
)