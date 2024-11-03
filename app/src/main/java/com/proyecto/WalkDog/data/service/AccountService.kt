package com.proyecto.WalkDog.data.service

import com.google.firebase.auth.FirebaseUser


interface AccountService {
    suspend fun authenticate(email: String, password: String)
    suspend fun createUser(email: String, password: String)
    suspend fun signOut()
    fun getCurrentUser(): FirebaseUser?
}
