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
    private var audioFile: File? = null  // Archivo donde se guardará la grabación de audio

    // Solicita permiso de audio y, si se concede, inicia la grabación
    fun requestAudioPermissionAndStartRecording(context: Context, mediaRecorder: MediaRecorder): File? {
        return if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // Si no se ha concedido permiso, solicita el permiso de grabación de audio
            ActivityCompat.requestPermissions(context as android.app.Activity, arrayOf(Manifest.permission.RECORD_AUDIO), 200)
            null  // Devuelve null ya que no se puede iniciar la grabación sin permiso
        } else {
            startRecording(context, mediaRecorder)  // Si el permiso está concedido, inicia la grabación
        }
    }

    // Inicia la grabación de audio y guarda el archivo en una ubicación específica
    private fun startRecording(context: Context, mediaRecorder: MediaRecorder): File? {
        // Crear una carpeta de grabaciones en el almacenamiento interno de la app
        val audioDir = File(context.filesDir, "recordings")
        if (!audioDir.exists()) {
            audioDir.mkdir()  // Crea la carpeta si no existe
        }

        // Crear un archivo de audio con un nombre único utilizando la marca de tiempo actual
        audioFile = File(audioDir, "voice_recording_${System.currentTimeMillis()}.mp3")
        try {
            mediaRecorder.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)  // Fuente de audio: micrófono
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)  // Formato de salida: MPEG_4
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)  // Codificador de audio: AAC
                setOutputFile(audioFile?.absolutePath)  // Especifica el archivo de salida
                prepare()  // Prepara el MediaRecorder para la grabación
                start()  // Inicia la grabación
            }
        } catch (e: IOException) {
            e.printStackTrace()  // Imprime el error en caso de fallo de la grabación
            return null
        }
        return audioFile  // Devuelve el archivo de audio creado
    }

    // Detiene la grabación de audio y devuelve el archivo grabado
    fun stopRecording(mediaRecorder: MediaRecorder): File? {
        try {
            mediaRecorder.stop()  // Detiene la grabación de audio
            mediaRecorder.reset()  // Reinicia el MediaRecorder para futuras grabaciones
        } catch (e: RuntimeException) {
            e.printStackTrace()  // Imprime el error si ocurre un problema al detener la grabación
            return null
        }
        return audioFile  // Devuelve el archivo de audio grabado
    }
}
