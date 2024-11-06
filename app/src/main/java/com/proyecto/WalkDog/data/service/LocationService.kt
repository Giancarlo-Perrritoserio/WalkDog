package com.proyecto.WalkDog.data.service

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocationService @Inject constructor(@ApplicationContext private val context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    fun getUserLocation(onLocationReceived: (LatLng?) -> Unit) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                onLocationReceived(LatLng(it.latitude, it.longitude))
            } ?: run {
                onLocationReceived(null) // Maneja el caso en que la ubicación es nula
            }
        }
    }
}
