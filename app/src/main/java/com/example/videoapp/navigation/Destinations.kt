package com.example.videoapp.navigation

import kotlinx.serialization.Serializable

/*назначения для навигации*/

//экран списка видео
@Serializable
object VideoList

//экран просмотра видео, модель передается как аргумент
@Serializable
data class VideoItem(
    val title: String,
    val id: String,
    val description: String
)