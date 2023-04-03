package com.sabanciuniv.cs310_hw2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.ActionBar;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import android.util.Log;
import java.net.HttpURLConnection;
import androidx.annotation.NonNull;
import android.content.Intent;
import java.net.URL;
import android.view.Menu;
import android.view.MenuItem;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {

    ProgressDialog dialog; //prgDialog
    RecyclerView commentsRecView; //CommentsRecView
    int selectedNewsID; //selectedNewsID
    List<CommentItem> commentsList; //cdata
    CommentsAdapter commentsAdp; //adp
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        commentsList = new ArrayList<>();
        selectedNewsID = (int)getIntent().getSerializableExtra("selectedNewsID");
        commentsRecView = findViewById(R.id.comrec);
        commentsAdp = new CommentsAdapter(commentsList, this);
        commentsRecView.setLayoutManager(new LinearLayoutManager(this));
        commentsRecView.setAdapter(commentsAdp);

        getSupportActionBar().setTitle("Comments");
        ActionBar currbar = getSupportActionBar();
        currbar.setHomeButtonEnabled(true);
        currbar.setDisplayHomeAsUpEnabled(true);
        currbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_white_18dp);
        CommentAct commenting = new CommentAct();
        commenting.execute("http://10.3.0.14:8080/newsapp/getcommentsbynewsid");

    }

    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.comments_menu, menu); // some kind of id ???

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.mn_addcomment) {
            Intent in = new Intent(getApplicationContext(), PostCommentActivity.class);
            in.putExtra("selectedNewsID", selectedNewsID);
            startActivity(in);
        }
        else if(item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }



    class CommentAct extends AsyncTask<String, Void, String>{


        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(CommentsActivity.this);
            dialog.setTitle("Loading");
            dialog.setMessage("Please wait...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String newsID = String.valueOf(selectedNewsID);
            String urlstr = strings[0] + "/" + newsID;
            StringBuilder buffer = new StringBuilder();
            try {
                URL url = new URL(urlstr);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                BufferedReader bufferReader = new BufferedReader(new InputStreamReader((connection.getInputStream())));
                String str = "";
                while ((str = bufferReader.readLine()) != null){
                    buffer.append(str);
                }

            }
            catch (MalformedURLException exception){
                exception.printStackTrace();
            }
            catch (IOException ioEx){
                ioEx.printStackTrace();
            }
            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String s){
            commentsList.clear();
            try {
                JSONObject jobj = new JSONObject(s);
                if (jobj.getInt("serviceMessageCode") == 1){
                    JSONArray jarray = jobj.getJSONArray("items");
                    for (int i = 0; i < jarray.length(); i++){
                        JSONObject currobj = (JSONObject)jarray.get(i);
                        CommentItem comitem = new CommentItem(currobj.getInt("id"), currobj.getString("name"), currobj.getString("text"));
                        commentsList.add(comitem);
                    }
                }

                else{
                }
                    Log.i("DEV", String.valueOf(commentsList.size()));
                    commentsAdp.notifyDataSetChanged();
                    dialog.dismiss();
            }catch (JSONException jsnex){
                Log.e("DEV", jsnex.getMessage());
            }
        }

    }
}