package com.example.foodrecipes.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.foodrecipes.AppExecutors
import com.example.foodrecipes.models.Recipe
import com.example.foodrecipes.persistence.RecipeDao
import com.example.foodrecipes.persistence.RecipeDatabase
import com.example.foodrecipes.requests.ServiceGenerator
import com.example.foodrecipes.requests.responses.ApiResponse
import com.example.foodrecipes.requests.responses.RecipeSearchResponse
import com.example.foodrecipes.util.Constants
import com.example.foodrecipes.util.NetworkBoundResource
import com.example.foodrecipes.util.Resource

class RecipeRepository private constructor(context: Context) {

    private val recipeDao: RecipeDao = RecipeDatabase.getInstance(context).getRecipeDao()

    companion object {
        @Volatile
        private var instance: RecipeRepository? = null

        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: RecipeRepository(context).also { instance = it }
            }
    }

    fun searchRecipesApi(
        query: String,
        pageNumber: Int
    ): LiveData<Resource<List<Recipe>>> {
        return object :
            NetworkBoundResource<List<Recipe>, RecipeSearchResponse>(AppExecutors.getInstance()) {
            override fun saveCallResult(item: RecipeSearchResponse) {
                if (item.count != 0) {
                    val recipes = mutableListOf<Recipe>()
                    if (item.recipes.isNotEmpty()) {
                        for ((index, rowId) in recipeDao.insertRecipes(item.recipes).withIndex()) {
                            if (rowId.equals(-1)) {
                                Log.d(
                                    TAG,
                                    "saveCallResult: CONFLICT... This recipe is already in cache"
                                )
                                // If the recipe is already exists.... I don't want to set the ingredients or timestamp b/c
                                // they will be erased
                                recipeDao.updateRecipe(
                                    recipes[index].recipeId,
                                    recipes[index].title,
                                    recipes[index].publisher,
                                    recipes[index].imageUrl,
                                    recipes[index].socialRank
                                )
                            }
                        }
                    }
                }
            }

            override fun shouldFetch(data: List<Recipe>): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<List<Recipe>> =
                recipeDao.searchRecipes(query, pageNumber)

            override fun createCall(): LiveData<ApiResponse<RecipeSearchResponse>> {
                return ServiceGenerator.recipeApi.searchRecipe(
                    Constants.API_KEY,
                    query,
                    pageNumber.toString()
                )
            }

        }.asLiveData()
    }
}