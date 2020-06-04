package com.sg.core.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MetaData(
    val page: Int? = null,
    @SerializedName("total_pages")
    @Expose
    val totalPages: Int? = null
)