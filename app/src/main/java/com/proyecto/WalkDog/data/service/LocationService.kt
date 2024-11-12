package com.proyecto.WalkDog.data.service

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

// Clase `LocationService` que proporciona servicios de ubicación utilizando `FusedLocationProviderClient`
class LocationService @Inject constructor(@ApplicationContext private val context: Context) {

    // Instancia del cliente de ubicación proporcionada por Google Play Services
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    // Configuración del LocationRequest para obtener actualizaciones de ubicación en tiempo real
    private val locationRequest = LocationRequest.create().apply {
        interval = 1000  // Intervalo de actualización en milisegundos (1 segundo)
        fastestInterval = 500  // Intervalo más rápido de actualización en milisegundos
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY  // Prioridad alta para obtener la ubicación más precisa
    }

    // Guardamos la referencia a la función `onLocationReceived` que se pasará a `startLocationUpdates`
    private var onLocationReceived: ((LatLng?) -> Unit)? = null

    // Declaramos un LocationCallback global para ser usado en todo el servicio
    private val locationCallback = object : LocationCallback() {
        // Sobrescribimos el método de LocationCallback para obtener la ubicación
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult?.let {
                val location = it.lastLocation
                if (location != null) {
                    // Cuando se recibe una nueva ubicación, se llama a la función `onLocationReceived` con las coordenadas de LatLng
                    onLocationReceived?.invoke(LatLng(location.latitude, location.longitude))
                }
            }
        }
    }

    // Función para obtener la ubicación del usuario en tiempo real
    @SuppressLint("MissingPermission") // Suprime el warning de permisos, asumiendo que ya se han concedido
    fun startLocationUpdates(onLocationReceived: (LatLng?) -> Unit) {
        // Guardamos la función de callback que se pasará
        this.onLocationReceived = onLocationReceived

        // Iniciar las actualizaciones de ubicación con el LocationRequest configurado
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    // Función para detener las actualizaciones de ubicación cuando ya no se necesitan
    fun stopLocationUpdates() {
        // Detener las actualizaciones de ubicación utilizando el mismo LocationCallback que se usó al iniciar
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

}
