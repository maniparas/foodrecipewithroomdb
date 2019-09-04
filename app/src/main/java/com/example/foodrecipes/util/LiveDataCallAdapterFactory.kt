package com.example.foodrecipes.util

import androidx.lifecycle.LiveData
import com.example.foodrecipes.requests.responses.ApiResponse
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class LiveDataCallAdapterFactory : CallAdapter.Factory() {

    /**
     * This method performs number of checks and then returns the response type for the retrofit requests.
     * {@bodyType is the ResponseType. It can be RecipeResponse or RecipeSearchResponse}
     *
     * Check #1) returnType returns LiveData
     * Check #2) Type LiveData<T> is of ApiResponse.class
     * Check #3) Make sure ApiResponse is parameterized. AKA: ApiResponse<T> exists
     */

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {

        // Check # 1
        // Make sure Call Adapter is returning a type of LiveData
        if (getRawType(returnType) != LiveData::class.java) {
            return null
        }

        // Check # 2
        // Type that LiveData is wrapping
        val observableType: Type = getParameterUpperBound(0, returnType as ParameterizedType)

        // Check if its of Type ApiResponse
        val rawObservableType: Type = getRawType(observableType)
        require(rawObservableType == ApiResponse::class.java) { "Type must be a defined resource" }

        // Check # 3
        // Check if ApiResponse is parameterized. AKA: Does ApiResponse<T> exists? (must wrap around T)
        // FYI: T is either RecipeResponse or T will be a RecipeSearchResponse
        require(!(observableType is ParameterizedType).not()) { "resource must be parameterized" }

        val bodyType: Type = getParameterUpperBound(0, observableType as ParameterizedType)
        return LiveDataCallAdapter<Type>(bodyType)
    }

}