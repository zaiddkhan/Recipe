package com.example.recipeassignment.recipedetail




import android.view.MotionEvent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.recipeassignment.R
import com.example.recipeassignment.data.ExtendedIngredient
import com.example.recipeassignment.data.RecipeDetail
import com.example.recipeassignment.recipelistscreen.RecipeListViewModel
import kotlinx.coroutines.launch



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecipeDetail(
    recipeId: Int,
    viewModel: RecipeListViewModel,
) {


    val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val scaffoldBottomState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )

    val recipeInfo = produceState<RecipeDetail>(initialValue = RecipeDetail()) {
            value = viewModel.loadRecipeDetail(recipeId)
        }.value



    BottomSheetScaffold(
        scaffoldState = scaffoldBottomState,
        sheetContent =
        {

        },
        sheetPeekHeight = 0.dp,
        sheetShape = RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {



            var serves = 0
            val imageUrl = recipeInfo.image?.let {
                ImageDetail(
                    sheetState,
                    modifier = Modifier
                        .fillMaxHeight(0.5f)
                        .fillMaxWidth(),
                    imageUrl = it, recipeDetail = recipeInfo
                )
                serves = recipeInfo.servings


            }

            RatingSection()
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "RATINGS",
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier.padding(start = 70.dp)
            )
            Spacer(modifier = Modifier.height(30.dp))
            val ingredientsList = recipeInfo.extendedIngredients?.let {
                IngredientsSection(serves = 5, ingredients = it)
            }


            val steps = recipeInfo.instructions?.let {
                it.replace("<.*?>", "")
                StepsSection(steps = it)
            }
            Spacer(modifier = Modifier.height(40.dp))
            CommentSection()
            Spacer(modifier = Modifier.height(40.dp))
            UploadPhotoSection()
            Spacer(modifier = Modifier.height(40.dp))

        }
    }

}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ImageDetail(
    sheetState: BottomSheetState,
    recipeDetail: RecipeDetail,
    modifier: Modifier = Modifier,
    imageUrl : String
) {
    Box {
        val request = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .build()
        val painter = rememberAsyncImagePainter(
            model = request
        )
        Image(
            modifier = modifier.fillMaxWidth(),
            painter = painter,
            contentDescription = "Food Image",
            contentScale = ContentScale.Crop
        )

        CircularBoxWithImage(
            sheetState = sheetState,
            "Back",
            imageVector = Icons.Default.ArrowBack,
            description = "Back Button",
            modifier = Modifier.padding(top = 10.dp, start = 10.dp)
        )
        CircularBoxWithImage(
            sheetState = sheetState,
            "Share",
            imageVector = Icons.Default.Share,
            description = "Share Button",
            modifier = Modifier.padding(top = 10.dp, start = 295.dp)
        )
        TitleCard(title = recipeDetail.title, madeBy = recipeDetail.sourceName)
    }
}


@Composable
fun RatingSection(
    modifier: Modifier = Modifier
) {
    Row {
        Spacer(modifier = modifier.width(60.dp))
        Text(text = "3", fontWeight = FontWeight.Bold, fontSize = 20.sp,modifier = Modifier.padding(top = 15.dp))
        Spacer(modifier = modifier.width(5.dp))
        Row(
            modifier = Modifier
                .padding(start = 10.dp, top = 20.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            for (i in 1..5) {
                Icon(
                    painter = painterResource(id = R.drawable.star),
                    contentDescription = "",
                    modifier = modifier
                        .width(15.dp)
                        .height(15.dp),
                    tint = if (i <= 3) Color(0XFFFFD700) else Color(0XFFA2ADB1)

                )
            }
        }
    }
}
@Composable
fun TitleCard(
    title: String,
    madeBy: String
) {
    Card(
        modifier = Modifier
            .padding(top = 350.dp, start = 15.dp, end = 15.dp)
            .fillMaxWidth(),
        elevation = 20.dp,
        shape = RoundedCornerShape(15.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "RECIPE", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(

                text = title.uppercase(),
                fontSize = 14.sp,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(text = "by", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = madeBy, fontSize = 13.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }


}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CircularBoxWithImage(
    sheetState: BottomSheetState,
    buttonType : String,
    modifier : Modifier = Modifier,
    imageVector: ImageVector,
    description : String
) {
    val scope = rememberCoroutineScope()
    Box(
        modifier = modifier
            .padding(start = 5.dp)
            .width(30.dp)
            .height(30.dp)
            .clip(CircleShape)
            .clickable {
                if (buttonType.equals("Fav")) {
                    scope.launch {
                        if (sheetState.isExpanded) {
                            sheetState.collapse()
                        } else {
                            sheetState.expand()
                        }
                    }
                }
            }
            .background(
                Brush.linearGradient(
                    listOf(Color.Gray, Color.Gray)
                ), RectangleShape, 0.5f
            ),
        contentAlignment = Alignment.Center,
    )
    {
        Image(
            imageVector = imageVector,
            contentDescription = description
        )
    }
}



@Composable
fun IngredientsSection(
    serves:Int,
    ingredients : List<ExtendedIngredient>
) {
    Column() {
        Text(
            text = "Ingredients",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.padding(start = 20.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically)
        {
            Text(text = "Serves $serves",
                fontSize = 20.sp
            )
            Text(text = "US metrics",
                fontSize = 14.sp
            )
        }
        Column(modifier = Modifier.padding(top = 20.dp)){
            for(i in 0..ingredients.size-1) {
                IngredientItem(ingredients.get(i))
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
        Text(text = "${ingredient.amount.toString()} ${ingredient.unit}", fontSize = 15.sp, color = Color.Gray)

        Spacer(modifier = Modifier.width(30.dp) )
        Text(text = ingredient.name, fontSize = 15.sp, color = Color.Black, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(10.dp) )
    }
}

@Composable
fun StepsSection(
    steps : String
) {
    Column {

        Text(
            text = "Steps",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.padding(start = 20.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = steps,
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 20.dp)
        )

    }
}

@Composable
fun CommentSection()
{
    Row(modifier = Modifier
        .padding(start = 20.dp, end = 20.dp)
        .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween)
    {
        Text(text = "Tap to Rate : ", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        ClickableRatingBar(rating = 0)
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ClickableRatingBar(
    rating : Int
) {

    var rating by remember {
        mutableStateOf(rating)
    }

    var selected by remember {
        mutableStateOf(false)
    }
    val size by animateDpAsState(targetValue = if(selected) 35.dp else 30.dp , spring(Spring.DampingRatioMediumBouncy))

    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center)
    {
        for(i in 1..5){
            Icon(painter = painterResource(id = R.drawable.star),
                contentDescription = "stars",
                modifier = Modifier
                    .width(size)
                    .height(size)
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                selected = true
                                rating = i
                            }

                            MotionEvent.ACTION_UP -> {
                                selected = false
                            }
                        }
                        true
                    },
                tint = if(i <= rating) Color(0XFFFFd700) else Color(0XFFA2ADB1)
            )
        }

    }

}

@Composable
fun UploadPhotoSection() {

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent() ,
        onResult ={

        } )
    Row(modifier = Modifier.padding(start = 20.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Box(
            modifier = Modifier
                .padding(start = 5.dp)
                .width(30.dp)
                .height(30.dp)
                .clip(CircleShape)
                .background(
                    Color.Black
                ),
            contentAlignment = Alignment.Center,
        )
        {
            Image(
                painter = painterResource(id = R.drawable.camera),
                contentDescription = "description",
                modifier = Modifier
                    .clickable {
                        singlePhotoPickerLauncher.launch(
                            "image/"
                        )
                    }
            )
        }
        Spacer(modifier = Modifier.width(90.dp))
        var text by remember { mutableStateOf(TextFieldValue("Add a comment")) }
        TextField(
            value = text,
            onValueChange = { newText ->
                text = newText
            },
            modifier = Modifier
                .width(300.dp)
                .padding(end = 20.dp)
                .clip(RoundedCornerShape(20.dp))
        )
    }
}





