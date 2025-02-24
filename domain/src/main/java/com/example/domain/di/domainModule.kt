package com.example.domain.di

import com.example.domain.VideosRepository
import org.koin.dsl.module

//модуль domain
val domainModule = module {
    //репозиторий
    single { VideosRepository(
        api = get(),
        dao = get(),
        context = get()
    ) }
}