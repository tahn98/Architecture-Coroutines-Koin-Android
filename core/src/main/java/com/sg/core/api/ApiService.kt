package com.sg.core.api

import com.sg.core.model.*
import com.sg.core.param.LoginParam
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("movie/now_playing")
    suspend fun getListNowPlayingMovies(
        @Query("page") page: Int = 1,
        @Query("api_key") key: String = "a07e22bc18f5cb106bfe4cc1f83ad8ed"
    ): ListResponse<Movie>

    @GET("movie/{movie_id}/videos")
    suspend fun getTrailer(@Path("movie_id") movieId: Int?)

    @GET("movie/{movie_id}/recommendations")
    suspend fun getRecommendations(@Path("movie_id") movieId: Int?)
}