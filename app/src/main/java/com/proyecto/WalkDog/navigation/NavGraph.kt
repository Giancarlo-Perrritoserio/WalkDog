package com.proyecto.WalkDog.navigation

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.proyecto.WalkDog.data.model.User
import com.proyecto.WalkDog.ui.components.AppTopBar
import com.proyecto.WalkDog.ui.components.AppBottomBar
import com.proyecto.WalkDog.ui.components.FloatingActionButtonWithOptions
import com.proyecto.WalkDog.ui.components.RestrictedZoneViewModel
import com.proyecto.WalkDog.ui.configurations.SettingsScreen
import com.proyecto.WalkDog.ui.home.HomeScreen
import com.proyecto.WalkDog.ui.login.LoginScreen
import com.proyecto.WalkDog.ui.map.MapScreen
import com.proyecto.WalkDog.ui.profile.ProfileScreen
import com.proyecto.WalkDog.ui.register.RegisterScreen
import com.proyecto.WalkDog.ui.restrictedzone.RestrictedZonesScreen
import com.proyecto.WalkDog.ui.restrictedzone.VoiceRecordingScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph(navController: NavHostController, user: User) {
    // Obtener el ViewModel utilizando Hilt
    val viewModel: RestrictedZoneViewModel = hiltViewModel()
    // Estado para la ubicación
    var userLocation by remember { mutableStateOf<Location?>(null) }
    var isLocationAvailable by remember { mutableStateOf(false) }

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
                bottomBar = { AppBottomBar(navController) },
                floatingActionButton = {
                    FloatingActionButtonWithOptions(
                        navController = navController, // Se pasa el navController
                        viewModel = viewModel, // Se pasa el viewModel (supongo que tienes acceso al viewModel en el contexto)
                        user = user,// Se pasa el usuario (también debería estar disponible en el contexto)
                        onSaveZone = {
                            // Aquí verificamos si la ubicación está disponible antes de guardar la zona
                            if (isLocationAvailable && userLocation != null) {
                                viewModel.saveRestrictedZone(user)  // Guarda la zona usando la ubicación obtenida
                            } else {
                                // Si la ubicación no está disponible, mostramos un mensaje
                                Toast.makeText(navController.context, "Esperando ubicación... Intenta de nuevo", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            ) { innerPadding ->
                HomeScreen(navController, modifier = Modifier.padding(innerPadding))
            }
        }
        composable(route = Screen.Map.route) {
            Scaffold(
                topBar = { AppTopBar(title = "Map", navController = navController) },
                bottomBar = { AppBottomBar(navController) },
                floatingActionButton = {
                    FloatingActionButtonWithOptions(
                        navController = navController, // Se pasa el navController
                        viewModel = viewModel, // Se pasa el viewModel (supongo que tienes acceso al viewModel en el contexto)
                        user = user, // Se pasa el usuario (también debería estar disponible en el contexto)
                        onSaveZone = {
                            // Aquí verificamos si la ubicación está disponible antes de guardar la zona
                            if (isLocationAvailable && userLocation != null) {
                                viewModel.saveRestrictedZone(user)  // Guarda la zona usando la ubicación obtenida
                            } else {
                                // Si la ubicación no está disponible, mostramos un mensaje
                                Toast.makeText(navController.context, "Esperando ubicación... Intenta de nuevo", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            ) { innerPadding ->
                MapScreen(modifier = Modifier.padding(innerPadding))
            }
        }

        composable(route = Screen.RestrictedZones.route) {
            Scaffold(
                topBar = { AppTopBar(title = "Restricted Zones", navController = navController) },
                bottomBar = { AppBottomBar(navController) },
                floatingActionButton = {
                    FloatingActionButtonWithOptions(
                        navController = navController, // Se pasa el navController
                        viewModel = viewModel, // Se pasa el viewModel (supongo que tienes acceso al viewModel en el contexto)
                        user = user, // Se pasa el usuario (también debería estar disponible en el contexto)
                        onSaveZone = {
                            // Aquí verificamos si la ubicación está disponible antes de guardar la zona
                            if (isLocationAvailable && userLocation != null) {
                                viewModel.saveRestrictedZone(user)  // Guarda la zona usando la ubicación obtenida
                            } else {
                                // Si la ubicación no está disponible, mostramos un mensaje
                                Toast.makeText(navController.context, "Esperando ubicación... Intenta de nuevo", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            ) { innerPadding ->
                RestrictedZonesScreen(modifier = Modifier.padding(innerPadding))
            }
        }
        composable(route = Screen.VoiceRecording.route) {
            Scaffold(
                topBar = { AppTopBar(title = "Voice Recording", navController = navController) },
                bottomBar = { AppBottomBar(navController) },
                floatingActionButton = {
                    FloatingActionButtonWithOptions(
                        navController = navController, // Se pasa el navController
                        viewModel = viewModel, // Se pasa el viewModel (supongo que tienes acceso al viewModel en el contexto)
                        user = user, // Se pasa el usuario (también debería estar disponible en el contexto)
                        onSaveZone = {
                            // Aquí verificamos si la ubicación está disponible antes de guardar la zona
                            if (isLocationAvailable && userLocation != null) {
                                viewModel.saveRestrictedZone(user)  // Guarda la zona usando la ubicación obtenida
                            } else {
                                // Si la ubicación no está disponible, mostramos un mensaje
                                Toast.makeText(navController.context, "Esperando ubicación... Intenta de nuevo", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
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
            Scaffold(
                topBar = { AppTopBar(title = "Perfil de Usuario", navController = navController) },
                bottomBar = { AppBottomBar(navController) },
            ) {
                // Aquí se coloca la pantalla ProfileScreen dentro del Scaffold
                ProfileScreen(
                    navController = navController,
                    onProfileUpdated = {
                        // Aquí maneja lo que ocurre cuando el perfil es actualizado, por ejemplo, redirigir a otra pantalla.
                        navController.popBackStack() // Si quieres volver atrás después de guardar los cambios
                    },
                    onError = { errorMessage ->
                        // Aquí maneja el error, como mostrar un mensaje en pantalla.
                        Log.e("ProfileError", errorMessage)
                    }
                )
            }
        }

        composable(route = Screen.Settings.route) {
            SettingsScreen(navController)  // Dirige a la pantalla de configuración
        }
    }
}
