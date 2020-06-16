package com.sg.core.repository.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sg.core.api.ApiService
import com.sg.core.data.remote.BasePagingSource
import com.sg.core.data.remote.NetworkBoundResource
import com.sg.core.model.ListResponse
import com.sg.core.model.Movie
import com.sg.core.model.Result
import com.sg.core.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import retrofit2.Response

class MovieRepositoryImpl(private val api: ApiService) : MovieRepository {
    override fun getNowPlayingList(): Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
        pagingSourceFactory = {
            object : BasePagingSource<Movie>() {
                override suspend fun fetchFromNetwork(page: Int): ListResponse<Movie> = api.getListNowPlayingMovies(page = page)
            }
        }
    ).flow

    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }

}