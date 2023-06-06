package com.example.recipeassignment.shoppingscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.recipeassignment.data.ExtendedIngredient
import com.example.recipeassignment.recipelistscreen.RecipeListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(viewModel: RecipeListViewModel) {

    val listOfIngredient by remember {
        mutableStateOf(viewModel.shopOfIngredients.value)
    }
    Scaffold(
        bottomBar = {
            Button(onClick = {

            },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)

            ) {
                Text(text = "Shop now", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    ) {


    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(it)
    ) {
        Column(
            modifier = Modifier
                .padding(start = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Cart Items",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            LazyColumn(
                contentPadding = PaddingValues(top = 10.dp, start = 10.dp)
            ) {
                items(listOfIngredient.size) {
                    IngredientItem(ingredient = listOfIngredient[it])
                }
            }
        }
    }
}
}

@Composable
fun IngredientItem(
    ingredient: ExtendedIngredient
){
    val request = ImageRequest.Builder(LocalContext.current)
        .data(ingredient.image)
        .build()
    val painter = rememberAsyncImagePainter(
        model = request
    )
    Row (verticalAlignment = Alignment.CenterVertically){
        Spacer(modifier = Modifier.width(10.dp) )
        Image(
            painter = painter,
            contentDescription = "Ingredient image",
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
                .clip(CircleShape)
            ,
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(20.dp) )
        Text(text = "${ingredient.amount} ${ingredient.unit}", fontSize = 15.sp, color = Color.Gray)

        Spacer(modifier = Modifier.width(30.dp) )
        Text(text = ingredient.name, fontSize = 15.sp, color = Color.Black, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(10.dp) )
    }
}