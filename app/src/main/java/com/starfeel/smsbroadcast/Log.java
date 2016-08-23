package com.starfeel.smsbroadcast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Abdelghani on 23/08/2016.
 */
public class Log {

    private long timestamp;
    private String message;
    private String origin;
    private String server;

    Log(long t, String m, String o, String s) {
        setTimestamp(t);
        setMessage(m);
        setOrigin(o);
        setServer(s);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    static public ArrayList<Log> fromJson(JSONArray json) {

        ArrayList<Log> list = new ArrayList<>();

        for (int i = 0; i < json.length(); i++) {
            JSONObject obj = json.getJSONObject(i);
            list.add(new Log(obj.getLong("timestamp"), obj.getString("message"), obj.getString("origin")));
        }

        return list;
    }
}
