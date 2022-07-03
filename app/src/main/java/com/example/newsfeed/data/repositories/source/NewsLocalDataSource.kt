package com.example.newsfeed.data.repositories.source

import androidx.room.*
import com.example.newsfeed.data.models.NewsResponse
import com.example.newsfeed.data.models.NewsResult
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Response
@Dao
interface NewsLocalDataSource:NewsDataSource {
    override fun getNews(page: Int): Single<Response<NewsResponse>> {
        TODO("Not yet implemented")
    }

    @Query("SELECT * FROM news")
    override fun getFavorites(): Single<List<NewsResult>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun addToFavorites(result: NewsResult): Completable
    @Delete
    override fun deleteFromFavorites(result: NewsResult): Completable


}