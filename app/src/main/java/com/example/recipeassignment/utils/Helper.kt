package com.example.recipeassignment.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


inline fun <reified T> Gson.frommJson(json: String) =
    fromJson<T>(json, object : TypeToken<T>() {}.type)

//fun RecipeX.toRecipe(): SavedRecipe {
//    return SavedRecipe(
//        name = label,
//        image = image,
//        totalWeight = totalWeight,
//        totalTime = totalTime,
//        mealType = mealType,
//        source = source
//    )
//}