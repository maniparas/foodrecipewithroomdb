package com.example.foodrecipes.requests

import com.example.foodrecipes.util.Constants
import com.example.foodrecipes.util.LiveDataCallAdapterFactory
import com.github.aurae.retrofit2.LoganSquareConverterFactory
import retrofit2.Retrofit

class ServiceGenerator {

    companion object {
        private val retrofitBuilder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .addConverterFactory(LoganSquareConverterFactory.create())

        private val retrofit: Retrofit = retrofitBuilder.build()

        val recipeApi: RecipeApi
            get() = retrofit.create(RecipeApi::class.java)
    }
}