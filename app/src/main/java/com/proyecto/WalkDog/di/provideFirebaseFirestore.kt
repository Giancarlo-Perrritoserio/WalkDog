package com.proyecto.WalkDog.di

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class) // Esto asegura que la instancia de FirebaseFirestore es un singleton
object FirebaseModule {

    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance() // Obtienes la instancia de FirebaseFirestore
    }
}
