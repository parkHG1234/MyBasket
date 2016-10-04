package com.mysports.playbasket;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by park on 2016-06-22.
 */
public class Navigation_TeamIntro_Focus_Join extends Activity{
    View view;
    ImageView TeamIntro_Foucs_Join_ImageView_Emblem;
    TextView TeamIntro_Foucs_Join_TextView_TeamName;
    EditText TeamIntro_Foucs_Join_EditText_Introduce;
    Button TeamIntro_Foucs_Join_Button_Join;
    static String TeamName, Emblem, Introduce_msg ,Id;
    String[][] parsedData;
    String ImageUrl4;
    Bitmap bmImg;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_navigation_teamintro_focus_join);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        TeamIntro_Foucs_Join_ImageView_Emblem = (ImageView)findViewById(R.id.TeamIntro_Foucs_Join_ImageView_Emblem);
        TeamIntro_Foucs_Join_TextView_TeamName = (TextView)findViewById(R.id.TeamIntro_Foucs_Join_TextView_TeamName);
        TeamIntro_Foucs_Join_EditText_Introduce = (EditText)findViewById(R.id.TeamIntro_Foucs_Join_EditText_Introduce);
        TeamIntro_Foucs_Join_Button_Join = (Button)findViewById(R.id.TeamIntro_Foucs_Join_Button_Join);

        Intent intent1 = getIntent();
        TeamName = intent1.getStringExtra("TeamName");
        Emblem = intent1.getStringExtra("Emblem");
        Id = intent1.getStringExtra("Id");

        TeamIntro_Foucs_Join_TextView_TeamName.setText(TeamName);
        try{
            String En_Emblem = URLEncoder.encode(Emblem, "utf-8");
            if(!Emblem.equals(""))
            {
                ImageUrl4 = "http://210.122.7.195:8080/Web_basket/imgs/Emblem/" + En_Emblem + ".jpg";
                back4 task4 = new back4();
                task4.execute(ImageUrl4);
            }
        }
        catch (UnsupportedEncodingException e)
        {

        }
        Introduce_msg = TeamIntro_Foucs_Join_EditText_Introduce.getText().toString();
        TeamIntro_Foucs_Join_Button_Join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view = v;
                String result="";
                try {
                    HttpClient client = new DefaultHttpClient();
                    String postURL = "http://210.122.7.195:8080/Web_basket/Navi_TeamIntro_Focus_Join.jsp";
                    HttpPost post = new HttpPost(postURL);

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("Id", Id));
                    params.add(new BasicNameValuePair("TeamName", TeamName));
                    params.add(new BasicNameValuePair("Introduce_msg", Introduce_msg));

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
                if(parsedData[0][0].equals("succed")){

                    Snackbar.make(view, "가입 신청 완료", Snackbar.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
    /////성공했는지 받아온 json 파싱합니다.//////////////////////////////////////////////////////////
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
    private class back4 extends AsyncTask<String, Integer,Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            // TODO Auto-generated method stub
            try{
                URL myFileUrl = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection)myFileUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();

                InputStream is = conn.getInputStream();

                bmImg = BitmapFactory.decodeStream(is);


            }catch(IOException e){
                e.printStackTrace();
            }
            return bmImg;
        }

        protected void onPostExecute(Bitmap img){

            TeamIntro_Foucs_Join_ImageView_Emblem.setImageBitmap(bmImg);
        }

    }
}
