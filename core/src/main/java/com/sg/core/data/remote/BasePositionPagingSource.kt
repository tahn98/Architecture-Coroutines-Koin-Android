package com.sg.core.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import java.io.IOException

abstract class BasePositionPagingSource<I : Any> : PagingSource<Int, I>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, I> {
        return try {
            val response = createCall()

            LoadResult.Page(
                data = response, prevKey = null,
                nextKey = params.loadSize
            )

        } catch (e: Exception) {
             LoadResult.Error(e)
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getRefreshKey(state: PagingState<Int, I>): Int? {
        return state.anchorPosition
    }

    protected abstract suspend fun createCall(): List<I>

}