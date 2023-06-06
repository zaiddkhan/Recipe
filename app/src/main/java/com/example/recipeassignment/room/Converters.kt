package com.example.recipeassignment.room

import androidx.room.TypeConverter
import com.example.recipeassignment.utils.frommJson
import com.google.gson.Gson

class Converters {


    @TypeConverter
    fun fromCuisines(value : List<String>):String{
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toCuisines(value : String): List<String>{
        return  try {
            Gson().frommJson<List<String>>(value)
        }catch (e : java.lang.Exception){
            listOf()
        }
    }
}