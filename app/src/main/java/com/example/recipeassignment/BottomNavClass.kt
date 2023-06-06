package com.example.recipeassignment

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavClass (
    val route : String,
    val title : String,
    val icon : ImageVector
){
    object Home : BottomNavClass(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )
    object Profile : BottomNavClass(
        route = "profile",
        title = "Profile",
        icon = Icons.Default.Person
    )
    object Saved : BottomNavClass(
        route = "saved",
        title = "Saved",
        icon = Icons.Default.Favorite
    )

}