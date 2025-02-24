package com.example.network.api

import com.example.network.responses.VideoResponse
import com.example.network.responses.VideoListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

//api
interface VideoApi {

    //получить все видео
    @GET("videos")
    suspend fun getVideos(): Response<VideoListResponse>

    //получить видео по id
    @GET("videos/{id}")
    suspend fun getVideoById(
        @Path(value = "id") id: String
    ): Response<VideoResponse>
}