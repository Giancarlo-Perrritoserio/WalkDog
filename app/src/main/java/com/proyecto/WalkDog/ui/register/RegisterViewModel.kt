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
    private val accountService: AccountService,
    private val logService: LogService // Inyectado y usado
) : ViewModel() {

    var uiState by mutableStateOf(RegisterUiState())
        private set

    fun onEmailChange(newValue: String) {
        uiState = uiState.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState = uiState.copy(password = newValue)
    }

    fun onRegisterClick(
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
                accountService.createUser(email, password)
                logService.logEvent("User registered with email: $email") // Ejemplo de uso
                onSuccess()
            } catch (e: Exception) {
                logService.logEvent("Registration failed: ${e.message}")
                onError("Registration failed: ${e.message}")
            }
        }
    }
}
