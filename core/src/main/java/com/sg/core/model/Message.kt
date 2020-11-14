package com.sg.core.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sg.core.vo.ItemViewModel
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Message(
    @PrimaryKey
    var id: String = "",
    var email: String? = "",
    val badgeID: String?,
    val created_at: String?,
    val isLoginFirst: Boolean?,
    val name: String?,
    val numberOfLogin: Int?,
    val profilePhoto: String? = null,
    val type: String?,
    val updated_at: String?,
    val username: String?
) : Parcelable