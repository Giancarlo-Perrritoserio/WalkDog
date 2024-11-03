package com.proyecto.WalkDog.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home") // Por si deseas añadir una pantalla principal más adelante
    object Map : Screen("map")
}
