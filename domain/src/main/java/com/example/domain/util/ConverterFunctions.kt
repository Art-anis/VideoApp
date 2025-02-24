package com.example.domain.util

import com.example.db.entities.VideoEntity
import com.example.domain.models.Video
import com.example.domain.models.VideoListItem
import com.example.network.responses.VideoResponse

/*функции конвертеры*/

//из response в элемент списка
fun VideoResponse.toVideoListItem(): VideoListItem {
    return VideoListItem(
        id = this.id ?: "",
        name = this.title ?: "No title",
        duration = this.duration ?: "",
        description = this.description ?: "No description"
    )
}

//из response в отдельное видео
fun VideoResponse.toVideo(): Video {
    return Video(
        url = this.hlsUrl ?: "",
        name = this.title ?: "No title",
        description = this.description ?: "No description",
        duration = this.duration ?: ""
    )
}

//из сущности БД в отдельное видео
fun VideoEntity.toVideo(): Video {
    return Video(
        url = this.url,
        name = this.name,
        description = this.description,
        duration = this.duration
    )
}

//из сущности БД в элемент списка
fun VideoEntity.toVideoListItem(): VideoListItem {
    return VideoListItem(
        id = this.videoId,
        name = this.name,
        duration = this.duration,
        description = this.description
    )
}

//из response в сущность БД
fun VideoResponse.toVideoEntity(): VideoEntity {
    return VideoEntity(
        videoId = this.id ?: "",
        name = this.title ?: "No title",
        description = this.description ?: "No description",
        duration = this.duration ?: "",
        url = this.hlsUrl ?: ""
    )
}