package com.proyecto.WalkDog.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.proyecto.WalkDog.data.model.User
import com.proyecto.WalkDog.navigation.Screen
import com.proyecto.WalkDog.ui.map.MapViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavHostController, viewModel: MapViewModel = hiltViewModel()) {
    val userLocation by viewModel.userLocation.collectAsState()

    // Obtener la ubicación del usuario al iniciar la pantalla
    LaunchedEffect(Unit) {
        viewModel.fetchUserLocation() // Obtener ubicación al cargar la pantalla
    }

    // Obtener el usuario autenticado de Firebase
    val currentUser = FirebaseAuth.getInstance().currentUser
    val user = currentUser?.let {
        User(
            uid = it.uid,
            email = it.email ?: "",
            name = it.displayName
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                userLocation?.let {
                    user?.let { u ->
                        // Guardar el punto como zona restringida, pasando el usuario completo
                        viewModel.saveRestrictedZone(it, u)
                    }
                }
            }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar zona restringida")
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(onClick = { navController.navigate(Screen.Map.route) }) {
                    Text(text = "Ir al Mapa")
                }

                Button(onClick = { navController.navigate(Screen.RestrictedZones.route) }) {
                    Text(text = "Ver Zonas Restringidas")
                }

                Spacer(modifier = Modifier.height(20.dp))

                userLocation?.let {
                    Text("Ubicación actual: Lat: ${it.latitude}, Lng: ${it.longitude}")
                } ?: Text("Obteniendo ubicación...")
            }
        }
    }
}
