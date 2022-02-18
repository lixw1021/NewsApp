package com.example.xianweili.newsapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xianweili.newsapp.NewsAdapter.OnClickListenerCallback
import com.example.xianweili.newsapp.NewsListsInteractor.OnNewsListCallback
import com.example.xianweili.newsapp.data.model.responsemodel.NewsListsResponse
import com.example.xianweili.newsapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnNewsListCallback, OnClickListenerCallback {

    private lateinit var binding: ActivityMainBinding
    var toolbar: Toolbar? = null
    private var newsAdapter: NewsAdapter? = null
    private var interactor: NewsListsInteractor? = null
    private var curPage = 1
    private val scrollListener: EndlessRecyclerViewScrollListener? = null
    private var mIsLoading = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val linearLayoutManager = LinearLayoutManager(this)
        initialRecyclerView(linearLayoutManager)
        initialInteractor()
        newsAdapter!!.setCallback(this)
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
        val mScrollListener: RecyclerView.OnScrollListener =
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (mIsLoading) return
                    val visibleItemCount = linearLayoutManager.childCount
                    val totalItemCount = linearLayoutManager.itemCount
                    val pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition()
                    if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                        interactor!!.getNewsList(curPage)
                        mIsLoading = true
                    }
                }
            }
        binding.rvNews.addOnScrollListener(mScrollListener)
        initialSwipeRefreshLayout()
    }

    private fun initialSwipeRefreshLayout() {
        binding.srlNewsRefresh.setOnRefreshListener {
            newsAdapter!!.cleanData()
            curPage = 1
            interactor!!.getNewsList(curPage)
        }
    }

    private fun initialInteractor() {
        interactor = NewsListsInteractor()
        interactor!!.setNewsListCallback(this)
        curPage = 1
        interactor!!.getNewsList(curPage)
    }

    private fun initialRecyclerView(linearLayoutManager: LinearLayoutManager) {
        newsAdapter = NewsAdapter(this)
        binding.rvNews.apply {
            layoutManager = linearLayoutManager
            adapter = newsAdapter
        }

    }

    override fun onNewsListReceived(newsListsResponse: NewsListsResponse) {
        binding.srlNewsRefresh.isRefreshing = false
        if (newsListsResponse.status == "ok" && newsListsResponse.articles.size > 0) {
            newsAdapter!!.swapDate(newsListsResponse.articles)
            Log.i("xianwei", "curPage$curPage")
            curPage++
            mIsLoading = false
        }
    }

    override fun onNewsListError(errorMessage: String) {
        Log.i("xianwei", errorMessage)
    }

    override fun onDestroy() {
        super.onDestroy()
        interactor!!.destroyResource()
    }

    // adapter callbacks////////////////////////////////////////////////////////////////////////////
    override fun onNewsClicked() {
        Toast.makeText(this, "News Clicked", Toast.LENGTH_LONG).show()
    }

    override fun onShareClicked() {
        Toast.makeText(this, "Share Button Clicked", Toast.LENGTH_LONG).show()
    }

    override fun onMessageClicked() {
        Toast.makeText(this, "Message Button Clicked", Toast.LENGTH_LONG).show()
    }

    override fun onFavoriteClicked() {
        Toast.makeText(this, "Favorite Button Clicked", Toast.LENGTH_LONG).show()
    } // adapter callbacks end////////////////////////////////////////////////////////////////////////
}