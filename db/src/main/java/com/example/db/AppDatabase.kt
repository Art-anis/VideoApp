package com.example.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.db.dao.VideoDao
import com.example.db.entities.VideoEntity

//база данных
@Database(
    entities = [VideoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    //единственное dao
    abstract fun videoDao(): VideoDao
}