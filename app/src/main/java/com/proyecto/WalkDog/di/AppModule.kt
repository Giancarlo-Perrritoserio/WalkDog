package com.proyecto.WalkDog.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
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
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideAccountService(
        firebaseAuth: FirebaseAuth
    ): AccountService {
        return AccountServiceImpl(firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideLogService(): LogService {
        return LogServiceImpl()
    }

    @Provides
    @Singleton
    fun provideLocationService(
        @ApplicationContext context: Context
    ): LocationService {
        return LocationService(context)
    }
}
