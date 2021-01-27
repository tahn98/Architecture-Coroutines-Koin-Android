package com.sg.core.data.local

import androidx.room.Dao
import androidx.room.Query
import com.sg.core.domain.Message

@Dao
interface MessageDao : BaseDao<Message>{

    @Query("SELECT * FROM Message")
    suspend fun getMessages(): List<Message>
}