package com.proyecto.WalkDog.utils

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import javax.inject.Inject

class AudioUploader @Inject constructor() {

    private val storageRef: StorageReference = FirebaseStorage.getInstance().reference
    private val db = FirebaseFirestore.getInstance()

    /**
     * Función para subir el archivo de audio a Firebase Storage.
     * @param fileUri Uri del archivo de audio que se quiere subir.
     * @param onSuccess Callback que recibe la URL de descarga si la subida es exitosa.
     * @param onFailure Callback que recibe una excepción si la subida falla.
     */
    fun uploadAudio(
        fileUri: Uri,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        // Crear una referencia de archivo en Firebase Storage con un nombre único basado en el tiempo
        val audioRef = storageRef.child("audios/${System.currentTimeMillis()}.mp3")

        // Subir el archivo de audio
        audioRef.putFile(fileUri)
            .addOnSuccessListener {
                // Obtener la URL de descarga después de una carga exitosa
                audioRef.downloadUrl
                    .addOnSuccessListener { uri ->
                        val audioUrl = uri.toString()
                        onSuccess(audioUrl)
                    }
                    .addOnFailureListener { exception ->
                        Log.e("AudioUploader", "Error obteniendo URL de descarga", exception)
                        onFailure(exception)
                    }
            }
            .addOnFailureListener { exception ->
                Log.e("AudioUploader", "Error al subir el archivo", exception)
                onFailure(exception)
            }
    }

    /**
     * Función para guardar la URL del audio en Firestore, asociada a una zona restringida.
     * @param zoneId ID del documento de zona restringida.
     * @param audioUrl URL del audio a guardar en Firestore.
     */
    fun saveAudioUrlToFirestore(zoneId: String, audioUrl: String) {
        val restrictedZoneRef = db.collection("restricted_zones").document(zoneId)

        // Guardar la URL del audio en el documento de Firestore
        restrictedZoneRef.update("audioUrl", audioUrl)
            .addOnSuccessListener {
                Log.d("AudioUploader", "URL del audio guardada exitosamente en Firestore")
            }
            .addOnFailureListener { e ->
                Log.e("AudioUploader", "Error guardando URL del audio en Firestore", e)
            }
    }
}
