package com.lb.technews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Serializable {
    String keys[];
    ArrayList<String> keysActual = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button start = (Button) findViewById(R.id.Enterbtn);
        DownloadTask task = new DownloadTask();

        String link = "https://hacker-news.firebaseio.com/v0/topstories.json";//api keys
        try {
            task.execute(link).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent articleIntent = new Intent(MainActivity.this, Articles.class);
                articleIntent.putExtra("keysActual", (Serializable) keysActual);
                startActivity(articleIntent);
            }
        });
    }


    public class DownloadTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                keys = result.split(",");

                for (int i = 0; i < 21; i++) {
                    keysActual.add(keys[i]);

                }

                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}