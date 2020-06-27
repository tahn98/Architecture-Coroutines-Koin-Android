package com.sg.base.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.sg.core.model.Message
import com.sg.core.model.Result
import com.sg.core.model.User
import com.sg.core.param.LoginParam
import com.sg.core.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel @ViewModelInject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    val loginLiveData = MediatorLiveData<User>()
    val messagesLiveData = MediatorLiveData<PagedList<Message>>()
    val loadStateLiveData = MediatorLiveData<Result<Message>>()

    fun login(param: LoginParam) {
        viewModelScope.launch {
            loginLiveData.addSource(repository.login(param)) {
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
            loginLiveData.addSource(repository.loginDB(param)) {
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
            val request = repository.message()

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
            val request = repository.messageDB()

            messagesLiveData.addSource(request.result) {
                messagesLiveData.value = it
            }
        }
    }
}