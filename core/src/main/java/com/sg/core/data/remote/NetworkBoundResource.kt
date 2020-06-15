package com.sg.core.data.remote

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.sg.core.model.ObjectResponse
import retrofit2.Response
import com.sg.core.model.Result
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

abstract class NetworkBoundResource<RequestType, ResultType>

/**
 * Want to load data from api or database [shouldFetch].
 */
constructor(private val shouldFetch: Boolean = true) {

    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    private val TAG = NetworkBoundResource::class.java.name

    fun buildAsFlow() = flow<Result<ResultType>> {
        emit(Result.Loading)
        if (shouldFetch) {
            Log.i(TAG, "Fetch data from network")
            try {
                fetchFromNetwork()
            } catch (e: java.lang.Exception) {
                Log.e(TAG, "An error happened: $e")
                emit(Result.Error(e.message ?: "", 404))
            }
        } else {
            Log.d(TAG, "Return data from local database")
            loadFromDb()?.collect {
                emit(Result.Success(it))
            }
        }
    }.flowOn(dispatcher)

    private suspend fun FlowCollector<Result<ResultType>>.fetchFromNetwork() {
        Log.i(TAG, "Data fetched from network")
        Log.d(TAG, Thread.currentThread().name)
        val apiResponse = createCall()
        if (apiResponse.isSuccessful) {
            val body = apiResponse.body()
            when (apiResponse.code()) {
                // 204 for delete brand story
                200, 201, 204 -> {
                    body?.let {
                        val message = ""
                        if (it == null) {
                            emit(Result.Success(it, message))
                        } else {
                            val result = processResponse(it)
                            saveCallResult(result)
                            emit(Result.Success(result))
                        }
                    }
                    if (body == null) {
                        emit(Result.Success(null, apiResponse.message()))
                    }
                }
                else -> {
                    emit(Result.Error(apiResponse.message(), apiResponse.code()))
                }
            }
        } else {
            val response =
                Gson().fromJson(apiResponse.errorBody()?.string(), ObjectResponse::class.java)
            val errorMsg = response?.detail ?: ""
            emit(Result.Error(errorMsg, apiResponse.code()))
        }
    }

    /**
     * This Func handle data before return to View [processResponse].
     */
    protected abstract fun processResponse(response: RequestType): ResultType?

    /**
     * Save data into database [saveCallResult].
     */
    protected open suspend fun saveCallResult(item: ResultType?) {}

    /**
     * Load data from database [loadFromDb].
     */
    protected open suspend fun loadFromDb(): Flow<ResultType>? = null

    /**
     * Call data from network [createCall].
     */
    protected abstract suspend fun createCall(): Response<RequestType>
}
