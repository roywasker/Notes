package com.example.notesmoveo.domain.repository

import com.example.notesmoveo.data.model.User


interface UserRepository {

    suspend fun getUserById(
        userUid: String,
        onSuccess: (User) -> Unit,
        onError: (String) -> Unit)
}