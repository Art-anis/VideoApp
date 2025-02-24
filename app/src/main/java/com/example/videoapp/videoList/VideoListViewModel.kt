package com.example.videoapp.videoList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.VideosRepository
import com.example.domain.models.VideoListItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

//viewmodel экрана списка видео
class VideoListViewModel(private val repository: VideosRepository): ViewModel() {

    //сам список
    private var _videos: MutableLiveData<List<VideoListItem>?> = MutableLiveData(emptyList())
    val videos: LiveData<List<VideoListItem>?>
        get() = _videos

    //флаг загрузки
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    //при создании сразу идем за видео
    init {
        getVideos()
    }

    //получение видео
    fun getVideos() {
        viewModelScope.launch {
            //ставим флаг
            _isLoading.value = true
            //получаем видео и перемешиваем для обеспечения эффекта обновления
            _videos.value = repository.getVideos().shuffled()
            //ставим флаг
            _isLoading.value = false
        }
    }
}