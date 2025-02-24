package com.example.network.responses

import com.google.gson.annotations.SerializedName

//response
data class VideoListResponse(
    @SerializedName("pager") val pager: Pager,
    @SerializedName("videos") val videos: List<VideoResponse>
)

//не используется
data class Pager(
    @SerializedName("page") var page: Int? = null,
    @SerializedName("totalPages") var totalPages: Int? = null,
    @SerializedName("totalResults") var totalResults: Int? = null,
    @SerializedName("sort") var sort: String? = null
)
