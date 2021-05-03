package com.sg.core.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sg.core.model.RemoteKeys

@Dao
interface RemoteKeysDao {

    @Query("SELECT * FROM RemoteKeys WHERE repoId = :id")
    suspend fun remoteKeysId(id: String): RemoteKeys?

    @Query("DELETE FROM RemoteKeys")
    suspend fun clearRemoteKeys()
}

