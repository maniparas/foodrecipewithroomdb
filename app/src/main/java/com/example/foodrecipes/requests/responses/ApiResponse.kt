package com.example.foodrecipes.requests.responses

import retrofit2.Response

open class ApiResponse<T> {
    class ApiSuccessResponse<T>(var body: T) : ApiResponse<T>()
    class ApiErrorResponse<T>(var errorMessage: String) : ApiResponse<T>()
    class ApiEmptyResponse<T>() : ApiResponse<T>()

    fun create(throwable: Throwable?): ApiResponse<T> =
        ApiErrorResponse(
            if (throwable?.message?.isNotEmpty()!!)
                throwable.message!!
            else "Unknown error\nCheck network connection"
        )

    fun create(response: Response<T>): ApiResponse<T> {
        if (response.isSuccessful) {
            val body: T? = response.body()
            return if (body == null || response.code() == 204) { //204 empty response code
                ApiEmptyResponse()
            } else {
                ApiSuccessResponse(body)
            }
        } else {
            var errorMsg: String
            errorMsg = try {
                response.errorBody().toString()
            } catch (e: Exception) {
                e.printStackTrace()
                e.message!!
            }
            return ApiErrorResponse(errorMsg)
        }
    }
}