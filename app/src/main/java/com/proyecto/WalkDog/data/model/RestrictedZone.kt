package com.proyecto.WalkDog.data.model

data class RestrictedZone(
    val id: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val ownerId: String = "" // Se agrega el ownerId para identificar al propietario
)
