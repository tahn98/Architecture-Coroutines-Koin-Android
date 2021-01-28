package com.sg.domain.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class User(
    @PrimaryKey
    var id : Int = 0,
    var email: String? = "",
    var avatar: String? = "",
    @TypeConverters(TokenConverter::class)
    var token: Token? = Token()
) : Parcelable

@Parcelize
data class Token(var token: String? = "") : Parcelable

class TokenConverter {
    @TypeConverter
    fun objectToJson(value: Token?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToObject(value: String?): Token? = Gson().fromJson(value, Token::class.java)

}

