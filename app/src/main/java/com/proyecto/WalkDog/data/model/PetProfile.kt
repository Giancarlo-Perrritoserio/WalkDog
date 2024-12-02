package com.proyecto.WalkDog.data.model

data class PetProfile(
    val id: String = "",           // Identificador único
    val name: String = "",
    val species: String = "",
    val breed: String = "",
    val age: Int = 0,
    val photoUrl: String = "",
    val ownerId: String = ""       // Relación con el dueño
)
