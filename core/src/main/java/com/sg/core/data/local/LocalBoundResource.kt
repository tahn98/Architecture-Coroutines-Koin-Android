package com.sg.core.data.local

import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.sg.core.model.Result

abstract class LocalBoundResource<RequestType, ResultType>
constructor(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {

    private val TAG = LocalBoundResource::class.java.name

    private val result = MutableLiveData<Result<ResultType>>()

    fun build(): LocalBoundResource<RequestType, ResultType> {

        result.value = Result.Loading

        CoroutineScope(dispatcher).launch {
            try {
                fetchFromDatabase()
            } catch (e: java.lang.Exception) {
                Log.e(TAG, "An error happened: $e")
                setValue(Result.Error(e.message ?: "", 404))
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

    private suspend fun fetchFromDatabase() {
        val errorMsg = "Not match in Database"

        val response = loadFromDb()

        Log.i(TAG, "Data fetched from Database")

        if (response != null) {
            saveCallResult(processResponse(response))
            val result = processResponse(response) ?: response
            setValue(Result.Success(result))
        } else {
            val result = processResponse(response) ?: response
            setValue(Result.Success(result))
        }
    }

    fun asLiveData(): LiveData<Result<ResultType>> = result

    @WorkerThread
    protected abstract suspend fun processResponse(response: ResultType?): ResultType?

    @WorkerThread
    protected open suspend fun saveCallResult(item: ResultType?) {
    }

    @MainThread
    protected abstract suspend fun loadFromDb(): ResultType?
}
