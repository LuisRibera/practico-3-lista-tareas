package com.example.app_lista_tareas.data.entidades

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class TareaConEtiquetas(
    @Embedded val tarea: Tarea,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = TareaEtiquetaReferencia::class,
            parentColumn = "tareaId",
            entityColumn = "etiquetaId"
        )
    )
    val etiquetas: List<Etiqueta>
)