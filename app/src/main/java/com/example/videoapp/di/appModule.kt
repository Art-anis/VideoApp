package com.example.videoapp.di

import com.example.videoapp.videoList.VideoListViewModel
import com.example.videoapp.videoView.VideoViewViewModel
import com.google.android.exoplayer2.ExoPlayer
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

//модуль приложения для koin
val appModule = module {
    //плеер, в единственном экземпляре
    single { ExoPlayer.Builder(get()).build() }

    //viewmodel экрана списка видео
    viewModel { VideoListViewModel(repository = get()) }

    //viewmodel экрана просмотра видео
    viewModel { VideoViewViewModel(
        player = get(),
        repository = get()
    ) }
}