package com.sg.core.repository.impl

import com.sg.core.api.ApiService
import com.sg.core.data.remote.NetworkBoundResource
import com.sg.core.model.ListResponse
import com.sg.core.model.Movie
import com.sg.core.model.Result
import com.sg.core.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import retrofit2.Response

class MovieRepositoryImpl(private val api: ApiService) : MovieRepository {
    override suspend fun getNowPlayingList(page: Int): Flow<Result<ListResponse<Movie>>> =
        object : NetworkBoundResource<ListResponse<Movie>, ListResponse<Movie>>() {
            override fun processResponse(response: ListResponse<Movie>): ListResponse<Movie>? = response
            override suspend fun fetchFromNetwork(): Response<ListResponse<Movie>> = api.getListNowPlayingMovies(page)
        }.asFlow()

}