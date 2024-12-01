package com.proyecto.WalkDog.ui.components

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.gms.maps.model.LatLng
import com.proyecto.WalkDog.data.model.User
import com.proyecto.WalkDog.data.service.LocationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestrictedZoneViewModel @Inject constructor(
    private val locationService: LocationService,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _userLocation = MutableStateFlow<LatLng?>(null)
    val userLocation: StateFlow<LatLng?> = _userLocation

    // Inicia las actualizaciones de ubicación
    fun startLocationUpdates() {
        viewModelScope.launch {
            locationService.startLocationUpdates { location ->
                location?.let {
                    _userLocation.value = it
                }
            }
        }
    }

    // Detiene las actualizaciones de ubicación
    fun stopLocationUpdates() {
        locationService.stopLocationUpdates()
    }

    // Guardar zona restringida en Firestore
    fun saveRestrictedZone(user: User) {
        val currentLocation = _userLocation.value
        if (currentLocation != null) {
            val restrictedZone = hashMapOf(
                "latitude" to currentLocation.latitude,
                "longitude" to currentLocation.longitude,
                "ownerId" to user.uid,
                "name" to "Zona Restringida",
                "audioUrl" to ""
            )

            firestore.collection("restrictedZones")
                .add(restrictedZone)
                .addOnSuccessListener {
                    Log.d("RestrictedZone", "Punto restringido guardado exitosamente")
                }
                .addOnFailureListener { e ->
                    Log.e("RestrictedZone", "Error al guardar el punto: ${e.message}")
                }
        } else {
            Log.e("RestrictedZone", "Ubicación no disponible")
        }
    }
}
