package com.sabanciuniv.cs310_hw2;

import java.io.Serializable;
import java.util.Date;
import android.graphics.Bitmap;

public class NewsItem implements Serializable {
    private String title;
    private String text;

    //name of image in Drawable folder
    private String imagePath;
    private Date newsDate;
    private int id;
    private transient Bitmap bitmap;

    public NewsItem() {
    }

    public NewsItem(String title, String text, String imagePath, Date newsDate, int id) {
        this.title = title;
        this.text = text;
        this.imagePath = imagePath;
        this.newsDate = newsDate;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Date getNewsDate() {
        return newsDate;
    }

    public void setNewsDate(Date newsDate) {
        this.newsDate = newsDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
