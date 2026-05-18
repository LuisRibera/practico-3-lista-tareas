package com.example.app_lista_tareas.di

import android.content.Context
import androidx.room.Room
import com.example.app_lista_tareas.data.daos.EtiquetaDao
import com.example.app_lista_tareas.data.daos.TareaDao
import com.example.app_lista_tareas.data.db.AppDatabase
import com.example.app_lista_tareas.repositorios.RepositorioEtiquetas
import com.example.app_lista_tareas.repositorios.RepositorioTareas
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.NOMBRE_BASE_DATOS
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideTareaDao(appDatabase: AppDatabase): TareaDao {
        return appDatabase.tareaDao()
    }

    @Provides
    @Singleton
    fun provideEtiquetaDao(appDatabase: AppDatabase): EtiquetaDao {
        return appDatabase.etiquetaDao()
    }

    @Provides
    @Singleton
    fun provideRepositorioTareas(tareaDao: TareaDao): RepositorioTareas {
        return RepositorioTareas(tareaDao)
    }

    @Provides
    @Singleton
    fun provideRepositorioEtiquetas(etiquetaDao: EtiquetaDao): RepositorioEtiquetas {
        return RepositorioEtiquetas(etiquetaDao)
    }
}