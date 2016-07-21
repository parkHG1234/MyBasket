package com.example.mybasket;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by park on 2016-04-04.
 */
public class Navigation_TeamManager_TeamMake1 extends AppCompatActivity {
    LinearLayout TeamManager_TeamMake_Layout_Maker;
    LinearLayout TeamManager_TeamMake_Layout_OverLap;
    String Str_TeamManager_TeamMake_EditText_TeamName="";
    String Str_TeamManager_TeamMake_EditText_TeamAddress_do="";
    String Str_TeamManager_TeamMake_EditText_TeamAddress_se="";
    String Str_TeamManager_TeamMake_EditText_HomeCourt;
    String Str_TeamManager_TeamMake_EditText_Time;
    String Str_TeamManager_TeamMake_EditText_TeamIntro;
    MaterialEditText TeamManager_TeamMake_EditText_TeamName;
    MaterialEditText TeamManager_TeamMake_EditText_HomeCourt;
    MaterialEditText TeamManager_TeamMake_EditText_Time;
    EditText TeamManager_TeamMake_EditText_TeamIntro;
    Spinner TeamManager_TeamMake_Spinner_Address_Do;
    Spinner TeamManager_TeamMake_Spinner_Address_Se;
    static String Id="";
    ArrayAdapter<CharSequence> adspin1, adspin2;
    String[][] parsedData;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_teammanager_teammake1);
        TeamManager_TeamMake_EditText_TeamName = (MaterialEditText)findViewById(R.id.TeamManager_TeamMake_EditText_TeamName);
        TeamManager_TeamMake_EditText_HomeCourt = (MaterialEditText)findViewById(R.id.TeamManager_TeamMake_EditText_HomeCourt);
        TeamManager_TeamMake_EditText_Time = (MaterialEditText)findViewById(R.id.TeamManager_TeamMake_EditText_Time);
        TeamManager_TeamMake_EditText_TeamIntro = (EditText)findViewById(R.id.TeamManager_TeamMake_EditText_TeamIntro);
        TeamManager_TeamMake_Spinner_Address_Do = (Spinner)findViewById(R.id.TeamManager_TeamMake_Spinner_Address_Do);
        TeamManager_TeamMake_Spinner_Address_Se = (Spinner)findViewById(R.id.TeamManager_TeamMake_Spinner_Address_Se);
        TeamManager_TeamMake_Layout_Maker = (LinearLayout)findViewById(R.id.TeamManager_TeamMake_Layout_Maker);
        TeamManager_TeamMake_Layout_OverLap = (LinearLayout)findViewById(R.id.TeamManager_TeamMake_Layout_OverLap);
        Button TeamManager_TeamMake1_Button_Next = (Button)findViewById(R.id.TeamManager_TeamMake_Button_Next);

        Intent intent1 = getIntent();
        Id = intent1.getStringExtra("Id");

        //이미 팀 존재하는 지 확인 후 중복 제거
        String result="";
        try {
            HttpClient client = new DefaultHttpClient();
            String postURL = "http://210.122.7.195:8080/Web_basket/TeamMake_OverLap.jsp";
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
        if(parsedData[0][0].equals("overLap"))
        {
            TeamManager_TeamMake_Layout_Maker.setVisibility(View.GONE);
            TeamManager_TeamMake_Layout_OverLap.setVisibility(View.VISIBLE);
        }
        ///////////////////////////////////////////////////////////////////////
        else
        {
            TeamManager_TeamMake_Layout_Maker.setVisibility(View.VISIBLE);
            TeamManager_TeamMake_Layout_OverLap.setVisibility(View.GONE);
            adspin1 = ArrayAdapter.createFromResource(Navigation_TeamManager_TeamMake1.this, R.array.spinner_do, R.layout.zfile_spinner_test);
            adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            TeamManager_TeamMake_Spinner_Address_Do.setAdapter(adspin1);
            TeamManager_TeamMake_Spinner_Address_Do.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if (adspin1.getItem(i).equals("서울")) {
                        Str_TeamManager_TeamMake_EditText_TeamAddress_do = "서울";
                        //두번째 스피너 이벤트
                        adspin2 = ArrayAdapter.createFromResource(Navigation_TeamManager_TeamMake1.this, R.array.spinner_do_seoul, R.layout.zfile_spinner_test);
                        adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        TeamManager_TeamMake_Spinner_Address_Se.setAdapter(adspin2);
                        TeamManager_TeamMake_Spinner_Address_Se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                Str_TeamManager_TeamMake_EditText_TeamAddress_se = adspin2.getItem(i).toString();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });
                    } else if (adspin1.getItem(i).equals("경기도")) {
                        Str_TeamManager_TeamMake_EditText_TeamAddress_do = "경기도";
                        adspin2 = ArrayAdapter.createFromResource(Navigation_TeamManager_TeamMake1.this
                                , R.array.spinner_do_Gyeonggido, R.layout.zfile_spinner_test);
                        adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        TeamManager_TeamMake_Spinner_Address_Se.setAdapter(adspin2);
                        TeamManager_TeamMake_Spinner_Address_Se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                Str_TeamManager_TeamMake_EditText_TeamAddress_se = adspin2.getItem(i).toString();
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            TeamManager_TeamMake1_Button_Next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Str_TeamManager_TeamMake_EditText_TeamName = TeamManager_TeamMake_EditText_TeamName.getText().toString();
                    Str_TeamManager_TeamMake_EditText_HomeCourt = TeamManager_TeamMake_EditText_HomeCourt.getText().toString();
                    Str_TeamManager_TeamMake_EditText_Time = TeamManager_TeamMake_EditText_Time.getText().toString();
                    Str_TeamManager_TeamMake_EditText_TeamIntro = TeamManager_TeamMake_EditText_TeamIntro.getText().toString();
                    if(Str_TeamManager_TeamMake_EditText_TeamName.equals(""))
                    {
                        Toast.makeText(getApplicationContext(),"팀명을 입력해주세요.",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent intent1 = new Intent(getApplicationContext(), Navigation_TeamManager_TeamMake2.class);
                        intent1.putExtra("Id", Id);
                        intent1.putExtra("TeamName", Str_TeamManager_TeamMake_EditText_TeamName);
                        intent1.putExtra("TeamAddress_do", Str_TeamManager_TeamMake_EditText_TeamAddress_do);
                        intent1.putExtra("TeamAddress_se", Str_TeamManager_TeamMake_EditText_TeamAddress_se);
                        intent1.putExtra("HomeCourt", Str_TeamManager_TeamMake_EditText_HomeCourt);
                        intent1.putExtra("Time", Str_TeamManager_TeamMake_EditText_Time);
                        intent1.putExtra("TeamIntro", Str_TeamManager_TeamMake_EditText_TeamIntro);
                        startActivity(intent1);
                        finish();
                    }
                }
            });
        }
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
}
