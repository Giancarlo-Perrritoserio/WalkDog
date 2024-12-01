// Paquete donde se encuentra la implementación del servicio `AccountService` para la autenticación de usuarios en el proyecto WalkDog
package com.proyecto.WalkDog.data.service

// Importa FirebaseAuth y FirebaseUser para manejar la autenticación de usuarios
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore  // FirebaseFirestore para almacenar datos adicionales
// Importa `await` para manejar tareas asincrónicas de Firebase en coroutines
import kotlinx.coroutines.tasks.await
// Inyección de dependencias con Hilt
import javax.inject.Inject

// Implementación de la interfaz `AccountService`, que gestiona las operaciones de autenticación de usuarios
class AccountServiceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,  // FirebaseAuth inyectado para manejar la autenticación
    private val firestore: FirebaseFirestore  // FirebaseFirestore inyectado para guardar datos del usuario
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
    override suspend fun createUser(email: String, password: String): AuthResult {
        return FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, password)
            .await() // Espera a que la operación sea completada
    }

    // Método para registrar un nuevo usuario con datos adicionales (nombre, apellido, nombre de usuario)
    override suspend fun createUserWithAdditionalInfo(
        email: String,
        password: String,
        name: String,
        lastName: String,
        username: String
    ) {
        try {
            // Crea un nuevo usuario en Firebase Authentication
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password)
                .await() // Esperamos la creación del usuario

            val user = authResult.user  // Obtenemos el usuario creado

            if (user != null) {
                // Guardamos los datos adicionales en Firestore
                val userData = mapOf(
                    "email" to email,
                    "name" to name,
                    "lastName" to lastName,
                    "username" to username,
                    "uid" to user.uid,
                    "createdAt" to com.google.firebase.firestore.FieldValue.serverTimestamp() // Timestamp de creación
                )

                // Guardamos los datos del usuario en Firestore en la colección 'users'
                firestore.collection("users")
                    .document(user.uid)  // Usamos el UID del usuario como identificador
                    .set(userData)
                    .await()  // Esperamos que se complete la operación

            } else {
                throw Exception("Error al obtener el usuario después de la creación.")
            }
        } catch (e: Exception) {
            // Captura y lanza una excepción en caso de error
            throw Exception("Error al registrar usuario con datos adicionales: ${e.message}")
        }
    }

    // Método para cerrar sesión del usuario actual
    override suspend fun signOut() {
        firebaseAuth.signOut()  // Cierra la sesión activa en Firebase
    }

    // Método para obtener el usuario actualmente autenticado
    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser  // Devuelve el usuario actual o `null` si no hay sesión activa
    }
}
