package com.sabanciuniv.cs310_hw2;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    List<NewsItem> newsItem;
    Context ctx;
    NewsItemClickListener listener;

    public NewsAdapter(List<NewsItem> newsItem, Context ctx, NewsItemClickListener listener) {
        this.newsItem = newsItem;
        this.ctx = ctx;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NewsAdapter.NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.news_row_layout, parent, false);
        return new NewsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.NewsViewHolder holder, int position) {
        holder.textDate.setText(new SimpleDateFormat("dd/MM/yyy").format(newsItem.get(position).getNewsDate()));
        holder.textTitle.setText(newsItem.get(position).getTitle());
        holder.root.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                listener.newItemClicked(newsItem.get(position));

            }
        });

        if(newsItem.get(position).getBitmap() == null){
            new ImageDownload(holder.imgNews).execute(newsItem.get(position));
        }
        else{
            holder.imgNews.setImageBitmap(newsItem.get(position).getBitmap());
        }

    }

    @Override
    public int getItemCount() {
        return newsItem.size();
    }


    public interface NewsItemClickListener{
        public void newItemClicked(NewsItem selectedNewsItem);
    }

    class NewsViewHolder extends RecyclerView.ViewHolder{
        ImageView imgNews;
        TextView textTitle;
        TextView textDate;
        ConstraintLayout root;
        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            imgNews = itemView.findViewById(R.id.imgNews);
            textTitle = itemView.findViewById(R.id.textlisttitle);
            textDate = itemView.findViewById(R.id.textlistdate);
            root = itemView.findViewById(R.id.container);
        }
    }
}
