package com.example.notesmoveo.domain.impl

import com.example.notesmoveo.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl: AuthRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    override suspend fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
       try {
           auth.signInWithEmailAndPassword(email, password).await()
           onSuccess()
       }catch (e: Exception){
           onError(e.message ?: "Unknown error")
       }
    }

    override suspend fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val userUid = result.user?.uid

            if (userUid != null){
                val userMap = mapOf(
                    "id" to userUid,
                    "email" to email,
                    "firstName" to firstName,
                    "lastName" to lastName
                )

                firestore.collection("users")
                    .document(userUid)
                    .set(userMap)
                    .await()

                onSuccess()
            }else{
                onError("Failed to register new user")
            }

        } catch (e: Exception) {
            onError(e.message ?: "Unknown error")
        }
    }

    override suspend fun signOut(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ){
        return try {
            auth.signOut()
            onSuccess()
        }catch (e: Exception){
            onError("Error while signing out ${e.message}")
        }
    }
}