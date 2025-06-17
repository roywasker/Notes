package com.example.notesmoveo.screen.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object Login: Screen()

    @Serializable
    data object Signup: Screen()

    @Serializable
    data object Home: Screen()

    @Serializable
    data object ListView: Screen()

    @Serializable
    data object MapView: Screen()

    @Serializable
    data class Note(
        val id: String?
    ): Screen()

}