package com.starfeel.smsbroadcast;

import android.os.AsyncTask;
import android.telephony.SmsMessage;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Abdelghani on 24/08/2016.
 */
public class ServerTask extends AsyncTask<SmsMessage, Void, Boolean>
{

    public void writePostData(OutputStream stream, String m, String o, long t) {

        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream, "UTF-8"));

            writer.write("message=");
            writer.write(m);
            writer.write("&origin=%s");
            writer.write(o);
            writer.write("&timestamp=%s");
            writer.write(String.valueOf(t));

        } catch (IOException e) {
            Log.e(MainActivity.APPTAG, e.toString());
        }
    }

    protected Boolean doInBackground(SmsMessage... params) {

        try {
            URL target = new URL("http://192.168.2.99/smsbroadcast/index.php");

            HttpURLConnection connection;

            connection = (HttpURLConnection) target.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            SmsMessage m = params[0];

            OutputStream output = new BufferedOutputStream(connection.getOutputStream());
            writePostData(output, m.getDisplayMessageBody(), m.getDisplayOriginatingAddress(), m.getTimestampMillis());
            output.flush();
            output.close();

            int httpCode = connection.getResponseCode();

            if(httpCode == HttpURLConnection.HTTP_OK)
                Log.i(MainActivity.APPTAG_SERVER_OK, "Server record successfully");
            else
                Log.e(MainActivity.APPTAG_SERVER_ERR, "Server record failed");

            connection.disconnect();

            return true;

        } catch (IOException e) {
            Log.e(MainActivity.APPTAG, "Server connection error");

        }

        return false;
    }
}
