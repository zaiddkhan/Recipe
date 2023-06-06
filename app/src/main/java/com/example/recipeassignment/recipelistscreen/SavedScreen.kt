package com.example.recipeassignment.recipelistscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SavedScreen(
    viewModel: RecipeListViewModel
) {
    val recipeList = viewModel.recipe.observeAsState()
    Column {
        Text(
            modifier = Modifier
                .padding(top = 20.dp, start = 15.dp),
            text = "Saved Recipes",
            fontSize = 24.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        LazyVerticalGrid(
            columns = GridCells.Adaptive(160.dp),
            contentPadding = PaddingValues(top = 20.dp, start = 12.dp)
        )
        {
            recipeList.value?.let {list ->
                items(list.size) {
                    Box(modifier = Modifier
                        .padding(start = 18.dp, bottom = 20.dp),
                    ) {

                        Recipes(
                            toCartScreen = {},
                            onRecipeSaved = {},
                            toRecipeDetailScreen = {},
                            recipeSavedScreen = true,
                            forSearch = false,
                            onSearchRecipeClicked = {},
                            modifier = Modifier,
                            recipe = list[it],
                            addIngredientsToCart = {
                                viewModel.addToIngredients(it)
                            }
                        )
                    }
                }

            }
        }

    }
}