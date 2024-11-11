package com.proyecto.WalkDog.data.service

// Interfaz `LogService` para gestionar diferentes tipos de registros (logs) en la aplicación
interface LogService {

    // Método para registrar eventos específicos, como acciones del usuario o eventos importantes de la aplicación
    fun logEvent(event: String)

    // Método para registrar errores, útil para capturar excepciones y problemas durante la ejecución
    fun logError(error: String)

    // Método para registrar información general, como mensajes informativos para el seguimiento de procesos
    fun logInfo(info: String)
}
