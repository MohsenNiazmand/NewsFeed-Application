package com.example.newsfeed.ui.favorites

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsfeed.data.models.NewsResult
import com.example.newsfeed.data.repositories.NewsRepository
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class FavoritesViewModel(val newsRepository: NewsRepository) : ViewModel() {
    val compositeDisposable=CompositeDisposable()
    val favoritesLiveData=MutableLiveData<List<NewsResult>>()
    val progressBarLiveData = MutableLiveData<Boolean>()


    init {
        getFavorites()
    }

    fun getFavorites(){
        progressBarLiveData.value=true
        newsRepository.getFavorites()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<NewsResult>>{
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onSuccess(t: List<NewsResult>) {
                    favoritesLiveData.postValue(t)
                    progressBarLiveData.postValue(false)
                    Timber.i(t.toString())

                }

                override fun onError(e: Throwable) {
                    progressBarLiveData.postValue(false)
                    Timber.e(e)
                }

            })
    }

    fun removeFromFavorites(result: NewsResult) {
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

    }

}