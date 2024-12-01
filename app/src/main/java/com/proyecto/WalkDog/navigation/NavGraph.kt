package com.proyecto.WalkDog.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.proyecto.WalkDog.ui.components.AppTopBar
import com.proyecto.WalkDog.ui.components.AppBottomBar
import com.proyecto.WalkDog.ui.configurations.SettingsScreen
import com.proyecto.WalkDog.ui.home.HomeScreen
import com.proyecto.WalkDog.ui.login.LoginScreen
import com.proyecto.WalkDog.ui.map.MapScreen
import com.proyecto.WalkDog.ui.profile.ProfileScreen
import com.proyecto.WalkDog.ui.register.RegisterScreen
import com.proyecto.WalkDog.ui.restrictedzone.RestrictedZonesScreen
import com.proyecto.WalkDog.ui.restrictedzone.VoiceRecordingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(route = Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }
        composable(route = Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }
        composable(route = Screen.Home.route) {
            Scaffold(
                topBar = { AppTopBar(title = "Home", navController = navController) },
                bottomBar = { AppBottomBar(navController) }
            ) { innerPadding ->
                HomeScreen(navController, modifier = Modifier.padding(innerPadding))
            }
        }
        composable(route = Screen.Map.route) {
            Scaffold(
                topBar = { AppTopBar(title = "Map", navController = navController) },
                bottomBar = { AppBottomBar(navController) }
            ) { innerPadding ->
                MapScreen(modifier = Modifier.padding(innerPadding))
            }
        }

        composable(route = Screen.RestrictedZones.route) {
            Scaffold(
                topBar = { AppTopBar(title = "Restricted Zones", navController = navController) },
                bottomBar = { AppBottomBar(navController) }
            ) { innerPadding ->
                RestrictedZonesScreen(modifier = Modifier.padding(innerPadding))
            }
        }
        composable(route = Screen.VoiceRecording.route) {
            Scaffold(
                topBar = { AppTopBar(title = "Voice Recording", navController = navController) },
                bottomBar = { AppBottomBar(navController) }
            ) { innerPadding ->
                VoiceRecordingScreen(
                    zoneId = "someZoneId",
                    context = navController.context,
                    viewModel = hiltViewModel(),
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }

        composable(route = Screen.Profile.route) {
            ProfileScreen(navController) // Dirige a la pantalla de perfil
        }
        composable(route = Screen.Settings.route) {
            SettingsScreen(navController)  // Dirige a la pantalla de configuración
        }
    }
}
