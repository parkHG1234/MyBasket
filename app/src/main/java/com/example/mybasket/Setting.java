package com.example.mybasket;

/**
 * Created by 박효근 on 2016-07-22.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.rengwuxian.materialedittext.MaterialEditText;

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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by 박지훈 on 2016-06-21.
 */
public class Setting extends Activity {

    String Id;
    String Alarm;
    String Token;
    ImageView Setting_ImageView;
    Button Setting_Button_notice;
    Button Setting_Button_recommend;
    Button Setting_Button_Alarm;
    LinearLayout Setting_LinearLayout_Alarm;Switch Setting_Switch_Alarm;String Setting_Choice="close";
    Button Setting_Button_DropOut;


    ExpandableListView notice_ListView;
    MaterialEditText recommend_EditText;
    Button recommend_Button;
    boolean flag=true;

    private ArrayList<String> mGroupList = null;
    private ArrayList<ArrayList<String>> mChildList = null;
    private ArrayList<String> mChildListContent = null;
    Setting_Notice_Adapter noticeAdapter;
    ArrayList<Setting_Notice_Setting> arrData;
    String[][] parsedData,parsedData_Alarm, parsedData_BasicSetting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting);
        Intent intent1 = getIntent();
        Id = intent1.getStringExtra("Id");
        Token = intent1.getStringExtra("Token");

        Setting_ImageView = (ImageView)findViewById(R.id.Setting_ImageView);
        Setting_Button_notice = (Button) findViewById(R.id.Setting_Button_notice);
        notice_ListView =(ExpandableListView) findViewById(R.id.notice_ListView);
        Setting_Button_recommend = (Button) findViewById(R.id.Setting_Button_recommend);
        recommend_EditText = (MaterialEditText)findViewById(R.id.recommend_EditText);
        recommend_Button = (Button)findViewById(R.id.recommend_Button);
        Setting_Button_Alarm = (Button)findViewById(R.id.Setting_Button_Alarm);
        Setting_LinearLayout_Alarm = (LinearLayout)findViewById(R.id.Setting_LinearLayout_Alarm);
        Setting_Switch_Alarm = (Switch)findViewById(R.id.Setting_Switch_Alarm);

        String result="";
        try {
            HttpClient client = new DefaultHttpClient();
            String postURL = "http://210.122.7.195:8080/Web_basket/Gcm_IdAdd_Alarm.jsp";
            HttpPost post = new HttpPost(postURL);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Id", Id));
            params.add(new BasicNameValuePair("Token", Token));

            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);

            HttpResponse response = client.execute(post);
            BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));

            String line = null;
            while ((line = bufreader.readLine()) != null) {
                result += line;
            }
            Log.i("결과",result);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("실패","실패");
        }
        parsedData_BasicSetting=jsonParserList_BasicSetting(result);
        if(parsedData_BasicSetting[0][0].equals("on")){
            Setting_Switch_Alarm.setChecked(true);
        }
        else if(parsedData_BasicSetting[0][0].equals("off")){
            Setting_Switch_Alarm.setChecked(false);
        }
        Setting_ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Setting_Button_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = "";

                if(flag) {
                    notice_ListView.setVisibility(View.VISIBLE);
                    notice_ListView.setVisibility(View.VISIBLE);

                    try {
                        HttpClient client = new DefaultHttpClient();
                        String postURL = "http://210.122.7.195:8080/gg/notice_download.jsp";
                        HttpPost post = new HttpPost(postURL);
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
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

                        noticeAdapter = new Setting_Notice_Adapter(Setting.this, arrData);
                        notice_ListView.setAdapter(noticeAdapter);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    flag=false;
                }else{
                    notice_ListView.setVisibility(View.GONE);
                    notice_ListView.setVisibility(View.GONE);
                    flag=true;
                }
            }
        });

        Setting_Button_recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(flag) {
                    recommend_EditText.setVisibility(View.VISIBLE);
                    recommend_Button.setVisibility(View.VISIBLE);
                    flag=false;
                }else{
                    recommend_EditText.setVisibility(View.GONE);
                    recommend_Button.setVisibility(View.GONE);
                    flag=true;
                }
            }
        });
        recommend_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recommend_EditText.setVisibility(View.GONE);
                recommend_Button.setVisibility(View.GONE);
                recommend_EditText.setText("");
                flag=true;


                try {
                    HttpClient client = new DefaultHttpClient();
                    String postURL = "http://210.122.7.195:8080/gg/recommend_upload.jsp";
                    HttpPost post = new HttpPost(postURL);
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("recommend", String.valueOf(recommend_EditText.getText())));
                    params.add(new BasicNameValuePair("id", Id));
                    UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                    post.setEntity(ent);
                    HttpResponse response = client.execute(post);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Setting_Button_Alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Setting_Choice.equals("close")){
                    Setting_LinearLayout_Alarm.setVisibility(View.VISIBLE);
                    Setting_Choice = "open";

                }
                else if(Setting_Choice.equals("open")){
                    Setting_LinearLayout_Alarm.setVisibility(View.GONE);
                    Setting_Choice = "close";
                }
            }
        });
        Setting_Switch_Alarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked==true)
                {
                    String Alarm_off_result = "";
                    try {
                        HttpClient client = new DefaultHttpClient();
                        String postURL = "http://210.122.7.195:8080/Web_basket/Alarm.jsp";
                        HttpPost post = new HttpPost(postURL);

                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("Id", Id));
                        params.add(new BasicNameValuePair("Alarm", "on"));
                        params.add(new BasicNameValuePair("Token", Token));
                        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                        post.setEntity(ent);

                        HttpResponse response = client.execute(post);
                        BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));

                        String line = null;
                        while ((line = bufreader.readLine()) != null) {
                            Alarm_off_result += line;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    parsedData_Alarm = jsonParserList_Alarm(Alarm_off_result);
                    if(parsedData_Alarm[0][0].equals("succed")){
                        Setting_Switch_Alarm.setChecked(true);
                    }
                }
                else
                {

                    String Alarm_on_result = "";
                    try {
                        HttpClient client = new DefaultHttpClient();
                        String postURL = "http://210.122.7.195:8080/Web_basket/Alarm.jsp";
                        HttpPost post = new HttpPost(postURL);

                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("Id", Id));
                        params.add(new BasicNameValuePair("Alarm", "off"));
                        params.add(new BasicNameValuePair("Token", Token));
                        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                        post.setEntity(ent);

                        HttpResponse response = client.execute(post);
                        BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));

                        String line = null;
                        while ((line = bufreader.readLine()) != null) {
                            Alarm_on_result += line;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    parsedData_Alarm = jsonParserList_Alarm(Alarm_on_result);
                    if(parsedData_Alarm[0][0].equals("succed")){
                        Setting_Switch_Alarm.setChecked(false);
                    }
                }
            }
        });
    }

    private void setData() {
        arrData = new ArrayList<Setting_Notice_Setting>();
        for (int a = 0; a < parsedData.length; a++) {
            arrData.add(new Setting_Notice_Setting(parsedData[a][0],parsedData[a][1],parsedData[a][2]));
        }
    }

    public String[][] jsonParserList(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");

            String[] jsonName = {"notice_num","notice_title","notice_content"};
            String[][] parseredData = new String[jArr.length()][jsonName.length];
            for (int i = 0; i < jArr.length(); i++) {
                json = jArr.getJSONObject(i);
                for (int j = 0; j < jsonName.length; j++) {
                    parseredData[i][j] = json.getString(jsonName[j]);
                }
            }
            return parseredData;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    public String[][] jsonParserList_Alarm(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");

            String[] jsonName = {"msg1"};
            String[][] parseredData = new String[jArr.length()][jsonName.length];
            for (int i = 0; i < jArr.length(); i++) {
                json = jArr.getJSONObject(i);
                for (int j = 0; j < jsonName.length; j++) {
                    parseredData[i][j] = json.getString(jsonName[j]);
                }
            }
            return parseredData;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    public String[][] jsonParserList_BasicSetting(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");

            String[] jsonName = {"msg1"};
            String[][] parseredData = new String[jArr.length()][jsonName.length];
            for (int i = 0; i < jArr.length(); i++) {
                json = jArr.getJSONObject(i);
                for (int j = 0; j < jsonName.length; j++) {
                    parseredData[i][j] = json.getString(jsonName[j]);
                }
            }
            return parseredData;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}