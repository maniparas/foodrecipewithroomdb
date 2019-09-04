package com.example.foodrecipes.util

import android.util.Log
import com.example.foodrecipes.models.Recipe

fun printRecipe(recipeList: List<Recipe>, tag: String) {
    for (recipe in recipeList) {
        Log.d(tag, "onChanged: ${recipe.title}")
    }
}