package com.proyecto.WalkDog.ui.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.proyecto.WalkDog.data.model.EditProfileUiState
import com.proyecto.WalkDog.data.service.LogService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,  // Inyectamos FirebaseFirestore
    private val auth: FirebaseAuth,  // Inyectamos FirebaseAuth para obtener el usuario actual
    private val logService: LogService  // Inyectamos LogService para registrar eventos
) : ViewModel() {

    var uiState by mutableStateOf(EditProfileUiState())  // Mantiene el estado de la UI (nombre, apellido, etc.)
        private set

    // Método que actualiza el nombre en el estado UI
    fun onNameChange(newValue: String) {
        uiState = uiState.copy(name = newValue)  // Actualiza el valor del nombre
    }

    // Método que actualiza el apellido en el estado UI
    fun onLastNameChange(newValue: String) {
        uiState = uiState.copy(lastName = newValue)  // Actualiza el valor del apellido
    }

    // Método que actualiza el nombre de usuario en el estado UI
    fun onUsernameChange(newValue: String) {
        uiState = uiState.copy(username = newValue)  // Actualiza el valor del nombre de usuario
    }

    // Método que obtiene los datos del usuario desde Firestore
    fun getUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            firestore.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val userData = document.data
                        uiState = EditProfileUiState(
                            name = userData?.get("name") as? String ?: "",
                            lastName = userData?.get("lastName") as? String ?: "",
                            username = userData?.get("username") as? String ?: ""
                        )
                    }
                }
                .addOnFailureListener { e ->
                    logService.logEvent("Failed to fetch user data: ${e.message}")
                }
        }
    }

    // Método que guarda los datos actualizados del usuario en Firestore
    fun saveUserData(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val updatedData = mapOf(
                "name" to uiState.name,
                "lastName" to uiState.lastName,
                "username" to uiState.username
            )

            firestore.collection("users")
                .document(userId)
                .update(updatedData)
                .addOnSuccessListener {
                    logService.logEvent("User data updated successfully.")
                    onSuccess()  // Si todo sale bien, ejecuta el callback de éxito
                }
                .addOnFailureListener { e ->
                    logService.logEvent("Failed to update user data: ${e.message}")
                    onError("Failed to update user data: ${e.message}")  // En caso de error, se notifica
                }
        } else {
            onError("User not logged in.")
        }
    }
}
