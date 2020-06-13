package com.sg.core.data.remote

import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.sg.core.model.ObjectResponse
import retrofit2.Response
import com.sg.core.model.Result
import com.sg.core.util.io
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
abstract class NetworkBoundResource<RequestType, ResultType>
constructor(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {

    private val TAG = NetworkBoundResource::class.java.name

    private val result = MutableLiveData<Result<ResultType>>()

//    fun build(): NetworkBoundResource<RequestType, ResultType> {
//
//        result.value = Result.Loading
//
//        CoroutineScope(dispatcher).launch {
//
//            val dbResult = loadFromDb()
//
//            if (shouldFetch(dbResult)) {
//                try {
//                    fetchFromNetwork()
//                } catch (e: java.lang.Exception) {
//                    Log.e(TAG, "An error happened: $e")
//                    setValue(Result.Error(e.message ?: "", 404))
//                }
//            } else {
//                Log.d(TAG, "Return data from local database")
//                if (dbResult != null)
//                    setValue(Result.Success(dbResult))
//            }
//
//        }
//        return this
//    }

    fun buildAsFlow() = flow<Result<ResultType>> {
        Log.d("THREAD_FLOW_HANDLE_API", Thread.currentThread().name)
        Log.i(TAG, "Fetch data from network")
        Log.i(TAG, "Data fetched from network")
        val dbResult = loadFromDb()
        emit(Result.Loading)
        fetchFromNetwork()
    }.flowOn(dispatcher)

    private suspend fun FlowCollector<Result<ResultType>>.fetchFromNetwork() {
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
//                                saveCallResult(processResponse(it))
                            val result = processResponse(it)
//                                setValue(Result.Success(result))
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

//    fun asFlowData(): Flow<Result<ResultType>> = result

    @WorkerThread
    protected abstract fun processResponse(response: RequestType): ResultType?

//    @WorkerThread
//    protected open suspend fun saveCallResult(item: ResultType?) {
//    }

//    @MainThread
//    protected open fun shouldFetch(data: ResultType?): Boolean = true

    @MainThread
    protected open suspend fun loadFromDb(): ResultType? = null

    @MainThread
    protected abstract suspend fun createCall(): Response<RequestType>
}
