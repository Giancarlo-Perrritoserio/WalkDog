package com.proyecto.WalkDog.ui.petProfiles

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.proyecto.WalkDog.Repository.PetProfileRepository
import com.proyecto.WalkDog.data.model.PetProfile
import com.proyecto.WalkDog.data.model.PetUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class PetProfileViewModel @Inject constructor(
    private val repository: PetProfileRepository,
    private val auth: FirebaseAuth  // Inyectamos FirebaseAuth para obtener el usuario actual
) : ViewModel() {

    // Lista observable de mascotas
    private val _petsList = MutableStateFlow<List<PetProfile>>(emptyList())
    val petsList: StateFlow<List<PetProfile>> = _petsList

    // Estado que indica si estamos en el modo de edición
    private val _isEditing = MutableStateFlow(false)  // Nuevo estado para isEditing
    val isEditing: StateFlow<Boolean> = _isEditing

    var petUiState by mutableStateOf(PetUiState())
        private set

    init {
        loadPets()  // Cargar la lista al iniciar el ViewModel
    }

    // Cargar la lista de mascotas desde el repositorio filtrada por el ownerId del usuario logeado
    private fun loadPets() {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid
            if (userId != null) {
                // Llamar al repositorio para obtener las mascotas del usuario logeado
                _petsList.value = repository.getPetsByOwnerId(userId)
            } else {
                // Si no hay usuario logeado, se devuelve una lista vacía
                _petsList.value = emptyList()
            }
        }
    }

    // Obtener los datos de las mascotas en un formato adecuado para mostrar en cuadraditos
    fun getPetsForDisplay(): List<PetProfile> {
        return _petsList.value.map { pet ->
            PetProfile(
                id = pet.id,
                name = pet.name,
                species = pet.species,
                breed = pet.breed,
                age = pet.age,
                photoUrl = pet.photoUrl
            )
        }
    }

    // Seleccionar una mascota para editarla
    fun selectPet(petId: String) {
        viewModelScope.launch {
            val petData = repository.getPetProfile(petId)
            petUiState = PetUiState(
                id = petData.id,
                name = petData.name,
                species = petData.species,
                breed = petData.breed,
                age = petData.age,
                photoUrl = petData.photoUrl,
                ownerId = petData.ownerId
            )
        }
    }

    // Cambiar el estado de edición
    fun toggleEditingState() {
        _isEditing.value = !_isEditing.value
    }

    // Crear un nuevo perfil vacío para una mascota
    fun createNewPet(petProfile: PetProfile) {
        val userId = auth.currentUser?.uid // Obtén el ID del usuario logeado
        if (userId != null) {
            // Asociamos la mascota con el usuario logeado
            val updatedPetProfile = petProfile.copy(ownerId = userId)
            petUiState = PetUiState(
                id = updatedPetProfile.id,
                name = updatedPetProfile.name,
                species = updatedPetProfile.species,
                breed = updatedPetProfile.breed,
                age = updatedPetProfile.age,
                photoUrl = updatedPetProfile.photoUrl,
                ownerId = updatedPetProfile.ownerId
            )
        } else {
            // Maneja el caso en que el usuario no está logeado
            Log.e("createNewPet", "Usuario no logeado")
        }
    }

    // Función para seleccionar y cargar la imagen desde el dispositivo
    fun selectAndUploadImage(uri: Uri, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        // Subir la imagen a Firebase Storage
        val storageReference = FirebaseStorage.getInstance().reference
        val imageReference = storageReference.child("pet_photos/${UUID.randomUUID()}.jpg")  // Carpeta para las fotos de las mascotas

        // Subir la imagen al Firebase Storage
        imageReference.putFile(uri)
            .addOnSuccessListener {
                // Obtener la URL de descarga de la imagen subida
                imageReference.downloadUrl.addOnSuccessListener { downloadUrl ->
                    // Llamar a onSuccess con la URL de la imagen
                    onSuccess(downloadUrl.toString())
                }
            }
            .addOnFailureListener {
                // Manejar errores de subida
                onError("Error al subir la imagen: ${it.message}")
            }
    }

    // Guardar o actualizar el perfil de la mascota con la URL de la imagen
    fun savePetProfile(petProfile: PetProfile, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                // Verificar que ownerId no esté vacío
                var updatedPetProfile = petProfile  // Crear una nueva variable para el perfil actualizado

                if (updatedPetProfile.ownerId.isEmpty()) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        updatedPetProfile = updatedPetProfile.copy(ownerId = userId)  // Asignar la nueva copia a updatedPetProfile
                    } else {
                        onError("No hay un usuario logeado.")
                        return@launch
                    }
                }

                // Guardar el perfil actualizado
                repository.savePetProfile(updatedPetProfile) // Usa updatedPetProfile en lugar de petProfile original
                loadPets()  // Cargar las mascotas después de guardar
                onSuccess()  // Notificar que la operación fue exitosa
            } catch (e: Exception) {
                onError(e.message ?: "Error al guardar el perfil")
            }
        }
    }



    // Actualizar el estado de la UI
    fun updatePetUiState(newUiState: PetUiState) {
        petUiState = newUiState
    }



}
