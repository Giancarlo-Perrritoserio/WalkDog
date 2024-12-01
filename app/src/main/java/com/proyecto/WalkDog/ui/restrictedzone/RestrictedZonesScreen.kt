package com.proyecto.WalkDog.ui.restrictedzone

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.proyecto.WalkDog.data.model.RestrictedZone
import com.proyecto.WalkDog.navigation.Screen
import com.proyecto.WalkDog.ui.map.MapViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestrictedZonesScreen(
    viewModel: MapViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val restrictedZones by viewModel.restrictedZones.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchRestrictedZones()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Zonas Restringidas") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(restrictedZones) { zone ->
                RestrictedZoneItem(zone = zone, viewModel = viewModel)
            }
        }
    }
}

@Composable
fun RestrictedZoneItem(zone: RestrictedZone, viewModel: MapViewModel = hiltViewModel()) {
    val context = LocalContext.current
    var name by remember { mutableStateOf(zone.name) }
    var audioUri by remember { mutableStateOf<Uri?>(null) }
    var isUploading by remember { mutableStateOf(false) }
    var isEditing by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Configura el selector para archivos de audio mp3
    val audioPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            audioUri = it
            isUploading = true
            coroutineScope.launch {
                viewModel.uploadAudio(it, zone.id, context)
                isUploading = false
            }
        }
    }

    // Obtener el nombre del archivo de audio asociado a la zona
    val currentAudioFileName = if (zone.audioUrl.isNotEmpty()) {
        zone.audioUrl.substringAfterLast("/")
    } else {
        null
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f), RoundedCornerShape(16.dp)),
        elevation = CardDefaults.elevatedCardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "ID: ${zone.id}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Nombre de la zona y botón de opciones (menú desplegable)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black),
                    modifier = Modifier.weight(1f)
                )

                // Icono de menú desplegable
                IconButton(onClick = { showMenu = !showMenu }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Más opciones"
                    )
                }

                // Menú desplegable
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        onClick = {
                            isEditing = true
                            showMenu = false
                        },
                        text = { Text("Editar nombre") },
                        leadingIcon = { Icon(Icons.Default.Edit, contentDescription = "Editar") }
                    )
                    DropdownMenuItem(
                        onClick = {
                            audioPickerLauncher.launch("audio/*")
                            showMenu = false
                        },
                        text = { Text("Seleccionar audio") },
                        leadingIcon = { Icon(Icons.Default.PlayArrow, contentDescription = "Seleccionar audio") }
                    )
                    DropdownMenuItem(
                        onClick = {
                            viewModel.deleteRestrictedZone(zone.id)
                            showMenu = false
                        },
                        text = { Text("Eliminar zona") },
                        leadingIcon = { Icon(Icons.Default.Delete, contentDescription = "Eliminar") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de edición de nombre, se muestra solo si `isEditing` es verdadero
            if (isEditing) {
                TextField(
                    value = name,
                    onValueChange = { newName -> name = newName },
                    label = { Text("Nuevo nombre de la zona") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Botón para guardar el nombre editado
                IconButton(onClick = {
                    viewModel.updateZoneName(zone.id, name)
                    isEditing = false // Salir del modo de edición
                }) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Guardar Nombre",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Estado de carga de audio y UI para audio seleccionado
            if (isUploading) {
                // Mostrar un indicador de carga mientras el audio se está subiendo
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Subiendo audio...",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            } else {
                // Mostrar el nombre del audio asociado a la zona si ya existe
                currentAudioFileName?.let {
                    Text(
                        text = "Audio: $it",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                // Mostrar opciones si un nuevo audio fue seleccionado
                if (audioUri != null) {
                    val selectedAudioName = audioUri!!.lastPathSegment ?: "Audio seleccionado"

                    Text(
                        text = "Nuevo audio: $selectedAudioName",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Botones para guardar o cancelar la selección del audio
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                viewModel.saveAudio(audioUri, context) // Llama a saveAudio del viewModel
                                audioUri = null // Reinicia el audioUri después de guardar
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Guardar Audio",
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Guardar")
                        }

                        OutlinedButton(
                            onClick = {
                                audioUri = null // Cancela la selección del audio
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color.Transparent
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Cancelar",
                                tint = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Cancelar", color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}

