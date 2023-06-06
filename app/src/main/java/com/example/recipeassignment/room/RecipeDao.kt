package com.example.recipeassignment.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.recipeassignment.data.Result


@Dao
interface RecipeDao {

    @Insert
    suspend fun insertRecipe(recipe: com.example.recipeassignment.data.Result)


    @Query("SELECT * from result")
    fun getAllRecipes() : LiveData<List<Result>>
}