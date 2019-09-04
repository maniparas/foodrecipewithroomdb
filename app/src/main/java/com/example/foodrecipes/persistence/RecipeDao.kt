package com.example.foodrecipes.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foodrecipes.models.Recipe

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertRecipes(recipe: List<Recipe>): Array<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipe(recipe: Recipe): Long

    @Query("UPDATE RECIPES SET TITLE = :title, PUBLISHER = :publisher, IMAGE_URL = :imageUrl, SOCIAL_RANk = :socialRank WHERE RECIPE_ID = :recipeId")
    fun updateRecipe(
        recipeId: String,
        title: String,
        publisher: String,
        imageUrl: String,
        socialRank: Float
    ): Int

    @Query("SELECT * FROM RECIPES WHERE TITLE LIKE '%' || :query || '%' OR INGREDIENTS LIKE '' || :query || '%' ORDER BY SOCIAL_RANK DESC LIMIT (:pageNumber * 30)")
    fun searchRecipes(
        query: String,
        pageNumber: Int
    ): LiveData<List<Recipe>>

    @Query("SELECT * FROM RECIPES WHERE RECIPE_ID = :recipeId")
    fun getRecipe(recipeId: String): LiveData<Recipe>
}