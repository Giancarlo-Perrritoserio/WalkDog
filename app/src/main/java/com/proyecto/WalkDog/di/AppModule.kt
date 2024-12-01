package com.proyecto.WalkDog.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.proyecto.WalkDog.data.service.AccountService
import com.proyecto.WalkDog.data.service.AccountServiceImpl
import com.proyecto.WalkDog.data.service.LogService
import com.proyecto.WalkDog.data.service.LogServiceImpl
import com.proyecto.WalkDog.data.service.LocationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Indica que este módulo se instalará en el contenedor de dependencias de la aplicación, con un ciclo de vida único (singleton)
object AppModule {

    // Proveedor de FirebaseAuth como dependencia. Este método se asegura de que solo haya una instancia de FirebaseAuth.
    @Provides
    @Singleton // Asegura que esta instancia de FirebaseAuth sea única en toda la aplicación (singleton)
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance() // Devuelve una instancia de FirebaseAuth configurada
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance() // Devuelve la instancia de Firestore
    }

    // El proveedor de AccountService
    @Provides
    @Singleton
    fun provideAccountService(
        firebaseAuth: FirebaseAuth, // FirebaseAuth ya está inyectado
        firestore: FirebaseFirestore // Ahora también se inyecta Firestore
    ): AccountService {
        return AccountServiceImpl(firebaseAuth, firestore) // Se pasa firestore a la implementación
    }

    // Proveedor de LogService, que se encarga de registrar eventos, errores e información.
    @Provides
    @Singleton // Asegura que solo haya una instancia de LogService en toda la aplicación
    fun provideLogService(): LogService {
        return LogServiceImpl() // Retorna una instancia de LogServiceImpl
    }

    // Proveedor de LocationService, que necesita el contexto de la aplicación para acceder a servicios de ubicación.
    @Provides
    @Singleton // Asegura que solo haya una instancia de LocationService en toda la aplicación
    fun provideLocationService(
        @ApplicationContext context: Context // Inyecta el contexto de la aplicación
    ): LocationService {
        return LocationService(context) // Retorna una instancia de LocationService usando el contexto de la aplicación
    }


}
