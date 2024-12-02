package com.proyecto.WalkDog.Repository

import com.google.firebase.firestore.FirebaseFirestore
import com.proyecto.WalkDog.data.model.PetProfile
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PetProfileRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    // Obtener un perfil de mascota por su ID
    suspend fun getPetProfile(petId: String): PetProfile {
        val document = firestore.collection("petProfiles").document(petId).get().await()
        return document.toObject(PetProfile::class.java) ?: PetProfile()
    }

    // Guardar o actualizar un perfil de mascota
    suspend fun savePetProfile(petUiState: PetProfile) {
        val petId = petUiState.id.ifEmpty { firestore.collection("petProfiles").document().id }  // Generar un nuevo ID si está vacío

        // Verificar que el ownerId esté presente
        if (petUiState.ownerId.isEmpty()) {
            throw Exception("El ownerId está vacío. No se puede guardar el perfil.")
        }

        val petData = PetProfile(
            id = petId,
            name = petUiState.name,
            species = petUiState.species,
            breed = petUiState.breed,
            age = petUiState.age,
            photoUrl = petUiState.photoUrl,
            ownerId = petUiState.ownerId   // Relación con el dueño
        )

        // Guardar los datos en Firestore
        firestore.collection("petProfiles").document(petId).set(petData).await()
    }


    // Obtener todos los perfiles de mascotas
    suspend fun getAllPets(): List<PetProfile> {
        val snapshot = firestore.collection("petProfiles").get().await()
        return snapshot.documents.mapNotNull { document ->
            document.toObject(PetProfile::class.java)
        }
    }

    // Método para obtener las mascotas de un usuario específico
    suspend fun getPetsByOwnerId(ownerId: String): List<PetProfile> {
        val snapshot = firestore.collection("petProfiles")
            .whereEqualTo("ownerId", ownerId)  // Filtrar por ownerId
            .get()
            .await()

        return snapshot.documents.mapNotNull { document ->
            document.toObject(PetProfile::class.java)
        }
    }
}
