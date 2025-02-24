package com.example.db.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

//сущность видео в БД
@Entity(tableName = "videos", indices = [Index(value = ["videoId", "url"], unique = true)])
data class VideoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, //автогенерируемый id для БД
    val videoId: String = "", //id video в api
    val url: String = "", //hlsUrl
    val name: String = "No title", //название
    val description: String = "No description", //описание
    val duration: String = "" //продолжительность
)
