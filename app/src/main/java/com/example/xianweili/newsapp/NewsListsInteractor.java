package com.example.xianweili.newsapp;

import android.net.ConnectivityManager;

import com.example.xianweili.newsapp.NetworkUtil.NetworkUtil;
import com.example.xianweili.newsapp.data.model.responsemodel.NewsListsResponse;
import com.example.xianweili.newsapp.data.service.NewsListsService;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xianweili on 7/24/18.
 */

public class NewsListsInteractor {
    private CompositeDisposable compositeDisposable;
    private OnNewsListCallback newsListCallback;

    public NewsListsInteractor() {
        compositeDisposable = new CompositeDisposable();
    }

    public void setNewsListCallback(OnNewsListCallback callback) {
        newsListCallback = callback;
    }

    public void getNewsList(int page){
        Observable<NewsListsResponse> getNewsListAPi =
                NetworkUtil
                        .getRetrofit()
                        .create(NewsListsService.class)
                        .getNewsList("us",
                                "84cce694de214d928bf162917492d2c0",
                                page,
                                5);

        compositeDisposable.add(getNewsListAPi.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                newsListsResponse -> {
                    newsListCallback.onNewsListReceived(newsListsResponse);
                }, throwable -> {
                    newsListCallback.onNewsListError(throwable.getMessage());
                }
        ));
    }

    public void destroyResource(){
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
        unregisterCallback();
    }

    public void unregisterCallback(){
        newsListCallback = null;
    }

    public interface OnNewsListCallback{
        void onNewsListReceived(NewsListsResponse newsListsResponse);

        void onNewsListError(String errorMessage);
    }
}
