package com.example.app_lista_tareas.repositorios

import com.example.app_lista_tareas.data.daos.EtiquetaDao
import com.example.app_lista_tareas.data.entidades.Etiqueta
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositorioEtiquetas @Inject constructor(
    private val etiquetaDao: EtiquetaDao
) {
    fun obtenerTodasLasEtiquetas(): Flow<List<Etiqueta>> =
        etiquetaDao.obtenerTodasLasEtiquetas()

    suspend fun insertarEtiqueta(etiqueta: Etiqueta) =
        etiquetaDao.insertarEtiqueta(etiqueta)

    suspend fun eliminarEtiqueta(etiqueta: Etiqueta) =
        etiquetaDao.eliminarEtiqueta(etiqueta)
}