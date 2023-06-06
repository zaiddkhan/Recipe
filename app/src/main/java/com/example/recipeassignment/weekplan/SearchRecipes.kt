package com.example.recipeassignment.weekplan

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recipeassignment.recipelistscreen.CustomTextField
import com.example.recipeassignment.recipelistscreen.RecipeListViewModel
import com.example.recipeassignment.recipelistscreen.Recipes

@Composable
fun SearchRecipes(
    viewModel: RecipeListViewModel,
    closeScreen : () -> Unit,
    weekDay:String
) {
    val recipe by remember{
        mutableStateOf( viewModel.recipeList)
    }
    Column(
        modifier = Modifier.padding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Search from recipes",
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "",
                tint = Color.Black,
                modifier = Modifier
                    .size(45.dp)
                    .clickable {
                        closeScreen()
                    }
            )
        }
        Spacer(modifier = Modifier.height(20.dp))

        CustomTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .border(2.dp, Color.Black)
                .padding(start = 15.dp, end = 15.dp),
            placeholderText = "Search..",
            searchQuery = {
                viewModel.loadRecipes(it)
            }
        )
        Spacer(modifier = Modifier.height(13.dp))
        LazyVerticalGrid(
            columns = GridCells.Adaptive(160.dp),
            contentPadding = PaddingValues(top = 20.dp, start = 12.dp)
        )
        {
            items(recipe.value.results.size) {
                Box(modifier = Modifier
                    .padding(start = 18.dp, bottom = 20.dp),
                ) {
                    Recipes(
                        toCartScreen = {},
                        onRecipeSaved = {},
                        toRecipeDetailScreen = {},
                        recipeSavedScreen = false,
                        onSearchRecipeClicked = {
                              viewModel.addRecipeToWeekPlan(weekDay,it)
                        },
                        forSearch = true,
                        modifier = Modifier,
                        recipe = recipe.value.results[it],
                        addIngredientsToCart = {
                        }
                    )
                }
            }
        }

    }
}