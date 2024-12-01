package com.proyecto.WalkDog.ui.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.media.MediaPlayer
import android.net.Uri
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.proyecto.WalkDog.data.model.RestrictedZone
import com.proyecto.WalkDog.data.model.User
import com.proyecto.WalkDog.data.service.LocationService
import com.proyecto.WalkDog.utils.AudioUploader
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val locationService: LocationService, // Servicio para obtener la ubicación del usuario
    private val firestore: FirebaseFirestore, // Servicio de Firestore para manejar datos en Firebase
    private val audioUploader: AudioUploader,  // Aquí inyectamos AudioUploader para manejar la subida de audios


) : ViewModel() {

    private var mediaPlayer: MediaPlayer? = null // Variable para manejar la reproducción de audio

    private val _userLocation = MutableStateFlow<LatLng?>(null) // StateFlow para almacenar la ubicación del usuario
    val userLocation: StateFlow<LatLng?> = _userLocation

    // StateFlow para manejar la lista de zonas restringidas
    private val _restrictedZones = MutableStateFlow<List<RestrictedZone>>(emptyList())
    val restrictedZones: StateFlow<List<RestrictedZone>> = _restrictedZones

    // StateFlow para almacenar la última ubicación del dispositivo ESP32
    private val _deviceLocation = MutableStateFlow<LatLng?>(null) // Mutable flujo para mantener la ubicación actualizada
    val deviceLocation: StateFlow<LatLng?> = _deviceLocation.asStateFlow() // Exposición pública inmutable

    ///modificacion 1.2

    // Método para comenzar a obtener actualizaciones de ubicación en tiempo real
    fun startLocationUpdates() {
        viewModelScope.launch {
            locationService.startLocationUpdates { location ->
                location?.let {
                    _userLocation.value = it // Actualiza la ubicación del usuario en tiempo real
                }
            }
        }
    }

    // Detener la actualización de ubicación cuando ya no sea necesario
    fun stopLocationUpdates() {
        locationService.stopLocationUpdates() // Detiene la actualización de ubicación en el servicio
    }

    // Nueva función para obtener las zonas restringidas, sin interferir con la ubicación
    fun getRestrictedZonesFromFirestore() {
        firestore.collection("restrictedZones")
            .get()
            .addOnSuccessListener { documents ->
                val zones = documents.map { doc ->
                    RestrictedZone(
                        id = doc.id,
                        latitude = doc.getDouble("latitude") ?: 0.0,
                        longitude = doc.getDouble("longitude") ?: 0.0,
                        name = doc.getString("name") ?: "Sin nombre"
                    )
                }
                _restrictedZones.value = zones
            }
            .addOnFailureListener { e ->
                println("Error al cargar zonas restringidas: ${e.message}")
            }
    }




    //fin de modificacion

    // Método para cargar las zonas restringidas desde Firestore
    fun fetchRestrictedZones() {
        firestore.collection("restrictedZones")
            .get()
            .addOnSuccessListener { documents ->
                val zones = documents.map { doc ->
                    RestrictedZone(
                        id = doc.id,
                        latitude = doc.getDouble("latitude") ?: 0.0,
                        longitude = doc.getDouble("longitude") ?: 0.0,
                        name = doc.getString("name") ?: ""
                    )
                }
                _restrictedZones.value = zones

            }
            .addOnFailureListener { e ->
                println("Error al cargar zonas restringidas: ${e.message}")
            }
    }


    //modificaciones parte 3.1
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

    // Eliminar la zona
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

    //fin de modificacion


    // Modificamos el método uploadAudio para utilizar AudioUploader
    fun uploadAudio(uri: Uri, zoneId: String, context: Context) {
        viewModelScope.launch {
            try {
                // Subimos el archivo de audio a Firebase Storage
                val storageRef = FirebaseStorage.getInstance().reference
                val audioRef = storageRef.child("restrictedZones/$zoneId/audio/${uri.lastPathSegment}")
                val uploadTask = audioRef.putFile(uri).await() // Subimos el archivo de audio
                println("Audio subido exitosamente a Firebase Storage: $uploadTask")

                // Obtenemos la URL del archivo de audio en Firebase Storage
                val downloadUrl = audioRef.downloadUrl.await()
                println("URL de descarga obtenida: $downloadUrl")

                // Actualizamos la URL del audio en Firestore
                updateAudioUrl(zoneId, downloadUrl.toString())
            } catch (e: Exception) {
                println("Error al cargar el audio: ${e.message}")
            }
        }
    }

    // Función privada para actualizar la URL del audio en Firestore
    private suspend fun updateAudioUrl(zoneId: String, audioUrl: String) {
        val zoneRef = firestore.collection("restrictedZones").document(zoneId)

        try {
            // Actualizamos la URL en Firestore
            zoneRef.update("audioUrl", audioUrl).await()
            println("URL del audio actualizada exitosamente en Firestore para la zona: $zoneId")

        } catch (e: Exception) {
            println("Error al actualizar la URL del audio en Firestore: ${e.message}")
        }
    }

    //Agregado
    fun saveAudio(audioUri: Uri?, context: Context) {
        val savedPath = audioUploader.saveAudioToLocalDirectory(audioUri, context)
        savedPath?.let {
            // Aquí puedes realizar acciones adicionales, como mostrar un mensaje de confirmación
            println("Audio guardado exitosamente en: $it")
        }
    }

    // Función para obtener datos de ubicación en tiempo real desde Firestore
    fun getDeviceLocationUpdates() {
        firestore.collection("deviceLocations").document("esp32-location")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("MapViewModel", "Error al obtener los datos de ubicación del ESP32: ${error.message}")
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    snapshot.data?.let { data ->
                        val latitude = data["latitude"] as? Double ?: 0.0
                        val longitude = data["longitude"] as? Double ?: 0.0

                        // Actualiza el flujo con la nueva ubicación
                        _deviceLocation.value = LatLng(latitude, longitude)

                        Log.d("MapViewModel", "Nueva ubicación ESP32: lat=$latitude, lng=$longitude")
                    }
                } else {
                    Log.w("MapViewModel", "Documento de ubicación ESP32 no encontrado o vacío.")
                }
            }
    }

}
