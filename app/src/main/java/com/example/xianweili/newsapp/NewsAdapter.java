package com.example.xianweili.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xianweili.newsapp.data.model.responsemodel.Article;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xianweili on 7/24/18.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsListViewHolder> {
    private Context context;
    private List<Article> newsList;
    private OnClickListenerCallback callback;

    public NewsAdapter(Context context) {
        this.context = context;
        newsList = new ArrayList<>();
    }

    public void setCallback(OnClickListenerCallback callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public NewsAdapter.NewsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_news, parent, false);
        return new NewsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.NewsListViewHolder holder, int position) {
        Article item = newsList.get(position);
        holder.newsSourceTv.setText(item.getSource().getName());
        holder.newsTitleTv.setText(item.getTitle());

        Picasso.get()
                .load(item.getUrlToImage())
                .resize(1200,800)
                .centerCrop()
                .into(holder.newsImage);
    }

    @Override
    public int getItemCount() {
        return newsList == null ? 0 : newsList.size();
    }

    public void swapDate(List<Article> articles) {
//        newsList.clear();
        newsList.addAll(articles);
        notifyDataSetChanged();
    }

    public void cleanData() {
        newsList.clear();
        notifyDataSetChanged();
    }

    public class NewsListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_news_image) ImageView newsImage;
        @BindView(R.id.tv_news_source) TextView newsSourceTv;
        @BindView(R.id.tv_news_hours) TextView newsHoursTv;
        @BindView(R.id.tv_new_title) TextView newsTitleTv;
        @BindView(R.id.tv_news_reactions) TextView newsReactionTv;
        @BindView(R.id.iv_news_more_btn) ImageButton newsMoreBtn;
        @BindView(R.id.ib_news_favorite) ImageButton newsFavoriteBtn;
        @BindView(R.id.ib_news_message) ImageButton newsMessageBtn;
        @BindView(R.id.ib_news_share) ImageButton newsSahreBtn;

        public NewsListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.onNewsClicked();
                }
            });
        }

        @OnClick(R.id.ib_news_share)
        void shareNews(){
            callback.onShareClicked();
        }

        @OnClick(R.id.ib_news_message)
        void messageNews() {
            callback.onMessageClicked();
        }

        @OnClick(R.id.ib_news_favorite)
        void saveFavoriteNews() {
            callback.onFavoriteClicked();
        }
    }

    public interface OnClickListenerCallback{
        void onNewsClicked();

        void onShareClicked();

        void onMessageClicked();

        void onFavoriteClicked();
    }
}
