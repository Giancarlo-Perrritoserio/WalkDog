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
    private val accountService: AccountService,
    private val logService: LogService
) : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance() // Obtenemos la instancia de FirebaseAuth

    // Esto puede ser un LiveData o State para almacenar el usuario autenticado
    var user by mutableStateOf<User?>(null)
        private set

    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onEmailChange(newValue: String) {
        uiState = uiState.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState = uiState.copy(password = newValue)
    }

    fun onSignInClick(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val email = uiState.email
        val password = uiState.password
        if (email.isBlank() || password.isBlank()) {
            onError("Email and password cannot be empty.")
            return
        }

        viewModelScope.launch {
            try {
                accountService.authenticate(email, password)
                val currentUser = auth.currentUser // Obtener el usuario autenticado de Firebase
                currentUser?.let {
                    user = User(uid = it.uid, email = it.email ?: "", name = it.displayName)
                    onSuccess() // Aquí pasa lo que necesitas hacer después de iniciar sesión
                }
            } catch (e: Exception) {
                onError("Login failed: ${e.message}")
            }
        }
    }
    fun onSignInClick(email: String, password: String) {
        logService.logEvent("Intentando iniciar sesión con email: $email")

        // lógica de autenticación...

        logService.logInfo("Inicio de sesión exitoso para $email")
    }
    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


}
