package com.sg.base.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sg.core.model.ListResponse
import com.sg.core.model.Movie
import com.sg.core.model.Result
import com.sg.core.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {
    val liveData = MutableLiveData<ListResponse<Movie>>()
    val isLoading = MutableLiveData<Boolean>().apply {
        value = false
    }
    val errorNowPlayingMovieGet = MutableLiveData<Result.Error>()

//    fun getMovies(page: Int) = viewModelScope.launch {
//        repository.getNowPlayingList(page).collect {
//            when (it) {
//                is Result.Loading -> isLoading.value = true
//                is Result.Success -> {
//                    liveData.value = it.data
//                    isLoading.value = false
//                }
//                is Result.Error -> {
//                    errorNowPlayingMovieGet.value = it
//                    isLoading.value = false
//                }
//            }
//        }
//    }

    fun getMovies() : Flow<PagingData<Movie>> = repository.getNowPlayingList().cachedIn(viewModelScope)
}