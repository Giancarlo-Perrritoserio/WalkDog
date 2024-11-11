package com.proyecto.WalkDog.utils

import android.media.MediaPlayer
import java.io.File
import java.io.IOException

object AudioPlayer {
    // Inicia la reproducción del archivo de audio
    fun startPlaying(audio: File, mediaPlayer: MediaPlayer, onCompletion: () -> Unit) {
        mediaPlayer.apply {
            setDataSource(audio.absolutePath)
            prepare()
            start()
            setOnCompletionListener {
                stopPlaying(this)
                onCompletion()
            }
        }
    }

    // Detiene la reproducción del archivo de audio
    fun stopPlaying(mediaPlayer: MediaPlayer) {
        mediaPlayer.apply {
            if (isPlaying) {
                stop()
            }
            reset()
        }
    }
}