package com.sabanciuniv.cs310_hw2;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class DetailedNews extends AppCompatActivity {

    NewsItem selectedNews;
    ImageView imgDetail;
    TextView title;
    TextView date;
    TextView news;
    int newsId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_news);
        imgDetail = findViewById(R.id.imgdetail);
        title = findViewById(R.id.txtdetailtitle);
        date = findViewById(R.id.txtdetaildate);
        news = findViewById(R.id.txtdetailnews);
        selectedNews = (NewsItem)getIntent().getSerializableExtra("selectedNewsItem");
        title.setText(selectedNews.getTitle());
        date.setText(new SimpleDateFormat("dd/MM/yyy").format(selectedNews.getNewsDate()));
        news.setText(selectedNews.getText());
        newsId = selectedNews.getId();

        if(selectedNews.getBitmap() == null){
            new ImageDownload(imgDetail).execute(selectedNews);
        }
        else{
            imgDetail.setImageBitmap(selectedNews.getBitmap());
        }
        getSupportActionBar().setTitle("News Details");

        ActionBar currbar =getSupportActionBar();
        currbar.setHomeButtonEnabled(true);
        currbar.setDisplayHomeAsUpEnabled(true);
        currbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_white_18dp);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.mn_comment) {
            Intent i = new Intent(getApplicationContext(), CommentsActivity.class);
            i.putExtra("selectedNewsID", newsId);
            startActivity(i);
        }
        else if(item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }

}