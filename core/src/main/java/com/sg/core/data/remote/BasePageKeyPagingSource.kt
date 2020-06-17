package com.sg.core.data.remote

import android.util.Log
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingSource
import com.google.gson.Gson
import com.sg.core.model.ListResponse
import retrofit2.Response

abstract class BasePageKeyPagingSource<I, O : Any>() : PagingSource<Int, O>() {

    val TAG = BasePageKeyPagingSource::class.java.name


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, O> {
        try {
            Log.i(TAG, "Data fetched from network")
            Log.i(TAG, Thread.currentThread().name)
            val apiResponse = createCall(params.key ?: 1)
            if (apiResponse.isSuccessful) {
                val body = apiResponse.body()
                when (apiResponse.code()) {
                    200, 201 -> {
                        body?.let {
                            val result = handleResponse(it)
                            return if (result.isNotEmpty()) {
                                LoadResult.Page(
                                    data = result, prevKey = null,
                                    nextKey = body.metadata?.next_page
                                )
                            } else {
                                LoadResult.Error(Exception("End Data"))
                            }
                        }
                    }
                    else -> {
                        return LoadResult.Error(
                            Exception(Gson().toJson(apiResponse.errorBody()))
                        )
                    }
                }
            } else {
                return LoadResult.Error(
                    Exception(Gson().toJson(apiResponse.errorBody()))
                )
            }
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
        return LoadResult.Error(Exception("Have a problem"))
    }

    protected abstract suspend fun createCall(page: Int): Response<ListResponse<I>>

    protected abstract suspend fun handleResponse(items: ListResponse<I>): List<O>
}