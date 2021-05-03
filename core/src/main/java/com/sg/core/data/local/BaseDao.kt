package com.sg.core.data.local

import androidx.room.*

@Dao
interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg obj: T?)

//    @Query("DELETE FROM T")
//    suspend fun clearRemoteKeys()
    @Delete
    suspend fun clear(entity: T?)
}