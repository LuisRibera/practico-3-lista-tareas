package com.example.app_lista_tareas.repositorios

import com.example.app_lista_tareas.data.daos.TareaDao
import com.example.app_lista_tareas.data.entidades.Tarea
import com.example.app_lista_tareas.data.entidades.TareaConEtiquetas
import com.example.app_lista_tareas.data.entidades.TareaEtiquetaReferencia
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositorioTareas @Inject constructor(
    private val tareaDao: TareaDao
) {
    fun obtenerTodasLasTareasConEtiquetas(): Flow<List<TareaConEtiquetas>> =
        tareaDao.obtenerTodasLasTareasConEtiquetas()

    fun obtenerTareasPendientesConEtiquetas(): Flow<List<TareaConEtiquetas>> =
        tareaDao.obtenerTareasPendientesConEtiquetas()

    fun buscarTareasPorTitulo(busqueda: String): Flow<List<TareaConEtiquetas>> =
        tareaDao.buscarTareasPorTitulo(busqueda)

    fun filtrarTareasPorEstado(estaCompletada: Boolean): Flow<List<TareaConEtiquetas>> =
        tareaDao.filtrarTareasPorEstado(estaCompletada)

    fun filtrarTareasPorPrioridad(prioridad: Int): Flow<List<TareaConEtiquetas>> =
        tareaDao.filtrarTareasPorPrioridad(prioridad)

    fun filtrarTareasPorEtiqueta(etiquetaId: Int): Flow<List<TareaConEtiquetas>> =
        tareaDao.filtrarTareasPorEtiqueta(etiquetaId)

    fun obtenerTareasOrdenadasPorVencimiento(): Flow<List<TareaConEtiquetas>> =
        tareaDao.obtenerTareasOrdenadasPorVencimiento()

    fun obtenerTareasOrdenadasPorCreacion(): Flow<List<TareaConEtiquetas>> =
        tareaDao.obtenerTareasOrdenadasPorCreacion()

    fun obtenerTareasOrdenadasPorPrioridad(): Flow<List<TareaConEtiquetas>> =
        tareaDao.obtenerTareasOrdenadasPorPrioridad()

    fun obtenerTareasOrdenadasPorTitulo(): Flow<List<TareaConEtiquetas>> =
        tareaDao.obtenerTareasOrdenadasPorTitulo()

    fun obtenerTareaConEtiquetasPorId(id: Int): Flow<TareaConEtiquetas?> =
        tareaDao.obtenerTareaConEtiquetasPorId(id)

    suspend fun insertarTarea(tarea: Tarea): Long =
        tareaDao.insertarTarea(tarea)

    suspend fun actualizarTarea(tarea: Tarea) =
        tareaDao.actualizarTarea(tarea)

    suspend fun eliminarTarea(tarea: Tarea) =
        tareaDao.eliminarTarea(tarea)

    suspend fun asociarEtiquetaATarea(tareaId: Int, etiquetaId: Int) {
        tareaDao.insertarTareaEtiquetaReferencia(TareaEtiquetaReferencia(tareaId, etiquetaId))
    }

    suspend fun desasociarEtiquetasDeTarea(tareaId: Int) {
        tareaDao.eliminarReferenciasPorTareaId(tareaId)
    }

    suspend fun desasociarEtiquetaEspecifica(tareaId: Int, etiquetaId: Int) {
        tareaDao.eliminarReferenciaEspecifica(tareaId, etiquetaId)
    }
}