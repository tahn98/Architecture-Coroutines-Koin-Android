package com.sg.presentation.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.sg.domain.User
import com.sg.domain.entity.Message
import com.sg.domain.param.LoginParam
import kotlinx.coroutines.launch
import com.sg.domain.common.Result
import com.sg.domain.repository.AuthRepository

class AuthViewModel(private val repo: AuthRepository) : ViewModel() {

    val loginLiveData = MediatorLiveData<User>()
    val messagesLiveData = MediatorLiveData<PagedList<Message>>()
    val loadStateLiveData = MediatorLiveData<Result<Message>>()

    fun login(param: LoginParam) {
        viewModelScope.launch {
            loginLiveData.addSource(repo.login(param)) {
                when (it) {
                    is Result.Success -> {
                        loginLiveData.value = it.data
                    }
                }
            }
        }
    }

    fun loginDB(param: LoginParam) {
        viewModelScope.launch {
            loginLiveData.addSource(repo.loginDB(param)) {
                when (it) {
                    is Result.Success -> {
                        loginLiveData.value = it.data
                    }
                }
            }
        }
    }

    fun messagePaging() {
        viewModelScope.launch {
            val request = repo.message()

            messagesLiveData.addSource(request.result) {
                messagesLiveData.value = it
            }

            messagesLiveData.addSource(request.status) {
                loadStateLiveData.value = it
            }
        }
    }

    fun messagePagingDB() {
        viewModelScope.launch {
            val request = repo.messageDB()

            messagesLiveData.addSource(request.result) {
                messagesLiveData.value = it
            }
        }
    }
}