package com.proyecto.WalkDog.ui.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.WalkDog.data.model.RegisterUiState
import com.proyecto.WalkDog.data.service.AccountService
import com.proyecto.WalkDog.data.service.LogService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val accountService: AccountService,  // Inyectamos AccountService para manejar la creación de usuarios
    private val logService: LogService // Inyectamos LogService para realizar el registro de eventos, útil para auditoría y depuración
) : ViewModel() {

    var uiState by mutableStateOf(RegisterUiState())  // Mantiene el estado de la UI (email y password) para el registro de usuario
        private set

    // Método que actualiza el email en el estado UI cada vez que el usuario lo cambia
    fun onEmailChange(newValue: String) {
        uiState = uiState.copy(email = newValue)  // Actualiza el valor del email en el estado
    }

    // Método que actualiza la contraseña en el estado UI cada vez que el usuario lo cambia
    fun onPasswordChange(newValue: String) {
        uiState = uiState.copy(password = newValue)  // Actualiza el valor de la contraseña en el estado
    }

    // Método llamado cuando el usuario hace clic en el botón de registro
    fun onRegisterClick(
        onSuccess: () -> Unit,  // Callback en caso de éxito
        onError: (String) -> Unit  // Callback en caso de error
    ) {
        val email = uiState.email  // Obtiene el email ingresado
        val password = uiState.password  // Obtiene la contraseña ingresada

        // Verificación de que los campos de email y contraseña no estén vacíos
        if (email.isBlank() || password.isBlank()) {
            onError("Email and password cannot be empty.")  // Si están vacíos, muestra un mensaje de error
            return
        }

        // Llamada asíncrona para registrar al usuario
        viewModelScope.launch {
            try {
                // Intentamos crear al usuario en Firebase
                accountService.createUser(email, password)  // Llama al servicio de cuenta para crear un nuevo usuario
                logService.logEvent("User registered with email: $email")  // Registra el evento de creación de usuario en LogService
                onSuccess()  // Si todo sale bien, ejecuta el callback de éxito
            } catch (e: Exception) {
                // En caso de error, logeamos el error y mostramos el mensaje correspondiente
                logService.logEvent("Registration failed: ${e.message}")  // Registra el error en LogService
                onError("Registration failed: ${e.message}")  // Llama al callback de error con el mensaje
            }
        }
    }
}
