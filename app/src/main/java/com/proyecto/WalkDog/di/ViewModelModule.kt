package com.proyecto.WalkDog.di

import com.google.firebase.firestore.FirebaseFirestore
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

    // Proveedor de RegisterViewModel
    @Provides
    @ViewModelScoped // Asegura que el ViewModel se mantenga durante el ciclo de vida
    fun provideRegisterViewModel(
        accountService: AccountService,  // Inyecta el servicio de autenticación
        logService: LogService,         // Inyecta el servicio de logs
        firestore: FirebaseFirestore    // Agrega FirebaseFirestore como parámetro
    ): RegisterViewModel {
        return RegisterViewModel(accountService, logService, firestore) // Pasa firestore al constructor
    }
}
