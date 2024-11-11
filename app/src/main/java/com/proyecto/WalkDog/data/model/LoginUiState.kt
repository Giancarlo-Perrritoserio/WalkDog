// Paquete donde se encuentra la clase `LoginUiState`, dentro del proyecto WalkDog
package com.proyecto.WalkDog.data.model

// Definición de la clase de datos `LoginUiState`
// Esta clase representa el estado de la interfaz de usuario para el inicio de sesión en la aplicación.
data class LoginUiState(
    val email: String = "",              // Correo electrónico ingresado por el usuario para iniciar sesión.
    val password: String = "",           // Contraseña ingresada por el usuario.

    val isLoading: Boolean = false,      // Indica si la aplicación está en proceso de autenticación.
    // Muestra un indicador de carga mientras se realiza la acción de inicio de sesión.

    val errorMessage: String? = null     // Mensaje de error para mostrar en caso de que ocurra algún problema durante el inicio de sesión.
)
