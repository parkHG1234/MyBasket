package com.example.mybasket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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
 * Created by ldong on 2016-07-05.
*/
public class JoinIdActivity extends Activity {
    EditText join_id_EditText;
    AlertDialog dlg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_id_layout);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        join_id_EditText = (EditText) findViewById(R.id.join_id_layout_id_EditText);
    }

    public void onClickNext(View view) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String id = join_id_EditText.getText().toString();
        String result = SendByHttp(id);
        Log.i("JSON을 분석한 데이터2222 :", result);
        String[][] parsedData = jsonParserList(result);
        if(parsedData != null && parsedData[0][0].equals("noDuplicate")) {
            if(id.length() < 5) {
                dlg = new AlertDialog.Builder(this).setTitle("알동")
                        ////나중에 아이콘모양 넣기 .setIcon(R.drawable.icon)~~
                        .setMessage("아이디는 5글자 이상의 영문 숫자로 이루어진 문자여야합니다.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
            }else {
                Intent intent = new Intent(JoinIdActivity.this, JoinPwActivity.class);
                intent.putExtra("id",id);

                startActivity(intent);
            }

        }else {
            dlg = new AlertDialog.Builder(this).setTitle("알동")
                    ////나중에 아이콘모양 넣기 .setIcon(R.drawable.icon)~~
                    .setMessage("중복된 아이디가 있습니다.")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
        }
    }


    private String SendByHttp(String id) {
        try {
            HttpClient httpClient = new DefaultHttpClient();
            String postURL = "http://210.122.7.195:8080/pp/CheckJoinedId.jsp";
            HttpPost post = new HttpPost(postURL);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id", id));

            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(entity);

            HttpResponse response = httpClient.execute(post);
            BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));

            String line = null;
            String result = "";

            while ((line = bufreader.readLine()) != null) {
                result += line;
            }
            return result;
        }catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    private String[][] jsonParserList(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jarr = json.getJSONArray("List");

            //String[] jsonName = {"login_check","usercode","password","name","sex","email","univ","club" };

            String[] jsonName = {"CheckId"};
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
