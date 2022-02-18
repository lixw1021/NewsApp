package com.example.xianweili.newsapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.xianweili.newsapp.NewsAdapter.NewsListViewHolder
import com.example.xianweili.newsapp.data.model.responsemodel.Article
import com.example.xianweili.newsapp.databinding.ItemNewsBinding
import com.squareup.picasso.Picasso

class NewsAdapter(private val context: Context) : RecyclerView.Adapter<NewsListViewHolder>() {
    private val newsList: MutableList<Article>?
    private var callback: OnClickListenerCallback? = null
    fun setCallback(callback: OnClickListenerCallback?) {
        this.callback = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsListViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(context), parent, false)
        return NewsListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsListViewHolder, position: Int) {
        val article = newsList!![position]
        holder.binding.apply {
            tvNewsSource.text = article.source!!.name
            tvNewTitle.text = article.title
            Picasso.get()
                .load(article.urlToImage)
                .resize(1200, 800)
                .centerCrop()
                .into(holder.binding.ivNewsImage)
        }
    }

    override fun getItemCount(): Int {
        return newsList?.size ?: 0
    }

    fun swapDate(articles: List<Article>?) {
//        newsList.clear();
        newsList!!.addAll(articles!!)
        notifyDataSetChanged()
    }

    fun cleanData() {
        newsList!!.clear()
        notifyDataSetChanged()
    }

    inner class NewsListViewHolder(val binding: ItemNewsBinding) : ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener { callback!!.onNewsClicked() }
            binding.ibNewsShare.setOnClickListener {
                callback!!.onShareClicked()
            }
            binding.ibNewsMessage.setOnClickListener {
                callback!!.onMessageClicked()
            }
        }
    }

    interface OnClickListenerCallback {
        fun onNewsClicked()
        fun onShareClicked()
        fun onMessageClicked()
        fun onFavoriteClicked()
    }

    init {
        newsList = ArrayList()
    }
}