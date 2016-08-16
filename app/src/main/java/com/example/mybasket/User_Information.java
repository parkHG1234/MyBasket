package com.example.mybasket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 박지훈 on 2016-08-09.
 */

public class User_Information extends Activity {

    ArrayList<User_Information_Setting> InformationComment;

    ImageView Layout_CustomDialog_TeamPlayer_Profile;
    Button Layout_CustomDialog_TeamPlayer_TeamNameAndDuty;
    Button Layout_CustomDialog_TeamPlayer_Name;
    Button Layout_CustomDialog_TeamPlayer_Position;
    Button Layout_CustomDialog_TeamPlayer_Age;
    Button Layout_CustomDialog_TeamPlayer_Sex;


    String[][] parsedData;
    String[] jsonName = {"Name", "Birth", "Sex", "Position", "Team", "Profile", "Height", "Weight", "Phone"};
    String NewsFeed_User;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_customdialog_teamplayer);


        Layout_CustomDialog_TeamPlayer_Profile = (ImageView)findViewById(R.id.Layout_CustomDialog_TeamPlayer_Profile);
        Layout_CustomDialog_TeamPlayer_TeamNameAndDuty = (Button)findViewById(R.id.Layout_CustomDialog_TeamPlayer_TeamNameAndDuty);
        Layout_CustomDialog_TeamPlayer_Name = (Button)findViewById(R.id.Layout_CustomDialog_TeamPlayer_Name);
        Layout_CustomDialog_TeamPlayer_Position = (Button)findViewById(R.id.Layout_CustomDialog_TeamPlayer_Position);
        Layout_CustomDialog_TeamPlayer_Age = (Button)findViewById(R.id.Layout_CustomDialog_TeamPlayer_Age);
        Layout_CustomDialog_TeamPlayer_Sex = (Button)findViewById(R.id.Layout_CustomDialog_TeamPlayer_Sex);


        final Intent CommentIntent = getIntent();
        NewsFeed_User = CommentIntent.getExtras().getString("NewsFeed_User");

        String result = "";
        try {
            HttpClient client = new DefaultHttpClient();
            String postURL = "http://210.122.7.195:8080/gg/newsfeed_information_download.jsp";
            HttpPost post = new HttpPost(postURL);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("NewsFeed_User", NewsFeed_User));
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);
            HttpResponse response = client.execute(post);
            BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
            String line = null;
            while ((line = bufreader.readLine()) != null) {
                result += line;
            }
            parsedData = jsonParserList(result);
            setData();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String[][] jsonParserList(String pRecvServerPage) {
        Log.i("댓글에서 받은 전체 내용", pRecvServerPage);
        try {

            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");

            parsedData = new String[jArr.length()][jsonName.length];
            for (int i = 0; i < jArr.length(); i++) {
                json = jArr.getJSONObject(i);
                for (int j = 0; j < jsonName.length; j++) {
                    parsedData[i][j] = json.getString(jsonName[j]);
                }
            }
            return parsedData;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }



    private void setData() {
        InformationComment = new ArrayList<User_Information_Setting>();
        for (int a = 0; a < parsedData.length; a++) {
            InformationComment.add(new User_Information_Setting(parsedData[a][0], parsedData[a][1], parsedData[a][2], parsedData[a][3], parsedData[a][4], parsedData[a][5], parsedData[a][6], parsedData[a][7], parsedData[a][8]));
        }
    }
}
