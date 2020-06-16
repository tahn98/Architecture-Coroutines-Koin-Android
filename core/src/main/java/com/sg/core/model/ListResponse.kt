package com.sg.core.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

//class ListResponse<T>(
//    val metadata: MetaData? = null,
//    val code: String? = null,
//    @SerializedName(value = "detail", alternate = ["message"])
//    val detail: String? = null,
//    val status: Boolean? = false,
//    @SerializedName(value = "data", alternate = ["results"])
//    val data: ArrayList<T>? = null
//)

class ListResponse<T>(
    val results: List<T>? = null,
    val page: Int? = null,
    @SerializedName("total_results")
    @Expose
    val totalResults: Int? = null,
    @SerializedName("total_pages")
    @Expose
    val totalPages: Int? = null
)