package com.example.foodrecipes.requests

import androidx.lifecycle.LiveData
import com.example.foodrecipes.requests.responses.ApiResponse
import com.example.foodrecipes.requests.responses.RecipeResponse
import com.example.foodrecipes.requests.responses.RecipeSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeApi {

    @GET("api/search")
    fun searchRecipe(
        @Query("key") key: String,
        @Query("q") query: String,
        @Query("page") page: String
    ): LiveData<ApiResponse<RecipeSearchResponse>>

    @GET("api/get")
    fun getRecipe(
        @Query("key") key: String,
        @Query("rId") recipe_id: String
    ): LiveData<ApiResponse<RecipeResponse>>
}