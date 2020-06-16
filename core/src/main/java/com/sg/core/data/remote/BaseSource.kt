package com.sg.core.data.remote

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource


abstract class BaseSource<T : Any>(val data: List<T>) : DataSource.Factory<Int, T>() {

    val sourceLiveData = MutableLiveData<BasePositionalDataSource<T>>()

    override fun create(): DataSource<Int, T> {
        val source = object : BasePositionalDataSource<T>(data) {}

        sourceLiveData.postValue(source)

        return source
    }
}