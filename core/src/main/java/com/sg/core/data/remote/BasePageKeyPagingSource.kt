package com.sg.core.data.remote

import android.util.Log
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.gson.Gson
import com.sg.core.model.ListResponse
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

abstract class BasePageKeyPagingSource<I, O : Any> : PagingSource<Int, O>() {

    val TAG = BasePageKeyPagingSource::class.java.name

    companion object {
        const val DEFAULT_PAGE_INDEX = 1
    }

    /**
     * calls api if there is any error getting results then return the [LoadResult.Error]
     * for successful response return the results using [LoadResult.Page] for some reason if the results
     * are empty from service like in case of no more data from api then we can pass [null] to
     * send signal that source has reached the end of list
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, O> {
        try {
            Log.i(TAG, "Data fetched from network")
            Log.i(TAG, Thread.currentThread().name)
            val page = params.key ?: DEFAULT_PAGE_INDEX
            val apiResponse = createCall(page)
            if (apiResponse.isSuccessful) {
                val body = apiResponse.body()
                when (apiResponse.code()) {
                    200, 201 -> {
                        body?.let {
                            val result = handleResponse(it)
                            return LoadResult.Page(
                                data = result, prevKey = if (page == 1) null else page.minus(1),
                                nextKey = if (result.isEmpty()) null else page.plus(1)
                            )
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
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
        return LoadResult.Error(Exception("Have a problem"))
    }

    override fun getRefreshKey(state: PagingState<Int, O>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            // This loads starting from previous page, but since PagingConfig.initialLoadSize spans
            // multiple pages, the initial load will still load items centered around
            // anchorPosition. This also prevents needing to immediately launch prepend due to
            // prefetchDistance.
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    protected abstract suspend fun createCall(page: Int): Response<ListResponse<I>>

    protected abstract suspend fun handleResponse(items: ListResponse<I>): List<O>
}