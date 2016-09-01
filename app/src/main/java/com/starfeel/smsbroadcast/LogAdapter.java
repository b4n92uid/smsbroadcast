package com.starfeel.smsbroadcast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Abdelghani on 23/08/2016.
 */
public class LogAdapter extends ArrayAdapter<LogItem> {

    public LogAdapter(Context context, ArrayList<LogItem> logItems) {
        super(context, 0, logItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LogItem l = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.log_item, parent, false);
        }

        TextView tagView = (TextView) convertView.findViewById(R.id.tagView);
        TextView dateView = (TextView) convertView.findViewById(R.id.dateView);
        TextView messageView = (TextView) convertView.findViewById(R.id.messageView);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);

        // TODO: Correct date format
        DateFormat df = new SimpleDateFormat("dd/MM HH:mm");

        tagView.setText(l.getTag());
        dateView.setText(df.format(l.getDate()));
        messageView.setText(l.getMessage());

        if(l.getLevel().equals("E")) {
            if(l.getTag().equals(MainActivity.APPTAG_SERVER_ERR))
                imageView.setImageResource(R.drawable.ic_action_halt);
            else
                imageView.setImageResource(R.drawable.ic_action_warning);
        }

        else if(l.getLevel().equals("I")) {
            if(l.getTag().equals(MainActivity.APPTAG_SERVER_OK))
                imageView.setImageResource(R.drawable.ic_action_tick);
            else
                imageView.setImageResource(R.drawable.ic_action_info);
        }

        return convertView;
    }
}
