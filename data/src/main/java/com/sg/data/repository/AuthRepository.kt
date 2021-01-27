package com.sg.data.repository

import androidx.lifecycle.LiveData
import com.sg.domain.entity.Message
import com.sg.domain.common.Result
import com.sg.domain.User
import com.sg.domain.param.LoginParam
import com.sg.domain.common.Listing

interface AuthRepository {

    suspend fun login(param: LoginParam): LiveData<Result<User>>

    suspend fun loginDB(param: LoginParam) : LiveData<Result<User>>

    suspend fun message(page: Int = 1): Listing<Message>

    suspend fun messageDB() : Listing<Message>
}