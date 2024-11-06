package com.proyecto.WalkDog.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Home : Screen("home") // Por si deseas añadir una pantalla principal más adelante
    data object Map : Screen("map")
}
