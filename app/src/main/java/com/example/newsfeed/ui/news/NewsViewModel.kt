package com.example.newsfeed.ui.news

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsfeed.data.models.NewsResponse
import com.example.newsfeed.data.models.NewsResult
import com.example.newsfeed.data.repositories.NewsRepository
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import timber.log.Timber

class NewsViewModel(val newsRepository: NewsRepository):ViewModel() {
    val compositeDisposable = CompositeDisposable()
    val progressBarLiveData = MutableLiveData<Boolean>()
    val newsLiveData = MutableLiveData<Response<NewsResponse>>()
    var page = 1


    init {
        getNews(page)
    }

    fun getNews(page:Int) {
        progressBarLiveData.value = true
        newsRepository.getNews(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<Response<NewsResponse>> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onSuccess(t: Response<NewsResponse>) {
                    progressBarLiveData.postValue(false)
                    newsLiveData.postValue(t)

                }

                override fun onError(e: Throwable) {
                    Timber.e(e)
                    progressBarLiveData.postValue(false)
                }


            })
    }

//



    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun addNewsFavorites(result: NewsResult) {
        if (result.isFavorite)
            newsRepository.deleteFromFavorites(result)
                .subscribeOn(Schedulers.io())
                .subscribe(object :CompletableObserver{
                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                    }

                    override fun onComplete() {
                        result.isFavorite = false
                    }

                    override fun onError(e: Throwable) {
                        Timber.e(e)
                    }

                })


        else
            newsRepository.addToFavorites(result)
                .subscribeOn(Schedulers.io())
                .subscribe(object :CompletableObserver{
                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                    }

                    override fun onComplete() {
                        result.isFavorite = true
                    }

                    override fun onError(e: Throwable) {
                        Timber.e(e)
                    }

                })

    }
}