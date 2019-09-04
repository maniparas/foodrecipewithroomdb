package com.example.foodrecipes.util

import androidx.lifecycle.LiveData
import com.example.foodrecipes.requests.responses.ApiResponse
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

class LiveDataCallAdapter<T>(
    private val responseType: Type
) : CallAdapter<T, LiveData<ApiResponse<T>>> {

    override fun adapt(call: Call<T>): LiveData<ApiResponse<T>> {
        return object : LiveData<ApiResponse<T>>() {
            override fun onActive() {
                super.onActive()
                val apiResponse = ApiResponse<T>()
                call.enqueue(object : Callback<T> {
                    override fun onFailure(call: Call<T>, t: Throwable) {
                        postValue(apiResponse.create(t))
                    }

                    override fun onResponse(call: Call<T>, response: Response<T>) {
                        postValue(apiResponse.create(response))
                    }

                })
            }
        }
    }

    override fun responseType(): Type = responseType

}