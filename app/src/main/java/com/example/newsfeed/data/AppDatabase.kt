package com.example.newsfeed.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newsfeed.data.models.NewsResult
import com.example.newsfeed.data.repositories.source.NewsLocalDataSource

@Database(entities = [NewsResult::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsLocalDataSource
}