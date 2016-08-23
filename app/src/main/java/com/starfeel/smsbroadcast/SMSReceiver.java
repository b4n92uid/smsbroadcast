package com.starfeel.smsbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

public class SMSReceiver extends BroadcastReceiver {
    public SMSReceiver() {
    }

    private final String ACTION_RECEIVE_SMS = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(ACTION_RECEIVE_SMS)) {
            Bundle bundle = intent.getExtras();

            try {

                if (bundle != null) {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    String format = (String) bundle.get("format");

                    for (int i = 0; i < pdus.length; i++) {

                        SmsMessage m = SmsMessage.createFromPdu((byte[])pdus[i], format);

                        String message = m.getDisplayMessageBody();
                        String origin = m.getDisplayOriginatingAddress();
                        long timestamp = m.getTimestampMillis();

                        // Emit message to server

                        URL target = new URL("http://localhost/smsbroadcast");

                        HttpURLConnection connection = (HttpURLConnection) target.openConnection()
                        connection.setDoOutput(true);
                        connection.setChunkedStreamingMode(0);

                        // Log server response with messages info

                        SharedPreferences pref = context.getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);

                        String rawdata = pref.getString("LOG_DATA", "[]");

                        JSONObject object = new JSONObject();
                        object.put("message", message);
                        object.put("origin", origin);
                        object.put("timestamp", timestamp);
                        object.put("server", timestamp);

                        JSONArray array = new JSONArray(rawdata);
                        array.put(object);

                        SharedPreferences.Editor editor =  pref.edit();
                        editor.putString("LOG_DATA", array.toString());

                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    }
                }
            } catch (Exception e) {
                Log.e("SMSReceiver", e.toString());
            }
        }
    }
}
