package com.example.app_lista_tareas.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.app_lista_tareas.data.entidades.Tarea
import com.example.app_lista_tareas.data.entidades.TareaConEtiquetas
import com.example.app_lista_tareas.data.entidades.TareaEtiquetaReferencia
import kotlinx.coroutines.flow.Flow

@Dao
interface TareaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTarea(tarea: Tarea): Long

    @Update
    suspend fun actualizarTarea(tarea: Tarea)

    @Delete
    suspend fun eliminarTarea(tarea: Tarea)

    @Transaction
    @Query("SELECT * FROM tareas WHERE id = :id")
    fun obtenerTareaConEtiquetasPorId(id: Int): Flow<TareaConEtiquetas?>

    @Transaction
    @Query("SELECT * FROM tareas")
    fun obtenerTodasLasTareasConEtiquetas(): Flow<List<TareaConEtiquetas>>

    @Transaction
    @Query("SELECT * FROM tareas WHERE estaCompletada = 0")
    fun obtenerTareasPendientesConEtiquetas(): Flow<List<TareaConEtiquetas>>

    @Transaction
    @Query("SELECT * FROM tareas WHERE titulo LIKE '%' || :busqueda || '%'")
    fun buscarTareasPorTitulo(busqueda: String): Flow<List<TareaConEtiquetas>>

    @Transaction
    @Query("SELECT * FROM tareas WHERE estaCompletada = :estaCompletada")
    fun filtrarTareasPorEstado(estaCompletada: Boolean): Flow<List<TareaConEtiquetas>>

    @Transaction
    @Query("SELECT * FROM tareas WHERE prioridad = :nivelPrioridad")
    fun filtrarTareasPorPrioridad(nivelPrioridad: Int): Flow<List<TareaConEtiquetas>>

    @Transaction
    @Query("SELECT t.* FROM tareas t INNER JOIN tarea_etiqueta_referencia r ON t.id = r.tareaId WHERE r.etiquetaId = :etiquetaId")
    fun filtrarTareasPorEtiqueta(etiquetaId: Int): Flow<List<TareaConEtiquetas>>

    @Transaction
    @Query("SELECT * FROM tareas ORDER BY CASE WHEN fechaVencimiento IS NULL THEN 1 ELSE 0 END, fechaVencimiento ASC")
    fun obtenerTareasOrdenadasPorVencimiento(): Flow<List<TareaConEtiquetas>>

    @Transaction
    @Query("SELECT * FROM tareas ORDER BY fechaCreacion DESC")
    fun obtenerTareasOrdenadasPorCreacion(): Flow<List<TareaConEtiquetas>>

    @Transaction
    @Query("SELECT * FROM tareas ORDER BY prioridad DESC")
    fun obtenerTareasOrdenadasPorPrioridad(): Flow<List<TareaConEtiquetas>>

    @Transaction
    @Query("SELECT * FROM tareas ORDER BY titulo ASC")
    fun obtenerTareasOrdenadasPorTitulo(): Flow<List<TareaConEtiquetas>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTareaEtiquetaReferencia(referencia: TareaEtiquetaReferencia)

    @Query("DELETE FROM tarea_etiqueta_referencia WHERE tareaId = :tareaId")
    suspend fun eliminarReferenciasPorTareaId(tareaId: Int)

    @Query("DELETE FROM tarea_etiqueta_referencia WHERE tareaId = :tareaId AND etiquetaId = :etiquetaId")
    suspend fun eliminarReferenciaEspecifica(tareaId: Int, etiquetaId: Int)
}