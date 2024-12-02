package com.proyecto.WalkDog.ui.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.proyecto.WalkDog.ui.components.AppTopBar
import com.proyecto.WalkDog.ui.components.RestrictedZoneViewModel

@Composable
fun SaveRestrictedZoneScreen(
    viewModel: RestrictedZoneViewModel = hiltViewModel(),
    navController: NavHostController
) {
    // Obtener la ubicación actual del usuario desde el ViewModel
    val userLocation by viewModel.userLocation.collectAsState()
    val isLocationAvailable = userLocation != null

    // Botón para guardar la zona
    fun saveZone() {
        if (isLocationAvailable) {
            // Llamamos al método para guardar la zona restringida
            viewModel.saveRestrictedZone()
            Toast.makeText(navController.context, "Zona guardada exitosamente", Toast.LENGTH_SHORT).show()
        } else {
            // Si la ubicación no está disponible, mostramos un mensaje
            Toast.makeText(navController.context, "Esperando ubicación...", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Guardar una nueva zona restringida.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Contenedor con título y botón para guardar el punto
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, shape = MaterialTheme.shapes.medium) // Aplicar sombra en lugar de elevation
                        .clip(MaterialTheme.shapes.medium) // Usar el mismo shape para recortar la sombra
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .background(Color.White) // Fondo blanco para la tarjeta
                    ) {
                        Text(
                            text = "Guardar Punto",
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        // Botón para guardar el punto
                        Button(
                            onClick = { saveZone() },
                            enabled = isLocationAvailable,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Guardar Zona")
                        }

                        if (!isLocationAvailable) {
                            Text(
                                text = "Esperando ubicación...",
                                color = Color.Red,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    )

    // Iniciar actualizaciones de ubicación cuando la pantalla está activa
    LaunchedEffect(Unit) {
        viewModel.startLocationUpdates()
    }

    // Detener las actualizaciones de ubicación cuando la Composable se desecha
    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopLocationUpdates()
        }
    }
}

