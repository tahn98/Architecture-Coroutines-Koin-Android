package com.sg.domain.common

import com.google.gson.annotations.SerializedName

open class ObjectResponse<T>(
    val metadata: MetaData? = null,
    val code: String? = null,
    @SerializedName(value = "detail", alternate = ["message"])
    val detail: String? = null,
    val status: Boolean? = false,
    @SerializedName(value = "data", alternate = ["results"])
    val data: T? = null
)

