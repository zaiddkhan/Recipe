package com.example.recipeassignment.data


data class Recipe(
    val number: Int = 0,
    val offset: Int = 0,
    val results: List<Result> = emptyList(),
    val totalResults: Int = 0
)