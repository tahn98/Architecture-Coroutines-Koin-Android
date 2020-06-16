package com.sg.core.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.sg.core.model.*
import com.sg.core.param.LoginParam
import com.sg.core.vo.Listing
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getNowPlayingList(): Flow<PagingData<Movie>>
}