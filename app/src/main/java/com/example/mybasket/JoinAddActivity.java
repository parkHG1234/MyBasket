package com.example.mybasket;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by ldong on 2016-07-05.
 */
public class JoinAddActivity extends Activity{

    static String id, pw;
    RadioGroup join_sex_Radio, join_posi_Radio;
    EditText join_name_EditText, join_bYear_EditText, join_bMonth_EditText, join_bDay_EditText,join_layout_weight_editText,join_layout_height_editText;
    int year, month, day, hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_add_layout);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Intent intentGet = getIntent();
        id = intentGet.getStringExtra("id");
        pw = intentGet.getStringExtra("pw");


        GregorianCalendar calendar = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        join_name_EditText = (EditText) findViewById(R.id.join_layout_name_editText);
        join_sex_Radio = (RadioGroup) this.findViewById(R.id.sex_radio);
        join_posi_Radio = (RadioGroup) this.findViewById(R.id.position_radio);
        join_bYear_EditText = (EditText) findViewById(R.id.join_layout_year_editText);
        join_bMonth_EditText = (EditText) findViewById(R.id.join_layout_month_editText);
        join_bDay_EditText = (EditText) findViewById(R.id.join_layout_day_editText);
        join_layout_weight_editText= (EditText)findViewById(R.id.join_layout_weight_editText);
        join_layout_height_editText =(EditText)findViewById(R.id.join_layout_height_editText);

    }

    public void onClickCommit(View view) {
        String name = join_name_EditText.getText().toString();
        String bYear = join_bYear_EditText.getText().toString();
        String bMonth = join_bMonth_EditText.getText().toString();
        String bDay = join_bDay_EditText.getText().toString();
        String birth = bYear +"/"+bMonth+"/"+bDay;
        String height = join_layout_height_editText.getText().toString();
        String weight = join_layout_weight_editText.getText().toString();

        //라디오 버튼에서 성별값 받아옴
        int sex_id = join_sex_Radio.getCheckedRadioButtonId();
        RadioButton sex_radio = (RadioButton) findViewById(sex_id);
        String sex = sex_radio.getText().toString();

        //라디오 버튼에서 포지션 값 받아옴
        int posi_id = join_posi_Radio.getCheckedRadioButtonId();
        RadioButton posi_radio = (RadioButton) findViewById(posi_id);
        String posi = posi_radio.getText().toString();

        Toast.makeText(JoinAddActivity.this, "id = "+id, Toast.LENGTH_LONG).show();
        JoinByHttp(id,pw,name,sex,birth,posi,weight,height);
        //joinThread jt = new joinThread();
        //jt.run();

    }
        private String JoinByHttp(String _id, String _pw, String _name, String _sex,String _birth, String _posi, String _weight, String _height) {
            try {
                HttpClient client = new DefaultHttpClient();
                String url = "http://210.122.7.195:8080/Web_basket/Join.jsp";
                HttpPost post = new HttpPost(url);

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("_id", _id));
                params.add(new BasicNameValuePair("_pw", _pw));
                params.add(new BasicNameValuePair("_name", _name));
                params.add(new BasicNameValuePair("_sex", _sex));
                params.add(new BasicNameValuePair("_birth", _birth));
                params.add(new BasicNameValuePair("_posi", _posi));
                params.add(new BasicNameValuePair("_weight", _weight));
                params.add(new BasicNameValuePair("_height", _height));;

                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                post.setEntity(ent);

                HttpResponse response = client.execute(post);
                BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));

                String line = null;
                String result = "";

                while ((line = bufreader.readLine()) != null) {
                    result += line;
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }

    }

}
