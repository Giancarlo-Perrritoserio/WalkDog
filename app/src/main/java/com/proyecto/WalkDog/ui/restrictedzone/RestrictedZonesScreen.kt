package com.proyecto.WalkDog.ui.restrictedzone

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
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
import com.proyecto.WalkDog.ui.map.MapViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestrictedZonesScreen(viewModel: MapViewModel = hiltViewModel()) {
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
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(restrictedZones) { zone ->
                RestrictedZoneItem(zone)
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
    var showMenu by remember { mutableStateOf(false) } // Control del menú desplegable
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

            // Estado de carga de audio
            if (isUploading) {
                Text(
                    text = "Subiendo audio...",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            } else if (audioUri != null) {
                Text(
                    text = "Audio seleccionado: ${audioUri.toString()}",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Mostrar URL del audio si ya está disponible
            if (zone.audioUrl.isNotEmpty()) {
                Text(
                    text = "Audio URL: ${zone.audioUrl}",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
