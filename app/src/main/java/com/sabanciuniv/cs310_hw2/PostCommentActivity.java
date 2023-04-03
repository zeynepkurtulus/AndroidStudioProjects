package com.sabanciuniv.cs310_hw2;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class PostCommentActivity extends AppCompatActivity {

    ProgressDialog dialog;
    int selectedNewsID;
    EditText editname;
    EditText editcomment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_comment);
        editname =  findViewById(R.id.editname);
        editcomment = findViewById(R.id.editcomment);
        getSupportActionBar().setTitle("Post Comment");

        ActionBar currentBar =getSupportActionBar();
        currentBar.setHomeButtonEnabled(true);
        currentBar.setDisplayHomeAsUpEnabled(true);
        currentBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_white_18dp);

        selectedNewsID = (int)getIntent().getSerializableExtra("selectedNewsID");

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }

    public void addcomment(View v)
    {
        PostCommentTask tsk = new PostCommentTask();
        tsk.execute("http://10.3.0.14:8080/newsapp/savecomment", editname.getText().toString(), editcomment.getText().toString(), String.valueOf(selectedNewsID));
    }
    class PostCommentTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(PostCommentActivity.this);
            dialog.setTitle("Loading...");
            dialog.setMessage("Please wait...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder strBuilder = new StringBuilder();
            String urlStr = strings[0];
            String name = strings[1];
            String message = strings[2];
            String news_id = strings[3];
            JSONObject jobj = new JSONObject();
            try {
                jobj.put("name", name);
                jobj.put("text", message);
                jobj.put("news_id", news_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                URL url = new URL(urlStr);
                HttpURLConnection connect = (HttpURLConnection)url.openConnection();
                connect.setDoInput(true);
                connect.setDoOutput(true);
                connect.setRequestMethod("POST");
                connect.setRequestProperty("Content-type", "application/json");
                connect.connect();

                DataOutputStream output = new DataOutputStream(connect.getOutputStream());
                output.writeBytes(jobj.toString());

                if(connect.getResponseCode()== HttpURLConnection.HTTP_OK)
                {


                    BufferedReader reader = new BufferedReader(new InputStreamReader(connect.getInputStream()));
                    String str = "";
                    while ((str = reader.readLine() )!= null){
                        strBuilder.append(str);
                    }


                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(PostCommentActivity.this);
                    builder.setMessage("Failed");
                    builder.setNegativeButton("OK", null);
                    builder.show();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return strBuilder.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            finish();

        }
    }

}