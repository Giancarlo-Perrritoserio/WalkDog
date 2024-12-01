package com.proyecto.WalkDog.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.proyecto.WalkDog.data.model.User

@Composable
fun UserGreeting(user: User?) {
    // Fondo con bordes redondeados y sombra
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.background,
        shadowElevation = 4.dp // Añadimos sombra para mayor profundidad
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp), // Padding alrededor de todo el contenido
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Texto de bienvenida grande
            Text(
                text = "¡Bienvenido a WalkDog!",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Texto con el nombre del usuario
            Text(
                text = "Hola, ${user?.name ?: "Usuario"}",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Descripción adicional
            Text(
                text = "¡Sigue el progreso de tu mascota en tiempo real y asegúrate de que siempre esté segura!",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Spacer para añadir espacio antes de otros elementos
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
