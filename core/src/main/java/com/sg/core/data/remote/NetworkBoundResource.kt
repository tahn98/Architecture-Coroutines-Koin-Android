package com.sg.core.data.remote

import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.sg.core.model.ObjectResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import com.sg.core.model.Result

abstract class NetworkBoundResource<RequestType, ResultType>
constructor(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {

    private val TAG = NetworkBoundResource::class.java.name

    private val result = MutableLiveData<Result<ResultType>>()

    /**
     *  Init this abstract class [build].
     */
    fun build(): NetworkBoundResource<RequestType, ResultType> {

        result.value = Result.Loading

        CoroutineScope(dispatcher).launch {

            val dbResult = loadFromDb()

            if (shouldFetch(dbResult)) {
                try {
                    fetchFromNetwork()
                } catch (e: java.lang.Exception) {
                    Log.e(TAG, "An error happened: $e")
                    setValue(Result.Error(e.message ?: "", 404))
                }
            } else {
                Log.d(TAG, "Return data from local database")
                if (dbResult != null)
                    setValue(Result.Success(dbResult))
            }

        }
        return this
    }

    @MainThread
    private fun setValue(newValue: Result<ResultType>) {
        if (result.value != newValue) {
            result.postValue(newValue)
        }
    }

    /**
     * This Func handle response from Network [fetchFromNetwork].
     */
    private suspend fun fetchFromNetwork() {
        Log.i(TAG, "Fetch data from network")

        val apiResponse = createCall()

        Log.i(TAG, "Data fetched from network")

        if (apiResponse.isSuccessful) {
            val body = apiResponse.body()
            when (apiResponse.code()) {
                // 204 for delete brand story
                200, 201, 204 -> {
                    body?.let {
                        val message = ""
                        if (it == null) {
                            setValue(Result.Success(it, message))
                        } else {
                            saveCallResult(processResponse(it))
                            val result = loadFromDb() ?: processResponse(it)
                            setValue(Result.Success(result))
                        }

                    }
                    if (body == null) {
                        setValue(Result.Success(null, apiResponse.message()))
                    }
                }
                else -> {
                    setValue(Result.Error(apiResponse.message(), apiResponse.code()))
                }
            }
        } else {
            val response =
                Gson().fromJson(apiResponse.errorBody()?.string(), ObjectResponse::class.java)
            val errorMsg = response?.detail ?: ""
            setValue(Result.Error(errorMsg, apiResponse.code()))
        }
    }

    /**
     * Return a LiveData [asLiveData].
     */
    fun asLiveData(): LiveData<Result<ResultType>> = result

    /**
     * This Func handle data before return to View [processResponse].
     */
    @WorkerThread
    protected abstract fun processResponse(response: RequestType): ResultType?

    /**
     * Save data into database [saveCallResult] (Optional).
     */
    @WorkerThread
    protected open suspend fun saveCallResult(item: ResultType?) {
    }

    /**
     * Want to load data from api or database [shouldFetch].
     * If [loadFromDb] return a [result] -> [shouldFetch] is false
     */
    @MainThread
    protected open fun shouldFetch(data: ResultType?): Boolean = true

    /**
     * Load data from database [loadFromDb] (Optional).
     */
    @MainThread
    protected open suspend fun loadFromDb(): ResultType? = null

    /**
     * Call data from network [createCall] (Required).
     */
    @MainThread
    protected abstract suspend fun createCall(): Response<RequestType>
}
