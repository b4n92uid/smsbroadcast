package com.starfeel.smsbroadcast;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView mLogView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences pref = this.getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);

        String logdata = pref.getString("LOG_DATA", "[]");
        JSONArray parser = new JSONArray(logdata);

        ArrayList<Log> data = Log.fromJson(parser);

        LogAdapter adapter = new LogAdapter(this, data);

        mLogView = (ListView)findViewById(R.id.listView);
        mLogView.setAdapter(adapter);
    }
}
