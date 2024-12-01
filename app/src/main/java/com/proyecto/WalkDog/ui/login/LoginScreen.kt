package com.proyecto.WalkDog.ui.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.proyecto.WalkDog.R

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val uiState = viewModel.uiState
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo de pantalla (opcional)
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground), // Aquí puedes usar tu imagen
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Caja para el formulario de login
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(32.dp)
                .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(16.dp))
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Título
            Text(
                text = "Welcome Back!",
                style = MaterialTheme.typography.headlineSmall, // Cambié a headlineSmall
                modifier = Modifier.padding(bottom = 16.dp),
                color = MaterialTheme.colorScheme.primary
            )

            // Campo de correo electrónico
            OutlinedTextField(
                value = uiState.email,
                onValueChange = viewModel::onEmailChange,
                label = { Text("Email Address") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de contraseña
            OutlinedTextField(
                value = uiState.password,
                onValueChange = viewModel::onPasswordChange,
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de login
            Button(
                onClick = {
                    viewModel.onSignInClick(
                        onSuccess = {
                            onLoginSuccess() // Iniciar el flujo de éxito
                            snackbarMessage = "Login Successful!"
                            showSnackbar = true
                        },
                        onError = { message ->
                            snackbarMessage = message // Mostrar el error
                            showSnackbar = true
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Login", style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de registro
            TextButton(onClick = onNavigateToRegister) {
                Text(
                    text = "Don't have an account? Register here",
                    style = MaterialTheme.typography.bodySmall, // Cambié a bodySmall
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Mostrar Snackbar para éxito o error
        if (showSnackbar) {
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                Text(text = snackbarMessage)
            }
        }
    }
}
