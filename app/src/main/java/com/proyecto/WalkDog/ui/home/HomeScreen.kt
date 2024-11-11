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
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.data.UiToolingDataApi
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.proyecto.WalkDog.data.model.User
import com.proyecto.WalkDog.navigation.Screen
import com.proyecto.WalkDog.ui.map.MapViewModel

@OptIn(ExperimentalMaterial3Api::class, UiToolingDataApi::class)
@Composable
fun HomeScreen(navController: NavHostController, viewModel: MapViewModel = hiltViewModel()) {
    // La variable userLocation obtiene la ubicación actual del usuario desde el ViewModel, que se actualiza automáticamente a medida que cambia el estado
    val userLocation by viewModel.userLocation.collectAsState()

    // Obtener la ubicación del usuario cuando la pantalla se carga por primera vez
    LaunchedEffect(Unit) {
        viewModel.startLocationUpdates() // Llama a la función fetchUserLocation en el ViewModel para obtener la ubicación del usuario
    }

    // Obtener el usuario autenticado desde Firebase (si está autenticado)
    val currentUser = FirebaseAuth.getInstance().currentUser
    val user = currentUser?.let {
        User(  // Si el usuario está autenticado, crea un objeto User con su información (uid, email, name)
            uid = it.uid,
            email = it.email ?: "",
            name = it.displayName
        )
    }

    // Scaffold es una estructura base para crear pantallas con una disposición básica.
    // Contiene elementos como la barra superior (TopAppBar), el botón flotante (FloatingActionButton), y la barra inferior (BottomAppBar)
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,  // Define el color de fondo de la pantalla
        topBar = {  // Barra superior con el título de la pantalla
            TopAppBar(
                title = { Text("WalkDog App") },  // Título que aparece en la parte superior
                colors = TopAppBarDefaults.mediumTopAppBarColors() // Definir el color de la barra
            )
        },
        floatingActionButton = {  // Botón flotante que realiza una acción cuando se hace clic
            FloatingActionButton(
                onClick = {  // Acción cuando el usuario hace clic en el botón
                    userLocation?.let { location ->  // Verifica si la ubicación del usuario está disponible
                        user?.let { u ->  // Verifica si el objeto User está disponible
                            viewModel.saveRestrictedZone(location, u)  // Llama al ViewModel para guardar la zona restringida
                        }
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,  // Color del botón flotante
                contentColor = MaterialTheme.colorScheme.onPrimary  // Color del ícono del botón flotante
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar zona restringida")  // Ícono dentro del botón
            }
        },
        bottomBar = {  // Barra inferior con navegación
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.surface,  // Color de la barra inferior
                contentColor = MaterialTheme.colorScheme.onSurface  // Color del contenido de la barra inferior
            ) {
                BottomNavigation {
                    BottomNavigationItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                        selected = false,
                        onClick = { navController.navigate(Screen.Home.route) }  // Navega a la pantalla principal
                    )
                    BottomNavigationItem(
                        icon = { Icon(Icons.Default.Place, contentDescription = "Zonas Restringidas") },
                        selected = false,
                        onClick = { navController.navigate(Screen.RestrictedZones.route) }  // Navega a la pantalla de zonas restringidas
                    )
                    BottomNavigationItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = "Grabar Audio") },
                        selected = false,
                        onClick = { navController.navigate(Screen.VoiceRecording.route) }  // Navega a la pantalla de grabación de audio
                    )
                }
            }
        }
    ) { padding ->  // El contenido principal de la pantalla
        Box(
            modifier = Modifier
                .fillMaxSize()  // El Box toma todo el tamaño de la pantalla
                .padding(padding)  // Aplica el padding recibido para ajustarse al Scaffold
                .padding(16.dp),  // Aplica un padding adicional de 16dp
            contentAlignment = Alignment.Center  // Centra el contenido dentro del Box
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),  // La columna ocupa todo el ancho disponible
                horizontalAlignment = Alignment.CenterHorizontally,  // Centra el contenido horizontalmente
                verticalArrangement = Arrangement.Center  // Centra el contenido verticalmente
            ) {
                // Saludo al usuario con su nombre (si está disponible) o con un texto predeterminado
                Text(
                    text = "Bienvenido ${user?.name ?: "Usuario"}",  // Muestra el nombre del usuario si está autenticado, de lo contrario "Usuario"
                    style = MaterialTheme.typography.headlineMedium,  // Estilo del texto
                    color = MaterialTheme.colorScheme.onBackground,  // Color del texto
                    modifier = Modifier.padding(bottom = 24.dp)  // Aplica un margen inferior
                )

                // Texto explicativo de ejemplo
                Text(
                    text = "¡Sigue el progreso de tu mascota en tiempo real y asegúrate de que siempre esté segura!",
                    style = MaterialTheme.typography.bodyLarge,  // Estilo del texto
                    color = MaterialTheme.colorScheme.onBackground,  // Color del texto
                    modifier = Modifier.padding(bottom = 24.dp)  // Aplica un margen inferior
                )

                // Botón para ir al mapa
                Button(
                    onClick = { navController.navigate(Screen.Map.route) },  // Navega a la pantalla del mapa cuando se hace clic
                    modifier = Modifier
                        .fillMaxWidth()  // El botón ocupa todo el ancho disponible
                        .padding(bottom = 16.dp),  // Aplica un margen inferior
                    shape = RoundedCornerShape(12.dp),  // Bordes redondeados
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)  // Colores del botón
                ) {
                    Text(
                        text = "Ir al Mapa",  // Texto dentro del botón
                        style = MaterialTheme.typography.bodyLarge,  // Estilo del texto
                        color = MaterialTheme.colorScheme.onPrimary  // Color del texto
                    )
                }


                // Espaciador entre botones y ubicación
                Spacer(modifier = Modifier.height(20.dp))

            }
        }
    }
}
