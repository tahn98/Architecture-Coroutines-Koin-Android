package com.sg.base.viewmodel

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import androidx.paging.PagingData
import com.sg.core.model.Message
import com.sg.core.model.Result
import com.sg.core.model.User
import com.sg.core.param.LoginParam
import com.sg.core.repository.AuthRepository
import com.sg.core.util.collectValue
import com.sg.core.util.ui
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    val loginLiveData = MediatorLiveData<User>()
    val messagesLiveData = MediatorLiveData<PagingData<Message>>()
//    val loadStateLiveData = MediatorLiveData<Result<Message>>()

    fun login(param: LoginParam) {
        viewModelScope.launch {
            repository.login(param).collectValue {
                when (it) {
                    is Result.Success -> {
                        Log.d("THREAD_FLOW_UPDATE_UI", Thread.currentThread().name)
                        loginLiveData.value = it.data
                    }
                }
            }
//            loginLiveData.addSource(repository.login(param)) {
//                when (it) {
//                    is Result.Success -> {
//                        loginLiveData.value = it.data
//                    }
//                }
//            }
        }
    }

    //    fun loginDB(param: LoginParam){
//        viewModelScope.launch {
//            loginLiveData.addSource(repository.loginDB(param)){
//                when(it){
//                    is Result.Success -> {
//                        loginLiveData.value = it.data
//                    }
//                }
//            }
//        }
//    }
//
    fun messagePaging() {
        viewModelScope.launch {
            repository.message().collectLatest {
                messagesLiveData.value = it
            }

        }
    }
//
//    fun messagePagingDB() {
//        viewModelScope.launch {
//            val request = repository.messageDB()
//
//            messagesLiveData.addSource(request.result) {
//                messagesLiveData.value = it
//            }
//        }
//    }
}