package com.sg.core.repository

import androidx.lifecycle.LiveData
import com.sg.core.model.Message
import com.sg.core.model.Result
import com.sg.core.model.User
import com.sg.core.param.LoginParam
import com.sg.core.vo.Listing

interface AuthRepository {

    suspend fun login(param: LoginParam): LiveData<Result<User>>

    suspend fun loginDB(param: LoginParam) : LiveData<Result<User>>

    suspend fun message(page: Int = 1): Listing<Message>

    suspend fun messageDB() : Listing<Message>
}