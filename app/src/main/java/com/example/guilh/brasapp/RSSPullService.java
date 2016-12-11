package com.example.guilh.brasapp;

import android.app.IntentService;
import android.content.Intent;
import android.os.CountDownTimer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by guilh on 11/12/2016.
 */

public class RSSPullService extends IntentService {
    Exception e = null;
    static String response = null;

    public RSSPullService(){
        super(RSSPullService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        final String dataUrl = workIntent.getStringExtra("dataUrl");
        // Do work here, based on the contents of dataString

        getResponse(dataUrl);

    }

    public void getResponse(String dataUrl){
        try{
            URL url = new URL(dataUrl);
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

    }

    public static String retResponse(){
        return RSSPullService.response;
    }

}
