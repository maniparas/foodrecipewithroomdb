package com.example.foodrecipes.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.foodrecipes.models.Recipe

@Database(entities = [Recipe::class], version = 1)
@TypeConverters(Converters::class)
abstract class RecipeDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "recipes_db"

        @Volatile
        private var instance: RecipeDatabase? = null

        fun getInstance(context: Context) =
            instance ?: synchronized(context.applicationContext) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    RecipeDatabase::class.java,
                    DATABASE_NAME
                ).build().also { instance = it }
            }
    }

    abstract fun getRecipeDao(): RecipeDao
}