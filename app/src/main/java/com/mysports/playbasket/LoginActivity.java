package com.mysports.playbasket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.util.exception.KakaoException;

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
 * Created by ldong on 2016-06-30.
 */
public class LoginActivity extends Activity {
    EditText id_EditText, pw_EditText;
    Button login_Button;
    TextView join_Button;
    String[][] parsedData;
    AlertDialog dlg;
    CheckBox autoLoginChkbox;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private SessionCallback callback;      //콜백 선언

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        GlobalApplication.setCurrentActivity(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SharedPreferences preferences = getSharedPreferences("autoLogin", MODE_PRIVATE);

        id_EditText = (EditText) findViewById(R.id.id_Layout_EditText);
        pw_EditText = (EditText) findViewById(R.id.pw_Layout_EditText);
        login_Button = (Button) findViewById(R.id.login_button);
        join_Button = (TextView) findViewById(R.id.join_button);
        autoLoginChkbox = (CheckBox) findViewById(R.id.autuLogin_chkbox);
        join_Button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                join_Button(view);
            }
        });
        login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_Button(view);
            }
        });

        autoLoginChkbox.setChecked(false);
        String autoChkbox = preferences.getString("auto","");  //로그아웃시 autologinChkbox ""로 변경 or preference 삭제
        if(autoChkbox.equals("true")) {
            autoLoginChkbox.setChecked(true);
        }

        callback = new SessionCallback();                  // 이 두개의 함수 중요함
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();

        if(autoLoginChkbox.isChecked()){

            //preference 이름을 autoLogin
            String autologID = preferences.getString("id", "");
            String autologPW = preferences.getString("pw", "");
            if(autologID.equals("")) {
            }else {
                String result = SendByHttp(autologID, autologPW);
                parsedData = jsonParserList(result);
                if(parsedData != null && parsedData[0][0].equals("succed")) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("LoginCheck",parsedData[0][0]);
                    intent.putExtra("Id",parsedData[0][1]);

                    startActivity(intent);
                    finish();
                }
            }
        }

    }
    public void join_Button (View view) {
        Intent intent = new Intent(LoginActivity.this, JoinIdActivity.class);
        startActivity(intent);
        finish();

    }
    public void login_Button (View view) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String _id = id_EditText.getText().toString();
        String _pw = pw_EditText.getText().toString();
        if(_id.equals("")) {
            Snackbar.make(view, "아이디를 입력해 주세요.", Snackbar.LENGTH_LONG)
                    .show();
        }else if(_pw.equals("")) {
            Snackbar.make(view, "비밀번호를 입력해 주세요.", Snackbar.LENGTH_LONG)
                    .show();
        }else {
            String result = SendByHttp(_id, _pw);
            Log.i("JSON을 분석한 데이터1111 :", result);
            parsedData = jsonParserList(result);


            if(parsedData != null && parsedData[0][0].equals("succed"))
            {
                if(autoLoginChkbox.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("autoLogin", MODE_PRIVATE);
                    //preference 이름을 autoLogin
                    SharedPreferences.Editor editor = preferences.edit();


                    editor.putString("id", _id);
                    editor.putString("pw", _pw);
                    editor.putString("auto", "true");
                    editor.commit();
                    Snackbar.make(view, preferences.getString("auto",""), Snackbar.LENGTH_LONG)
                            .show();


                }else {
                }
                //메인엑티비티에다 데이터를보내
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("LoginCheck",parsedData[0][0]);
                intent.putExtra("Id",parsedData[0][1]);

                startActivity(intent);

                finish();
            }
            else if(parsedData != null && parsedData[0][0].equals("failed")){
                dlg = new AlertDialog.Builder(this).setTitle("플레이 바스켓")
                        ////나중에 아이콘모양 넣기 .setIcon(R.drawable.icon)~~
                        .setMessage("아이뒤 패스워드를 확인해주세요.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
            }
            else{
                dlg = new AlertDialog.Builder(this).setTitle("플레이 바스켓")
                        ////나중에 아이콘모양 넣기 .setIcon(R.drawable.icon)~~
                        .setMessage("서버와의 접속에 실패하였습니다.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
            }
        }


    }
    /*
        public class loginHttp extends AsyncTask<String, Void, Void> {
            @Override
            protected Void doInBackground(String. .. params) {
                try {
                    String url = "http://ldong.cafe24.com:8080/login_test.jsp";
                    URL obj = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("GET");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setRequestProperty("Content-Type","application/json");
                    byte[] outputInBytes = params[0].getBytes("UTF-8");
                    OutputStream os = conn.getOutputStream();
                    os.write( outputInBytes );
                    os.close();
                    int retCode = conn.getResponseCode();
                    InputStream is = conn.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line;
                    StringBuffer response = new StringBuffer();
                    while((line = br.readLine()) != null) {
                        response.append(line);
                        response.append('\r');
                    }
                    br.close();
                    String res = response.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }SendByHttp
    */
    private String SendByHttp(String _id, String _password) {
        if (_id == null)
        {
            _id = "";
        }
        try {
            HttpClient client = new DefaultHttpClient();
            String postURL = "http://210.122.7.195:8080/pp/Login.jsp";
            HttpPost post = new HttpPost(postURL);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("_id", _id));
            params.add(new BasicNameValuePair("_password", _password));

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

            String[] jsonName = {"login_check","id"};
            String[][] parseredData = new String[jarr.length()][jsonName.length];
            for(int i = 0; i<jarr.length();i++){
                json = jarr.getJSONObject(i);
                for (int j=0;j<jsonName.length; j++){
                    parseredData[i][j] = json.getString(jsonName[j]);
                }
            }
            for(int i=0;i<parseredData.length;i++) {
                Log.i("JSON을 분석한 데이터" + i + ":", parseredData[i][0]);
                Log.i("JSON을 분석한 데이터" + i + ":", parseredData[i][1]);
            }
            return parseredData;
        }catch(JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            Log.d("TAG","세션 오픈됨");
            redirectSignupActivity();  // 세션 연결성공 시 redirectSignupActivity() 호출
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception != null) {
                Log.d("TAG",exception.getMessage());
            }
            setContentView(R.layout.login_layout); // 세션 연결이 실패했을때
        }                                            // 로그인화면을 다시 불러옴
    }

    protected void redirectSignupActivity() {       //세션 연결 성공 시 SignupActivity로 넘김
        final Intent intent = new Intent(this, KakaoSignupActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }
}
