package com.proyecto.WalkDog.ui.restrictedzone

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.proyecto.WalkDog.data.model.RestrictedZone
import com.proyecto.WalkDog.ui.map.MapViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestrictedZonesScreen(viewModel: MapViewModel = hiltViewModel()) {
    val restrictedZones by viewModel.restrictedZones.collectAsState()

    // Cargar las zonas al iniciar la pantalla
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
    var name by remember { mutableStateOf(zone.name) }

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

        // Campo de texto para editar el nombre
        TextField(
            value = name,
            onValueChange = { newName ->
                name = newName
                // Actualizamos el nombre en la base de datos
                viewModel.updateZoneName(zone.id, newName)
            },
            label = { Text("Nombre de la Zona") }
        )
    }
}

