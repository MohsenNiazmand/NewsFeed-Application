package com.example.newsfeed.di

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.example.newsfeed.data.AppDatabase
import com.example.newsfeed.data.repositories.NewsRepository
import com.example.newsfeed.data.repositories.NewsRepositoryImpl
import com.example.newsfeed.data.repositories.source.NewsLocalDataSource
import com.example.newsfeed.data.repositories.source.NewsRemoteDataSource
import com.example.newsfeed.services.createApiServiceInstance
import com.example.newsfeed.ui.favorites.FavoritesViewModel
import com.example.newsfeed.ui.news.NewsViewModel
import com.facebook.drawee.backends.pipeline.Fresco
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module
import timber.log.Timber



class MyApp : Application() {


    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Fresco.initialize(this)

        val myModules=module{
            single { createApiServiceInstance() }
            single { Room.databaseBuilder(this@MyApp, AppDatabase::class.java, "db_app").build() }
            single<SharedPreferences>{
                this@MyApp.getSharedPreferences(
                    "app_settings",
                    MODE_PRIVATE
                )
            }
            single<NewsRepository> {
                NewsRepositoryImpl(
                    NewsRemoteDataSource(get()),
                    get<AppDatabase>().newsDao()
                )
            }
        viewModel { NewsViewModel(get()) }
        viewModel { FavoritesViewModel(get()) }
        }

        startKoin{
            androidContext(this@MyApp)
            modules(myModules)
        }
    }
}