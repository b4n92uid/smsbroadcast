package com.starfeel.smsbroadcast;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Abdelghani on 23/08/2016.
 */
public class LogAdapter extends ArrayAdapter<Log> {

    public LogAdapter(Context context, ArrayList<Log> logs) {
        super(context, 0, logs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log l = getItem(position);

        TextView originView = (TextView) convertView.findViewById(R.id.originView);
        TextView dateView = (TextView) convertView.findViewById(R.id.dateView);
        TextView statusView = (TextView) convertView.findViewById(R.id.statusView);

        originView.setText(l.getOrigin());
    }
}
