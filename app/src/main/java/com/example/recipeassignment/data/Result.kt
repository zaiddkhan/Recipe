package com.example.recipeassignment.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Result(
    @PrimaryKey
    val id: Int,
    val image: String,
    val imageType: String,
    val title: String,
    var isSaved : Boolean = false
)