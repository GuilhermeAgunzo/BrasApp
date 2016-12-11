package com.example.guilh.brasapp;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by guilh on 11/12/2016.
 */

public class DataReader extends AsyncTask<String,Void,Void> {
    MainActivity activity = null;
    String url = null;
    String response = null;
    Exception e = null;
    public DataReader(MainActivity activity, String url){
        this.activity = activity;
        this.url = url;
    }
    @Override
    protected  Void doInBackground(String... params){
        this.e = null;
        try{
            URL url = new URL(this.url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            StringBuilder response = new StringBuilder();
            while((line = reader.readLine()) != null){
                response.append(line).append('\n');
            }
            int start = response.indexOf("{",response.indexOf("{") + 1);
            int end = response.lastIndexOf("}");
            this.response = response.substring(start,end);
        }catch(Exception e){
            this.e = e;
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void unused){
        activity.processData(this.response);
    }
}
