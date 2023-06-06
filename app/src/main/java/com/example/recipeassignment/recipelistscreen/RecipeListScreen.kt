package com.example.recipeassignment.recipelistscreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Snackbar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListScreen(
    modifier: Modifier = Modifier,
    viewModel: RecipeListViewModel,
    toCartScreen : () -> Unit,
    toRecipeDetailScreen : (Int) -> Unit
) {
    val recipe by remember{
       mutableStateOf( viewModel.recipeList)
    }
    var shoppingCartSize by remember {
        mutableStateOf(viewModel.shopOfIngredients.value.size)
    }
    val scope = rememberCoroutineScope()

    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(key1 = viewModel.shopOfIngredients.value.size ){
        shoppingCartSize = viewModel.shopOfIngredients.value.size
    }
    Scaffold(
        topBar = { TopAppBar(viewModel = viewModel) }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = it)
        ) {
            Column {
                Spacer(modifier = Modifier.height(20.dp))
                CustomTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(start = 15.dp, end = 15.dp),
                    placeholderText = "Search..",
                    searchQuery = {
                        viewModel.loadRecipes(it)
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))
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
                                toCartScreen = {
                                       toCartScreen()
                                },
                                onRecipeSaved = {
                                    scope.launch {
                                    viewModel.addRecipeToDatabase(it)
                                    scaffoldState.snackbarHostState.showSnackbar("Recipe saved")
                                }
                                },
                                recipeSavedScreen = false,
                                toRecipeDetailScreen = {
                                    toRecipeDetailScreen(it)
                                },
                                forSearch = false,
                                onSearchRecipeClicked = {},
                                modifier = Modifier,
                                recipe = recipe.value.results[it],
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


}

@Composable
fun TopAppBar(
    viewModel : RecipeListViewModel
) {

    var size by remember {
        mutableStateOf(viewModel.shopOfIngredients.value.size)
    }
    LaunchedEffect(key1 = viewModel.shopOfIngredients.value.size){
        size = viewModel.shopOfIngredients.value.size
    }
    Card(
        elevation = CardDefaults.cardElevation(8.dp)
    ) {


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .align(Alignment.TopEnd)
                    .padding(top = 10.dp, end = 10.dp)
                    .background(
                        Color.Black,
                        RoundedCornerShape(15.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)
                ) {

                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Shopping Cart",
                        modifier = Modifier.background(
                            Color.Black,
                            RoundedCornerShape(10.dp)
                        ),
                        tint = Color.White
                    )
                    Text(
                        text = size.toString(),
                        color = Color.White, fontSize = 14.sp
                    )

                }
            }
        }
    }

}


@Composable
fun CustomTextField(
    searchQuery : (String) -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    placeholderText: String = "Placeholder",
    fontSize: TextUnit = MaterialTheme.typography.body2.fontSize
) {
    var text by rememberSaveable { mutableStateOf("") }
    BasicTextField(modifier = modifier
        .background(
            MaterialTheme.colors.surface,
            RoundedCornerShape(8.dp),
        )
        .fillMaxWidth(),
        value = text,
        onValueChange = {
            text = it
            searchQuery(text)
        },
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colors.primary),
        textStyle = LocalTextStyle.current.copy(
            color = MaterialTheme.colors.onSurface,
            fontSize = fontSize
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null) leadingIcon()
                Box(Modifier.weight(1f)) {
                    if (text.isEmpty())
                        androidx.compose.material.Text(
                        placeholderText,
                        style = LocalTextStyle.current.copy(
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f),
                            fontSize = fontSize
                        )
                    )
                    innerTextField()
                }

            }
        }
    )
}
