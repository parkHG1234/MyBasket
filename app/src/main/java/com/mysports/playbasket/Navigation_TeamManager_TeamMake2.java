package com.mysports.playbasket;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


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
import org.xdty.preference.colorpicker.ColorPickerDialog;
import org.xdty.preference.colorpicker.ColorPickerSwatch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by park on 2016-04-05.
 */
public class Navigation_TeamManager_TeamMake2 extends Activity implements
        Navigation_TeamManager_TeamMake_ColorPicker.OnColorChangedListener{
    static String Id="";
    static String TeamName="";
    static String TeamAddress_do="";
    static String TeamAddress_se="";
    static String HomeCourt="";
    static String Time="";
    static String TeamIntro="";
    static String UniformTop = "";
    static String UniformBottom = "";
    private int mSelectedColor;
    boolean Top_Color, Bottom_Color;
    ColorPickerDialog dialog;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_teammanager_teammake2);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Intent intent1 = getIntent();
        TeamName = intent1.getStringExtra("TeamName");
        TeamAddress_do = intent1.getStringExtra("TeamAddress_do");
        TeamAddress_se = intent1.getStringExtra("TeamAddress_se");
        HomeCourt = intent1.getStringExtra("HomeCourt");
        Time = intent1.getStringExtra("Time");
        TeamIntro = intent1.getStringExtra("TeamIntro");
        Id = intent1.getStringExtra("Id");


        mSelectedColor = ContextCompat.getColor(this, R.color.flamingo);
        int[] mColors = getResources().getIntArray(R.array.default_rainbow);
        dialog = ColorPickerDialog.newInstance(R.string.color_picker_default_title,
                mColors,
                mSelectedColor,
                5, // Number of columns
                ColorPickerDialog.SIZE_SMALL);

        dialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {

            @Override
            public void onColorSelected(int color) {
                mSelectedColor = color;
                Navigation_TeamManager_TeamMake2.this.findViewById(R.id.TeamManager__TeamMake_Button_Top).setBackgroundColor(mSelectedColor);
                UniformTop = Integer.toString(mSelectedColor);
            }

        });
        this.activity = this;
        final Button TeamManager__TeamMake_Button_Top = (Button)findViewById(R.id.TeamManager__TeamMake_Button_Top);
        TeamManager__TeamMake_Button_Top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Top_Color = true;
                Bottom_Color = false;
               // getColor(view);
                dialog.show(getFragmentManager(), "color_dialog_test");
            }
        });
        final Button TeamManager__TeamMake_Button_Bottom = (Button)findViewById(R.id.TeamManager__TeamMake_Button_Bottom);
        TeamManager__TeamMake_Button_Bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Top_Color = false;
                Bottom_Color = true;
                getColor(view);
            }
        });
        final Button TeamManager__TeamMake_Button_Next = (Button)findViewById(R.id.TeamManager__TeamMake_Button_Next);
        TeamManager__TeamMake_Button_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    HttpClient client = new DefaultHttpClient();
                    String postURL = "http://210.122.7.195:8080/Web_basket/TeamMake.jsp";
                    HttpPost post = new HttpPost(postURL);

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("Id", Id));
                    params.add(new BasicNameValuePair("TeamName", TeamName));
                    params.add(new BasicNameValuePair("TeamAddress_do", TeamAddress_do));
                    params.add(new BasicNameValuePair("TeamAddress_se", TeamAddress_se));
                    params.add(new BasicNameValuePair("HomeCourt", HomeCourt));
                    params.add(new BasicNameValuePair("Time", Time));
                    params.add(new BasicNameValuePair("TeamIntro", TeamIntro));
                    params.add(new BasicNameValuePair("UniformTop", UniformTop));
                    params.add(new BasicNameValuePair("UniformBottom", "no"));

                    UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                    post.setEntity(ent);

                    HttpResponse response = client.execute(post);
                    BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));

                    String line = null;
                    String result = "";
                    while ((line = bufreader.readLine()) != null) {
                        result += line;
                    }
                    String[][] parsedData = jsonParserList(result);
                    if (parsedData[0][0].equals("succed")) {
                        SharedPreferences prefs_login = getSharedPreferences("basketball_user", MODE_PRIVATE);
                        SharedPreferences.Editor prefs_user= prefs_login.edit();
                        prefs_user.putString("Team", TeamName);
                        prefs_user.commit();
                        Intent intent2 = new Intent(getApplicationContext(),Navigation_TeamManager_TeamMake3.class);
                        startActivity(intent2);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    public String[][] jsonParserList(String pRecvServerPage){
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
    @Override
    public void colorChanged(String str,int color) {
        if(Top_Color == true)
        {
            Navigation_TeamManager_TeamMake2.this.findViewById(R.id.TeamManager__TeamMake_Button_Top).setBackgroundColor(color);
            UniformTop = Integer.toString(color);
        }
        if(Bottom_Color == true)
        {
            Navigation_TeamManager_TeamMake2.this.findViewById(R.id.TeamManager__TeamMake_Button_Bottom).setBackgroundColor(color);
            UniformBottom = Integer.toString(color);
        }
    }

    Activity activity;

    public void getColor(View v) {
        new Navigation_TeamManager_TeamMake_ColorPicker(activity, this, "", Color.BLACK, Color.WHITE).show();
    }

}
