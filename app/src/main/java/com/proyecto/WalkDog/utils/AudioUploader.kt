package com.proyecto.WalkDog.utils

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import javax.inject.Inject

class AudioUploader @Inject constructor() {

    // Referencia principal al almacenamiento de Firebase
    private val storageRef: StorageReference = FirebaseStorage.getInstance().reference
    // Referencia a Firestore para guardar datos relacionados con las zonas restringidas
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
        // Crear una referencia de archivo en Firebase Storage, con nombre único basado en el tiempo
        val audioRef = storageRef.child("audios/${System.currentTimeMillis()}.mp3")

        // Subir el archivo de audio a la referencia especificada en Firebase Storage
        audioRef.putFile(fileUri)
            .addOnSuccessListener {
                // Al completar la subida, obtener la URL de descarga del archivo
                audioRef.downloadUrl
                    .addOnSuccessListener { uri ->
                        val audioUrl = uri.toString()  // Convertir Uri a String para facilidad de uso
                        onSuccess(audioUrl)  // Llamar a la función de éxito con la URL del archivo
                    }
                    .addOnFailureListener { exception ->
                        Log.e("AudioUploader", "Error obteniendo URL de descarga", exception)  // Log para depuración
                        onFailure(exception)  // Llamar a la función de error si falla la obtención de la URL
                    }
            }
            .addOnFailureListener { exception ->
                Log.e("AudioUploader", "Error al subir el archivo", exception)  // Log para errores de subida
                onFailure(exception)  // Llamar a la función de error si falla la subida
            }
    }

    /**
     * Función para guardar la URL del audio en Firestore, asociada a una zona restringida.
     * @param zoneId ID del documento de zona restringida.
     * @param audioUrl URL del audio a guardar en Firestore.
     */
    fun saveAudioUrlToFirestore(zoneId: String, audioUrl: String) {
        // Referencia al documento de la zona restringida específica en Firestore
        val restrictedZoneRef = db.collection("restricted_zones").document(zoneId)

        // Actualizar el campo "audioUrl" en el documento de Firestore con la URL proporcionada
        restrictedZoneRef.update("audioUrl", audioUrl)
            .addOnSuccessListener {
                Log.d("AudioUploader", "URL del audio guardada exitosamente en Firestore")  // Log para confirmar éxito
            }
            .addOnFailureListener { e ->
                Log.e("AudioUploader", "Error guardando URL del audio en Firestore", e)  // Log para errores de actualización
            }
    }
}
