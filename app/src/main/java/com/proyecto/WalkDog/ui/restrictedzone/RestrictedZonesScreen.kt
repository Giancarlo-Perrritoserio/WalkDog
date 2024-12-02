package com.proyecto.WalkDog.ui.restrictedzone

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import com.google.firebase.auth.FirebaseAuth
import com.proyecto.WalkDog.data.model.RestrictedZone

//Función Principal
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestrictedZonesScreen(
    viewModel: ZoneManagementViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val restrictedZones by viewModel.restrictedZones.collectAsState()
    val currentUser = FirebaseAuth.getInstance().currentUser  // Obtener usuario actual

    LaunchedEffect(Unit) {
        if (currentUser != null) {
            viewModel.fetchRestrictedZonesForCurrentUser()  // Cargar zonas solo si el usuario está autenticado
        }
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
        if (currentUser == null) {
            // Mostrar mensaje si el usuario no está autenticado
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Text(
                    text = "Debes iniciar sesión para ver tus zonas restringidas.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        } else {
            // Mostrar la lista de zonas restringidas o mensaje si está vacía
            if (restrictedZones.isEmpty()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    Text(
                        text = "No tienes zonas restringidas registradas.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            } else {
                LazyColumn(modifier = modifier.padding(padding)) {
                    items(restrictedZones) { zone ->
                        RestrictedZoneItem(zone = zone, viewModel = viewModel)
                    }
                }
            }
        }
    }
}



@Composable
fun RestrictedZoneItem(zone: RestrictedZone, viewModel: ZoneManagementViewModel) {
    val context = LocalContext.current
    var name by remember { mutableStateOf(zone.name) }
    var description by remember { mutableStateOf(zone.description ?: "") } // Nueva variable para la descripción
    var isEditing by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f), RoundedCornerShape(16.dp)),
        elevation = CardDefaults.elevatedCardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            ZoneHeader(zone.id)  // Encabezado de la zona

            // Pasar tanto el nombre como la descripción a la función de edición
            ZoneNameSection(
                name = name,
                description = description, // Añadir descripción aquí
                isEditing = isEditing,
                onNameChange = { newName -> name = newName },
                onDescriptionChange = { newDesc -> description = newDesc }, // Función para actualizar la descripción
                onSave = {
                    // Llamar a la función actualizada del ViewModel
                    viewModel.updateZoneDetails(zone.id, name, description)
                    isEditing = false
                }
            )

            ZoneMenu(            // Menú desplegable
                showMenu = showMenu,
                onMenuToggle = { showMenu = !showMenu },
                viewModel = viewModel,
                zone = zone
            )
        }
    }
}




//Encabezado de la zona
@Composable
fun ZoneHeader(zoneId: String) {
    Text(
        text = "ID: $zoneId",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
    )
    Spacer(modifier = Modifier.height(8.dp))
}


//Sección del nombre y edición
@Composable
fun ZoneNameSection(
    name: String,
    description: String,  // Asegúrate de pasar la descripción
    isEditing: Boolean,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,  // Nueva función para actualizar la descripción
    onSave: () -> Unit
) {
    if (isEditing) {
        // Sección para el nombre de la zona
        TextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Nuevo nombre de la zona") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Sección para la descripción de la zona
        TextField(
            value = description,
            onValueChange = onDescriptionChange,  // Cambiar la descripción
            label = { Text("Descripción de la zona") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        IconButton(onClick = onSave) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Guardar cambios",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    } else {
        // Mostrar el nombre y la descripción cuando no estamos en modo de edición
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
        )
    }
}



//Menú desplegable
@Composable
fun ZoneMenu(
    showMenu: Boolean,
    onMenuToggle: () -> Unit,
    viewModel: ZoneManagementViewModel,
    zone: RestrictedZone
) {
    IconButton(onClick = onMenuToggle) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "Más opciones"
        )
    }

    DropdownMenu(
        expanded = showMenu,
        onDismissRequest = { onMenuToggle() }
    ) {
        DropdownMenuItem(
            onClick = { /* Lógica de edición aquí */ },
            text = { Text("Editar nombre") },
            leadingIcon = { Icon(Icons.Default.Edit, contentDescription = "Editar") }
        )
        DropdownMenuItem(
            onClick = { /* Abrir selector de audio */ },
            text = { Text("Seleccionar audio") },
            leadingIcon = { Icon(Icons.Default.PlayArrow, contentDescription = "Seleccionar audio") }
        )
        DropdownMenuItem(
            onClick = { viewModel.deleteRestrictedZone(zone.id) },
            text = { Text("Eliminar zona") },
            leadingIcon = { Icon(Icons.Default.Delete, contentDescription = "Eliminar") }
        )
    }
}



