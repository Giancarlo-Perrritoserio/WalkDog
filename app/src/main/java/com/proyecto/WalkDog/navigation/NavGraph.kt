package com.proyecto.WalkDog.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.proyecto.WalkDog.ui.home.HomeScreen
import com.proyecto.WalkDog.ui.login.LoginScreen
import com.proyecto.WalkDog.ui.map.MapScreen
import com.proyecto.WalkDog.ui.register.RegisterScreen
import com.proyecto.WalkDog.ui.restrictedzone.RestrictedZonesScreen
import com.proyecto.WalkDog.ui.restrictedzone.VoiceRecordingScreen

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
            HomeScreen(navController)
        }
        composable(route = Screen.Map.route) {
            MapScreen()
        }
        composable(route = Screen.RestrictedZones.route) {  // Asegúrate de que Screen.RestrictedZones esté bien definido
            RestrictedZonesScreen()  // Llama a la pantalla RestrictedZonesScreen
        }
        composable(route = Screen.VoiceRecording.route) {  // Nueva entrada para VoiceRecordingScreen
            VoiceRecordingScreen(
                zoneId = "someZoneId", // Pasa el ID de la zona cuando sea necesario
                context = navController.context,
                viewModel = hiltViewModel() // Inyecta el ViewModel para la grabación
            )
        }
    }
}