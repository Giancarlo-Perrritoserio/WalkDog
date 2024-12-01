package com.proyecto.WalkDog.ui.restrictedzone

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.proyecto.WalkDog.R
import com.proyecto.WalkDog.ui.map.MapViewModel
import com.proyecto.WalkDog.utils.AudioPlayer.startPlaying
import com.proyecto.WalkDog.utils.AudioPlayer.stopPlaying
import com.proyecto.WalkDog.utils.AudioRecorder
import com.proyecto.WalkDog.utils.AudioRecorder.requestAudioPermissionAndStartRecording
import com.proyecto.WalkDog.utils.AudioRecorder.stopRecording
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceRecordingScreen(
    zoneId: String,
    context: Context,
    viewModel: MapViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    var isRecording by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }
    var audioFile: File? by remember { mutableStateOf(null) }
    val mediaRecorder = remember { MediaRecorder() }
    val mediaPlayer = remember { MediaPlayer() }
    val scope = rememberCoroutineScope()
    val localContext = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    // Simulación de lista de audios para la vista previa
    val audioList = remember { mutableStateListOf<File>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Audios de Voz") },
                colors = TopAppBarDefaults.mediumTopAppBarColors()
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Botón de grabación
            Button(
                onClick = {
                    if (!isRecording) {
                        AudioRecorder.requestAudioPermissionAndStartRecording(localContext, mediaRecorder)?.let {
                            audioFile = it
                            isRecording = true
                        }
                    } else {
                        AudioRecorder.stopRecording(mediaRecorder)
                        isRecording = false
                        audioFile?.let { file ->
                            audioList.add(file)  // Agregar el audio grabado a la lista
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(text = if (isRecording) "Detener Grabación" else "Iniciar Grabación")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de audios grabados
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(audioList) { audio ->
                    AudioItem(
                        audio = audio,
                        mediaPlayer = mediaPlayer,
                        isPlaying = isPlaying,
                        onPlayToggle = { isPlaying = !isPlaying },
                        onRename = { newName ->
                            val renamedFile = File(audio.parent, "$newName.mp3")
                            audio.renameTo(renamedFile)
                            audioList[audioList.indexOf(audio)] = renamedFile
                        },
                        onDelete = {
                            audio.delete()
                            audioList.remove(audio)
                        },
                        onViewLocation = {
                            scope.launch {
                                snackbarHostState.showSnackbar("Guardado en: ${audio.absolutePath}")
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AudioItem(
    audio: File,
    mediaPlayer: MediaPlayer,
    isPlaying: Boolean,
    onPlayToggle: () -> Unit,
    onRename: (String) -> Unit,
    onDelete: () -> Unit,
    onViewLocation: () -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf(audio.nameWithoutExtension) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isEditing) {
                    TextField(
                        value = newName,
                        onValueChange = { newName = it },
                        label = { Text("Nombre del Audio") },
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = {
                        onRename(newName)
                        isEditing = false
                    }) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = "Guardar Nombre")
                    }
                } else {
                    Text(
                        text = audio.nameWithoutExtension,
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { isEditing = true }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar Nombre")
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = {
                    if (!isPlaying) {
                        startPlaying(audio, mediaPlayer) { onPlayToggle() }
                    } else {
                        stopPlaying(mediaPlayer)
                        onPlayToggle()
                    }
                }) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Clear else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Detener" else "Reproducir"
                    )
                }

                IconButton(onClick = { onDelete() }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar")
                }

                IconButton(onClick = { onViewLocation() }) {
                    Icon(imageVector = Icons.Default.Send, contentDescription = "Ver Ubicación")
                }
            }
        }
    }
}

