package com.starfeel.smsbroadcast;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    static public String APPTAG = "SMSBROADCAST";
    static public String APPTAG_SERVER_OK = APPTAG+".SERVER.OK";
    static public String APPTAG_SERVER_ERR = APPTAG+".SERVER.ERR";

    private ListView mLogView;
    private Toolbar mToolbar;

    public ArrayList<LogItem> getLog(String filter)
    {
        ArrayList<LogItem> data  = new ArrayList<>();

        try {
            String[] command = new String[] { "logcat", "-d", "-v", "threadtime" };

            Process process = Runtime.getRuntime().exec(command);

            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            String line;

            while ((line = bufferedReader.readLine()) != null) {

                if (line.contains(filter)) {

                    Pattern p = Pattern.compile("(\\d+-\\d+ [\\d :.-]+) +\\d+ +\\d+ ([A-Z]) ([\\w.]+): (.+)");
                    Matcher m = p.matcher(line);

                    if(m.find()) {

                        String date = m.group(1);
                        String level = m.group(2);
                        String tag = m.group(3);
                        String message = m.group(4);

                        SimpleDateFormat format = new SimpleDateFormat("M-d H:m:s.S");

                        data.add(new LogItem(format.parse(date), tag, level, message));
                    }

                }
            }
        } catch (IOException ex) {
            Log.e(APPTAG, "Gathering log data failed");

        } catch (ParseException ex) {
            Log.e(APPTAG, "Parse log data failed");

        }

        Collections.reverse(data);

        return data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);

        Log.i(APPTAG, "SMSBroadcast Started");

        final LogAdapter adapter = new LogAdapter(MainActivity.this, getLog(APPTAG));

        mLogView = (ListView)findViewById(R.id.listView);
        mLogView.setAdapter(adapter);

        final Handler handler = new Handler();

        Runnable timer = new Runnable() {
            @Override
            public void run() {
                adapter.clear();
                adapter.addAll(getLog(APPTAG));

                handler.postDelayed(this, 1000);
            }
        };

        handler.post(timer);
    }

    public void exportLogAction(View button) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/YY HH:mm");
        Date d = new Date();

        String filename = "SMSBroadcast Log " + df.format(d) + "csv";

        FileOutputStream stream;

        try {
            stream = openFileOutput(filename, Context.MODE_PRIVATE);

            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(stream)
            );

            ArrayList<LogItem> data  = new ArrayList<>();

            for (int i = 0; i < data.size(); i++)
            {
                StringBuilder line = new StringBuilder();

                line.append(data.get(i).getLevel());
                line.append(",");
                line.append(data.get(i).getDate());
                line.append(",");
                line.append(data.get(i).getMessage());
                line.append(",");
                line.append(data.get(i).getTag());

                writer.write(line.toString());
            }


            stream.close();

        } catch (Exception e) {

        }

    }

    public void clearLogAction(View button) {
        String[] command = new String[] { "logcat", "-c"};

        try {
            Runtime.getRuntime().exec(command);

            LogAdapter adapter = (LogAdapter)mLogView.getAdapter();
            adapter.clear();

        } catch(IOException ex) {
            Log.e(APPTAG, "Clearing log data failed");

        }
    }
}
