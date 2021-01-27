package com.sg.core.repository

import androidx.lifecycle.LiveData
import com.sg.core.domain.Message
import com.sg.core.domain.response.Result
import com.sg.core.domain.User
import com.sg.core.domain.param.LoginParam
import com.sg.core.domain.vo.Listing

interface AuthRepository {

    suspend fun login(param: LoginParam): LiveData<Result<User>>

    suspend fun loginDB(param: LoginParam) : LiveData<Result<User>>

    suspend fun message(page: Int = 1): Listing<Message>

    suspend fun messageDB() : Listing<Message>
}