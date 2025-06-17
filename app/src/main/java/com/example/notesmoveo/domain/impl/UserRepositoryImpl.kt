package com.example.notesmoveo.domain.impl

import com.example.notesmoveo.data.model.User
import com.example.notesmoveo.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl: UserRepository {

    private val firestore = FirebaseFirestore.getInstance()

    override suspend fun getUserById(
        userUid: String,
        onSuccess: (User) -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val user = firestore.collection("users").document(userUid).get().await()
            if (user.exists()) {
                val currentUser = User(
                    uid = user.getString("id") ?: "",
                    email = user.getString("email") ?: "",
                    firstName = user.getString("firstName") ?: "",
                    lastName = user.getString("lastName") ?: ""
                )
                onSuccess(currentUser)
            } else {
                onError("User not found.")
            }
        }catch (e: Exception){
            onError(e.message ?: "Unknown error")
        }
    }
}