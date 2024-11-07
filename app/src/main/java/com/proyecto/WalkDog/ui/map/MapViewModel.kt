package com.proyecto.WalkDog.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
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

    fun fetchUserLocation() {
        viewModelScope.launch {
            locationService.getUserLocation { location ->
                _userLocation.value = location
            }
        }
    }

    // Método para guardar la zona restringida
    fun saveRestrictedZone(location: LatLng) {
        val restrictedZone = hashMapOf(
            "latitude" to location.latitude,
            "longitude" to location.longitude
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
}
