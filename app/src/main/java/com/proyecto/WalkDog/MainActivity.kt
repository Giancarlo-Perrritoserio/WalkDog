package com.proyecto.WalkDog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.proyecto.WalkDog.data.model.toUser
import com.proyecto.WalkDog.navigation.NavGraph
import com.proyecto.WalkDog.ui.login.LoginScreen
import com.proyecto.WalkDog.ui.theme.WalkDogTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            // Crear un navController para la navegación
            val navController = rememberNavController()

            // Obtener el usuario logueado desde Firebase Authentication
            val firebaseUser = FirebaseAuth.getInstance().currentUser

            // Verificar si el usuario está logueado
            if (firebaseUser != null) {
                // Convertir FirebaseUser a User personalizado
                val user = firebaseUser.toUser()

                // Si el usuario está logueado, pasar el usuario al NavGraph
                NavGraph(navController = navController, user = user)
            } else {
                // Si el usuario no está logueado, navegar solo a LoginScreen
                LoginScreen(
                    onLoginSuccess = {
                        // Si el login es exitoso, navegar al NavGraph
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true } // Para evitar que el usuario regrese al login
                        }
                    },
                    onNavigateToRegister = {
                        // Aquí puedes manejar la navegación a la pantalla de registro
                    }
                )
            }
        }
    }
}

