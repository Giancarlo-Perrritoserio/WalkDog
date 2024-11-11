package com.proyecto.WalkDog.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.proyecto.WalkDog.data.model.LoginUiState
import com.proyecto.WalkDog.data.model.User
import com.proyecto.WalkDog.data.service.AccountService
import com.proyecto.WalkDog.data.service.LogService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val accountService: AccountService, // Servicio para manejar la autenticación de usuario.
    private val logService: LogService // Servicio para registrar eventos y errores.
) : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance() // Obtenemos la instancia de FirebaseAuth para gestionar la autenticación con Firebase.

    // Estado mutable que mantiene la información del usuario autenticado.
    var user by mutableStateOf<User?>(null)
        private set

    // Estado mutable que mantiene la información del formulario de inicio de sesión.
    var uiState by mutableStateOf(LoginUiState())
        private set

    // Se llama cuando cambia el email ingresado en el formulario.
    fun onEmailChange(newValue: String) {
        uiState = uiState.copy(email = newValue) // Actualiza el estado con el nuevo email ingresado.
    }

    // Se llama cuando cambia la contraseña ingresada en el formulario.
    fun onPasswordChange(newValue: String) {
        uiState = uiState.copy(password = newValue) // Actualiza el estado con la nueva contraseña ingresada.
    }

    // Llamado cuando el usuario hace clic en "Iniciar sesión"
    fun onSignInClick(
        onSuccess: () -> Unit, // Callback en caso de éxito
        onError: (String) -> Unit // Callback en caso de error
    ) {
        val email = uiState.email // Obtiene el email del estado
        val password = uiState.password // Obtiene la contraseña del estado
        if (email.isBlank() || password.isBlank()) { // Valida que el email y la contraseña no estén vacíos
            onError("Email and password cannot be empty.") // Si están vacíos, muestra un mensaje de error
            return
        }

        viewModelScope.launch { // Inicia una coroutine para ejecutar la lógica de inicio de sesión en un hilo separado
            try {
                accountService.authenticate(email, password) // Llama al servicio de autenticación para iniciar sesión
                val currentUser = auth.currentUser // Obtiene el usuario autenticado de Firebase
                currentUser?.let { // Si hay un usuario autenticado
                    user = User(uid = it.uid, email = it.email ?: "", name = it.displayName) // Almacena la información del usuario autenticado
                    onSuccess() // Llama al callback de éxito
                }
            } catch (e: Exception) {
                onError("Login failed: ${e.message}") // Si ocurre un error, llama al callback de error
            }
        }
    }

    // Segunda versión del método onSignInClick, parece duplicado.
    fun onSignInClick(email: String, password: String) {
        logService.logEvent("Intentando iniciar sesión con email: $email") // Registra un evento de intento de inicio de sesión.

        // lógica de autenticación... (suponemos que está incompleta aquí)

        logService.logInfo("Inicio de sesión exitoso para $email") // Registra un mensaje informativo sobre un inicio de sesión exitoso.
    }

    // Método para validar el formato del email.
    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() // Verifica si el email es válido utilizando un patrón de expresión regular.
    }
}
