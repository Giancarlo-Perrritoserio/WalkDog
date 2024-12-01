// Paquete donde se encuentra la interfaz `AccountService`, dentro del proyecto WalkDog
package com.proyecto.WalkDog.data.service

// Importa la clase FirebaseUser de Firebase Authentication
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

// Definición de la interfaz `AccountService`
interface AccountService {

    // Función `authenticate`: permite la autenticación de un usuario con email y contraseña.
    suspend fun authenticate(email: String, password: String)

    // Función `createUser`: crea un nuevo usuario en Firebase Authentication con email y contraseña.
    suspend fun createUser(email: String, password: String): AuthResult

    // Función `createUserWithAdditionalInfo`: registra un nuevo usuario con datos adicionales (nombre, apellido, nombre de usuario)
    suspend fun createUserWithAdditionalInfo(
        email: String,
        password: String,
        name: String,
        lastName: String,
        username: String
    )

    // Función `signOut`: cierra la sesión del usuario actualmente autenticado en Firebase.
    suspend fun signOut()

    // Función `getCurrentUser`: obtiene el usuario autenticado actualmente, si existe.
    fun getCurrentUser(): FirebaseUser?
}
