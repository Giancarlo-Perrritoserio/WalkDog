package com.proyecto.WalkDog.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.proyecto.WalkDog.data.service.AccountService
import com.proyecto.WalkDog.data.service.AccountServiceImpl
import com.proyecto.WalkDog.ui.login.LoginViewModel
import com.proyecto.WalkDog.ui.register.RegisterViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.scopes.ViewModelScoped
import com.proyecto.WalkDog.data.service.LogService
import com.proyecto.WalkDog.data.service.LogServiceImpl
import dagger.Binds
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideAccountService(firebaseAuth: FirebaseAuth): AccountService {
        return AccountServiceImpl(firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideLogService(): LogService {
        return LogServiceImpl() // Implementación de LogService
    }

}
