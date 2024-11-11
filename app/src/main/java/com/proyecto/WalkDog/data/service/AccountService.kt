// Paquete donde se encuentra la interfaz `AccountService`, dentro del proyecto WalkDog
package com.proyecto.WalkDog.data.service

// Importa la clase FirebaseUser de Firebase Authentication
import com.google.firebase.auth.FirebaseUser

// Definición de la interfaz `AccountService`
// Esta interfaz proporciona una capa de abstracción para manejar la autenticación de usuarios en la aplicación.
interface AccountService {

    // Función `authenticate`: permite la autenticación de un usuario con email y contraseña.
    // Esta función es suspendida porque realiza una operación asíncrona.
    suspend fun authenticate(email: String, password: String)

    // Función `createUser`: crea un nuevo usuario en Firebase Authentication con email y contraseña.
    // También es suspendida, ya que se comunica con Firebase.
    suspend fun createUser(email: String, password: String)

    // Función `signOut`: cierra la sesión del usuario actualmente autenticado en Firebase.
    suspend fun signOut()

    // Función `getCurrentUser`: obtiene el usuario autenticado actualmente, si existe.
    // Retorna un objeto `FirebaseUser` o null si no hay un usuario autenticado.
    fun getCurrentUser(): FirebaseUser?
}
