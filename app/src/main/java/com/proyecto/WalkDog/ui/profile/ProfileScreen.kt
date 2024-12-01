package com.proyecto.WalkDog.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel(),
    onProfileUpdated: () -> Unit,
    onError: (String) -> Unit
) {
    val uiState = viewModel.uiState
    var isEditing by remember { mutableStateOf(false) }

    // Llamar al método para obtener los datos del usuario
    LaunchedEffect(Unit) {
        viewModel.getUserData()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = if (isEditing) "Edit Profile" else "Perfil de Usuario",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Mostrar o editar nombre completo
        if (isEditing) {
            OutlinedTextField(
                value = uiState.name,  // Asegúrate de que uiState.name sea un String
                onValueChange = { viewModel.onNameChange(it) },
                label = { Text("First Name") },
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Text(
                text = "Nombre: ${uiState.name} ${uiState.lastName}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar o editar apellido
        if (isEditing) {
            OutlinedTextField(
                value = uiState.lastName,
                onValueChange = { viewModel.onLastNameChange(it) },
                label = { Text("Last Name") },
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Text(
                text = "Apellido: ${uiState.lastName}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar o editar nombre de usuario
        if (isEditing) {
            OutlinedTextField(
                value = uiState.username,
                onValueChange = { viewModel.onUsernameChange(it) },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Text(
                text = "Nombre de usuario: ${uiState.username}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Mostrar un botón para cambiar entre edición y vista
        if (isEditing) {
            // Botón para guardar cambios
            Button(
                onClick = {
                    viewModel.saveUserData(
                        onSuccess = {
                            onProfileUpdated()  // Acción después de actualizar
                            isEditing = false    // Deshabilitar el modo de edición
                        },
                        onError = onError
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Changes")
            }
        } else {
            // Botón para entrar en modo de edición
            Button(
                onClick = { isEditing = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Edit Profile")
            }
        }
    }
}
