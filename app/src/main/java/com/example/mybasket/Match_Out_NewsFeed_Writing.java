package com.example.mybasket;

/**
 * Created by 박효근 on 2016-07-22.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

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
 * Created by 박지훈 on 2016-06-21.
 */
public class Match_Out_NewsFeed_Writing extends Activity {


    Spinner NewsFeed_Writing_addDoSpinner;
    Spinner NewsFeed_Writing_addSiSpinner;
    Spinner NewsFeed_Writing_addCourtSpinner;
    ArrayList arr;
    Button NewsFeed_Writing_Button;
    EditText NewsFeed_Writing_TextEditText;
    EditText NewsFeed_Writing_PersonEditText;
    final int REQ_SELECT=0;

    Button NewsFeed_Writing_CameraButton;
    ImageView NewsFeed_Camera_Image;


    ArrayAdapter<CharSequence> adspin1, adspin2, adspin3;
    static int spinnum1, spinnum2;
    String[][] parsedData;
    static String Do, Si, Court;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_match_out_newsfeed_writing);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        NewsFeed_Writing_CameraButton = (Button) findViewById(R.id.NewsFeed_Writing_CameraButton);
//        NewsFeed_Camera_SurfaceView=(SurfaceView)findViewById(R.id.NewsFeed_Camera_SurfaceView);
        NewsFeed_Camera_Image = (ImageView) findViewById(R.id.NewsFeed_Camera_Image);

        NewsFeed_Writing_addDoSpinner = (Spinner) findViewById(R.id.NewsFeed_Writing_addDoSpinner);
        NewsFeed_Writing_addSiSpinner = (Spinner) findViewById(R.id.NewsFeed_Writing_addSiSpinner);
        NewsFeed_Writing_addCourtSpinner = (Spinner) findViewById(R.id.NewsFeed_Writing_addCourtSpinner);
        NewsFeed_Writing_Button = (Button) findViewById(R.id.NewsFeed_Writing_Button);
        NewsFeed_Writing_TextEditText = (EditText) findViewById(R.id.NewsFeed_Writing_TextEditText);
        NewsFeed_Writing_PersonEditText = (EditText) findViewById(R.id.NewsFeed_Writing_PersonEditText);
        adspin1 = ArrayAdapter.createFromResource(Match_Out_NewsFeed_Writing.this, R.array.spinner_do, R.layout.zfile_spinner_test);
        adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        NewsFeed_Writing_addDoSpinner.setAdapter(adspin1);
        NewsFeed_Writing_addDoSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        spinnum1 = i;
                        Do = adspin1.getItem(i).toString();
                        if (adspin1.getItem(i).equals("서울")) {
                            adspin2 = ArrayAdapter.createFromResource(Match_Out_NewsFeed_Writing.this, R.array.spinner_do_seoul, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            NewsFeed_Writing_addSiSpinner.setAdapter(adspin2);
                            NewsFeed_Writing_addSiSpinner.setOnItemSelectedListener(
                                    new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            spinnum2 = i;
                                            Si = adspin2.getItem(i).toString();
                                            String result = "";
                                            try {
                                                HttpClient client = new DefaultHttpClient();
                                                String postURL = "http://210.122.7.195:8080/gg/CourtInformation.jsp";
                                                HttpPost post = new HttpPost(postURL);
                                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                                params.add(new BasicNameValuePair("NewsFeed_Do", (String) adspin1.getItem(spinnum1)));
                                                params.add(new BasicNameValuePair("NewsFeed_Si", (String) adspin2.getItem(spinnum2)));
                                                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                                post.setEntity(ent);
                                                HttpResponse response = client.execute(post);
                                                BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                                String line = null;
                                                while ((line = bufreader.readLine()) != null) {
                                                    result += line;
                                                }
                                                parsedData = jsonParserList(result);
                                                arr = new ArrayList();
                                                for (int a = 0; a < parsedData.length; a++) {
                                                    arr.add(parsedData[a][0]);
                                                }
                                                adspin3 = new ArrayAdapter<CharSequence>(Match_Out_NewsFeed_Writing.this, android.R.layout.simple_spinner_item, arr);
                                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                NewsFeed_Writing_addCourtSpinner.setAdapter(adspin3);
                                                NewsFeed_Writing_addCourtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                        Court = adspin3.getItem(i).toString();
                                                        NewsFeed_Writing_Button.setEnabled(true);
                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                                    }
                                                });
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {
                                        }
                                    }
                            );
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                }
        );
        NewsFeed_Writing_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    HttpClient client = new DefaultHttpClient();
                    String postURL = "http://210.122.7.195:8080/gg/newsfeed_data_upload.jsp";
                    HttpPost post = new HttpPost(postURL);
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("NewsFeed_Do", Do));
                    params.add(new BasicNameValuePair("NewsFeed_Si", Si));
                    params.add(new BasicNameValuePair("NewsFeed_Court", Court));
                    params.add(new BasicNameValuePair("NewsFeed_UserCount", NewsFeed_Writing_PersonEditText.getText().toString()));
                    params.add(new BasicNameValuePair("NewsFeed_Data", NewsFeed_Writing_TextEditText.getText().toString()));
                    params.add(new BasicNameValuePair("NewsFeed_Month", new SimpleDateFormat("MM").format(new java.sql.Date(System.currentTimeMillis()))));
                    params.add(new BasicNameValuePair("NewsFeed_Day", new SimpleDateFormat("dd").format(new java.sql.Date(System.currentTimeMillis()))));
                    params.add(new BasicNameValuePair("NewsFeed_Hour", new SimpleDateFormat("kk").format(new java.sql.Date(System.currentTimeMillis()))));
                    params.add(new BasicNameValuePair("NewsFeed_Minute", new SimpleDateFormat("mm").format(new java.sql.Date(System.currentTimeMillis()))));
                    UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                    post.setEntity(ent);
                    HttpResponse response = client.execute(post);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        NewsFeed_Writing_CameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Match_Out_NewsFeed_Camera camera = new Match_Out_NewsFeed_Camera();
                camera.camera_ImageView(NewsFeed_Camera_Image);
                Intent CameraIntent = new Intent(Match_Out_NewsFeed_Writing.this, Match_Out_NewsFeed_Camera.class);
                Match_Out_NewsFeed_Writing.this.startActivity(CameraIntent);

            }
        });

    }


    public String[][] jsonParserList(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");
            String[] jsonName = {"NewsFeed_Court"};
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
