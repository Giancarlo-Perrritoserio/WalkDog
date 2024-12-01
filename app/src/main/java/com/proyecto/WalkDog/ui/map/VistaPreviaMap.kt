package com.proyecto.WalkDog.ui.map

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.Marker
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerState

@Composable
fun VistaPreviaMap(location: LatLng?, onClick: () -> Unit) {
    // Configuración de la cámara para el mapa
    val cameraPositionState = rememberCameraPositionState {
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(location ?: LatLng(0.0, 0.0), 15f)
    }

    // Contenedor con diseño mejorado
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable(onClick = onClick)  // Hace que toda la vista previa sea clickeable
            .padding(16.dp) // Padding alrededor del mapa
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize(), // Asegura que la superficie cubra toda el área disponible
            shape = RoundedCornerShape(16.dp), // Bordes redondeados
            color = MaterialTheme.colorScheme.surface, // Fondo del contenedor
            shadowElevation = 8.dp // Sombra para dar profundidad
        ) {
            GoogleMap(
                modifier = Modifier
                    .fillMaxSize(), // Asegura que el mapa ocupe todo el espacio disponible
                cameraPositionState = cameraPositionState
            ) {
                location?.let {
                    // Marcador para la ubicación
                    Marker(
                        state = MarkerState(position = it),
                        title = "Ubicación actual"
                    )
                }
            }
        }
    }
}
