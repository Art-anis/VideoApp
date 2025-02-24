package com.example.videoapp.videoView

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.VideosRepository
import com.example.domain.models.VideoListItem
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

//viewmodel просмотре видео
class VideoViewViewModel(
    val player: ExoPlayer, //инстанс плеера
    val repository: VideosRepository //репозиторий
): ViewModel() {

    //ссылка на видео
    private var _url: MutableStateFlow<String?> = MutableStateFlow(null)
    val url: StateFlow<String?>
        get() = _url

    //флаг ожидания возвращения на экран
    private var _expectingReturn: Boolean = false
    val expectingReturn: Boolean
        get() = _expectingReturn

    //список остальных видео
    private var _otherVideos: MutableStateFlow<List<VideoListItem>> = MutableStateFlow(emptyList())
    val otherVideos: StateFlow<List<VideoListItem>>
        get() = _otherVideos.asStateFlow()

    //при создании подготавливаем плеер
    init {
        player.prepare()
        player.playWhenReady = true
    }

    //подготовка видео при первичном запуске
    fun prepareVideo(id: String) {
        viewModelScope.launch {
            //получаем видео
            val video = repository.getVideoById(id = id)
            video?.let {
                //получаем mediaItem из url
                val mediaItem = MediaItem.fromUri(Uri.parse(it.url))
                //устанавливаем его в плеер
                player.setMediaItem(mediaItem)
                //обновляем состояние url
                _url.value = it.url
                //получаем остальные видео
                _otherVideos.value = repository.getOtherVideos(id = id)
            }
        }
    }

    //функция для восстановления плеера после возвращения на экран
    fun onReturn() {
        viewModelScope.launch {
            //восстанаваливаем mediaItem и устанавливаем его в плеер
            val mediaItem = MediaItem.fromUri(Uri.parse(url.value))
            player.setMediaItem(mediaItem)
            _expectingReturn = false
        }
    }

    //объявляем, что ждем возвращения на этот экран
    fun initiateLeave() {
        _expectingReturn = true
    }

    //остановка плеера
    fun endPlaying() {
        _url.value = null
        player.stop()
    }
}