package com.proyecto.WalkDog.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

object AudioRecorder {
    // Solicita permiso para grabar audio y empieza la grabación
    fun requestAudioPermissionAndStartRecording(context: Context, mediaRecorder: MediaRecorder): File? {
        // Verifica si el permiso está otorgado
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                context as android.app.Activity,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                1
            )
            return null
        }
        return startRecording(context, mediaRecorder)
    }

    // Inicia la grabación y guarda el archivo en el directorio de música de la aplicación
    private fun startRecording(context: Context, mediaRecorder: MediaRecorder): File? {
        val outputDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        val audioFile = File.createTempFile("audio_", ".mp3", outputDir)
        mediaRecorder.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(audioFile.absolutePath)
            prepare()
            start()
        }
        return audioFile
    }

    // Detiene la grabación
    fun stopRecording(mediaRecorder: MediaRecorder) {
        mediaRecorder.apply {
            stop()
            reset()
        }
    }


}