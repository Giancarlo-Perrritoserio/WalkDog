package com.proyecto.WalkDog.ui.restrictedzone

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
            TopAppBar(title = { Text("Zonas Restringidas") })
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(text = "ID: ${zone.id}")
        Text(text = "Latitud: ${zone.latitude}")
        Text(text = "Longitud: ${zone.longitude}")

        TextField(
            value = name,
            onValueChange = { newName ->
                name = newName
            },
            label = { Text("Nombre de la Zona") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                // Abre el selector de audio filtrado a archivos mp3
                audioPickerLauncher.launch("audio/*")
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(text = "Seleccionar Audio")
        }

        if (isUploading) {
            Text(text = "Subiendo audio...", color = Color.Gray, modifier = Modifier.padding(top = 8.dp))
        } else if (audioUri != null) {
            Text(text = "Audio seleccionado: ${audioUri.toString()}", color = Color.Gray, modifier = Modifier.padding(top = 8.dp))
        }

        if (zone.audioUrl.isNotEmpty()) {
            Text(text = "Audio URL: ${zone.audioUrl}", color = Color.Blue, modifier = Modifier.padding(top = 8.dp))
        }
    }
}
