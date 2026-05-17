package com.example.app_lista_tareas.data.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "etiquetas")
data class Etiqueta(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String
)