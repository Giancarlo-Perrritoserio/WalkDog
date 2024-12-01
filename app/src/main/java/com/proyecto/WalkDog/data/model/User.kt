// Paquete donde se encuentra la clase `User`, dentro del proyecto WalkDog
package com.proyecto.WalkDog.data.model

import com.google.firebase.auth.FirebaseUser

// Definición de la clase de datos `User`
// Esta clase se utiliza para representar un usuario autenticado en la aplicación WalkDog,
// y contiene información básica que se puede obtener de Firebase Authentication.
data class User(
    val uid: String = "",      // ID único del usuario en Firebase Authentication.
    // Es una cadena de texto que se utiliza como identificador principal del usuario.

    val email: String = "",     // Dirección de correo electrónico del usuario.
    // Este campo es opcional, pero generalmente está disponible para usuarios registrados.

    val name: String? = null    // Nombre del usuario, obtenido de Firebase.
    // Puede ser nulo si el usuario no ha proporcionado un nombre en su perfil.
)

fun FirebaseUser.toUser(): User {
    return User(
        uid = this.uid,
        email = this.email ?: "" // Si el correo es null, asignamos una cadena vacía
    )
}
