package com.proyecto.WalkDog.ui.common

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object SnackbarManager {
    private val scope = CoroutineScope(Dispatchers.Main)
    val snackbarHostState = SnackbarHostState()
    val currentMessage = mutableStateOf<String?>(null)

    /**
     * Muestra un mensaje en la snackbar.
     * @param message Texto del mensaje que se desea mostrar.
     */
    fun showMessage(message: String) {
        currentMessage.value = message
        scope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
        }
    }

    /**
     * Muestra un mensaje en la snackbar por un tiempo determinado.
     * @param message Texto del mensaje que se desea mostrar.
     * @param duration Duración del mensaje en pantalla (Short, Long, Indefinite).
     */
    fun showMessageWithDuration(message: String, duration: SnackbarDuration) {
        currentMessage.value = message
        scope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                duration = duration
            )
        }
    }
}
