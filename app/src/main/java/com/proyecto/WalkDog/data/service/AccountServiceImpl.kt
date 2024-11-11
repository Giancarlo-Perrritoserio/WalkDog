// Paquete donde se encuentra la implementación del servicio `AccountService` para la autenticación de usuarios en el proyecto WalkDog
package com.proyecto.WalkDog.data.service

// Importa FirebaseAuth y FirebaseUser para manejar la autenticación de usuarios
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
// Importa `await` para manejar tareas asincrónicas de Firebase en coroutines
import kotlinx.coroutines.tasks.await
// Inyección de dependencias con Hilt
import javax.inject.Inject

// Implementación de la interfaz `AccountService`, que gestiona las operaciones de autenticación de usuarios
class AccountServiceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth // FirebaseAuth inyectado para manejar la autenticación
) : AccountService {

    // Método para autenticar usuarios con email y contraseña
    override suspend fun authenticate(email: String, password: String) {
        try {
            // Inicia sesión con email y contraseña y espera que la tarea se complete
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .await() // `await` permite suspender la coroutine hasta que se complete la operación
        } catch (e: Exception) {
            // Captura y lanza una excepción en caso de error en el inicio de sesión
            throw Exception("Error al iniciar sesión: ${e.message}")
        }
    }

    // Método para registrar un nuevo usuario con email y contraseña
    override suspend fun createUser(email: String, password: String) {
        try {
            // Crea un nuevo usuario en Firebase Authentication y espera que la tarea se complete
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .await() // `await` suspende la coroutine hasta que se complete el registro
        } catch (e: Exception) {
            // Captura y lanza una excepción en caso de error en el registro de usuario
            throw Exception("Error al registrar usuario: ${e.message}")
        }
    }

    // Método para cerrar sesión del usuario actual
    override suspend fun signOut() {
        firebaseAuth.signOut() // Cierra la sesión activa en Firebase
    }

    // Método para obtener el usuario actualmente autenticado
    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser // Devuelve el usuario actual o `null` si no hay sesión activa
    }
}
