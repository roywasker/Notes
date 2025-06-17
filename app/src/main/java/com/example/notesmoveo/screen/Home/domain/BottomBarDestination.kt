package com.example.notesmoveo.screen.Home.domain

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Map
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.notesmoveo.screen.navigation.Screen

enum class BottomBarDestination(
    val icon: ImageVector,
    val title: String,
    val screen: Screen
) {
    List(
        icon = Icons.Default.List,
        title = "List",
        screen = Screen.ListView
    ),
    Map(
        icon = Icons.Default.Map,
        title = "Map",
        screen = Screen.MapView
    )
}