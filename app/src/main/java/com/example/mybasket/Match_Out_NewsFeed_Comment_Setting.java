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
    private String comment_user;
    private String data;
    private String month;
    private String day;
    private String hour;
    private String minute;

///////////////////User_Information 변수////////////////////

    private String Name;
    private String Birth;
    private String Sex;
    private String Position;
    private String Team;
    private String Profile;
    private String Height;
    private String Weight;
    private String Phone;

    public Match_Out_NewsFeed_Comment_Setting(String comment_num, String newsfeed_num,String comment_user, String data, String month, String day, String hour, String minute, String Name, String Birth, String Sex, String Position, String Team, String Profile, String Height, String Weight, String Phone) {
        this.comment_num = comment_num;
        this.newsfeed_num = newsfeed_num;
        this.comment_user = comment_user;
        this.data = data;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;

///////////////////User_Information 변수값 입력////////////////////
        this.Name = Name;
        this.Birth = Birth;
        this.Sex = Sex;
        this.Position = Position;
        this.Team = Team;
        this.Profile = Profile;
        this.Height = Height;
        this.Weight = Weight;
        this.Phone = Phone;
    }

    public String getcomment_num() {
        return comment_num;
    }

    public String getnewsfeed_num() {
        return newsfeed_num;
    }

    public String getcomment_user() { return comment_user;}

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

///////////////////User_Information 함수////////////////////

    public String getInformation_Name() { return Name; }

    public String getInformation_Birth() {
        return Birth;
    }

    public String getInformation_Sex() { return Sex;}

    public String getInformation_Position() {
        return Position;
    }

    public String getInformation_Team() {
        return Team;
    }

    public String getInformation_Profile() { return Profile; }

    public String getInformation_Height() {
        return Height;
    }

    public String getInformation_Weight() {
        return Weight;
    }

    public String getInformation_Phone() {
        return Phone;
    }
}


