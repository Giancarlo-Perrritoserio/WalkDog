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
@InstallIn(ViewModelComponent::class) // Este módulo se instala en el componente ViewModel, lo que asegura que las instancias proporcionadas se vivan durante el ciclo de vida de los ViewModels.
object ViewModelModule {

    // Proporciona la instancia de LoginViewModel, que maneja la lógica de la vista de inicio de sesión.
    @Provides
    @ViewModelScoped // Esto asegura que el ViewModel sea único para cada ViewModel en la aplicación y se conserve durante el ciclo de vida del ViewModel.
    fun provideLoginViewModel(
        accountService: AccountService, // Inyecta el servicio de autenticación de usuarios
        logService: LogService // Inyecta el servicio para registrar eventos y errores
    ): LoginViewModel {
        return LoginViewModel(accountService, logService) // Crea y devuelve el ViewModel de Login
    }

    // Proporciona la instancia de RegisterViewModel, que maneja la lógica de la vista de registro de usuario.
    @Provides
    @ViewModelScoped // Esto asegura que el ViewModel sea único para cada ViewModel en la aplicación y se conserve durante el ciclo de vida del ViewModel.
    fun provideRegisterViewModel(
        accountService: AccountService, // Inyecta el servicio de autenticación de usuarios
        logService: LogService // Inyecta el servicio para registrar eventos y errores
    ): RegisterViewModel {
        return RegisterViewModel(accountService, logService) // Crea y devuelve el ViewModel de Registro
    }
}
