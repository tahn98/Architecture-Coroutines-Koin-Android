package com.sg.core.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sg.core.model.User
import com.sg.core.param.LoginParam

@Dao
interface UserDao : BaseDao<User> {

    @Query("SELECT * FROM User")
    suspend fun getUser(): List<User>

    @Query("SELECT * FROM User WHERE email LIKE :email")
    suspend fun findUser(email : String?) : User?
}