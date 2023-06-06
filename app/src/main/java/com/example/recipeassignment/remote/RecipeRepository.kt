package com.example.recipeassignment.remote

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.recipeassignment.data.Recipe
import com.example.recipeassignment.data.RecipeDetail
import com.example.recipeassignment.data.Result
import com.example.recipeassignment.room.RecipeDatabase
import com.example.recipeassignment.utils.Resource

class RecipeRepository(
    val api: RecipeApi,
    val db : RecipeDatabase
) {

    fun getSavedRecipes():LiveData<List<Result>> {
        return db.dao.getAllRecipes()
    }

    suspend fun saveRecipe(recipe: Result){
        db.dao.insertRecipe(recipe)
    }
    suspend fun getRandomRecipe(query: String) : Resource<Recipe>
    {
        val response = try {
            api.getRandomRecipes(query)
        }catch (e : java.lang.Exception){

            Log.d("api_response",e.message!!)
            return  Resource.Error(null,e.message!!)
        }
        return  Resource.Success(response)
    }

    suspend fun getRecipeDetail(id:Int): RecipeDetail{
       return api.getRecipeById(id)
    }
}