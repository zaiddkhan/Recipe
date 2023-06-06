package com.example.recipeassignment.weekplan

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.recipeassignment.R
import com.example.recipeassignment.recipelistscreen.RecipeListViewModel
import com.example.recipeassignment.recipelistscreen.Recipes


@Composable
fun WeekPlan(
    toSearchRecipeScreen :(String) -> Unit,
    viewModel: RecipeListViewModel
) {
    val scrollState = rememberScrollState()

    val weekDays = listOf("Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday")
    Surface(modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                modifier = Modifier
                    .padding(start = 20.dp),
                text = "Plan your week",
                fontSize = 23.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(20.dp))
            weekDays.forEach {
                Weekdays(toSearchRecipeScreen,weekday = it, viewModel = viewModel)
            }
        }

    }
}

@Composable
fun Weekdays(
    toSearchRecipeScreen: (String) -> Unit,
    weekday : String,
    viewModel: RecipeListViewModel
) {
    val scrollState = rememberScrollState()
    var expanded by remember{
        mutableStateOf(false)
    }

    val list by remember {
        mutableStateOf(viewModel.getRecipeListForWeekdays(weekday))
    }


    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(top = 20.dp, start = 10.dp, end = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = weekday,
                fontSize = 16.sp,
                color = Color.Black
            )

            Box() {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    // adding items
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                modifier = Modifier
                                    .size(25.dp),
                                painter = painterResource(id = R.drawable.bookmark),
                                contentDescription = "",
                                tint = Color.White
                            )
                        },
                        text = {
                            Text(text = "Add saved recipe", fontSize = 14.sp)
                        },
                        onClick = {
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                modifier = Modifier
                                    .size(25.dp),
                                imageVector = Icons.Default.Search,
                                contentDescription = "",
                                tint = Color.White
                            )
                        },
                        text = {
                            Text(text = "Search new recipe", fontSize = 14.sp)
                        },
                        onClick = {
                            toSearchRecipeScreen(weekday)
                            expanded = false
                        }
                    )
                }

                Box(
                    modifier = Modifier
                        .background(Color.DarkGray, RoundedCornerShape(15.dp))
                        .clickable {
                            expanded = true
                        }
                ) {
                    Text(
                        text = "+ADD",
                        color = Color.Gray,
                        modifier = Modifier.padding(10.dp),
                        fontSize = 14.sp
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        list?.let { list ->
            LazyRow(
                contentPadding = PaddingValues(bottom = 15.dp)
            ){
                items(list.size){
                    Box(modifier = Modifier
                        .padding(start = 10.dp))
                    Recipes(
                        toCartScreen = {},
                        onRecipeSaved = {},
                        recipeSavedScreen = false,
                        toRecipeDetailScreen = {},
                        onSearchRecipeClicked = {},
                        forSearch = true,
                        recipe = list[it],
                        addIngredientsToCart = {}
                    )
                }
            }
        }
        Divider()
    }

}