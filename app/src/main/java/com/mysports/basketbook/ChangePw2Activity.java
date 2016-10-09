package com.mysports.basketbook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

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

public class ChangePw2Activity extends AppCompatActivity{
    LinearLayout change_pw2_layout_root;
    EditText ChangePw1_EditText, ChangePw2_EditText;
    String Id, Pw1, Pw2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pw2_layout);

        Intent intent = getIntent();
        Id = intent.getStringExtra("Id");

        change_pw2_layout_root = (LinearLayout)findViewById(R.id.change_pw2_layout_root);
        ChangePw1_EditText = (EditText) findViewById(R.id.changePw_layout_pw1_EditText);
        ChangePw2_EditText = (EditText) findViewById(R.id.changePw_layout_pw2_EditText);

        change_pw2_layout_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(ChangePw1_EditText.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(ChangePw2_EditText.getWindowToken(), 0);

            }
        });
    }

    public void onClickChangeConfirm(View view) {
        Pw1 = ChangePw1_EditText.getText().toString();
        Pw2 = ChangePw2_EditText.getText().toString();
        if(Pw1.equals(Pw2)) {
            if(Pw1.length() < 3) {
                Snackbar.make(view, "비밀번호는 4자 이상이어야 합니다.", Snackbar.LENGTH_LONG)
                        .show();
            }else {
                String result = SendByHttp(Id, Pw1);
                String[][] parsedData = jsonParserList(result);
                if(parsedData!=null && parsedData[0][0].equals("succed")) {
                    AlertDialog dlg = new AlertDialog.Builder(this).setTitle("비밀번호 변경")
                            .setMessage("비밀번호가 변경되었습니다.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            }).show();
                }else {
                    AlertDialog dlg = new AlertDialog.Builder(this).setTitle("오류")
                            .setMessage("에러가 발생하였습니다. 다시한번 시도해 주세요.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {}
                            }).show();
                }
            }
        }else {
            Snackbar.make(view, "비밀번호가 일치하지 않습니다.", Snackbar.LENGTH_LONG)
                    .show();
        }
    }
    private String SendByHttp(String id, String pw) {
        try {
            HttpClient httpClient = new DefaultHttpClient();
            String postURL = "http://210.122.7.195:8080/pp/ChangePassword.jsp";
            HttpPost post = new HttpPost(postURL);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("_id", id));
            params.add(new BasicNameValuePair("_pw", pw));

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
