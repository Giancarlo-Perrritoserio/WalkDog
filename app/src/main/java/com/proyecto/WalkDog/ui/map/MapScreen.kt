package com.proyecto.WalkDog.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.proyecto.WalkDog.R
import com.proyecto.WalkDog.data.model.RestrictedZone

@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel()  // Usa el ViewModel `MapViewModel` inyectado con Hilt
) {
    val context = LocalContext.current

    // Obtenemos la ubicación del usuario en tiempo real a través de un `State` del ViewModel
    val userLocation by viewModel.userLocation.collectAsState()

    // Obtenemos la lista de zonas restringidas desde el ViewModel
    val restrictedZones by viewModel.restrictedZones.collectAsState()

    // Estado para controlar la posición de la cámara del mapa
    val cameraPositionState = rememberCameraPositionState()

    // Llamamos a `getRestrictedZonesFromFirestore` para cargar las zonas restringidas al inicio
    LaunchedEffect(Unit) {
        // Aquí se llama explícitamente la función que carga las zonas desde Firestore
        viewModel.getRestrictedZonesFromFirestore()
    }

    // Configuración para solicitar permisos de ubicación
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Si el permiso es otorgado, obtenemos la ubicación del usuario y pasamos el mapa a startLocationUpdates
            viewModel.startLocationUpdates() // Aquí eliminamos el parámetro `GoogleMap`
        }
    }

    // Efecto lanzado al iniciar la pantalla para verificar permisos
    LaunchedEffect(Unit) {
        when (PackageManager.PERMISSION_GRANTED) {
            // Si ya tenemos permiso, llamamos a `startLocationUpdates` para obtener la ubicación
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) -> {
                viewModel.startLocationUpdates()  // Llamamos a `fetchUserLocation`
            }
            // Si no tenemos permiso, lanzamos la solicitud de permisos
            else -> locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // Layout de la pantalla principal donde se muestra el mapa
    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapLoaded = {
                // Este callback asegura que el mapa esté cargado antes de mover la cámara
                userLocation?.let {
                    cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(it, 15f))
                }
            }
        ) {
            // 1. Mostrar la ubicación actual del usuario (marcador morado)
            userLocation?.let {
                Marker(
                    state = rememberMarkerState(position = it),
                    title = "Ubicación Actual",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA) // Pin morado
                )
                // Mueve la cámara para centrarla en la ubicación actual si está disponible
                LaunchedEffect(it) {
                    cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(it, 15f))
                }
            }

            // 2. Mostrar las zonas restringidas en el mapa
            restrictedZones.forEach { zone ->
                // Agrega un marcador en el centro de cada zona restringida
                Marker(
                    state = rememberMarkerState(position = LatLng(zone.latitude, zone.longitude)),
                    title = zone.name, // Muestra el nombre de la zona como título del marcador
                    icon = BitmapDescriptorFactory.fromBitmap(
                        Bitmap.createScaledBitmap(
                            BitmapFactory.decodeResource(context.resources, R.drawable.advertencia),
                            100, 100, false // Redimensiona el icono a un tamaño adecuado (100x100 píxeles)
                        )
                    )
                )

            }
        }

        // 3. Mostrar un mensaje de "Obteniendo ubicación..." si `userLocation` es nulo
        if (userLocation == null) {
            Text(
                text = "Obteniendo ubicación...",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

