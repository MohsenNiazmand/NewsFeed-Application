package com.example.newsfeed.data.repositories.source

import com.example.newsfeed.data.models.NewsResponse
import com.example.newsfeed.data.models.NewsResult
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Response

interface NewsDataSource {
    fun getNews(page:Int): Single<Response<NewsResponse>>
    fun getFavorites():Single<List<NewsResult>>
    fun addToFavorites(result: NewsResult ): Completable

    fun deleteFromFavorites(result: NewsResult ): Completable

}