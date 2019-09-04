package com.example.foodrecipes.util

import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.foodrecipes.AppExecutors
import com.example.foodrecipes.requests.responses.ApiResponse

// CacheObject: Type for the Resource data (for Database Cache).
// RequestObject: Type for the API response (for network request).
abstract class NetworkBoundResource<CacheObject, RequestObject>(
    private val appExecutors: AppExecutors
) {

    companion object {
        const val TAG = "NetworkBoundResource"
    }

    private val results: MediatorLiveData<Resource<CacheObject>> = MediatorLiveData()

    init {
        // update livedata for loading status
        results.value = Resource.Loading(null) as Resource<CacheObject>

        // observe livedata source from local database
        val dbSource: LiveData<CacheObject> = loadFromDb()
        results.addSource(dbSource) {

            // Remove observer from local db. Need to decide if read local db or network
            results.removeSource(dbSource)

            // get data from network if conditions in shouldFetch(boolean) are true
            if (shouldFetch(it)) {
                fetchFromNetwork(dbSource)
            } else {
                results.addSource(dbSource) { cacheObject ->
                    setValue(Resource.Success(cacheObject))
                }
            }
        }
    }


    /**
     * 1) Observer the localDb
     * 2) if <condition /> query the network
     * 3) stop observing the local db
     * 4) insert new data into local db
     * 5) begin observing local db again to see the refreshed data from network
     */
    private fun fetchFromNetwork(dbSource: LiveData<CacheObject>) {
        Log.d(TAG, "fetchFromNetwork: called")

        // update livedata for loading status
        results.addSource(dbSource) {
            setValue(Resource.Loading(it))
        }

        val apiResponse: LiveData<ApiResponse<RequestObject>> = createCall()
        results.addSource(apiResponse) {
            results.removeSource(dbSource)
            results.removeSource(apiResponse)

            /**
             * 3 cases:-
             *  1) ApiSuccessResponse
             *  2) ApiErrorResponse
             *  3) ApiEmptyResponse
             */
            when (it) {
                is ApiResponse.ApiSuccessResponse -> {
                    handleApiSuccessResponse(it, appExecutors)
                }
                is ApiResponse.ApiErrorResponse -> {
                    handleApiEmptyResponse()
                }
                is ApiResponse.ApiEmptyResponse -> {
                    handleApiErrorResponse(dbSource, it)
                }
            }
        }
    }

    private fun handleApiErrorResponse(
        dbSource: LiveData<CacheObject>,
        requestObjectApiResponse: ApiResponse.ApiEmptyResponse<RequestObject>
    ) {
        Log.d(TAG, "onChanged: ApiEmptyResponse")
        results.addSource(dbSource) {
            setValue(
                Resource.Error(
                    (requestObjectApiResponse as ApiResponse.ApiErrorResponse<RequestObject>).errorMessage,
                    it
                )
            )
        }
    }

    private fun handleApiEmptyResponse() {
        Log.d(TAG, "onChanged: ApiErrorResponse")
        appExecutors.mainThread.execute {
            results.addSource(loadFromDb()) {
                setValue(Resource.Success(it))
            }
        }
    }

    private fun handleApiSuccessResponse(
        it: ApiResponse.ApiSuccessResponse<RequestObject>,
        appExecutors: AppExecutors
    ) {
        Log.d(TAG, "onChanged: ApiSuccessResponse")
        appExecutors.diskIO.execute {
            // save the response to local db
            saveCallResult(processResponse(it) as RequestObject)
            appExecutors.mainThread.execute {
                results.addSource(loadFromDb()) {
                    setValue(Resource.Success(it))
                }
            }
        }
    }

    private fun processResponse(response: ApiResponse.ApiSuccessResponse<RequestObject>): CacheObject =
        response.body as CacheObject

    fun setValue(newValue: Resource<CacheObject>) {
        if (results.value != newValue) {
            results.value = newValue
        }
    }

    // Called to save the result of the API response into the database
    @WorkerThread
    protected abstract fun saveCallResult(item: RequestObject)

    // Called with the data in the database to decide whether to fetch
    // potentially updated data from the network.
    @MainThread
    protected abstract fun shouldFetch(data: CacheObject): Boolean

    // Called to get the cached data from the database.
    @MainThread
    protected abstract fun loadFromDb(): LiveData<CacheObject>

    // Called to create the API call.
    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestObject>>

    // Called when the fetch fails. The child class may want to reset components
    // like rate limiter.
    protected open fun onFetchFailed() {}

    // Returns a LiveData object that represents the resource that's implemented
    // in the base class.
    fun asLiveData(): LiveData<Resource<CacheObject>> = results
}