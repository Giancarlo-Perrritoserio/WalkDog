package com.proyecto.WalkDog.ui.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.proyecto.WalkDog.data.model.RegisterUiState
import com.proyecto.WalkDog.data.service.AccountService
import com.proyecto.WalkDog.data.service.LogService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.google.firebase.firestore.FieldValue  // Importa FieldValue para usar serverTimestamp

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val accountService: AccountService,  // Inyectamos AccountService para manejar la creación de usuarios
    private val logService: LogService,  // Inyectamos LogService para realizar el registro de eventos
    private val firestore: FirebaseFirestore  // Inyectamos FirebaseFirestore para guardar datos en Firestore
) : ViewModel() {

    var uiState by mutableStateOf(RegisterUiState())  // Mantiene el estado de la UI (email, password, nombre, apellido, etc.)
        private set

    // Método que actualiza el email en el estado UI
    fun onEmailChange(newValue: String) {
        uiState = uiState.copy(email = newValue)  // Actualiza el valor del email
    }

    // Método que actualiza la contraseña en el estado UI
    fun onPasswordChange(newValue: String) {
        uiState = uiState.copy(password = newValue)  // Actualiza el valor de la contraseña
    }

    // Método que actualiza el nombre en el estado UI
    fun onNameChange(newValue: String) {
        uiState = uiState.copy(name = newValue)  // Actualiza el valor del nombre
    }

    // Método que actualiza el apellido en el estado UI
    fun onLastNameChange(newValue: String) {
        uiState = uiState.copy(lastName = newValue)  // Actualiza el valor del apellido
    }

    // Método que actualiza el nombre de usuario (nickname) en el estado UI
    fun onUsernameChange(newValue: String) {
        uiState = uiState.copy(username = newValue)  // Actualiza el valor del nombre de usuario
    }

    // Método llamado cuando el usuario hace clic en el botón de registro
    fun onRegisterClick(
        onSuccess: () -> Unit,  // Callback en caso de éxito
        onError: (String) -> Unit  // Callback en caso de error
    ) {
        val email = uiState.email  // Obtiene el email ingresado
        val password = uiState.password  // Obtiene la contraseña ingresada
        val name = uiState.name  // Obtiene el nombre ingresado
        val lastName = uiState.lastName  // Obtiene el apellido ingresado
        val username = uiState.username  // Obtiene el nombre de usuario (nickname)

        // Verificación de que los campos necesarios no estén vacíos
        if (email.isBlank() || password.isBlank() || name.isBlank() || lastName.isBlank() || username.isBlank()) {
            onError("Todos los campos son obligatorios.")  // Si están vacíos, muestra un mensaje de error
            return
        }

        // Llamada asíncrona para registrar al usuario
        viewModelScope.launch {
            try {
                // Intentamos crear al usuario en Firebase Authentication
                val authResult = accountService.createUser(email, password)  // Crea el usuario en Firebase Authentication
                val user = authResult.user  // Ahora debería funcionar si authResult es un AuthResult

                if (user != null) {
                    // Guardamos los datos adicionales del usuario en Firestore
                    val userData = mapOf(
                        "email" to email,
                        "name" to name,
                        "lastName" to lastName,
                        "username" to username,
                        "uid" to user.uid,
                        "createdAt" to FieldValue.serverTimestamp()  // Timestamp de creación
                    )

                    // Guardamos los datos del usuario en Firestore en la colección 'users'
                    firestore.collection("users")
                        .document(user.uid)  // Usamos el UID del usuario como identificador
                        .set(userData)
                        .addOnSuccessListener {
                            logService.logEvent("User registered and saved to Firestore: $email")
                            onSuccess()  // Si todo sale bien, ejecuta el callback de éxito
                        }
                        .addOnFailureListener { e ->
                            logService.logEvent("Failed to save user to Firestore: ${e.message}")
                            onError("Failed to save user to Firestore: ${e.message}")  // En caso de error, se notifica
                        }
                } else {
                    onError("Failed to register user: No user returned from Firebase Authentication.")
                }
            } catch (e: Exception) {
                logService.logEvent("Registration failed: ${e.message}")  // Registra el error
                onError("Registration failed: ${e.message}")  // Llama al callback de error
            }
        }
    }
}
