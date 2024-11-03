package com.proyecto.WalkDog.ui.home

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    Button(onClick = { navController.navigate("map") }) {
        Text("Ir al Mapa")
    }
}
