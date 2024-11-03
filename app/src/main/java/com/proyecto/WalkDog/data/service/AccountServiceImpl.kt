package com.proyecto.WalkDog.data.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountServiceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AccountService {

    // Método para iniciar sesión
    override suspend fun authenticate(email: String, password: String) {
        try {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .await() // Espera a que se complete la tarea de inicio de sesión
        } catch (e: Exception) {
            throw Exception("Error al iniciar sesión: ${e.message}")
        }
    }

    // Método para registrar un nuevo usuario
    override suspend fun createUser(email: String, password: String) {
        try {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .await() // Espera a que se complete la tarea de registro
        } catch (e: Exception) {
            throw Exception("Error al registrar usuario: ${e.message}")
        }
    }

    // Método para cerrar sesión
    override suspend fun signOut() {
        firebaseAuth.signOut()
    }

    // Obtener el usuario actual
    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }
}
