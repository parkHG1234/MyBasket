package com.mysports.playbasket;

/**
 * Created by 박효근 on 2016-07-22.
 */
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

/**
 * Created by 박지훈 on 2016-06-24.
 */
public class Match_Out_NewsFeed_Data_Setting {
    private String num;
    private String user;
    private String Do;
    private String Si;
    private String court;
    private String data;
    private String month;
    private String day;
    private String hour;
    private String minute;
    private String Image;

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
    private String Comment_Count;



    public Match_Out_NewsFeed_Data_Setting(String num, String user, String Do, String Si, String court, String data, String month, String day, String hour, String minute, String Image, String Name, String Birth, String Sex, String Position, String Team, String Profile, String Height, String Weight, String Phone, String Comment_Count) {
        this.num = num;
        this.user = user;
        this.Do = Do;
        this.Si = Si;
        this.court = court;
        this.data = data;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.Image = Image;


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
        this.Comment_Count = Comment_Count;
    }


    public String getnum() {
        return num;
    }

    public String getuser() { return user; }

    public String getDo() { return Do; }

    public String getSi() {
        return Si;
    }

    public String getcourt() {
        return court;
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

    public String getHour() { return hour; }

    public String getMinute() {
        return minute;
    }

    public String getImage() { return Image; }

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

    public String getComment_Count() {
        return Comment_Count;
    }

}