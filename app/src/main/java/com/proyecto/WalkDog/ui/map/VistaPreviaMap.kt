package com.proyecto.WalkDog.ui.map

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.Marker
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerState

@Composable
fun VistaPreviaMap(location: LatLng?) {
    val cameraPositionState = rememberCameraPositionState {
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(location ?: LatLng(0.0, 0.0), 15f)
    }

    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),  // Ajusta la altura según lo necesites
        cameraPositionState = cameraPositionState
    ) {
        location?.let {
            Marker(
                state = MarkerState(position = it),  // Se usa MarkerState en lugar de position directamente
                title = "Ubicación actual"
            )
        }
    }
}
