package com.proyecto.WalkDog.ui.components

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
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