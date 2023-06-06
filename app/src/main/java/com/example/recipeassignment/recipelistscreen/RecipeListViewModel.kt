package com.example.recipeassignment.recipelistscreen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recipeassignment.data.ExtendedIngredient
import com.example.recipeassignment.data.Recipe
import com.example.recipeassignment.data.RecipeDetail
import com.example.recipeassignment.data.Result
import com.example.recipeassignment.remote.RecipeRepository
import com.example.recipeassignment.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    val repository: RecipeRepository
) : ViewModel() {

    var weekPlan : Map<String,MutableList<Result>> = mutableMapOf()
    var recipeList = mutableStateOf<Recipe>(Recipe())

    var recipe : LiveData<List<Result>>

    var shopOfIngredients =  mutableStateOf(mutableListOf<ExtendedIngredient>())




    init {
        recipe = repository.getSavedRecipes()
        loadRecipes("chicken")

    }
    fun getRecipeListForWeekdays(weekday: String):List<Result>?{
        return weekPlan.get(weekday)
    }
    fun addRecipeToDatabase(recipe: Result){
        viewModelScope.launch {
        repository.saveRecipe(recipe)
        }
    }
    suspend fun loadRecipeDetail(id:Int) : RecipeDetail{
        return  repository.getRecipeDetail(id = id)
    }

    fun addRecipeToWeekPlan(weekday:String,recipe:Result){
        val list = weekPlan.get(weekday)

        if(list != null) {
            list.add(recipe)
            weekPlan += weekday to list!!
        }else{
            val newList = mutableListOf<Result>()
            newList.add(recipe)
            weekPlan += weekday to newList
        }
    }

    fun loadRecipes(query: String){
        viewModelScope.launch {
            val result = repository.getRandomRecipe(query)
            when(result) {
                is Resource.Success -> {
                    recipeList.value = result.data!!
                }
                is Resource.Error -> {
                }
                is Resource.Loading -> {

                }
            }
        }
    }

    fun addToIngredients(id:Int){
        viewModelScope.launch {
             val recipe = repository.getRecipeDetail(id)
             shopOfIngredients.value += recipe.extendedIngredients

        }
    }

}