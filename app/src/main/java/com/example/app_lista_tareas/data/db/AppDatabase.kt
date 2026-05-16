package com.example.app_lista_tareas.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.app_lista_tareas.data.conversores.Conversores
import com.example.app_lista_tareas.data.daos.EtiquetaDao
import com.example.app_lista_tareas.data.daos.TareaDao
import com.example.app_lista_tareas.data.entidades.Etiqueta
import com.example.app_lista_tareas.data.entidades.Tarea
import com.example.app_lista_tareas.data.entidades.TareaEtiquetaReferencia

@Database(
    entities = [Tarea::class, Etiqueta::class, TareaEtiquetaReferencia::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Conversores::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tareaDao(): TareaDao
    abstract fun etiquetaDao(): EtiquetaDao

    companion object {
        const val NOMBRE_BASE_DATOS = "base_datos_tareas"
    }
}