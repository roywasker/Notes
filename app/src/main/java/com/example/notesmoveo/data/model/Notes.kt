package com.example.notesmoveo.data.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Notes(
    val id: String = UUID.randomUUID().toString(),
    val userId: String? = null,
    val title: String = "",
    val body: String= "",
    val date: Long = System.currentTimeMillis(),
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val imageUrl: String? = null
)