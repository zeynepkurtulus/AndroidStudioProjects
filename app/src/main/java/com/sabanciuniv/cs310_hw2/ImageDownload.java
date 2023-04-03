package com.sabanciuniv.cs310_hw2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
public class ImageDownload extends AsyncTask<NewsItem, Void, Bitmap>{
    ImageView imgView;
    private NewsItem[] newsItems;

    public ImageDownload(ImageView imgView) {
        this.imgView = imgView;
    }


    @Override
    protected Bitmap doInBackground(@NonNull NewsItem... newsItems) {
        NewsItem current = newsItems[0];
        Bitmap bitmap = null;
        try {
            URL url = new URL(current.getImagePath());
            InputStream is = new BufferedInputStream(url.openStream());

            bitmap = BitmapFactory.decodeStream(is);
            current.setBitmap(bitmap);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    @Override
    protected void onPostExecute(Bitmap bitmap) {
        imgView.setImageBitmap(bitmap);
    }

}
