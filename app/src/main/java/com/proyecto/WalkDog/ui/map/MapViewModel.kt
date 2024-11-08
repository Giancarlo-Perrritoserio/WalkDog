package com.proyecto.WalkDog.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.proyecto.WalkDog.data.model.RestrictedZone
import com.proyecto.WalkDog.data.model.User
import com.proyecto.WalkDog.data.service.LocationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val locationService: LocationService,
    private val firestore: FirebaseFirestore

) : ViewModel() {

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



}
