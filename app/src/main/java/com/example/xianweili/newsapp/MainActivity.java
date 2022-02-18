package com.example.xianweili.newsapp;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.example.xianweili.newsapp.data.model.responsemodel.NewsListsResponse;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        NewsListsInteractor.OnNewsListCallback,
        NewsAdapter.OnClickListenerCallback {
    @BindView(R.id.rv_news) RecyclerView recyclerView;
    @BindView(R.id.srl_news_refresh) SwipeRefreshLayout refreshLayout;
    @BindView(R.id.my_toolbar) Toolbar toolbar;

    private NewsAdapter newsAdapter;
    private NewsListsInteractor interactor;
    private int curPage = 1;
    private EndlessRecyclerViewScrollListener scrollListener;
    private boolean mIsLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        initialRecyclerView(linearLayoutManager);
        initialInteractor();
        newsAdapter.setCallback(this);
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (!recyclerView.canScrollVertically(1)) {
//                    interactor.getNewsList(curPage);
//                }
//            }
//        });

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//                interactor.getNewsList(page);
//            }
//        };
//        recyclerView.addOnScrollListener(scrollListener);

        RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (mIsLoading) return;

                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();
                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                    interactor.getNewsList(curPage);
                    mIsLoading = true;
                }
            }
        };

        recyclerView.addOnScrollListener(mScrollListener);

        initialSwipeRefreshLayout();
    }

    private void initialSwipeRefreshLayout() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                newsAdapter.cleanData();
                curPage = 1;
                interactor.getNewsList(curPage);
            }
        });
    }

    private void initialInteractor() {
        interactor = new NewsListsInteractor();
        interactor.setNewsListCallback(this);
        curPage = 1;
        interactor.getNewsList(curPage);
    }

    private void initialRecyclerView(LinearLayoutManager linearLayoutManager) {
        newsAdapter = new NewsAdapter(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(newsAdapter);
    }

    @Override
    public void onNewsListReceived(NewsListsResponse newsListsResponse) {
        refreshLayout.setRefreshing(false);
        if (newsListsResponse.getStatus().equals("ok") && newsListsResponse.getArticles().size() > 0) {
            newsAdapter.swapDate(newsListsResponse.getArticles());
            Log.i("xianwei", "curPage" + curPage);
            curPage++;
            mIsLoading = false;
        }
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

    // adapter callbacks////////////////////////////////////////////////////////////////////////////
    @Override
    public void onNewsClicked() {
        Toast.makeText(this, "News Clicked", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onShareClicked() {
        Toast.makeText(this, "Share Button Clicked", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMessageClicked() {
        Toast.makeText(this, "Message Button Clicked", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onFavoriteClicked() {
        Toast.makeText(this, "Favorite Button Clicked", Toast.LENGTH_LONG).show();

    }
    // adapter callbacks end////////////////////////////////////////////////////////////////////////

}
