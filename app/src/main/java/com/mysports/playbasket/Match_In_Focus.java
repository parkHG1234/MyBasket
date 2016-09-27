package com.mysports.playbasket;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

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

/**
 * Created by 박효근 on 2016-07-06.
 */
public class Match_In_Focus extends AppCompatActivity {
    private String ScheduleId="";
    private String TeamName="";
    private String Address="";
    private String Date="";
    private String Time = "";
    private String Title = "";
    private String FreeParking="";
    private String PaidParking="";
    private String NoParking="";
    private String Shower="";
    private String Display="";
    private String HeatingAndCooling="";
    private String Consideraion="";
    private String TeamAddress="";
    private String HomeCourt="";
    private String TeamTime="";
    private String TeamIntro="";
    private String Phone="";
    private String AddressFocus="";
    String Image1="";
    String Image2="";
    String Image3="";

    private TextView Match_In_Focus_TextView_Title;
    private TextView Match_In_Focus_TextView_Address;
    private TextView Match_In_Focus_TextView_Date;
    private TextView Match_In_Focus_TextView_Time;

    private CheckBox Match_In_Focus_CheckBox_FreeParking;
    private CheckBox Match_In_Focus_CheckBox_PaidParking;
    private CheckBox Match_In_Focus_CheckBox_NoParking;
    private CheckBox Match_In_Focus_CheckBox_Shower;
    private CheckBox Match_In_Focus_CheckBox_DIsplay;
    private CheckBox Match_In_Focus_CheckBox_HeatingAndCooling;
    private TextView Match_In_Focus_TextView_Consideration;

    private Button Match_In_Focus_Button_TeamName;
    private Button Match_In_Focus_Button_TeamAddress;
    private Button Match_In_Focus_Button_HomeCourt;
    private Button Match_In_Focus_Button_TeamTime;
    private ListView Match_In_Focus_ListView_Player;
    private TextView Match_In_Focus_TextView_TeamAgeAndHeight;
    ImageView Match_In_Focus_ImageVIew_TeamImageView1;
    ImageView Match_In_Focus_ImageVIew_TeamImageView2;
    ImageView Match_In_Focus_ImageVIew_TeamImageView3;
    private TextView Match_In_Focus_TextView_TeamIntroduce;
    private Button Match_In_Focus_Button_Call;
    String[][] parsedData,parsedData_Player;
    Match_In_Focus_Player_MyAdapter match_In_Focus_Player_MyAdapter;
    ArrayList<Match_In_Focus_Player_MyData> match_In_Focus_Player_MyData;
    String result="";
    int playerCount;
    int Height_Total;
    int Height_Avg;
    int Age_avg;
    int Age_Total;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_match_in_focus);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Intent intent1 = getIntent();
        ScheduleId = intent1.getStringExtra("ScheduleId");
        TeamName = intent1.getStringExtra("TeamName");

        Match_In_Focus_TextView_Title = (TextView)findViewById(R.id.Match_In_Focus_TextView_Title);
        Match_In_Focus_TextView_Address = (TextView)findViewById(R.id.Match_In_Focus_TextView_Address);
        Match_In_Focus_TextView_Date = (TextView)findViewById(R.id.Match_In_Focus_TextView_Date);
        Match_In_Focus_TextView_Time = (TextView)findViewById(R.id.Match_In_Focus_TextView_Time);

        Match_In_Focus_CheckBox_FreeParking = (CheckBox)findViewById(R.id.Match_In_Focus_CheckBox_FreeParking);
        Match_In_Focus_CheckBox_PaidParking = (CheckBox)findViewById(R.id.Match_In_Focus_CheckBox_PaidParking);
        Match_In_Focus_CheckBox_NoParking = (CheckBox)findViewById(R.id.Match_In_Focus_CheckBox_NoParking);
        Match_In_Focus_CheckBox_Shower = (CheckBox)findViewById(R.id.Match_In_Focus_CheckBox_Shower);
        Match_In_Focus_CheckBox_DIsplay = (CheckBox)findViewById(R.id.Match_In_Focus_CheckBox_Display);
        Match_In_Focus_CheckBox_HeatingAndCooling = (CheckBox)findViewById(R.id.Match_In_Focus_CheckBox_HeatingAndCooling);
        Match_In_Focus_TextView_Consideration = (TextView)findViewById(R.id.Match_In_Focus_TextView_Consideration);

        Match_In_Focus_Button_TeamName = (Button)findViewById(R.id.Match_In_Focus_Button_TeamName);
        Match_In_Focus_Button_TeamAddress = (Button)findViewById(R.id.Match_In_Focus_Button_TeamAddress);
        Match_In_Focus_Button_HomeCourt = (Button)findViewById(R.id.Match_In_Focus_Button_HomeCourt);
        Match_In_Focus_Button_TeamTime = (Button)findViewById(R.id.Match_In_Focus_Button_TeamTime);
        Match_In_Focus_ListView_Player = (ListView)findViewById(R.id.Match_In_Focus_ListView_Player);
        Match_In_Focus_TextView_TeamAgeAndHeight =(TextView)findViewById(R.id.Match_In_Focus_TextView_TeamAgeAndHeight);
        Match_In_Focus_ImageVIew_TeamImageView1 = (ImageView)findViewById(R.id.Match_In_Focus_ImageVIew_TeamImageView1);
        Match_In_Focus_ImageVIew_TeamImageView2 = (ImageView)findViewById(R.id.Match_In_Focus_ImageVIew_TeamImageView2);
        Match_In_Focus_ImageVIew_TeamImageView3 = (ImageView)findViewById(R.id.Match_In_Focus_ImageVIew_TeamImageView3);
        Match_In_Focus_TextView_TeamIntroduce =(TextView)findViewById(R.id.Match_In_Focus_TextView_TeamIntroduce);

        Match_In_Focus_Button_Call = (Button)findViewById(R.id.Match_In_Focus_Button_Call);
        String result="";
        try {
            HttpClient client = new DefaultHttpClient();
            String postURL = "http://210.122.7.195:8080/Web_basket/Match_In_Focus.jsp";
            HttpPost post = new HttpPost(postURL);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("ScheduleId", ScheduleId));
            params.add(new BasicNameValuePair("TeamName", TeamName));

            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);

            HttpResponse response = client.execute(post);
            BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));

            String line = null;

            while ((line = bufreader.readLine()) != null) {
                result += line;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        parsedData = jsonParserList(result);
        Title = parsedData[0][5];
        Address = parsedData[0][1];
        Time = parsedData[0][2];
        Date = parsedData[0][3];
        FreeParking=parsedData[0][6];
        PaidParking=parsedData[0][7];
        NoParking=parsedData[0][8];
        Shower=parsedData[0][9];
        Display=parsedData[0][10];
        HeatingAndCooling=parsedData[0][11];
        Consideraion = parsedData[0][12];
        TeamAddress = parsedData[0][14];
        HomeCourt = parsedData[0][15];
        TeamTime = parsedData[0][16];
        Image1 = parsedData[0][20];
        Image2 = parsedData[0][21];
        Image3 = parsedData[0][22];
        TeamIntro = parsedData[0][17];
        Phone = parsedData[0][23];
        AddressFocus = parsedData[0][24];
        //리스트 뷰 정보 입력
        Match_In_Focus_TextView_Title.setText(Title);
        Match_In_Focus_TextView_Address.setText(Address + AddressFocus);
        Match_In_Focus_TextView_Date.setText(Time);
        Match_In_Focus_TextView_Time.setText(Date);

        //추가정보 입력
        if(FreeParking.equals("true")){
            Match_In_Focus_CheckBox_FreeParking.setChecked(true);
        }
        if(PaidParking.equals("true")){
            Match_In_Focus_CheckBox_PaidParking.setChecked(true);
        }
        if(NoParking.equals("true")){
            Match_In_Focus_CheckBox_NoParking.setChecked(true);
        }
        if(Shower.equals("true")){
            Match_In_Focus_CheckBox_Shower.setChecked(true);
        }
        if(Display.equals("true")){
            Match_In_Focus_CheckBox_DIsplay.setChecked(true);
        }
        if(HeatingAndCooling.equals("true")){
            Match_In_Focus_CheckBox_HeatingAndCooling.setChecked(true);
        }
        Match_In_Focus_TextView_Consideration.setText(Consideraion);

        //팀소개 정보 입력
        Match_In_Focus_Button_TeamName.setText(TeamName);
        Match_In_Focus_Button_TeamAddress.setText(TeamAddress);
        Match_In_Focus_Button_HomeCourt.setText(HomeCourt);
        Match_In_Focus_Button_TeamTime.setText(TeamTime);
        //팀원 정보 리스트뷰
        String result_Player="";
        try {
            HttpClient client = new DefaultHttpClient();
            String postURL = "http://210.122.7.195:8080/Web_basket/Match_In_Focus_Player.jsp";
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
        match_In_Focus_Player_MyAdapter = new Match_In_Focus_Player_MyAdapter(Match_In_Focus.this, match_In_Focus_Player_MyData);
        //리스트뷰에 어댑터 연결
        Match_In_Focus_ListView_Player.setAdapter(match_In_Focus_Player_MyAdapter);

        for(int i =0 ; i<parsedData_Player.length; i++)
        {
            Age_Total = Age_Total+Integer.parseInt(ChangeAge(parsedData_Player[i][3]));
            Height_Total = Height_Total+Integer.parseInt(parsedData_Player[i][6]);
            playerCount++;
        }
        Height_Avg = Height_Total/playerCount;
        Age_avg = Age_Total/playerCount;
        Match_In_Focus_TextView_TeamAgeAndHeight.setText("평균 나이 "+Integer.toString(Age_avg)+"/ 신장 "+Integer.toString(Height_Avg));

        //팀소개 이미지 업로드
        try {
            String En_Image1 = URLEncoder.encode(Image1, "utf-8");
            String En_Image2 = URLEncoder.encode(Image2, "utf-8");
            String En_Image3 = URLEncoder.encode(Image3, "utf-8");
            if (Image1.equals("")) {
                Match_In_Focus_ImageVIew_TeamImageView1.setVisibility(View.GONE);
            }
            else{
                Glide.with(Match_In_Focus.this).load("http://210.122.7.195:8080/Web_basket/imgs/Team/" + En_Image1 + ".jpg").into(Match_In_Focus_ImageVIew_TeamImageView1);
            }
            if (Image2.equals("")) {
                Match_In_Focus_ImageVIew_TeamImageView2.setVisibility(View.GONE);
            }
            else{
                Glide.with(Match_In_Focus.this).load("http://210.122.7.195:8080/Web_basket/imgs/Team/" + En_Image2 + ".jpg").into(Match_In_Focus_ImageVIew_TeamImageView2);
            }
            if (Image3.equals("")) {
                Match_In_Focus_ImageVIew_TeamImageView3.setVisibility(View.GONE);
            }
            else{
                Glide.with(Match_In_Focus.this).load("http://210.122.7.195:8080/Web_basket/imgs/Team/" + En_Image3 + ".jpg").into(Match_In_Focus_ImageVIew_TeamImageView3);
            }
        } catch (UnsupportedEncodingException e){

        }

        Match_In_Focus_TextView_TeamIntroduce.setText(TeamIntro);

        Match_In_Focus_Button_Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+Phone));
                startActivity(intent);
            }
        });
    }
    public String[][] jsonParserList(String pRecvServerPage){
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try{
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");

            String[] jsonName = {"msg1","msg2","msg3","msg4","msg5","msg6","msg7","msg8","msg9", "msg10","msg11","msg12","msg13","msg14","msg15","msg16","msg17","msg18","msg19", "msg20","msg21","msg22","msg23","msg24","msg25"};
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

            String[] jsonName = {"msg1","msg2","msg3","msg4","msg5","msg6","msg7"};
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
        match_In_Focus_Player_MyData = new ArrayList<Match_In_Focus_Player_MyData>();
        for(int i =0; i<parsedData_Player.length; i++)
        {
            match_In_Focus_Player_MyData.add(new Match_In_Focus_Player_MyData(parsedData_Player[i][0],parsedData_Player[i][1],parsedData_Player[i][2],parsedData_Player[i][3],parsedData_Player[i][4],parsedData_Player[i][5],TeamName));
        }
    }
    public String http(){

        return result;
    }
    //date 입력받아 나이 구하는 함수
    public String ChangeAge(String Age){
        Calendar cal= Calendar.getInstance ();
        String[] str = new String(Age).split(" \\/ ");
        String[] str_day = new String(str[2]).split(" ");
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
