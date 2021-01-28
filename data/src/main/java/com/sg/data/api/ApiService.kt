package com.sg.data.api

import com.sg.domain.common.ObjectResponse
import com.sg.domain.common.ListResponse
import com.sg.domain.entity.Message
import com.sg.domain.entity.User
import com.sg.domain.param.LoginParam
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("auth/sign_in")
    suspend fun login(@Body login: LoginParam): Response<ObjectResponse<User>>

    @GET("messages")
    suspend fun getMessage(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 15
    ): Response<ListResponse<Message>>
}