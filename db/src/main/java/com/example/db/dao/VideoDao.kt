package com.example.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.db.entities.VideoEntity

//dao для БД
@Dao
interface VideoDao {

    //вставка видео
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addVideo(video: VideoEntity)

    //выбор всех видео
    @Query("select * from videos")
    suspend fun getAllVideos(): List<VideoEntity>

    //получение видео по id
    @Query("select * from videos where videoId = :id")
    suspend fun getVideoById(id: String): VideoEntity

    //получение всех видео, кроме заданного
    @Query("select * from videos where videoId != :id")
    suspend fun getOtherVideos(id: String): List<VideoEntity>
}