package com.example.xianweili.newsapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xianweili.newsapp.NewsAdapter.OnClickListenerCallback
import com.example.xianweili.newsapp.data.model.responsemodel.NewsListsResponse
import com.example.xianweili.newsapp.databinding.ActivityMainBinding
import com.example.xianweili.newsapp.presentation.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnClickListenerCallback {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private var newsAdapter: NewsAdapter? = null
    private var curPage = 1
    private var mIsLoading = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val linearLayoutManager = LinearLayoutManager(this)
        initialRecyclerView(linearLayoutManager)
        newsAdapter!!.setCallback(this)
        val mScrollListener: RecyclerView.OnScrollListener =
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (mIsLoading) return
                    val visibleItemCount = linearLayoutManager.childCount
                    val totalItemCount = linearLayoutManager.itemCount
                    val pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition()
                    if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                        viewModel.loadNextPage()
                        mIsLoading = true
                    }
                }
            }
        binding.rvNews.addOnScrollListener(mScrollListener)
        initialSwipeRefreshLayout()
        observeData()
    }

    private fun observeData() {
        viewModel.state.observe(this) {
            onNewsListReceived(it.newsListsResponse!!)
        }
    }

    private fun initialSwipeRefreshLayout() {
        binding.srlNewsRefresh.setOnRefreshListener {
            newsAdapter!!.cleanData()
            viewModel.loadData()
        }
    }

    private fun initialRecyclerView(linearLayoutManager: LinearLayoutManager) {
        newsAdapter = NewsAdapter(this)
        binding.rvNews.apply {
            layoutManager = linearLayoutManager
            adapter = newsAdapter
        }

    }

    fun onNewsListReceived(newsListsResponse: NewsListsResponse) {
        binding.srlNewsRefresh.isRefreshing = false
        if (newsListsResponse.status == "ok" && newsListsResponse.articles.size > 0) {
            newsAdapter!!.swapDate(newsListsResponse.articles)
            Log.i("xianwei", "curPage$curPage")
            mIsLoading = false
        }
    }

    fun onNewsListError(errorMessage: String) {
        Log.i("xianwei", errorMessage)
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