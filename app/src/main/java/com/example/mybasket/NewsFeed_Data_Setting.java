package com.example.mybasket;

/**
 * Created by 박효근 on 2016-07-22.
 */
import android.view.View;
import android.widget.ProgressBar;

/**
 * Created by 박지훈 on 2016-06-24.
 */
public class NewsFeed_Data_Setting {
    private String num;
    private String user;
    private String Do;
    private String Si;
    private String court;
    private String person;
    private String data;
    private String month;
    private String day;
    private String hour;
    private String minute;
    public ProgressBar NewsFeed_ProgressBar;

    public NewsFeed_Data_Setting(String num, String user, String Do, String Si, String court, String person, String data, String month, String day, String hour, String minute) {
        this.num = num;
        this.user = user;
        this.Do = Do;
        this.Si = Si;
        this.court = court;
        this.person = person;
        this.data = data;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }


    public String getnum() {
        return num;
    }

    public String getuser() {
        return user;
    }

    public String getDo() { return Do; }

    public String getSi() {
        return Si;
    }

    public String getcourt() {
        return court;
    }

    public String getperson() {
        return person;
    }

    public String getdata() {
        return data;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }

    public String getHour() {
        return hour;
    }

    public String getMinute() {
        return minute;
    }
}