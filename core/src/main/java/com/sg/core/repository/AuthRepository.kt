package com.sg.core.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingData
import com.sg.core.model.Message
import com.sg.core.model.Movie
import com.sg.core.model.Result
import com.sg.core.model.User
import com.sg.core.param.LoginParam
//import com.sg.core.vo.Listing
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun login(param: LoginParam): Flow<Result<User>>

//    suspend fun loginDB(param: LoginParam) : LiveData<Result<User>>
//
    suspend fun message(keyword : String = "", page: Int = 1):  Flow<PagingData<Message>>

    suspend fun movies(page: Int = 1):  Flow<PagingData<Movie>>

//
//    suspend fun messageDB() : Listing<Message>
}