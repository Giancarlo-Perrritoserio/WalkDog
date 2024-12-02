package com.proyecto.WalkDog.ui.configurations

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.proyecto.WalkDog.R  // Asegúrate de importar el paquete correcto para acceder a los recursos
import com.proyecto.WalkDog.navigation.Screen
//esta no funcional
@Composable
fun SettingsScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Configuraciones",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Opción de Perfil de Mascotas
        MenuItem(
            title = "Perfil de Mascotas",
            icon = painterResource(id = R.drawable.mascotaprueba),
            onClick = { navController.navigate(Screen.PetPerfile.route) }  // Usa la ruta de perfil de mascotas
        )


        // Otras opciones
        MenuItem(
            title = "Notificaciones",
            icon = painterResource(id = R.drawable.advertencia),
            onClick = { /* Navega o abre sección de Notificaciones */ }
        )

        MenuItem(
            title = "Cuenta",
            icon = painterResource(id = R.drawable.advertencia),  // Aquí cargamos la imagen desde drawable
            onClick = { /* Navega o abre sección de Cuenta */ }
        )
    }
}

// Componente reutilizable para cada opción del menú
@Composable
fun MenuItem(title: String, icon: Painter, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                painter = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
