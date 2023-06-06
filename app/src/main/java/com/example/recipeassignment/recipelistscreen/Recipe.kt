package com.example.recipeassignment.recipelistscreen

import android.view.translation.UiTranslationManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.recipeassignment.R
import com.example.recipeassignment.data.Result
import kotlinx.coroutines.launch

@Composable
fun Recipes(
    toCartScreen : () -> Unit,
    onRecipeSaved : (Result) -> Unit,
    toRecipeDetailScreen : (Int) -> Unit,
    recipeSavedScreen : Boolean,
    onSearchRecipeClicked : (Result) -> Unit,
    forSearch : Boolean,
    modifier: Modifier = Modifier,
    recipe: Result,
    addIngredientsToCart : (Int) -> Unit
) {
    val scope = rememberCoroutineScope()

    var savedState by remember(recipe.isSaved) {
        mutableStateOf(recipe.isSaved)
    }
    val title = recipe.title



    Card(
        modifier = Modifier
            .width(140.dp)
            .height(200.dp)
            .clickable {
                if(forSearch) {
                    onSearchRecipeClicked(recipe)
                }else{
                    toRecipeDetailScreen(recipe.id)
                }
            },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            contentAlignment = Alignment.TopStart
        ) {
            val request = ImageRequest.Builder(LocalContext.current)
                .data(recipe.image)
                .build()
            val painter = rememberAsyncImagePainter(
                model = request
            )


            Image(
                painter = painter,
                contentDescription = "Recipe Image",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentScale = ContentScale.Crop
            )




            val interactionSource = MutableInteractionSource()


            if(!forSearch) {
                Box(modifier = Modifier
                    .padding(top = 5.dp, start = 90.dp)
                    .align(Alignment.TopStart)
                    .width(40.dp)
                    .height(40.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        null
                    ) {

                    }
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Color.Gray, Color.Gray)
                        ), RectangleShape, 0.5f
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    val path = remember {
                        Path()
                    }
                    if (!recipe.isSaved && !recipeSavedScreen) {
                        Canvas(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 10.dp, start = 12.dp)
                                .clickable {
                                    scope.launch {
                                        savedState = true
                                        recipe.isSaved = true
                                        onRecipeSaved(recipe)

                                    }
                                }
                        ) {

                            path.moveTo(0f, 0f)
                            path.lineTo(30f, 0f)
                            path.moveTo(30f, 0f)
                            path.lineTo(30f, 40f)
                            path.quadraticBezierTo(30f, 50f, 15f, 25f)
                            path.moveTo(15f, 25f)
                            path.quadraticBezierTo(0f, 50f, 0f, 40f)
                            path.moveTo(0f, 40f)
                            path.lineTo(0f, 0f)
                            path.moveTo(0f, 0f)
                            drawPath(path, color = Color.Black, style = Stroke(2.dp.toPx()))

                        }
                    }
                    Box {
                        Column {
                            AnimatedVisibility(visible = savedState) {


                                val interactionSource = MutableInteractionSource()
                                Image(
                                    painter = painterResource(id = R.drawable.bookmark),
                                    contentDescription = "Saved Button",
                                    modifier = Modifier
                                        .height(40.dp)
                                        .width(25.dp)
                                        .clickable(
                                            interactionSource = interactionSource,
                                            null
                                        ) {
                                            savedState = false
                                            recipe.isSaved = false
                                        }
                                )
                            }
                        }
                    }

                }
            }

        }

        Column(modifier = Modifier) {

            Spacer(modifier = Modifier.height(5.dp))
            Text(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .height(23.dp)
                ,
                text = title,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(20.dp))

            if(!forSearch) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp)
                        .height(25.dp)
                        .clickable {
                            addIngredientsToCart(recipe.id)
                            toCartScreen()
                        }
                        .clip(RoundedCornerShape(20.dp))
                        .background(color = Color.White)
                        .border(2.dp, Color.Black, RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "Add ingredients to cart",
                        fontSize = 10.sp,
                        color = Color.Black,
                    )

                }
            }
        }
    }
}




@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating : Int
) {

    var ratingState by remember {
        mutableStateOf(rating)
    }

//    Row(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(start = 10.dp),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.Start
//    ) {
//        for(i in 1..5){
//            Icon(
//                painter = painterResource(id = R.drawable.star),
//                contentDescription = "",
//                modifier = modifier
//                    .width(15.dp)
//                    .height(15.dp),
//                tint = if(i <= ratingState) Color(0XFFFFD700) else Color(0XFFA2ADB1)
//
//            )
//        }
//    }


}