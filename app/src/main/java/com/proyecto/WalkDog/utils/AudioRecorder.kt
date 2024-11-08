package com.proyecto.WalkDog.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaRecorder
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.IOException

object AudioRecorder {
    private var audioFile: File? = null

    fun requestAudioPermissionAndStartRecording(context: Context, mediaRecorder: MediaRecorder): File? {
        return if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as android.app.Activity, arrayOf(Manifest.permission.RECORD_AUDIO), 200)
            null
        } else {
            startRecording(context, mediaRecorder)
        }
    }

    private fun startRecording(context: Context, mediaRecorder: MediaRecorder): File? {
        // Crear una carpeta específica dentro del almacenamiento interno
        val audioDir = File(context.filesDir, "recordings")
        if (!audioDir.exists()) {
            audioDir.mkdir()
        }

        // Crear un archivo de audio con un nombre único
        audioFile = File(audioDir, "voice_recording_${System.currentTimeMillis()}.mp3")
        try {
            mediaRecorder.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(audioFile?.absolutePath)
                prepare()
                start()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return audioFile
    }

    fun stopRecording(mediaRecorder: MediaRecorder): File? {
        try {
            mediaRecorder.stop()
            mediaRecorder.reset()
        } catch (e: RuntimeException) {
            e.printStackTrace()
            return null
        }
        return audioFile
    }
}
