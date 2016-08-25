package com.starfeel.smsbroadcast;

import java.util.Date;

/**
 * Created by Abdelghani on 23/08/2016.
 */
public class LogItem {

    private Date date;
    private String subtag;
    private String level;
    private String message;

    LogItem(Date d, String t, String l, String m) {
        setDate(d);
        setSubtag(t);
        setLevel(l);
        setMessage(m);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSubtag() {
        return subtag;
    }

    public void setSubtag(String subtag) {
        this.subtag = subtag;
    }
}
