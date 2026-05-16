package com.example.app_lista_tareas.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.app_lista_tareas.data.entidades.Etiqueta
import kotlinx.coroutines.flow.Flow

@Dao
interface EtiquetaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarEtiqueta(etiqueta: Etiqueta)

    @Query("SELECT * FROM etiquetas")
    fun obtenerTodasLasEtiquetas(): Flow<List<Etiqueta>>

    @Delete
    suspend fun eliminarEtiqueta(etiqueta: Etiqueta)
}