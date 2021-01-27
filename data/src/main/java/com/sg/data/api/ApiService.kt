package com.sg.data.api

import com.sg.domain.common.ObjectResponse
import com.sg.domain.common.ListResponse
import com.sg.domain.entity.Message
import com.sg.domain.User
import com.sg.domain.param.LoginParam
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