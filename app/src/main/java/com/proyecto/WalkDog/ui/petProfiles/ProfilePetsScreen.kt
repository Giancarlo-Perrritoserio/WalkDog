package com.proyecto.WalkDog.ui.petProfiles

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.proyecto.WalkDog.R
import com.proyecto.WalkDog.data.model.PetProfile
import com.proyecto.WalkDog.data.model.PetUiState
import com.proyecto.WalkDog.ui.components.AppTopBar
import java.util.UUID

@Composable
fun PetCard(pet: PetProfile, onSelectPet: (String) -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onSelectPet(pet.id) },
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.mascotaprueba),
                contentDescription = pet.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = pet.name,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun AddPetCard(onCreateNewPet: (PetProfile, (String) -> Unit) -> Unit, petProfileViewModel: PetProfileViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                showDialog = true
                Log.d("AddPetCard", "Card clicked to add new pet")
            },
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AddCircle,
                contentDescription = "Agregar mascota",
                modifier = Modifier.size(80.dp),
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Agregar",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Gray
            )
        }
    }

    if (showDialog) {
        AddPetDialog(
            onDismiss = {
                showDialog = false
                Log.d("AddPetCard", "Dialog dismissed")
            },
            onSavePet = { petName, petAge, petBreed, petSpecies, petPhotoUrl ->
                // Obtener el ID del usuario logueado
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId != null) {
                    val newPet = PetProfile(
                        id = UUID.randomUUID().toString(), // Generar un ID único
                        name = petName,
                        age = petAge.toIntOrNull() ?: 0, // Convertir edad a entero, por defecto 0
                        breed = petBreed,
                        species = petSpecies,
                        ownerId = userId, // Asignar el ID del usuario logeado
                        photoUrl = petPhotoUrl
                    )
                    // Aquí pasas el nuevo perfil de mascota y la función de éxito
                    onCreateNewPet(newPet) { message ->
                        successMessage = message // Guardar el mensaje de éxito
                        showDialog = false // Cerrar el diálogo al guardar
                    }
                } else {
                    Log.e("AddPetCard", "Usuario no logeado")
                    successMessage = "Error: Usuario no logeado"
                }
            },
            petProfileViewModel = petProfileViewModel // Pasamos el ViewModel aquí
        )
    }

    if (successMessage.isNotEmpty()) {
        Toast.makeText(LocalContext.current, successMessage, Toast.LENGTH_SHORT).show()
    }
}



@Composable
fun AddPetDialog(onDismiss: () -> Unit, onSavePet: (String, String, String, String, String) -> Unit, petProfileViewModel: PetProfileViewModel) {
    var petName by remember { mutableStateOf("") }
    var petAge by remember { mutableStateOf("") }
    var petBreed by remember { mutableStateOf("") }
    var petSpecies by remember { mutableStateOf("") }
    var petPhotoUrl by remember { mutableStateOf("") }
    val context = LocalContext.current

    // ActivityResultLauncher para seleccionar una imagen
    val pickImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                // Llamar al ViewModel para manejar la carga
                petProfileViewModel.selectAndUploadImage(it, onSuccess = { url ->
                    petPhotoUrl = url // Actualizar URL de la imagen
                }, onError = { error ->
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show() // Manejar el error
                })
            }
        }

    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        title = { Text("Agregar nueva mascota") },
        text = {
            Column {
                TextField(
                    value = petName,
                    onValueChange = { petName = it },
                    label = { Text("Nombre de la mascota") }
                )
                TextField(
                    value = petAge,
                    onValueChange = { petAge = it },
                    label = { Text("Edad de la mascota") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                TextField(
                    value = petBreed,
                    onValueChange = { petBreed = it },
                    label = { Text("Raza de la mascota") }
                )
                TextField(
                    value = petSpecies,
                    onValueChange = { petSpecies = it },
                    label = { Text("Especie de la mascota") }
                )
                // Botón para seleccionar imagen
                Button(
                    onClick = { pickImageLauncher.launch("image/*") }
                ) {
                    Text("Seleccionar imagen")
                }
                if (petPhotoUrl.isNotEmpty()) {
                    Text("Imagen seleccionada: $petPhotoUrl")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSavePet(petName, petAge, petBreed, petSpecies, petPhotoUrl)
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancelar")
            }
        }
    )
}


// Función para subir la imagen al Firebase Storage
@Composable
fun onSelectImage(uri: Uri, context: Context, onSuccess: (String) -> Unit) {
    val viewModel = hiltViewModel<PetProfileViewModel>()
    viewModel.selectAndUploadImage(uri, onSuccess = { url ->
        onSuccess(url)  // Pasar la URL de la imagen a la función de éxito
    }, onError = { errorMessage ->
        Toast.makeText(context, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
    })
}


@Composable
fun EditPetDialog(
    pet: PetProfile,
    isEditing: Boolean,
    onToggleEdit: () -> Unit,
    onUpdatePet: (PetProfile) -> Unit
) {
    // Logs para depurar
    Log.d("EditPetDialog", "isEditing before dialog: $isEditing")

    var name by remember { mutableStateOf(pet.name) }
    var species by remember { mutableStateOf(pet.species) }
    var breed by remember { mutableStateOf(pet.breed) }
    var age by remember { mutableStateOf(pet.age.toString()) }

    AlertDialog(
        onDismissRequest = {
            Log.d("EditPetDialog", "onDismissRequest called")
            onToggleEdit()
        },
        title = { Text(text = "Editar Mascota") },
        text = {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre de la mascota") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = species,
                    onValueChange = { species = it },
                    label = { Text("Especie") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = breed,
                    onValueChange = { breed = it },
                    label = { Text("Raza") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it },
                    label = { Text("Edad") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    Log.d("EditPetDialog", "onUpdatePet clicked, updating pet: $name")
                    onUpdatePet(
                        pet.copy(
                            name = name,
                            species = species,
                            breed = breed,
                            age = age.toIntOrNull() ?: pet.age
                        )
                    )
                    onToggleEdit()  // Cerrar el diálogo después de guardar
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF003366))
            ) {
                Text("Guardar", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = {
                Log.d("EditPetDialog", "Cancel clicked")
                onToggleEdit()
            }) {
                Text("Cancelar")
            }
        }
    )
}





@Composable
fun PetListScreen(viewModel: PetProfileViewModel, onClick: (PetProfile) -> Unit) {
    val pets = viewModel.getPetsForDisplay()
    var selectedPet by remember { mutableStateOf<PetProfile?>(null) }
    var isEditing by remember { mutableStateOf(false) }

    // Estado para manejar el mensaje del Toast
    var toastMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    // Mostrar el Toast si hay un mensaje
    toastMessage?.let {
        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        toastMessage = null  // Limpiar el mensaje después de mostrarlo
    }

    LazyColumn {
        items(pets) { pet ->
            PetItem(pet, onClick = {
                selectedPet = pet
                isEditing = true  // Mostrar el diálogo de edición
            })
        }
    }

    // Si una mascota está seleccionada, mostrar el diálogo de edición
    selectedPet?.let { pet ->
        EditPetDialog(
            pet = pet,
            isEditing = isEditing,
            onToggleEdit = { isEditing = !isEditing },
            onUpdatePet = { updatedPet ->
                // Guardamos el perfil de la mascota
                viewModel.savePetProfile(updatedPet,
                    onSuccess = {
                        // Muestra el Toast de éxito
                        toastMessage = "Mascota actualizada"
                    },
                    onError = { error ->
                        // Muestra el Toast de error
                        toastMessage = "Error: $error"
                    }
                )
            }
        )
    }
}


@Composable
fun PetItem(pet: PetProfile, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(100.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),  // Al hacer clic se abre el diálogo de edición
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberImagePainter(pet.photoUrl),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .padding(8.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(pet.name, fontWeight = FontWeight.Bold)
            Text(pet.species)
            Text("${pet.age} años")
        }
    }
}

@Composable
fun ProfilePetsScreen(
    navController: NavHostController,
    viewModel: PetProfileViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val petsList by viewModel.petsList.collectAsState(initial = emptyList())
    val isEditing by viewModel.isEditing.collectAsState() // Obtener el estado de isEditing desde el ViewModel
    var selectedPet by remember { mutableStateOf<PetProfile?>(null) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            AppTopBar(title = "Perfil de Mascotas", navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Administra y edita los perfiles de tus mascotas. Selecciona una para ver más detalles o agregar una nueva.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            AddPetCard(
                onCreateNewPet = { petProfile, onSuccess ->
                    viewModel.savePetProfile(petProfile, onSuccess = {
                        onSuccess("Mascota guardada exitosamente")
                        Toast.makeText(context, "Mascota guardada exitosamente", Toast.LENGTH_SHORT).show()
                    }, onError = { errorMessage ->
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    })
                },
                petProfileViewModel = viewModel
            )

            if (petsList.isNotEmpty()) {
                Text(
                    text = "Tus mascotas:",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
                )

                PetListScreen(viewModel = viewModel) { pet ->
                    selectedPet = pet
                    viewModel.toggleEditingState()  // Cambiar el estado de edición al seleccionar una mascota
                    Log.d("ProfilePetsScreen", "Pet selected: ${pet.name}, isEditing set to: $isEditing")
                }
            } else {
                Text(
                    text = "No tienes mascotas aún.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }

        // Mostrar el cuadro de diálogo de edición si hay una mascota seleccionada y el estado de edición es verdadero
        selectedPet?.let { pet ->
            if (isEditing) {
                Log.d("ProfilePetsScreen", "Edit dialog visible for pet: ${pet.name}")
                // Usar 'key' para forzar la recomposición cuando isEditing cambia
                // Aquí es donde se pasa la key
                key(isEditing) {
                    EditPetDialog(
                        pet = pet,
                        isEditing = isEditing,
                        onToggleEdit = {
                            Log.d("ProfilePetsScreen", "onToggleEdit called, isEditing set to false")
                            viewModel.toggleEditingState()  // Cambiar el estado de edición a 'false' cuando se cierra el diálogo
                            selectedPet = null  // Limpiar la mascota seleccionada para evitar que se muestre de nuevo
                        },
                        onUpdatePet = { updatedPet ->
                            viewModel.savePetProfile(updatedPet, onSuccess = {
                                Toast.makeText(context, "Mascota actualizada correctamente", Toast.LENGTH_SHORT).show()
                            }, onError = { errorMessage ->
                                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                            })
                            viewModel.toggleEditingState()  // Cerrar el diálogo después de guardar
                            selectedPet = null  // Limpiar la mascota seleccionada después de actualizar
                        }
                    )
                }
            }
        }
    }
}
