package com.sg.core.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.sg.core.model.Message

@Dao
interface MessageDao : BaseDao<Message> {

    @Query("SELECT * FROM Message")
    fun getMessagesPaging(): PagingSource<Int, Message>

    @Query("SELECT * FROM Message")
    suspend fun getMessages(): List<Message>
}