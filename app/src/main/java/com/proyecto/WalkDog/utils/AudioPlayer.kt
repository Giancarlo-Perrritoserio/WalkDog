package com.proyecto.WalkDog.utils

import android.media.MediaPlayer
import java.io.File
import java.io.IOException

object AudioPlayer {

    // Función para iniciar la reproducción de un archivo de audio
    fun startPlaying(file: File, mediaPlayer: MediaPlayer, onCompletion: () -> Unit) {
        try {
            mediaPlayer.apply {
                setDataSource(file.absolutePath)  // Establece la fuente de datos del archivo de audio a reproducir
                prepare()  // Prepara el MediaPlayer para la reproducción
                start()  // Comienza la reproducción del archivo de audio
                setOnCompletionListener {
                    onCompletion()  // Llama al callback cuando se completa la reproducción
                    reset()  // Reinicia el MediaPlayer para futuras reproducciones
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()  // Imprime la traza del error en caso de falla al iniciar la reproducción
        }
    }

    // Función para detener la reproducción de audio
    fun stopPlaying(mediaPlayer: MediaPlayer) {
        mediaPlayer.stop()  // Detiene la reproducción si está en curso
        mediaPlayer.reset()  // Reinicia el MediaPlayer para futuras reproducciones
    }
}
