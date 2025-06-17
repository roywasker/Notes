package com.example.notesmoveo.screen.Home.domain

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Output
import androidx.compose.ui.graphics.vector.ImageVector


enum class DrawerItem(
    val title: String,
    val icon: ImageVector
) {
    Note(
        title = "Note",
        icon = Icons.Default.Note
    ),
    SignOut(
        title = "SignOut",
        icon = Icons.Default.Output
    ),
}