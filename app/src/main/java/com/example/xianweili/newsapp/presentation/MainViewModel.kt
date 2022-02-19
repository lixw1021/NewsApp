package com.example.xianweili.newsapp.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.xianweili.newsapp.data.NewsRepository
import com.example.xianweili.newsapp.data.model.responsemodel.NewsListsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: NewsRepository,
) : ViewModel() {

    private val disposable = CompositeDisposable()
    private var page = 1;
    val state = MutableLiveData<NewsListState>()

    init {
        loadData()
    }

    fun loadData(page: Int = 1) {
        repository.getNewsList(country = "US", page = page, pageSize = 10)
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    state.postValue(
                        NewsListState(
                            page = page,
                            newsListsResponse = it
                        )
                    )
                },
                {
                    Log.i(this.javaClass.simpleName, "Load Error${it.toString()}")
                }
            ).let {
                disposable.add(it)
            }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

    fun loadNextPage() {
        loadData(++page)
    }
}

data class NewsListState(
    val page: Int = 1,
    val newsListsResponse: NewsListsResponse?,
)