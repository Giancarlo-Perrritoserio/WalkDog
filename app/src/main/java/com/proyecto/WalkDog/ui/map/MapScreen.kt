package com.proyecto.WalkDog.ui.map

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.maps.android.compose.Circle

@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val userLocation by viewModel.userLocation.collectAsState()
    val restrictedZones by viewModel.restrictedZones.collectAsState()  // Obtener las zonas restringidas desde el ViewModel

    val cameraPositionState = rememberCameraPositionState()
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.fetchUserLocation()
        }
    }

    LaunchedEffect(Unit) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) -> {
                viewModel.fetchUserLocation()
            }
            else -> locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            userLocation?.let {
                Marker(
                    state = rememberMarkerState(position = it),
                    title = "Ubicación Actual"
                )
                cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(it, 15f))
            }

            // Mostrar las zonas restringidas en el mapa
            restrictedZones.forEach { zone ->
                // Mostrar marcador en el centro de la zona restringida
                Marker(
                    state = rememberMarkerState(position = LatLng(zone.latitude, zone.longitude)),
                    title = zone.name
                )

                // Mostrar círculo representando el radio de la zona restringida
                Circle(
                    center = LatLng(zone.latitude, zone.longitude),
                    radius = zone.radius.toDouble(),
                    fillColor = Color(0x550000FF),  // Color de relleno con opacidad (en formato ARGB)
                    strokeColor = Color(0xFFFF0000),  // Color del borde en formato ARGB
                    strokeWidth = 2f
                )
            }
        }

        if (userLocation == null) {
            Text(
                text = "Obteniendo ubicación...",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}