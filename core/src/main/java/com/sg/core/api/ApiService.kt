package com.sg.core.api

import com.sg.core.domain.response.ObjectResponse
import com.sg.core.domain.response.ListResponse
import com.sg.core.domain.Message
import com.sg.core.domain.User
import com.sg.core.domain.param.LoginParam
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("users-login")
    suspend fun login(@Body login: LoginParam): Response<ObjectResponse<User>>

    @GET("search-user")
    suspend fun getMessage(
        @Query("page") page: Int = 1,
        @Query("current_per_page") perPage: Int = 15
    ): Response<ListResponse<Message>>
}