package com.proyecto.WalkDog.ui.restrictedzone

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.proyecto.WalkDog.data.model.RestrictedZone
import com.proyecto.WalkDog.utils.AudioUploader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ZoneManagementViewModel @Inject constructor(
    private val firestore: FirebaseFirestore, // Firestore para manejar datos
    private val audioUploader: AudioUploader // Para subir audios
) : ViewModel() {

    private val _restrictedZones = MutableStateFlow<List<RestrictedZone>>(emptyList())
    val restrictedZones: StateFlow<List<RestrictedZone>> = _restrictedZones

    // Obtener el ID del usuario logueado
    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    // Obtener las zonas restringidas asociadas al usuario logueado desde Firestore
    fun fetchRestrictedZonesForCurrentUser() {
        currentUserId?.let { userId ->
            firestore.collection("restrictedZones")
                .whereEqualTo("ownerId", userId) // Cambiar "userId" a "ownerId"
                .get()
                .addOnSuccessListener { documents ->
                    val zones = documents.map { doc ->
                        RestrictedZone(
                            id = doc.id,
                            latitude = doc.getDouble("latitude") ?: 0.0,
                            longitude = doc.getDouble("longitude") ?: 0.0,
                            name = doc.getString("name") ?: "",
                            audioUrl = doc.getString("audioUrl") ?: "" // Incluye este campo si lo necesitas en la UI
                        )
                    }
                    _restrictedZones.value = zones
                }
                .addOnFailureListener { e ->
                    println("Error al cargar zonas restringidas: ${e.message}")
                }
        } ?: println("No hay usuario logueado.")
    }


    // Guardar un audio para una zona restringida
    fun uploadAudio(uri: Uri, zoneId: String, context: Context) {
        viewModelScope.launch {
            try {
                val storageRef = FirebaseStorage.getInstance().reference
                val audioRef = storageRef.child("restrictedZones/$zoneId/audio/${uri.lastPathSegment}")
                audioRef.putFile(uri).await()

                val downloadUrl = audioRef.downloadUrl.await()
                updateAudioUrl(zoneId, downloadUrl.toString())
            } catch (e: Exception) {
                println("Error al cargar el audio: ${e.message}")
            }
        }
    }

    // Actualizar la URL del audio en Firestore
    private suspend fun updateAudioUrl(zoneId: String, audioUrl: String) {
        val zoneRef = firestore.collection("restrictedZones").document(zoneId)
        zoneRef.update("audioUrl", audioUrl).await()
    }

    // Guardar el audio en almacenamiento local
    fun saveAudio(audioUri: Uri?, context: Context) {
        val savedPath = audioUploader.saveAudioToLocalDirectory(audioUri, context)
        savedPath?.let {
            println("Audio guardado exitosamente en: $it")
        }
    }

    // Actualizar el nombre de la zona
    fun updateZoneName(zoneId: String, newName: String) {
        firestore.collection("restrictedZones").document(zoneId)
            .update("name", newName)
            .addOnSuccessListener {
                println("Zona actualizada correctamente")
            }
            .addOnFailureListener { e ->
                println("Error al actualizar la zona: ${e.message}")
            }
    }

    // Eliminar una zona
    fun deleteRestrictedZone(zoneId: String) {
        firestore.collection("restrictedZones").document(zoneId)
            .delete()
            .addOnSuccessListener {
                println("Zona eliminada correctamente")
            }
            .addOnFailureListener { e ->
                println("Error al eliminar la zona: ${e.message}")
            }
    }
}
