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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.proyecto.WalkDog.ui.components.AppBottomBar
import com.proyecto.WalkDog.ui.components.AppTopBar
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
        topBar = { AppTopBar(title = "Home", navController = navController) },  // Asegura la barra superior
        bottomBar = { AppBottomBar(navController) }                             // Asegura la barra inferior
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(top = 8.dp, bottom = 8.dp)  // Espacio extra para evitar solapamiento
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())  // Desplazamiento vertical
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // Llamada al Composable para mostrar el saludo y bienvenida
                UserGreeting(user = user)

                VistaPreviaMap(
                     location = userLocation,
                     onClick = {
                         println("Vista previa del mapa clickeada, redirigiendo...")
                         navController.navigate(Screen.Map.route)
                     }
                 )

                Spacer(modifier = Modifier.height(20.dp))

                 SaveRestrictedZoneScreen(
                     viewModel = hiltViewModel(),
                     navController = navController
                 )
            }
        }
    }
}
