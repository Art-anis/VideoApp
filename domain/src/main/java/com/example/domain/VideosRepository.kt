package com.example.domain

import android.content.Context
import android.widget.Toast
import com.example.db.dao.VideoDao
import com.example.domain.models.Video
import com.example.domain.models.VideoListItem
import com.example.domain.util.toVideo
import com.example.domain.util.toVideoEntity
import com.example.domain.util.toVideoListItem
import com.example.network.api.VideoApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

//репозиторий
class VideosRepository(
    private val api: VideoApi, //удаленный источник
    private val dao: VideoDao, //локальный источник
    private val context: Context //контекст для тостов
) {

    //получение видео по id
    suspend fun getVideoById(id: String): Video? {
        //в IO-потоках
        return withContext(Dispatchers.IO) {
            try {
                //делаем запрос
                val response = api.getVideoById(id)
                //что дальше делаем, зависит от кода
                when (response.code()) {
                    //все ок
                    200 -> {
                        //извлекаем видео, или если null, идем в БД
                        val video = response.body()
                        video?.toVideo() ?: getVideoFromDb(id)
                    }
                    //ошибка - выводим пользователю
                    else -> {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                context,
                                "Sorry, couldn't load the video",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        null
                    }
                }
            //ошибка - выводим пользователю
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "Sorry, couldn't load the video",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                null
            }
        }
    }

    //получение видео из БД
    private suspend fun getVideoFromDb(id: String): Video? {
        //в IO-потоках
        return withContext(Dispatchers.IO) {
            //пытаемся получить видео
            try {
                dao.getVideoById(id).toVideo()
            }
            //если ошибка - возвращаем null
            catch (e: Exception) {
                null
            }
        }
    }

    //загрузка всех видео
    suspend fun getVideos(): List<VideoListItem> {
        //в IO-потоках
        return withContext(Dispatchers.IO) {
            try {
                //делаем запрос
                val response = api.getVideos()
                //что дальше делаем, зависит от кода
                when (response.code()) {
                    //все ок
                    200 -> {
                        //извлекаем видео, или если null, идем в БД
                        val videos = response.body()
                        videos?.videos?.map {
                            dao.addVideo(it.toVideoEntity())
                            it.toVideoListItem()
                        } ?: getVideosFromDb()
                    }
                    //ошибка - выводим пользователю
                    else -> {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                context,
                                "Something went wrong while loading, now loading from DB",
                                Toast.LENGTH_SHORT).show()
                        }
                        getVideosFromDb()
                    }
                }
            //ошибка - выводим пользователю
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "Something went wrong while loading, now loading from DB",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                getVideosFromDb()
            }
        }
    }

    //получение всех видео из БД
    private suspend fun getVideosFromDb(): List<VideoListItem> {
        //в IO-потоках
        return withContext(Dispatchers.IO) {
            try {
                //получаем все видео и преобразуем их
                dao.getAllVideos()
                    .map { it.toVideoListItem() }
            }
            //иначе возвращаем пустой список
            catch (e: Exception) {
                emptyList()
            }
        }
    }

    //получение всех видео, кроме заданного
    suspend fun getOtherVideos(id: String): List<VideoListItem> {
        //в IO-потоках
        return withContext(Dispatchers.IO) {
            try {
                //делаем запрос
                val response = api.getVideos()
                //что дальше, зависит от кода
                when (response.code()) {
                    //все ок
                    200 -> {
                        //извлекаем видео и убираем данное, если null, идем в БД
                        val videos = response.body()
                        videos?.videos?.let { video ->
                            video
                                .map {
                                    dao.addVideo(it.toVideoEntity())
                                    it.toVideoListItem()
                                }
                                .filterNot { it.id == id }
                        } ?: getOtherVideoFromDb(id)
                    }
                    //ошибка - идем в БД
                    else -> {
                        getOtherVideoFromDb(id)
                    }
                }
            //ошибка - идем в БД
            } catch (e: Exception) {
                getOtherVideoFromDb(id)
            }
        }
    }

    //получение всех видео, кроме заданного, из БД
    private suspend fun getOtherVideoFromDb(id: String): List<VideoListItem> {
        return withContext(Dispatchers.IO) {
            try {
                //запрос в dao
                dao.getOtherVideos(id)
                    .map { it.toVideoListItem() }
            }
            //если ошибка, то возвращаем пустой список
            catch (e: Exception) {
                emptyList()
            }
        }
    }
}