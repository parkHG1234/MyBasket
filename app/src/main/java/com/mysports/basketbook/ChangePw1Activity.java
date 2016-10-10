package com.mysports.basketbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ldong on 2016-10-08.
 */

public class ChangePw1Activity extends AppCompatActivity {

    LinearLayout change_pw1_layout_root;
    String Id, Pw;
    EditText Change_Pw_EditText;
    Activity ac;
    View a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pw1_layout);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Intent intent = getIntent();
        Id = intent.getStringExtra("Id");
        Log.i("id", Id);
        change_pw1_layout_root = (LinearLayout)findViewById(R.id.change_pw1_layout_root);
        Change_Pw_EditText = (EditText) findViewById(R.id.Change_pw_layout_pw_EditText);


        change_pw1_layout_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(Change_Pw_EditText.getWindowToken(), 0);

            }
        });
    }

    public void onClickConfirm(View view) {
        Pw = Change_Pw_EditText.getText().toString();
        a = view;
        loginHttp loginHttp1 = new loginHttp();
        loginHttp1.execute(Id, Pw);

       // String result = SendByHttp(Id, Pw);
       // String[][] parsedData = jsonParserList(result);
/*
        if(parsedData != null && parsedData[0][0].equals("isOk")) {
            Intent intent1 = new Intent(ChangePw1Activity.this, ChangePw2Activity.class);
            intent1.putExtra("Id", Id);
            startActivity(intent1);
            finish();
        }else {
            AlertDialog dlg = new AlertDialog.Builder(this).setTitle("비밀번호 불일치")
                    .setMessage("비밀번호가 일치하지 않습니다. 다시 한번 입력해주세요.")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {}
                    }).show();
        }*/
    }


    public class loginHttp extends AsyncTask<String, Void, String> {
        ProgressDialog asyncDialog = new ProgressDialog(ChangePw1Activity.this);
        String[][] parsedData;
        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("로그인중입니다..");


            // show dialog
            asyncDialog.show();
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {

            String result = SendByHttp(params[0], params[1]);
            parsedData = jsonParserList(result);
            return result;
        }
        @Override
        protected void onPostExecute(String result) {
            asyncDialog.dismiss();
            if(parsedData != null && parsedData[0][0].equals("isOk")) {
                Intent intent1 = new Intent(ChangePw1Activity.this, ChangePw2Activity.class);
                intent1.putExtra("Id", Id);
                startActivity(intent1);
                finish();
            }else {
                Snackbar.make(a,"비밀번호가 일치하지 않습니다. 다시 한번 입력해주세요.",Snackbar.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }
    }
    private String SendByHttp(String id, String pw) {
        try {
            HttpClient httpClient = new DefaultHttpClient();
            String postURL = "http://210.122.7.195:8080/pp/isPasswordOk.jsp";
            HttpPost post = new HttpPost(postURL);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("_id", id));
            params.add(new BasicNameValuePair("_password", pw));

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
