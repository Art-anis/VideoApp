package com.example.db.di

import android.content.Context
import androidx.room.Room
import com.example.db.AppDatabase
import com.example.db.dao.VideoDao
import org.koin.dsl.module

//provide для БД
fun provideDatabase(context: Context): AppDatabase {
    return Room.databaseBuilder(
        context = context,
        klass = AppDatabase::class.java,
        name = "videos_db"
    ).build()
}

//provide для dao
fun provideVideoDao(db: AppDatabase): VideoDao {
    return db.videoDao()
}

//модуль БД
val dbModule = module {
    //БД, создается сразу при запуске
    single(createdAtStart = true) { provideDatabase(context = get()) }

    //dao
    single { provideVideoDao(db = get()) }
}