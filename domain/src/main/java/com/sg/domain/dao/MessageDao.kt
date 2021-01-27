package com.sg.domain.dao

import androidx.room.Dao
import androidx.room.Query
import com.sg.domain.entity.Message

@Dao
interface MessageDao : BaseDao<Message>{

    @Query("SELECT * FROM Message")
    suspend fun getMessages(): List<Message>
}