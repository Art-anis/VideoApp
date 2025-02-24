package com.example.domain.models

//модель для элемента списка
data class VideoListItem(
    val id: String, //id в api
    val name: String, //название
    val duration: String, //продолжительность
    val description: String //описание
)
