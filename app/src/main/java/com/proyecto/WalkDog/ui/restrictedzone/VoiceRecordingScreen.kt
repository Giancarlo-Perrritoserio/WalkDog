package com.proyecto.WalkDog.ui.restrictedzone

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.proyecto.WalkDog.ui.map.MapViewModel
import com.proyecto.WalkDog.utils.AudioPlayer.startPlaying
import com.proyecto.WalkDog.utils.AudioPlayer.stopPlaying
import com.proyecto.WalkDog.utils.AudioRecorder.requestAudioPermissionAndStartRecording
import com.proyecto.WalkDog.utils.AudioRecorder.stopRecording
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceRecordingScreen(
    zoneId: String,
    context: Context,
    viewModel: MapViewModel = hiltViewModel()
) {
    var isRecording by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }
    var audioFile: File? by remember { mutableStateOf(null) }
    val mediaRecorder = remember { MediaRecorder() }
    val mediaPlayer = remember { MediaPlayer() }
    val scope = rememberCoroutineScope()
    val localContext = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() } // Estado del Snackbar

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Grabar Audio de Voz") },
                colors = TopAppBarDefaults.mediumTopAppBarColors() // Color del TopAppBar
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) } // Agrega el Snackbar
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Botón de grabación
            Button(
                onClick = {
                    if (!isRecording) {
                        requestAudioPermissionAndStartRecording(localContext, mediaRecorder)?.let {
                            audioFile = it
                            isRecording = true
                        }
                    } else {
                        stopRecording(mediaRecorder)
                        isRecording = false
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp), // Botón más grande
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary // Color del botón
                )
            ) {
                Text(
                    text = if (isRecording) "Detener Grabación" else "Iniciar Grabación"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de reproducción
            Button(
                onClick = {
                    if (!isPlaying) {
                        audioFile?.let { file ->
                            startPlaying(file, mediaPlayer) {
                                isPlaying = false
                            }
                            isPlaying = true
                        }
                    } else {
                        stopPlaying(mediaPlayer)
                        isPlaying = false
                    }
                },
                enabled = audioFile != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp), // Botón más grande
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary // Color del botón
                )
            ) {
                Text(
                    text = if (isPlaying) "Detener Reproducción" else "Reproducir Audio"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de guardar
            Button(
                onClick = {
                    audioFile?.let {
                        scope.launch {
                            // Muestra un mensaje de confirmación en el Snackbar
                            snackbarHostState.showSnackbar("Audio guardado en almacenamiento local.")
                        }
                    }
                },
                enabled = audioFile != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp), // Botón más grande
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary // Color del botón
                )
            ) {
                Text(
                    text = "Guardar Audio en Local"
                )
            }
        }
    }
}
