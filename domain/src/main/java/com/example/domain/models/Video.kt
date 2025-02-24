package com.example.domain.models

//модель для отдельного видео
data class Video(
    val url: String, //hlsUrl
    val name: String, //название
    val description: String, //описание
    val duration: String //продолжительность
)