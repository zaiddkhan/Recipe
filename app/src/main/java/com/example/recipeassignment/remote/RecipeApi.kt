package com.example.recipeassignment.remote

import com.example.recipeassignment.data.Recipe
import com.example.recipeassignment.data.RecipeDetail
import com.example.recipeassignment.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApi {


    @GET("recipes/complexSearch")
    suspend fun getRandomRecipes(
        @Query("query") query : String = "chicken",
        @Query("apiKey") apiKey : String = Constants.API_KEY,
        @Query("number") number : Int = 10
        ) : Recipe

    @GET("recipes/{id}/information/")
    suspend fun getRecipeById(
        @Path("id") id : Int,
        @Query("apiKey") apiKey: String = Constants.API_KEY,
        @Query("includeNutrition") includeNutrition : Boolean = false
    ) : RecipeDetail
}