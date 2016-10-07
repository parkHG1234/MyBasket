package com.mysports.basketbook;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.dd.CircularProgressButton;
import com.mysports.basketbook.R;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

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
import java.util.Calendar;
import java.util.List;

/**
 * Created by 박효근 on 2016-07-04.
 */
public class Match_In_Register extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener{
    private String Id="";
    private String[][] parsedData;
    private String Title="";
    private String TeamName="";
    private String TeamAddress_Do = "";
    private String TeamAddress_Se = "";
    private String BookDay="";
    private String StartTime="";
    private String EndTime="";
    private String Consideration="";
    private String FreeParking="";
    private String PaidParking="";
    private String NoParking="";
    private String Shower="";
    private String Display="";
    private String HeatingAndCooling="";
    private String Phone="";
    private String AddressFocus="";

    private MaterialEditText Match_In_Register_EditText_Title;
    private MaterialEditText Match_In_Register_EditText_Consideration;
    private MaterialEditText Match_In_Register_EditText_AddressFocus;
    private Button Match_In_Register_Button_TeamName;
    private Button Match_In_Register_Button_TeamAddress;
    private Button Match_In_Register_Button_Schedule_Date;
    private Button Match_In_Register_Button_Schedule_Time;
    private Button Match_In_Register_Button_Schedule_TimeEnd;
    private CircularProgressButton Match_In_Register_Button_Register;
    private CheckBox Match_In_Register_CheckBox_FreeParking;
    private CheckBox Match_In_Register_CheckBox_PaidParking;
    private CheckBox Match_In_Register_CheckBox_NoParking;
    private CheckBox Match_In_Register_CheckBox_Shower;
    private CheckBox Match_In_Register_CheckBox_Display;
    private CheckBox Match_In_Register_CheckBox_HeatingAndCooling;
    private EditText Match_In_Register_EditText_Phone;

    static String Time_Start="off",Time_End="off";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_match_in_register);


        Match_In_Register_EditText_Title = (MaterialEditText)findViewById(R.id.Match_In_Register_EditText_Title);
        Match_In_Register_EditText_Consideration = (MaterialEditText)findViewById(R.id.Match_In_Register_EditText_Consideration);
        Match_In_Register_Button_TeamName = (Button)findViewById(R.id.Match_In_Register_Button_TeamName);
        Match_In_Register_Button_TeamAddress = (Button)findViewById(R.id.Match_In_Register_Button_TeamAddress);
        Match_In_Register_EditText_AddressFocus =(MaterialEditText)findViewById(R.id.Match_In_Register_EditText_AddressFocus);
        Match_In_Register_Button_Schedule_Date = (Button)findViewById(R.id.Match_In_Register_Button_Schedule_Date);
        Match_In_Register_Button_Schedule_Time = (Button)findViewById(R.id.Match_In_Register_Button_Schedule_Time);
        Match_In_Register_Button_Schedule_TimeEnd = (Button)findViewById(R.id.Match_In_Register_Button_Schedule_TimeEnd);
        Match_In_Register_Button_Register = (CircularProgressButton)findViewById(R.id.Match_In_Register_Button_Register);
        Match_In_Register_CheckBox_FreeParking = (CheckBox)findViewById(R.id.Match_In_Register_CheckBox_FreeParking);
        Match_In_Register_CheckBox_PaidParking = (CheckBox)findViewById(R.id.Match_In_Register_CheckBox_PaidParking);
        Match_In_Register_CheckBox_NoParking = (CheckBox)findViewById(R.id.Match_In_Register_CheckBox_NoParking);
        Match_In_Register_CheckBox_Shower = (CheckBox)findViewById(R.id.Match_In_Register_CheckBox_Shower);
        Match_In_Register_CheckBox_Display = (CheckBox)findViewById(R.id.Match_In_Register_CheckBox_Display);
        Match_In_Register_CheckBox_HeatingAndCooling = (CheckBox)findViewById(R.id.Match_In_Register_CheckBox_HeatingAndCooling);
        Match_In_Register_EditText_Phone = (EditText)findViewById(R.id.Match_In_Register_EditText_Phone);

        Intent intent1 = getIntent();
        Id = intent1.getStringExtra("Id");
        //팀명과 팀주소를 받아온다.
        String result="";
        try {
            HttpClient client = new DefaultHttpClient();
            String postURL = "http://210.122.7.195:8080/Web_basket/Match_In_Register_TeamInfo.jsp";
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
        parsedData = jsonParserList(result);
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //현재 시간을 받아온다.
        Calendar calendar = Calendar.getInstance();
        int CurrentYear = calendar.get(calendar.YEAR);
        int CurrentMonth = calendar.get(calendar.MONTH);
        int CurrentDate = calendar.get(calendar.DATE);
        int Currenthour = calendar.get ( calendar.HOUR_OF_DAY ) ;
        int CurrentMin = calendar.get ( calendar.MINUTE );

        TeamName = parsedData[0][0];
        TeamAddress_Do = parsedData[0][1];
        TeamAddress_Se = parsedData[0][2];
        BookDay = CurrentYear+ " - " + CurrentMonth + " - "+CurrentDate;
        StartTime = Currenthour+ " : " + CurrentMin;
        EndTime = (Currenthour+3)+ " : " + CurrentMin;

        Match_In_Register_Button_TeamName.setText(TeamName);
        Match_In_Register_Button_TeamAddress.setText(TeamAddress_Do+" "+TeamAddress_Se);
        Match_In_Register_Button_Schedule_Date.setText(BookDay);
        Match_In_Register_Button_Schedule_Time.setText(StartTime);
        Match_In_Register_Button_Schedule_TimeEnd.setText(EndTime);
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //운동일정 버튼 클릭 이벤트 -> Calender 다이얼로그 창 띄움
        Match_In_Register_Button_Schedule_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        Match_In_Register.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
        Match_In_Register_Button_Schedule_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Time_Start = "on";
                Time_End="off";
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        Match_In_Register.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                );
                tpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
        Match_In_Register_Button_Schedule_TimeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Time_Start = "off";
                Time_End="on";
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        Match_In_Register.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                );
                tpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
        Match_In_Register_Button_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Match_In_Register_Button_Register.setProgress(1);
                Title = Match_In_Register_EditText_Title.getText().toString();
                Consideration = Match_In_Register_EditText_Consideration.getText().toString();
                FreeParking = String.valueOf(Match_In_Register_CheckBox_FreeParking.isChecked());
                PaidParking = String.valueOf(Match_In_Register_CheckBox_PaidParking.isChecked());
                NoParking = String.valueOf(Match_In_Register_CheckBox_NoParking.isChecked());
                Shower = String.valueOf(Match_In_Register_CheckBox_Shower.isChecked());
                Display = String.valueOf(Match_In_Register_CheckBox_Display.isChecked());
                HeatingAndCooling = String.valueOf(Match_In_Register_CheckBox_HeatingAndCooling.isChecked());
                Phone =Match_In_Register_EditText_Phone.getText().toString();
                AddressFocus = Match_In_Register_EditText_AddressFocus.getText().toString();
                Match_In_Register_Button_Register.setProgress(25);
                String result="";
                try {
                    HttpClient client = new DefaultHttpClient();
                    String postURL = "http://210.122.7.195:8080/Web_basket/Match_In_Register_Register.jsp";
                    HttpPost post = new HttpPost(postURL);

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("Title", Title));
                    params.add(new BasicNameValuePair("Consideration", Consideration));
                    params.add(new BasicNameValuePair("TeamName", TeamName));
                    params.add(new BasicNameValuePair("TeamAddress_Do", TeamAddress_Do));
                    params.add(new BasicNameValuePair("TeamAddress_Se", TeamAddress_Se));
                    params.add(new BasicNameValuePair("BookDay", BookDay));
                    params.add(new BasicNameValuePair("BookTime", StartTime));
                    params.add(new BasicNameValuePair("BookTimeEnd", EndTime));
                    params.add(new BasicNameValuePair("FreeParking", FreeParking));
                    params.add(new BasicNameValuePair("PaidParking", PaidParking));
                    params.add(new BasicNameValuePair("NoParking", NoParking));
                    params.add(new BasicNameValuePair("Shower", Shower));
                    params.add(new BasicNameValuePair("Display", Display));
                    params.add(new BasicNameValuePair("HeatingAndCooling", HeatingAndCooling));
                    params.add(new BasicNameValuePair("Id", Id));
                    params.add(new BasicNameValuePair("NewsFeed_Month", new SimpleDateFormat("MM").format(new java.sql.Date(System.currentTimeMillis()))));
                    params.add(new BasicNameValuePair("NewsFeed_Day", new SimpleDateFormat("dd").format(new java.sql.Date(System.currentTimeMillis()))));
                    params.add(new BasicNameValuePair("NewsFeed_Hour", new SimpleDateFormat("kk").format(new java.sql.Date(System.currentTimeMillis()))));
                    params.add(new BasicNameValuePair("NewsFeed_Minute", new SimpleDateFormat("mm").format(new java.sql.Date(System.currentTimeMillis()))));
                    params.add(new BasicNameValuePair("Phone", Phone));
                    params.add(new BasicNameValuePair("AddressFocus", AddressFocus));

                    UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                    post.setEntity(ent);
                    Match_In_Register_Button_Register.setProgress(50);
                    HttpResponse response = client.execute(post);
                    BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));

                    String line = null;
                    while ((line = bufreader.readLine()) != null) {
                        result += line;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Match_In_Register_Button_Register.setProgress(70);
                parsedData = jsonParserList_Register(result);

                if(parsedData[0][0].equals("succed")){
                    Match_In_Register_Button_Register.setProgress(100);
                    new Handler().postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            finish();
                        }
                    }, 500);// 1초 정도 딜레이를 준 후 시작
                }
                else{
                    Match_In_Register_Button_Register.setProgress(-1);
                }
            }
        });
    }
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        BookDay = year+ " - " + monthOfYear + " - "+dayOfMonth;
        Match_In_Register_Button_Schedule_Date.setText(BookDay);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        if(Time_Start.equals("on")){
            StartTime = hourString+ " : " + minuteString;
            Match_In_Register_Button_Schedule_Time.setText(StartTime);
        }
        if(Time_End.equals("on")){
            EndTime = hourString+ " : " + minuteString;
            Match_In_Register_Button_Schedule_TimeEnd.setText(EndTime);
        }

    }
    /////네비 탭 - 매 팀정보 : 받아온 json 파싱합니다.//////////////////////////////////////////////////////////
    public String[][] jsonParserList(String pRecvServerPage){
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try{
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List1");

            String[] jsonName = {"msg1","msg2","msg3"};
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
    public String[][] jsonParserList_Register(String pRecvServerPage){
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
}
