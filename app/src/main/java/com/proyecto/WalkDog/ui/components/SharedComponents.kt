package com.proyecto.WalkDog.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import com.proyecto.WalkDog.data.model.User
import com.proyecto.WalkDog.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    navController: NavHostController
) {
    TopAppBar(
        title = { Text(title) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color(0xFF003366),  // Azul marino personalizado
            titleContentColor = Color.White,     // Color del texto en blanco
            actionIconContentColor = Color.White // Color de los íconos en blanco
        ),
        actions = {
            TopBarIcon(
                icon = Icons.Default.AccountCircle,
                description = "Perfil",
                onClick = { navController.navigate(Screen.Profile.route) }
            )
            TopBarIcon(
                icon = Icons.Default.Settings,
                description = "Configuración",
                onClick = { navController.navigate(Screen.Settings.route) }
            )
        }
    )
}

// Función para crear los íconos de la TopBar
@Composable
fun TopBarIcon(icon: ImageVector, description: String, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(imageVector = icon, contentDescription = description)
    }
}

@Composable
fun AppBottomBar(navController: NavHostController) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        BottomNavigation {
            BottomNavigationItem(
                icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                selected = false,
                onClick = { navController.navigate(Screen.Home.route) }
            )
            BottomNavigationItem(
                icon = { Icon(Icons.Default.Place, contentDescription = "Zonas Restringidas") },
                selected = false,
                onClick = { navController.navigate(Screen.RestrictedZones.route) }
            )
            BottomNavigationItem(
                icon = { Icon(Icons.Default.PlayArrow, contentDescription = "Grabar Audio") },
                selected = false,
                onClick = { navController.navigate(Screen.VoiceRecording.route) }
            )
        }
    }
}

@Composable
fun FloatingActionButtonWithOptions(
    navController: NavHostController,
    viewModel: RestrictedZoneViewModel,
    user: User,
    onSaveZone: () -> Unit  // Se agrega el callback para guardar la zona

) {
    var showDialog by remember { mutableStateOf(false) } // Estado para mostrar/ocultar el diálogo
    val userLocation by viewModel.userLocation.collectAsState() // Obtener la ubicación actual del usuario

    // Iniciar actualizaciones de ubicación cuando la pantalla esté activa
    LaunchedEffect(Unit) {
        viewModel.startLocationUpdates()
    }

    // Detener las actualizaciones de ubicación cuando la Composable se desecha
    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopLocationUpdates()
        }
    }

    fun onOptionSelected(option: String) {
        when (option) {
            "Guardar Zona" -> {
                if (userLocation != null) {
                    // Verifica si la ubicación es válida antes de guardarla
                    if (userLocation!!.latitude != 0.0 && userLocation!!.longitude != 0.0) {
                        // Llamar al callback para guardar la zona
                        onSaveZone()
                        Log.d("FloatingActionButton", "Guardando zona en ${userLocation!!.latitude}, ${userLocation!!.longitude}")
                    } else {
                        // Mostrar mensaje si la ubicación no es válida
                        Log.e("FloatingActionButton", "Ubicación inválida, no se puede guardar la zona.")
                    }
                } else {
                    // Mostrar mensaje o Snackbar si la ubicación no está disponible
                    Log.e("FloatingActionButton", "Ubicación no disponible, no se puede guardar la zona.")
                }
            }
            "Ver Mapa" -> {
                navController.navigate(Screen.Map.route) // Navegar al mapa
            }
        }
        showDialog = false // Cerrar el diálogo después de seleccionar una opción
    }


    // Botón flotante para mostrar las opciones
    FloatingActionButton(
        onClick = { showDialog = true },
        containerColor = Color(0xFF003366),
        contentColor = Color.White
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar Zona")
    }

    // Diálogo de opciones
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Seleccione una opción") },
            text = {
                Column {
                    TextButton(onClick = { onOptionSelected("Guardar Zona") }) {
                        Text("Guardar Zona")
                    }
                    TextButton(onClick = { onOptionSelected("Ver Mapa") }) {
                        Text("Ver Mapa")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cerrar")
                }
            }
        )
    }
}



