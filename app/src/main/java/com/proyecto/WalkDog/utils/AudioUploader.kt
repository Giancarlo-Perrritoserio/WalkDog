package com.proyecto.WalkDog.utils

import android.net.Uri
import javax.inject.Inject
import android.content.Context
import android.os.Environment
import java.io.File
import java.io.FileOutputStream


class AudioUploader @Inject constructor() {

    fun saveAudioToLocalDirectory(audioUri: Uri?, context: Context): String? {
        if (audioUri == null) return null

        val musicDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        val outputFile = File(musicDir, "audio_${System.currentTimeMillis()}.mp3")

        context.contentResolver.openInputStream(audioUri).use { input ->
            FileOutputStream(outputFile).use { output ->
                input?.copyTo(output)
            }
        }

        // Log para confirmar guardado en el directorio correcto
        val savedPath = outputFile.absolutePath
        println("Audio guardado en: $savedPath")
        return savedPath
    }
}
