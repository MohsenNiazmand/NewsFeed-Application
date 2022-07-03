package com.example.newsfeed.data.repositories

import com.example.newsfeed.data.models.NewsResponse
import com.example.newsfeed.data.models.NewsResult
import com.example.newsfeed.data.repositories.source.NewsDataSource
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Response

class NewsRepositoryImpl(
    val newsRemoteDataSource: NewsDataSource,
    val newsLocalDataSource: NewsDataSource
):NewsRepository {
    override fun getNews( page: Int): Single<Response<NewsResponse>> =
          newsLocalDataSource.getFavorites()
              .flatMap { favoriteNews->
                  newsRemoteDataSource.getNews(page).doOnSuccess {
                      val favoritesId=favoriteNews.map {
                          it.id
                      }
                      it.body()?.response?.results?.forEach {news->
                          if (favoritesId.contains(news.id))
                              news.isFavorite=true
                      }
                  }
              }

    override fun getFavorites(): Single<List<NewsResult>> {
       return newsLocalDataSource.getFavorites()
    }

    override fun addToFavorites(result: NewsResult): Completable {
       return newsLocalDataSource.addToFavorites(result)
    }

    override fun deleteFromFavorites(result: NewsResult): Completable {
        return newsLocalDataSource.deleteFromFavorites(result)
    }
}