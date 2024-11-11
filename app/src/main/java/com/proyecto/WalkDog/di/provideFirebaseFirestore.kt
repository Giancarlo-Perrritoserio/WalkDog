package com.proyecto.WalkDog.di

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class) // Esto asegura que la instancia de FirebaseFirestore es un singleton, es decir, se comparte a través de la aplicación.
object FirebaseModule {

    // Proporciona la instancia de FirebaseFirestore, que es la base de datos de Firebase para almacenar y recuperar datos.
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance() // Obtiene la instancia de FirebaseFirestore para interactuar con Firestore en la aplicación.
    }
}
