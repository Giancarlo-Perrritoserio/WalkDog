package com.proyecto.WalkDog.di

import com.proyecto.WalkDog.utils.AudioUploader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AudioUploaderModule {

    @Provides
    @Singleton
    fun provideAudioUploader(): AudioUploader {
        return AudioUploader()
    }
}
