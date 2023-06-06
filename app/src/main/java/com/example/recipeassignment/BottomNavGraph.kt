package com.example.recipeassignment

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Surface
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.recipeassignment.profilescreen.ProfileScreen
import com.example.recipeassignment.recipedetail.RecipeDetail
import com.example.recipeassignment.recipelistscreen.RecipeListScreen
import com.example.recipeassignment.recipelistscreen.RecipeListViewModel
import com.example.recipeassignment.recipelistscreen.SavedScreen
import com.example.recipeassignment.shoppingscreen.CartScreen
import com.example.recipeassignment.weekplan.SearchRecipes
import com.example.recipeassignment.weekplan.WeekPlan

@Composable
fun BottomNavGraph(navController: NavHostController) {
    val viewModel : RecipeListViewModel = hiltViewModel()
    NavHost(navController = navController, startDestination = BottomNavClass.Home.route){
        composable(route = BottomNavClass.Home.route){
            RecipeListScreen(viewModel = viewModel, toRecipeDetailScreen = {
                navController.navigate("recipe_detail_screen/${it}")
            }, toCartScreen = {
                navController.navigate("cart_screen")
            })
        }
        composable("cart_screen"){
            CartScreen(viewModel = viewModel)
        }
        composable("recipe_detail_screen/{recipeId}",
            arguments = listOf(
                navArgument("recipeId"){
                    type = NavType.IntType
                }
            )
        ){
            val recipeId = remember {
                it.arguments?.getInt("recipeId")
            }
            if(recipeId!=null){
                RecipeDetail(recipeId = recipeId, viewModel = viewModel )
            }
        }
        composable(route = BottomNavClass.Profile.route){
            ProfileScreen() {
                navController.navigate("week_plan_screen")
            }
        }
        composable("week_plan_screen"){
            WeekPlan(viewModel = viewModel, toSearchRecipeScreen = {
                navController.navigate("search_recipe_screen/${it}")
            })
        }
        composable(route = BottomNavClass.Saved.route){
            SavedScreen(viewModel = viewModel)
        }
        composable("search_recipe_screen/{weekDay}"
        , arguments = listOf(
            navArgument("weekDay"){
                type = NavType.StringType
            }
        )
        ){
            val weekDay = remember {
                it.arguments?.getString("weekDay")
            }
            SearchRecipes(
                viewModel = viewModel,
                weekDay = weekDay!!,
                closeScreen = { navController.popBackStack() }
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {

    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomBar(navController = navController)}
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            BottomNavGraph(navController = navController)
        }
    }

}

@Composable
fun BottomBar(navController: NavHostController) {

    val screens = listOf(
        BottomNavClass.Home,
        BottomNavClass.Saved,
        BottomNavClass.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentDestination = navBackStackEntry?.destination

    BottomNavigation {
        screens.forEach{
            AddItem(screen = it, currentDestination = currentDestination, navController = navController)
        }
    }

}

@Composable
fun RowScope.AddItem(
    screen : BottomNavClass,
    currentDestination : NavDestination?,
    navController: NavHostController
) {
    BottomNavigationItem(label = {
        Text(text = screen.title)
    },
        icon = {
            Icon(imageVector = screen.icon, contentDescription = "")
        },
        selected = currentDestination?.hierarchy?.any{
            it.route == screen.route
        } == true,
        onClick = {
            navController.navigate(screen.route){
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        },
        unselectedContentColor = LocalContentColor.current.copy(alpha = ContentAlpha.disabled)
        )
}