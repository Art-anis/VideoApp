package com.example.videoapp

import android.app.Application
import com.example.db.di.dbModule
import com.example.domain.di.domainModule
import com.example.network.di.networkModule
import com.example.videoapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        //запуск koin
        startKoin {
            //установка контекста
            androidContext(applicationContext)
            //установка логгера
            androidLogger(Level.DEBUG)
            //перечисленеи модулей
            modules(listOf(appModule, domainModule, dbModule, networkModule))
        }
    }
}