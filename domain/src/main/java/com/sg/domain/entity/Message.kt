package com.sg.domain.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Message(
    @PrimaryKey
    var id: String = "",
    var email: String? = ""
) : Parcelable