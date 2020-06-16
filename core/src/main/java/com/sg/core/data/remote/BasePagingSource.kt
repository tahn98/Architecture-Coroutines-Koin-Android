package com.sg.core.data.remote

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.paging.PagingSource
import com.sg.core.model.ListResponse
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

private const val DEFAULT_INDEX = 1

abstract class BasePagingSource<I : Any>() : PagingSource<Int, I>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, I> {
        val page = params.key ?: DEFAULT_INDEX
        return try {
            val response = fetchFromNetwork(page)
            val repos = response.results
            LoadResult.Page(
                data = repos?: arrayListOf(),
                prevKey = if (page == DEFAULT_INDEX) null else page - 1,
                nextKey = if (repos?.isEmpty()==true) null else page + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    @MainThread
    protected abstract suspend fun fetchFromNetwork(page: Int): ListResponse<I>


}