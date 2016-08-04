package com.example.mybasket;

/**
 * Created by 박효근 on 2016-07-22.
 */
import android.app.Activity;
import android.view.View;
import android.widget.ProgressBar;

/**
 * Created by 박지훈 on 2016-06-28.
 */
public class Match_Out_NewsFeed_Comment_Setting extends Activity {

    private String comment_num;
    private String newsfeed_num;
    private String person;
    private String data;
    private String month;
    private String day;
    private String hour;
    private String minute;
    private String Image;


    public Match_Out_NewsFeed_Comment_Setting(String comment_num, String newsfeed_num, String person, String data, String month, String day, String hour, String minute) {
        this.comment_num = comment_num;
        this.newsfeed_num = newsfeed_num;
        this.person = person;
//        person대신 아이디받기
        this.data = data;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;

    }

    public String getcomment_num() {
        return comment_num;
    }

    public String getnewsfeed_num() {
        return newsfeed_num;
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

    public String getImage() { return Image; }
}


