package com.proyecto.WalkDog.ui.map

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.proyecto.WalkDog.data.model.RestrictedZone
import com.proyecto.WalkDog.data.model.User
import com.proyecto.WalkDog.data.service.LocationService
import com.proyecto.WalkDog.utils.AudioUploader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val locationService: LocationService,
    private val firestore: FirebaseFirestore,
    private val audioUploader: AudioUploader  // Aquí inyectamos AudioUploader

) : ViewModel() {

    private var mediaPlayer: MediaPlayer? = null

    private val _userLocation = MutableStateFlow<LatLng?>(null)
    val userLocation: StateFlow<LatLng?> = _userLocation

    // StateFlow para manejar la lista de zonas restringidas
    private val _restrictedZones = MutableStateFlow<List<RestrictedZone>>(emptyList())
    val restrictedZones: StateFlow<List<RestrictedZone>> = _restrictedZones

    fun fetchUserLocation() {
        viewModelScope.launch {
            locationService.getUserLocation { location ->
                _userLocation.value = location
            }
        }
    }

    // Método para guardar la zona restringida
    fun saveRestrictedZone(location: LatLng, user: User) {
        val restrictedZone = hashMapOf(
            "latitude" to location.latitude,
            "longitude" to location.longitude,
            "ownerId" to user.uid // Asigna el ID del propietario al guardar
        )

        // Guardamos el punto como "restrictedZone" en Firebase
        firestore.collection("restrictedZones")
            .add(restrictedZone)
            .addOnSuccessListener {
                // Feedback exitoso al guardar el punto
                println("Punto restringido guardado exitosamente")
            }
            .addOnFailureListener { e ->
                // Manejo de errores
                println("Error al guardar el punto: ${e.message}")
            }
    }

    fun fetchRestrictedZones() {
        firestore.collection("restrictedZones")
            .get()
            .addOnSuccessListener { documents ->
                val zones = documents.map { doc ->
                    RestrictedZone(
                        id = doc.id,
                        latitude = doc.getDouble("latitude") ?: 0.0,
                        longitude = doc.getDouble("longitude") ?: 0.0,
                        name = doc.getString("name") ?: ""  // Asegúrate de obtener el nombre de la zona

                    )
                }
                _restrictedZones.value = zones
            }
            .addOnFailureListener { e ->
                println("Error al cargar zonas restringidas: ${e.message}")
            }
    }

    fun updateZoneName(zoneId: String, newName: String) {
        val zoneRef = firestore.collection("restrictedZones").document(zoneId)

        zoneRef.update("name", newName)
            .addOnSuccessListener {
                println("Nombre de la zona actualizado exitosamente")
            }
            .addOnFailureListener { e ->
                println("Error al actualizar el nombre de la zona: ${e.message}")
            }
    }

    // Modificamos el método uploadAudio para utilizar AudioUploader
    fun uploadAudio(uri: Uri, zoneId: String, context: Context) {
        // Usamos launch para ejecutar la función en una coroutine
        viewModelScope.launch {
            try {
                // Subimos el archivo a Firebase Storage
                val storageRef = FirebaseStorage.getInstance().reference
                val audioRef = storageRef.child("restrictedZones/$zoneId/audio/${uri.lastPathSegment}")
                val uploadTask = audioRef.putFile(uri).await()
                println("Audio subido exitosamente a Firebase Storage: $uploadTask")

                // Obtenemos la URL de descarga
                val downloadUrl = audioRef.downloadUrl.await()
                println("URL de descarga obtenida: $downloadUrl")

                // Llamamos a la función suspendida para actualizar la URL en Firestore
                updateAudioUrl(zoneId, downloadUrl.toString())
            } catch (e: Exception) {
                println("Error al cargar el audio: ${e.message}")
            }
        }
    }



    private suspend fun updateAudioUrl(zoneId: String, audioUrl: String) {
        val zoneRef = firestore.collection("restrictedZones").document(zoneId)

        try {
            // Actualizamos la URL en Firestore
            zoneRef.update("audioUrl", audioUrl).await()
            println("URL del audio actualizada exitosamente en Firestore para la zona: $zoneId")

        } catch (e: Exception) {
            // Manejo de errores
            println("Error al actualizar la URL del audio en Firestore: ${e.message}")
        }
    }


    
}
