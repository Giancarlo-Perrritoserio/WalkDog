package com.proyecto.WalkDog.data.service

import android.util.Log
import javax.inject.Inject

// Implementación concreta de la interfaz `LogService` para registrar eventos, errores e información utilizando el sistema de logs de Android
class LogServiceImpl @Inject constructor() : LogService {

    // Etiqueta para los logs, que se utiliza para identificar los mensajes en la salida de logs
    private val TAG = "WalkDog"

    // Método para registrar un evento. Utiliza `Log.d` para un log de nivel "debug" en Android.
    override fun logEvent(event: String) {
        Log.d(TAG, "Event: $event") // Registra un evento con un mensaje de tipo debug
    }

    // Método para registrar un error. Utiliza `Log.e` para un log de nivel "error" en Android.
    override fun logError(error: String) {
        Log.e(TAG, "Error: $error") // Registra un error con un mensaje de tipo error
    }

    // Método para registrar información general. Utiliza `Log.i` para un log de nivel "informativo" en Android.
    override fun logInfo(info: String) {
        Log.i(TAG, "Info: $info") // Registra una información con un mensaje de tipo informativo
    }
}
