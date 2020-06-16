package com.sg.base.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.sg.core.model.*
import com.sg.core.param.LoginParam
import com.sg.core.repository.AuthRepository
import com.sg.core.repository.MovieRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: MovieRepository) : ViewModel() {

    val liveData = MediatorLiveData<ListResponse<Movie>>()

    fun getMovies(page: Int) = viewModelScope.launch {
//        repository.getNowPlayingList(page).collect {
//            when (it) {
//                is Result.Success -> {
//                    liveData.value = it.data
//                }
//            }
//        }
    }

}