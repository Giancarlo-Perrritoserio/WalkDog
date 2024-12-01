package com.proyecto.WalkDog.ui.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.proyecto.WalkDog.data.model.ProfileUiState
import com.proyecto.WalkDog.data.service.LogService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val logService: LogService
) : ViewModel() {

    var uiState by mutableStateOf(ProfileUiState())
        private set

    // Método para obtener los datos del usuario
    fun getUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            firestore.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val userData = document.data
                        uiState = ProfileUiState(
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

    // Método para guardar los datos del usuario
    fun saveUserData(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val updatedData = hashMapOf(
                "name" to uiState.name,
                "lastName" to uiState.lastName,
                "username" to uiState.username
            )

            firestore.collection("users")
                .document(userId)
                .set(updatedData)
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener { e ->
                    onError("Failed to save user data: ${e.message}")
                }
        } else {
            onError("User is not authenticated")
        }
    }

    // Métodos para cambiar los valores de los campos de texto
    fun onNameChange(newName: String) {
        uiState = uiState.copy(name = newName)
    }

    fun onLastNameChange(newLastName: String) {
        uiState = uiState.copy(lastName = newLastName)
    }

    fun onUsernameChange(newUsername: String) {
        uiState = uiState.copy(username = newUsername)
    }
}
