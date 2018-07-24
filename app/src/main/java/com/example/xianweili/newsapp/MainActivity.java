package com.example.xianweili.newsapp;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.xianweili.newsapp.data.model.responsemodel.NewsListsResponse;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        NewsListsInteractor.OnNewsListCallback {
    @BindView(R.id.rv_news) RecyclerView recyclerView;
    @BindView(R.id.srl_news_refresh) SwipeRefreshLayout refreshLayout;

    private NewsAdapter newsAdapter;
    private NewsListsInteractor interactor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initialRecyclerView();
        initialInteractor();
        initialSwipeRefreshLayout();
    }

    private void initialSwipeRefreshLayout() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                interactor.getNewsList();
            }
        });
    }

    private void initialInteractor() {
        interactor = new NewsListsInteractor();
        interactor.setNewsListCallback(this);
        interactor.getNewsList();
    }

    private void initialRecyclerView() {
        newsAdapter = new NewsAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(newsAdapter);
    }

    @Override
    public void onNewsListReceived(NewsListsResponse newsListsResponse) {
        refreshLayout.setRefreshing(false);
        Log.i("xianwei", newsListsResponse.getStatus());
        Log.i("xianwei", String.valueOf(newsListsResponse.getArticles().size()));
        newsAdapter.swapDate(newsListsResponse.getArticles());
    }

    @Override
    public void onNewsListError(String errorMessage) {
        Log.i("xianwei", errorMessage);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        interactor.destroyResource();
    }
}
