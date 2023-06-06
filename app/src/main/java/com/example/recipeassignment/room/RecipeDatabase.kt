package com.example.recipeassignment.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.recipeassignment.data.Result

@Database(entities = [Result::class], version = 2)
@TypeConverters(Converters::class)
abstract class RecipeDatabase : RoomDatabase() {

    abstract val dao : RecipeDao

    companion object{
        const val DATABASE_NAME = "recipe2"
    }
}