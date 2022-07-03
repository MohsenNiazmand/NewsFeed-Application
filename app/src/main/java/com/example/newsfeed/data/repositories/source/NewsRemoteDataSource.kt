package com.example.newsfeed.data.repositories.source

import com.example.newsfeed.data.models.NewsResponse
import com.example.newsfeed.data.models.NewsResult
import com.example.newsfeed.services.ApiService
import com.example.newsfeed.utils.Consts.API_KEY
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Response

class NewsRemoteDataSource(val apiService: ApiService):NewsDataSource {
    override fun getNews(page: Int): Single<Response<NewsResponse>> {
        return apiService.getNews(API_KEY,page)
    }

    override fun getFavorites(): Single<List<NewsResult>> {
        TODO("Not yet implemented")
    }

    override fun addToFavorites(result: NewsResult): Completable {
        TODO("Not yet implemented")
    }

    override fun deleteFromFavorites(result: NewsResult): Completable {
        TODO("Not yet implemented")
    }
}