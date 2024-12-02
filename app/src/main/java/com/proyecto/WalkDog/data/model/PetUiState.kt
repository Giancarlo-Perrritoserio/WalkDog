package com.proyecto.WalkDog.data.model

data class PetUiState(
    val id: String = "",           // Identificador único, vacío por defecto
    val name: String = "",
    val species: String = "",
    val breed: String = "",
    val age: Int = 0,
    val photoUrl: String = "",
    val ownerId: String = ""       // Identificador del dueño de la mascota
)
