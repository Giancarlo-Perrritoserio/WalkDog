// Paquete donde se encuentra la clase `RestrictedZone`, dentro del proyecto WalkDog
package com.proyecto.WalkDog.data.model

// Definición de la clase de datos `RestrictedZone`
// Esta clase representa una zona restringida que puede configurarse en la aplicación WalkDog.
data class RestrictedZone(
    val id: String = "",             // ID único de la zona restringida.
    // Es usado para identificar esta zona en la base de datos de Firebase.

    val latitude: Double = 0.0,       // Latitud de la ubicación central de la zona restringida.
    // Define el punto central en coordenadas geográficas.

    val longitude: Double = 0.0,      // Longitud de la ubicación central de la zona restringida.
    // Define el punto central en coordenadas geográficas.

    val ownerId: String = "",         // ID del propietario de la zona.
    // Asociado al usuario que creó esta zona para poder filtrar zonas por usuario.

    val name: String = "",            // Nombre de la zona restringida.
    // Puede ser descriptivo, como "Cocina" o "Jardín".

    val audioUrl: String = "",        // URL del archivo de audio asociado a la zona.
    // Permite almacenar un mensaje grabado para reproducirse si la mascota entra en la zona.

    val radius: Float = 0f            // Radio de la zona restringida en metros.
    // Define el tamaño del área en torno al punto central en el cual la restricción es activa.
)
