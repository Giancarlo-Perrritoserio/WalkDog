package com.proyecto.WalkDog.di

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class) // Esto asegura que la instancia de FirebaseFirestore es un singleton, es decir, se comparte a través de la aplicación.
object FirebaseModule {

    // Elimina este método porque ya está siendo proporcionado en AppModule.
    // @Provides
    // fun provideFirebaseFirestore(): FirebaseFirestore {
    //    return FirebaseFirestore.getInstance()
    // }
}
