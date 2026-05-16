package com.example.app_lista_tareas.data.entidades

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "tarea_etiqueta_referencia",
    primaryKeys = ["tareaId", "etiquetaId"],
    foreignKeys = [
        ForeignKey(
            entity = Tarea::class,
            parentColumns = ["id"],
            childColumns = ["tareaId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Etiqueta::class,
            parentColumns = ["id"],
            childColumns = ["etiquetaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("tareaId"), Index("etiquetaId")]
)
data class TareaEtiquetaReferencia(
    val tareaId: Int,
    val etiquetaId: Int
)