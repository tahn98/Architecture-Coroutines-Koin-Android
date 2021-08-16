package com.sg.base.viewmodel

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagedList
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sg.core.model.Message
import com.sg.core.model.Movie
import com.sg.core.model.Result
import com.sg.core.model.User
import com.sg.core.param.LoginParam
import com.sg.core.repository.AuthRepository
import com.sg.core.util.collectValue
import com.sg.core.util.ui
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository, private val savedStateHandle : SavedStateHandle) : ViewModel() {

    val loginLiveData = MutableLiveData<User?>()
    val messagesLiveData = MediatorLiveData<PagingData<Message>>()
//    val movieLiveData = MediatorLiveData<PagingData<Movie>>()

//    val loadStateLiveData = MediatorLiveData<Result<Message>>()


    fun login(param: LoginParam) {
        viewModelScope.launch {
            repository.login(param).collect {
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
//    fun messagePaging() {
//        viewModelScope.launch {
//            repository.message().collectLatest {
//                messagesLiveData.value = it
//            }
//
//        }

    companion object {
        const val KEY_SUBREDDIT = "subreddit"
        const val DEFAULT_SUBREDDIT = ""
    }

    init {
        if (!savedStateHandle.contains(KEY_SUBREDDIT)) {
            savedStateHandle.set(KEY_SUBREDDIT, DEFAULT_SUBREDDIT)
        }
    }

    private val clearListCh = Channel<Unit>(Channel.CONFLATED)


//    var messageFlow = flow {
//        emitAll(repository.message())
//    }.cachedIn(viewModelScope) // Save PagingData in ViewModel when Rotation Screen -> No need call again

    var messageFlowSearch = flowOf(
        clearListCh.consumeAsFlow().map { PagingData.empty<Message>() },
        savedStateHandle.getLiveData<String>(KEY_SUBREDDIT)
            .asFlow()
            .flatMapLatest { repository.messageDB() }).flattenMerge(2).cachedIn(viewModelScope)

    private fun shouldShowSubreddit(
        keyword: String
    ) = savedStateHandle.get<String>(KEY_SUBREDDIT) != keyword

    fun searchUser(keyword: String = "") {
        if (!shouldShowSubreddit(keyword)) return

        clearListCh.offer(Unit)
        savedStateHandle.set(KEY_SUBREDDIT, keyword)
    }

//    fun moviePaging() {
//        viewModelScope.launch {
//            repository.movies().collectLatest {
//                movieLiveData.value = it
//            }
//
//        }
//    }
////
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