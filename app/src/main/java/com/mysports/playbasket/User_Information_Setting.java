//package com.example.mybasket;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Button;
//import android.widget.ImageView;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.protocol.HTTP;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by 박지훈 on 2016-08-09.
// */
//
//public class User_Information_Setting extends Activity {
//
//
//    private String Name;
//    private String Birth;
//    private String Sex;
//    private String Position;
//    private String Team;
//    private String Profile;
//    private String Height;
//    private String Weight;
//    private String Phone;
//
//
//    public User_Information_Setting(String Name, String Birth,String Sex, String Position, String Team, String Profile, String Height, String Weight, String Phone) {
//        this.Name = Name;
//        this.Birth = Birth;
//        this.Sex = Sex;
//        this.Position = Position;
//        this.Team = Team;
//        this.Profile = Profile;
//        this.Height = Height;
//        this.Weight = Weight;
//        this.Phone = Phone;
//    }
//
//    public String getInformation_Name() { return Name; }
//
//    public String getInformation_Birth() {
//        return Birth;
//    }
//
//    public String getInformation_Sex() { return Sex;}
//
//    public String getInformation_Position() {
//        return Position;
//    }
//
//    public String getInformation_Team() {
//        return Team;
//    }
//
//    public String getInformation_Profile() { return Profile; }
//
//    public String getInformation_Height() {
//        return Height;
//    }
//
//    public String getInformation_Weight() {
//        return Weight;
//    }
//
//    public String getInformation_Phone() {
//        return Phone;
//    }
//}
