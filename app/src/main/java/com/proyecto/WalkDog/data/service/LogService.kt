package com.proyecto.WalkDog.data.service

interface LogService {
    fun logEvent(event: String)
    fun logError(error: String)
    fun logInfo(info: String)
}
