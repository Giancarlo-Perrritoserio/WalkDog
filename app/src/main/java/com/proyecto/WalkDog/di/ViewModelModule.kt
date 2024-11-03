package com.proyecto.WalkDog.di

import com.proyecto.WalkDog.data.service.AccountService
import com.proyecto.WalkDog.data.service.LogService
import com.proyecto.WalkDog.ui.login.LoginViewModel
import com.proyecto.WalkDog.ui.register.RegisterViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    @ViewModelScoped
    fun provideLoginViewModel(
        accountService: AccountService,
        logService: LogService
    ): LoginViewModel {
        return LoginViewModel(accountService, logService)
    }

    @Provides
    @ViewModelScoped
    fun provideRegisterViewModel(
        accountService: AccountService,
        logService: LogService
    ): RegisterViewModel {
        return RegisterViewModel(accountService, logService)
    }
}
