package com.example.recipeassignment.di

import android.app.Application
import androidx.room.Room
import com.example.recipeassignment.remote.RecipeApi
import com.example.recipeassignment.remote.RecipeRepository
import com.example.recipeassignment.room.RecipeDatabase
import com.example.recipeassignment.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRecipeDatabase(app:  Application): RecipeDatabase {
        return Room.databaseBuilder(
            app,
            RecipeDatabase::class.java,
            RecipeDatabase.DATABASE_NAME,
        ).build()
    }

    @Provides
    @Singleton
    fun provideRecipeRepository(api: RecipeApi,db:RecipeDatabase) = RecipeRepository(api,db)


    @Provides
    @Singleton
    fun provideRecipeApi(): RecipeApi{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RecipeApi::class.java)
    }
}