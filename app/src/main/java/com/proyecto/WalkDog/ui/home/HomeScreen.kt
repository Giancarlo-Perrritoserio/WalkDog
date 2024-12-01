package com.proyecto.WalkDog.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.proyecto.WalkDog.ui.map.VistaPreviaMap


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: MapViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val userLocation by viewModel.userLocation.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.startLocationUpdates()
    }

    val currentUser = FirebaseAuth.getInstance().currentUser
    val user = currentUser?.let {
        User(
            uid = it.uid,
            email = it.email ?: "",
            name = it.displayName
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    userLocation?.let { location ->
                        user?.let { u ->
                            viewModel.saveRestrictedZone(location, u)
                        }
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar zona restringida")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(top = 56.dp)  // Margen para no estar tapado por el TopAppBar
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // Llamada al Composable para mostrar el saludo y bienvenida
                UserGreeting(user = user)

                // Vista previa del mapa
                VistaPreviaMap(location = userLocation)

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { navController.navigate(Screen.Map.route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = "Ir al Mapa",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}