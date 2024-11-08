package com.proyecto.WalkDog.data.model

data class RestrictedZone(
    val id: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val ownerId: String = "", // Se agrega el ownerId para identificar al propietario
    val name: String = "",  // Campo para el nombre de la zona
    val audioUrl: String = ""  // Campo para la URL del audio


)
