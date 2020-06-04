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
import kotlinx.coroutines.flow.*

abstract class NetworkBoundResource<RequestType, ResultType>
constructor(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {

    private val TAG = NetworkBoundResource::class.java.name


    fun asFlow() = flow {
        emit(Result.Loading)
        val dbResult = loadFromDb()?.first()
        if (shouldFetch(dbResult)) {
            val apiResponse = fetchFromNetwork()
            if (apiResponse.isSuccessful) {
                val body = apiResponse.body()
                when (apiResponse.code()) {
                    //status code for success
                    200 -> {
                        body?.let { it ->
                            val message = ""
                            if (it == null) {
                                emit(Result.Success(data = null, message = message))
                            } else {
                                saveCallResult(processResponse(it))
                                emitAll(loadFromDb()?.map {Result.Success(it)} ?: flow{
                                    emit(Result.Success(processResponse(it)))
                                })
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
    }

    @WorkerThread
    protected abstract fun processResponse(response: RequestType): ResultType?

    @WorkerThread
    protected open suspend fun saveCallResult(item: ResultType?) {
    }

    @MainThread
    protected open fun shouldFetch(data: ResultType?): Boolean = true

    @MainThread
    protected open suspend fun loadFromDb(): Flow<ResultType>? = null

    @MainThread
    protected abstract suspend fun fetchFromNetwork(): Response<RequestType>
}
