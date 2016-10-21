package com.mysports.basketbook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mysports.basketbook.R;

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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by park on 2016-06-16.
 */
public class Navigation_TeamIntro_Focus extends AppCompatActivity {
    ImageView TeamIntro_Foucs_ImageView_Emblem;
    Button TeamIntro_Foucs_Button_UniformTop, TeamIntro_Foucs_Button_Join;
    Button TeamIntro_Foucs_Button_TeamName,TeamIntro_Foucs_Button_TeamAddress,TeamIntro_Foucs_Button_HomeCourt,TeamIntro_Foucs_Button_Time;
    TextView TeamIntro_Foucs_TextView_TeamIntro,TeamIntro_Foucs_TextView_AgeAndHeight;
    ImageView TeamIntro_ImageView_Image1,TeamIntro_ImageView_Image2,TeamIntro_ImageView_Image3;
    ListView TeamIntro_Foucs_ImageView_TeamPlayer;
    ScrollView Teamintro_scrollView;
    static String TeamName, Emblem, Id;
    String[][] parsedData,parsedData_Player;
    String ImageUrl1="",ImageUrl2="",ImageUrl3="",ImageUrl4="";
    Bitmap bmImg;
    Navigation_TeamIntro_Focus_Player_MyAdapter navigation_TeamIntro_Focus_Player_MyAdapter;
    ArrayList<Navigation_TeamIntro_Focus_Player_MyData> navigation_TeamIntro_Focus_Player_MyData;
    int TotalPeople, TotalAge=0;
    String str_AvgAge="";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_navigation_teamintro_focus);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        TeamIntro_Foucs_ImageView_Emblem = (ImageView)findViewById(R.id.TeamIntro_Foucs_ImageView_Emblem);
        TeamIntro_Foucs_Button_UniformTop = (Button)findViewById(R.id.TeamIntro_Foucs_Button_UniformTop);
        TeamIntro_Foucs_Button_Join = (Button)findViewById(R.id.TeamIntro_Foucs_Button_Join);
        TeamIntro_Foucs_Button_TeamName= (Button)findViewById(R.id.TeamIntro_Foucs_TextView_TeamName);
        TeamIntro_Foucs_Button_TeamAddress = (Button)findViewById(R.id.TeamIntro_Foucs_TextView_TeamAddress);
        TeamIntro_Foucs_Button_HomeCourt = (Button)findViewById(R.id.TeamIntro_Foucs_TextView_HomeCourt);
        TeamIntro_Foucs_Button_Time = (Button)findViewById(R.id.TeamIntro_Foucs_TextView_Time);
        TeamIntro_Foucs_TextView_TeamIntro = (TextView) findViewById(R.id.TeamIntro_Foucs_TextView_TeamIntro);
        TeamIntro_ImageView_Image1 = (ImageView)findViewById(R.id.TeamIntro_ImageView_Image1);
        TeamIntro_ImageView_Image2 = (ImageView)findViewById(R.id.TeamIntro_ImageView_Image2);
        TeamIntro_ImageView_Image3 = (ImageView)findViewById(R.id.TeamIntro_ImageView_Image3);
        TeamIntro_Foucs_ImageView_TeamPlayer = (ListView)findViewById(R.id.TeamIntro_Foucs_ImageView_TeamPlayer);
        Teamintro_scrollView = (ScrollView)findViewById(R.id.Teamintro_scrollView);
        TeamIntro_Foucs_TextView_AgeAndHeight = (TextView)findViewById(R.id.TeamIntro_Foucs_TextView_AgeAndHeight);

        Intent intent1 = getIntent();
        TeamName = intent1.getStringExtra("TeamName");
        Id = intent1.getStringExtra("Id");
        String result="";
        try {
            HttpClient client = new DefaultHttpClient();
            String postURL = "http://210.122.7.195:8080/Web_basket/Navi_TeamIntro_Focus.jsp";
            HttpPost post = new HttpPost(postURL);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("TeamName", TeamName));

            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);

            HttpResponse response = client.execute(post);
            BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));

            String line = null;
            while ((line = bufreader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        parsedData = jsonParserList(result);
        TeamName = parsedData[0][0];
        String TeamAddress_do = parsedData[0][1];
        String TeamAddress_se = parsedData[0][2];
        String HomeCourt = parsedData[0][3];
        String TeamIntro = parsedData[0][4];
        String UniformTop = parsedData[0][5];
        String UniformBottom = parsedData[0][6];
        String Image1 = parsedData[0][7];
        String Image2 = parsedData[0][8];
        String Image3 = parsedData[0][9];
        Emblem = parsedData[0][10];
        String Time = parsedData[0][11];

        TeamIntro_Foucs_Button_TeamName.setText(TeamName);
        TeamIntro_Foucs_Button_TeamAddress.setText(TeamAddress_do+TeamAddress_se);
        TeamIntro_Foucs_Button_HomeCourt.setText(HomeCourt);
        TeamIntro_Foucs_Button_Time.setText(Time);
        TeamIntro_Foucs_TextView_TeamIntro.setText(TeamIntro);

        try{
            String En_Image1 = URLEncoder.encode(Image1, "utf-8");
            String En_Image2 = URLEncoder.encode(Image2, "utf-8");
            String En_Image3 = URLEncoder.encode(Image3, "utf-8");
            String En_Emblem = URLEncoder.encode(Emblem, "utf-8");
            if(Image1.equals("."))
            {
                TeamIntro_ImageView_Image1.setVisibility(View.GONE);
            }
            else{
                Glide.with(Navigation_TeamIntro_Focus.this).load("http://210.122.7.195:8080/Web_basket/imgs/Team/" + En_Image1 + ".jpg").diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(TeamIntro_ImageView_Image1);
            }
            if(Image2.equals("."))
            {
                TeamIntro_ImageView_Image2.setVisibility(View.GONE);
            }
            else{
                Glide.with(Navigation_TeamIntro_Focus.this).load("http://210.122.7.195:8080/Web_basket/imgs/Team/" + En_Image2 + ".jpg").diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(TeamIntro_ImageView_Image2);
            }
            if(Image3.equals("."))
            {
                TeamIntro_ImageView_Image3.setVisibility(View.GONE);
            }
            else{
                Glide.with(Navigation_TeamIntro_Focus.this).load("http://210.122.7.195:8080/Web_basket/imgs/Team/" + En_Image3 + ".jpg").diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(TeamIntro_ImageView_Image3);
            }
            if(Emblem.equals("."))
            {
                Glide.with(Navigation_TeamIntro_Focus.this).load(R.drawable.emblem).bitmapTransform(new CropCircleTransformation(Glide.get(Navigation_TeamIntro_Focus.this).getBitmapPool()))
                        .into(TeamIntro_Foucs_ImageView_Emblem);
            }
            else{
                Glide.with(Navigation_TeamIntro_Focus.this).load("http://210.122.7.195:8080/Web_basket/imgs/Emblem/" + En_Emblem + ".jpg").bitmapTransform(new CropCircleTransformation(Glide.get(Navigation_TeamIntro_Focus.this).getBitmapPool())).diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(TeamIntro_Foucs_ImageView_Emblem);
            }
        }
        catch (UnsupportedEncodingException e)
        {

        }
        int UniformTop_color = Integer.parseInt(UniformTop);
//        int UniformBottom_color = Integer.parseInt(UniformBottom);
        Navigation_TeamIntro_Focus.this.findViewById(R.id.TeamIntro_Foucs_Button_UniformTop).setBackgroundColor(UniformTop_color);
      //  Navigation_TeamIntro_Focus.this.findViewById(R.id.TeamIntro_Foucs_Button_UniformBottom).setBackgroundColor(UniformBottom_color);

        TeamIntro_Foucs_Button_Join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result="";
                try {
                    HttpClient client = new DefaultHttpClient();
                    String postURL = "http://210.122.7.195:8080/Web_basket/Navi_TeamIntro_Focus_OverLap.jsp";
                    HttpPost post = new HttpPost(postURL);

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("Id", Id));

                    UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                    post.setEntity(ent);

                    HttpResponse response = client.execute(post);
                    BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));

                    String line = null;
                    while ((line = bufreader.readLine()) != null) {
                        result += line;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                parsedData = jsonParserList_IdCheck(result);
                if(parsedData[0][0].equals("overLap")){
                    Snackbar.make(v,"이미 다른 팀에 가입 중 이십니다.", Snackbar.LENGTH_SHORT).show();
                }else{
                    String result_join="";
                    try {
                        HttpClient client = new DefaultHttpClient();
                        String postURL = "http://210.122.7.195:8080/Web_basket/Navi_TeamIntro_Focus_Join.jsp";
                        HttpPost post = new HttpPost(postURL);

                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("Id", Id));
                        params.add(new BasicNameValuePair("TeamName", TeamName));
                        params.add(new BasicNameValuePair("Introduce_msg", "test"));

                        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                        post.setEntity(ent);

                        HttpResponse response = client.execute(post);
                        BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));

                        String line = null;
                        while ((line = bufreader.readLine()) != null) {
                            result_join += line;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    parsedData = jsonParserList_IdCheck(result_join);
                    if(parsedData[0][0].equals("succed")){
                        Snackbar.make(v, "가입 신청 완료", Snackbar.LENGTH_SHORT).show();
                        finish();
                    }
                    else if(parsedData[0][0].equals("overlap")){
                        Snackbar.make(v, "해당 팀에 이미 신청중입니다", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });
        String result_Player="";
        try {
            HttpClient client = new DefaultHttpClient();
            String postURL = "http://210.122.7.195:8080/Web_basket/NaviTeamInfo_Player.jsp";
            HttpPost post = new HttpPost(postURL);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("TeamName", TeamName));

            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);

            HttpResponse response = client.execute(post);
            BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));

            String line = null;
            while ((line = bufreader.readLine()) != null) {
                result_Player += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        parsedData_Player = jsonParserList_Player(result_Player);
        setData_Player();
        navigation_TeamIntro_Focus_Player_MyAdapter = new Navigation_TeamIntro_Focus_Player_MyAdapter(Navigation_TeamIntro_Focus.this, navigation_TeamIntro_Focus_Player_MyData);
        //리스트뷰에 어댑터 연결
        TeamIntro_Foucs_ImageView_TeamPlayer.setAdapter(navigation_TeamIntro_Focus_Player_MyAdapter);
        TeamIntro_Foucs_ImageView_TeamPlayer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Teamintro_scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        TeamIntro_Foucs_TextView_AgeAndHeight.setText( setAgeAndHeight_Avg());
        //이미지 포커스
        TeamIntro_ImageView_Image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Navigation_TeamIntro_Focus.this, Navigation_TeamIntro_Focus_ImageFocus.class);
                intent.putExtra("ImageUrl1",ImageUrl1);
                intent.putExtra("ImageUrl2",ImageUrl2);
                intent.putExtra("ImageUrl3",ImageUrl3);
                intent.putExtra("Choice", "0");
                startActivity(intent);
                finish();
            }
        });
    }
    /////대회 탭  받아온 json 파싱합니다.//////////////////////////////////////////////////////////
    public String[][] jsonParserList(String pRecvServerPage){
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try{
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");

            String[] jsonName = {"msg1","msg2","msg3","msg4","msg5","msg6","msg7","msg8","msg9","msg10","msg11","msg12"};
            String[][] parseredData = new String[jArr.length()][jsonName.length];
            for(int i = 0; i<jArr.length();i++){
                json = jArr.getJSONObject(i);
                for (int j=0;j<jsonName.length; j++){
                    parseredData[i][j] = json.getString(jsonName[j]);
                }
            }
            return parseredData;
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }
    /////대회 탭  받아온 json 파싱합니다.//////////////////////////////////////////////////////////
    public String[][] jsonParserList_IdCheck(String pRecvServerPage){
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try{
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");

            String[] jsonName = {"msg1"};
            String[][] parseredData = new String[jArr.length()][jsonName.length];
            for(int i = 0; i<jArr.length();i++){
                json = jArr.getJSONObject(i);
                for (int j=0;j<jsonName.length; j++){
                    parseredData[i][j] = json.getString(jsonName[j]);
                }
            }
            return parseredData;
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }
    public String[][] jsonParserList_Player(String pRecvServerPage){
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try{
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List2");

            String[] jsonName = {"msg1","msg2","msg3","msg4", "msg5", "msg6"};
            String[][] parseredData = new String[jArr.length()][jsonName.length];
            for(int i = 0; i<jArr.length();i++){
                json = jArr.getJSONObject(i);
                for (int j=0;j<jsonName.length; j++){
                    parseredData[i][j] = json.getString(jsonName[j]);
                }
            }
            return parseredData;
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }
    private void setData_Player()
    {
        navigation_TeamIntro_Focus_Player_MyData = new ArrayList<Navigation_TeamIntro_Focus_Player_MyData>();
        for(int i =0; i<parsedData_Player.length; i++)
        {
            navigation_TeamIntro_Focus_Player_MyData.add(new Navigation_TeamIntro_Focus_Player_MyData(parsedData_Player[i][0],parsedData_Player[i][1],parsedData_Player[i][2],parsedData_Player[i][3],parsedData_Player[i][4],parsedData_Player[i][5],TeamName));
        }
    }
    private String setAgeAndHeight_Avg(){
        for(int i=0; i<parsedData_Player.length;i++){
            TotalAge = TotalAge + Integer.parseInt(ChangeAge(parsedData_Player[i][3]));
        }
        str_AvgAge = "평균나이"+Integer.toString(TotalAge/parsedData_Player.length);
        return str_AvgAge;
    }
    //date 입력받아 나이 구하는 함수
    public String ChangeAge(String Age){
        Calendar cal= Calendar.getInstance ();
        String[] str = new String(Age).split(" \\/ ");
        String[] str_day = new String(str[2]).split(" ");
        Log.i("test1", str[0]);
        Log.i("test1", str[1]);
        Log.i("test1", str_day[0]);
        int year = Integer.parseInt(str[0]);
        int month = Integer.parseInt(str[1]);
        int day = Integer.parseInt(str_day[0]);

        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month-1);
        cal.set(Calendar.DATE, day);

        Calendar now = Calendar.getInstance ();

        int age = now.get(Calendar.YEAR) - cal.get(Calendar.YEAR);
        if (  (cal.get(Calendar.MONTH) > now.get(Calendar.MONTH))
                || (    cal.get(Calendar.MONTH) == now.get(Calendar.MONTH)
                && cal.get(Calendar.DAY_OF_MONTH) > now.get(Calendar.DAY_OF_MONTH)   )
                ){
            age--;
        }
        String Str_age = Integer.toString(age);
        return Str_age;
    }
}
