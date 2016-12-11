package com.example.guilh.brasapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String dataUrl = "https://spreadsheets.google.com/tq?key=1jBmh4btz-CRo8RdPGordo0WvsNf2JH0KMAFSvn5PKQE";
    ArrayList<String> data = new ArrayList<>();
    ArrayAdapter<String> listViewAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listView);
        data = new ArrayList<>();
        int listLayout = android.R.layout.simple_list_item_1;
        listViewAdapter = new ArrayAdapter<String>(this,listLayout,data);
        listView.setAdapter(listViewAdapter);

        new DataReader(this,dataUrl).execute();

        new CountDownTimer(60000,1000){
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                Intent mServiceIntent = new Intent(MainActivity.this, RSSPullService.class);
                mServiceIntent.putExtra("dataUrl",dataUrl);
                startService(mServiceIntent);
                processData(RSSPullService.response);
                this.start();
            }
        }.start();


    }

    public void processData(String UrlResponse){
        try{
            JSONObject jsonObj = new JSONObject(UrlResponse);
            JSONArray rows = jsonObj.getJSONArray("rows");
            data.clear();
            for(int r = 1; r < rows.length();r++){
                JSONObject row = rows.getJSONObject(r);
                JSONArray columns = row.getJSONArray("c");
                String value = "";
                value += columns.getJSONObject(0).getString("v");
                value += "\n "+columns.getJSONObject(1).getString("v");
                value += " "+columns.getJSONObject(2).getString("v");
                value += " x "+columns.getJSONObject(4).getString("v");
                value += " "+columns.getJSONObject(5).getString("v");
                data.add(value);
            }
            listViewAdapter.notifyDataSetChanged();
        }catch(Exception e){
            Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public void updateData(String dataUrl){
        new DataReader(this,dataUrl).execute();
    }
}
