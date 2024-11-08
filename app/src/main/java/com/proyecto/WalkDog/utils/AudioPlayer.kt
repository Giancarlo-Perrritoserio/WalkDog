package com.proyecto.WalkDog.utils

import android.media.MediaPlayer
import java.io.File
import java.io.IOException

object AudioPlayer {

    fun startPlaying(file: File, mediaPlayer: MediaPlayer, onCompletion: () -> Unit) {
        try {
            mediaPlayer.apply {
                setDataSource(file.absolutePath)
                prepare()
                start()
                setOnCompletionListener {
                    onCompletion()
                    reset()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun stopPlaying(mediaPlayer: MediaPlayer) {
        mediaPlayer.stop()
        mediaPlayer.reset()
    }
}
