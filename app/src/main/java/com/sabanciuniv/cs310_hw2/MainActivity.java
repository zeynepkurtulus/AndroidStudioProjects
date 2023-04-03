package com.sabanciuniv.cs310_hw2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    ProgressDialog dialog;
    RecyclerView newsRecView;
    Spinner spinner;
    List<NewsItem> data;
    List<CategoryItem> categories;
    NewsAdapter adp;
    ArrayAdapter<CategoryItem> cadp;
    String selectedCategoryName;
    int selectedCategoryID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data = new ArrayList<>();

        newsRecView =findViewById(R.id.newsrec);


        adp = new NewsAdapter(data, this, selectedNewsItem -> {
            Intent i = new Intent(getApplicationContext(),DetailedNews.class);
            i.putExtra("selectedNewsItem", selectedNewsItem);
            startActivity(i);
        });
        newsRecView.setLayoutManager(new LinearLayoutManager(this));
        newsRecView.setAdapter(adp);
        getSupportActionBar().setTitle("News");


        CategoriesTask ctsk = new CategoriesTask();
        ctsk.execute("http://10.3.0.14:8080/newsapp/getallnewscategories");

        categories = new ArrayList<>();
        spinner =findViewById(R.id.categoryspinner);
        cadp = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        spinner.setAdapter(cadp);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategoryName= ((CategoryItem)spinner.getSelectedItem()).getName();
                selectedCategoryID = ((CategoryItem)spinner.getSelectedItem()).getId();


                NewsTask tsk = new NewsTask();
                if(selectedCategoryID == 0){
                    tsk.execute("http://10.3.0.14:8080/newsapp/getall");
                }
                else{
                    tsk.execute("http://10.3.0.14:8080/newsapp/getbycategoryid/" + selectedCategoryID);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    class NewsTask extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setTitle("Loading");
            dialog.setMessage("Please wait...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String urlStr = strings[0];
            StringBuilder buffer = new StringBuilder();
            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader((conn.getInputStream())));

                String line = "";
                while ((line = reader.readLine()) != null){
                    buffer.append(line);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            data.clear();
            try {
                JSONObject obj = new JSONObject(s);

                if(obj.getInt("serviceMessageCode") == 1){

                    JSONArray arr = obj.getJSONArray("items");

                    for(int i= 0; i< arr.length(); i++)
                    {
                        JSONObject current = (JSONObject)arr.get(i);

                        long date = current.getLong("date");
                        Date objDate = new Date(date);
                        NewsItem item = new NewsItem(current.getString("title"),
                                current.getString("text"),
                                current.getString("image"),
                                objDate,
                                current.getInt("id"));
                        data.add(item);
                    }
                }
                else{
                }
                Log.i("DEV", String.valueOf(data.size()));
                adp.notifyDataSetChanged();
                dialog.dismiss();
            } catch (JSONException e) {
                Log.e("DEV", e.getMessage());
            }
        }
    }

    class CategoriesTask extends AsyncTask<String, Void, String>
    {


        @Override
        protected String doInBackground(String... strings) {
            String urlStr = strings[0];
            StringBuilder buffer = new StringBuilder();
            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader((conn.getInputStream())));

                String line = "";
                while ((line = reader.readLine()) != null){

                    buffer.append(line);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            categories.clear();
            try {
                JSONObject obj = new JSONObject(s);

                if(obj.getInt("serviceMessageCode") == 1){

                    JSONArray arr = obj.getJSONArray("items");

                    CategoryItem itemForAll = new CategoryItem(0,"All");
                    categories.add(itemForAll);

                    for(int i= 0; i< arr.length(); i++)
                    {
                        JSONObject current = (JSONObject)arr.get(i);

                        CategoryItem item = new CategoryItem(current.getInt("id"),
                                current.getString("name")
                        );
                        categories.add(item);
                    }
                }
                else{
                }
                Log.i("DEV", String.valueOf(categories.size()));
                cadp.notifyDataSetChanged();
            } catch (JSONException e) {
                Log.e("DEV", e.getMessage());
            }
        }
    }
}