package com.proyecto.WalkDog.di

import com.proyecto.WalkDog.utils.AudioUploader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Este módulo se instala en el contenedor de dependencias de la aplicación y tiene un ciclo de vida de "singleton".
object AudioUploaderModule {

    // Proporciona una instancia única de AudioUploader, que se encarga de la subida de archivos de audio a Firebase Storage.
    @Provides
    @Singleton // Única instancia de AudioUploader en toda la app
    fun provideAudioUploader(): AudioUploader {
        return AudioUploader() // Crea y retorna la instancia de AudioUploader
    }
}
