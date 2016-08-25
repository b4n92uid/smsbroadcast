package com.starfeel.smsbroadcast;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    static final public String APPTAG = "SMSBROADCAST";

    private ListView mLogView;

    public ArrayList<LogItem> getLog()
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

                if (line.contains(APPTAG)) {

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

        Log.i(APPTAG, "SMSBroadcast Started");

        final LogAdapter adapter = new LogAdapter(MainActivity.this, getLog());

        mLogView = (ListView)findViewById(R.id.listView);
        mLogView.setAdapter(adapter);

        final Handler handler = new Handler();

        Runnable timer = new Runnable() {
            @Override
            public void run() {
                adapter.clear();
                adapter.addAll(getLog());

                handler.postDelayed(this, 1000);
            }
        };

        handler.post(timer);
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
