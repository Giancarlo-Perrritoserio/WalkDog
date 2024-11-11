// Paquete donde se encuentra la clase `RegisterUiState`, dentro del proyecto WalkDog
package com.proyecto.WalkDog.data.model

// Definición de la clase de datos `RegisterUiState`
// Esta clase representa el estado de la interfaz de usuario para el registro de nuevos usuarios en la aplicación.
data class RegisterUiState(
    val email: String = "",              // Correo electrónico ingresado por el usuario para registrarse.
    val password: String = "",           // Contraseña ingresada por el usuario.
    val confirmPassword: String = "",    // Confirmación de la contraseña ingresada para verificar coincidencia.

    val isLoading: Boolean = false,      // Indica si la aplicación está en proceso de registro.
    // Muestra un indicador de carga mientras se realiza la acción de registro.

    val errorMessage: String? = null     // Mensaje de error para mostrar en caso de que ocurra algún problema durante el registro.
)
