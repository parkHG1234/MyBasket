package com.example.mybasket;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
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
import org.w3c.dom.Text;

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

    static String id, pw, user_type;
    RadioGroup join_sex_Radio, join_posi_Radio;
    EditText join_name_EditText,join_layout_weight_editText,join_layout_height_editText;
    String[][] parsedData;

    TextView tvBirth;

    Spinner s_Sex;
    Spinner s_Position;

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
        user_type = intentGet.getStringExtra("user_type");

        final String birthTemp = "";
        String posiTemp = "";


        GregorianCalendar calendar = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        tvBirth = (TextView) findViewById(R.id.join_layout_birth_tv);

        s_Sex = (Spinner)findViewById(R.id.spinner_sex);
        s_Position = (Spinner)findViewById(R.id.spinner_position);

        findViewById(R.id.join_layout_birth_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(JoinAddActivity.this, dateSetListener, year, month, day).show();
            }
        });




        s_Sex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if(position==0) {

                }else if(position==1) {

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        s_Position.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if(position==0) {

                }else if(position==1) {

                }else if(position==2) {

                }else if(position==3) {

                }else if(position==4) {

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        join_name_EditText = (EditText) findViewById(R.id.join_layout_name_editText);
        join_sex_Radio = (RadioGroup) this.findViewById(R.id.sex_radio);
        join_posi_Radio = (RadioGroup) this.findViewById(R.id.position_radio);
        join_layout_weight_editText= (EditText)findViewById(R.id.join_layout_weight_editText);
        join_layout_height_editText =(EditText)findViewById(R.id.join_layout_height_editText);

    }


    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            String msg = String.format("%d / %d / %d", year,monthOfYear+1, dayOfMonth);
            tvBirth.setText(msg);

            Toast.makeText(JoinAddActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    };



    public void onClickCommit(View view) {
        String name = join_name_EditText.getText().toString();
        String birth = tvBirth.getText().toString();
        String height = join_layout_height_editText.getText().toString();
        String weight = join_layout_weight_editText.getText().toString();

        String sex = s_Sex.getSelectedItem().toString();
        String posi = s_Position.getSelectedItem().toString();

        Toast.makeText(JoinAddActivity.this, "height = "+height, Toast.LENGTH_LONG).show();
        String result = JoinByHttp(id,pw,user_type,name,sex,birth,posi,weight,height);
        parsedData = jsonParserList(result);

        //joinThread jt = new joinThread();
        //jt.run();

    }



    private String JoinByHttp(String _id, String _pw, String _user_type, String _name, String _sex,String _birth, String _posi, String _weight, String _height) {
        try {
            HttpClient client = new DefaultHttpClient();
            String url = "http://210.122.7.195:8080/pp/Join.jsp";
            HttpPost post = new HttpPost(url);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("_id", _id));
            params.add(new BasicNameValuePair("_pw", _pw));
            params.add(new BasicNameValuePair("user_type", _user_type));
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

    private String[][] jsonParserList(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jarr = json.getJSONArray("List");

            //String[] jsonName = {"login_check","usercode","password","name","sex","email","univ","club" };

            String[] jsonName = {"msg1"};
            String[][] parseredData = new String[jarr.length()][jsonName.length];
            for(int i = 0; i<jarr.length();i++){
                json = jarr.getJSONObject(i);
                for (int j=0;j<jsonName.length; j++){
                    parseredData[i][j] = json.getString(jsonName[j]);
                }
            }
            for(int i=0;i<parseredData.length;i++) {
                Log.i("JSON을 분석한 데이터" + i + ":", parseredData[i][0]);
            }
            return parseredData;
        }catch(JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
