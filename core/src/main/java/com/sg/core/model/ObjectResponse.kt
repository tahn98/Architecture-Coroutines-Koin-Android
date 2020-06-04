package com.sg.core.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class ObjectResponse<T>(
    val metadata: MetaData? = null,
    val code: String? = null,
    @SerializedName(value = "detail", alternate = ["message"])
    val detail: String? = null,
    @SerializedName("total_results")
    val totalResults: Int? = null,
    val status: Boolean? = false,
    @SerializedName(value = "data", alternate = ["results"])
    val data: T? = null
)

