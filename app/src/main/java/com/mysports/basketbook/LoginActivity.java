package com.mysports.basketbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.util.exception.KakaoException;
import com.mysports.basketbook.R;

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
import java.util.concurrent.ExecutionException;

/**
 * Created by ldong on 2016-06-30.
 */
public class LoginActivity extends AppCompatActivity {
    LinearLayout login_layout_root;
    EditText id_EditText, pw_EditText;
    Button login_Button;
    TextView join_Button;
    String[][] parsedData;
    AlertDialog dlg;
    View myview;
    CheckBox autoLoginChkbox;
    String Approach=".";
    String NewsFeed_Num=".";
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String version,fragment1,fragment2,fragment3,fragment4;
    String _id,_pw;
    private BackPressCloseHandler backPressCloseHandler;
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

        login_layout_root = (LinearLayout)findViewById(R.id.login_layout_root);
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


        backPressCloseHandler = new BackPressCloseHandler(this);



        final Intent StartIntent = getIntent();
        fragment1=StartIntent.getExtras().getString("fragment1");
        fragment2=StartIntent.getExtras().getString("fragment2");
        fragment3=StartIntent.getExtras().getString("fragment3");
        fragment4=StartIntent.getExtras().getString("fragment4");
        if(StartIntent.hasExtra("Approach")){
            Approach = StartIntent.getStringExtra("Approach");
            NewsFeed_Num = StartIntent.getStringExtra("NewsFeed_Num");
        }

        autoLoginChkbox.setChecked(false);
        String autoChkbox = preferences.getString("auto","");  //로그아웃시 autologinChkbox ""로 변경 or preference 삭제
        if(autoChkbox.equals("true")) {
            autoLoginChkbox.setChecked(true);
        }

        callback = new SessionCallback();                  // 이 두개의 함수 중요함
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();

        login_layout_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(id_EditText.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(pw_EditText.getWindowToken(), 0);
            }
        });


        if(autoLoginChkbox.isChecked()){
            Log.i("test123",Approach);
            //preference 이름을 autoLogin
            String autologID = preferences.getString("id", "");
            String autologPW = preferences.getString("pw", "");
            Log.i("first auto", autologID);
            if(autologID.equals("")) {
            }else {
                Log.i("log.i AUTOLOGID", autologID);
                String result = SendByHttp(autologID, autologPW);
                parsedData = jsonParserList(result);
                if(parsedData != null && parsedData[0][0].equals("succed")) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("LoginCheck",parsedData[0][0]);
                    intent.putExtra("Id",parsedData[0][1]);
                    intent.putExtra("Approach",Approach);
                    intent.putExtra("NewsFeed_Num",NewsFeed_Num);
                    intent.putExtra("fragment1",fragment1);
                    intent.putExtra("fragment2",fragment2);
                    intent.putExtra("fragment3",fragment3);
                    intent.putExtra("fragment4",fragment4);

                    startActivity(intent);
                    finish();
                }
            }
        }


    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }
    public void join_Button (View view) {
        Intent intent = new Intent(LoginActivity.this, JoinIdActivity.class);
        startActivity(intent);
    }
    public void login_Button (View view) {
        Log.i("test123",Approach);
        _id = id_EditText.getText().toString();
        _pw = pw_EditText.getText().toString();
        myview = view;
        if(_id.equals("")) {
            Snackbar.make(view, "아이디를 입력해 주세요.", Snackbar.LENGTH_LONG)
                    .show();
        }else if(_pw.equals("")) {
            Snackbar.make(view, "비밀번호를 입력해 주세요.", Snackbar.LENGTH_LONG)
                    .show();
        }else {
            loginHttp login = new loginHttp();
            login.execute(_id, _pw);
        }
    }
    public class loginHttp extends AsyncTask<String, Void, String> {
        ProgressDialog asyncDialog = new ProgressDialog(LoginActivity.this);

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("접속중입니다..");

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
                    Snackbar.make(myview, preferences.getString("auto",""), Snackbar.LENGTH_LONG)
                            .show();
                }else {
                }
                Log.i("test",Approach);
                //메인엑티비티에다 데이터를보내
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("LoginCheck",parsedData[0][0]);
                intent.putExtra("Id",parsedData[0][1]);
                intent.putExtra("Approach",Approach);
                intent.putExtra("NewsFeed_Num",NewsFeed_Num);
                intent.putExtra("fragment1",fragment1);
                intent.putExtra("fragment2",fragment2);
                intent.putExtra("fragment3",fragment3);
                intent.putExtra("fragment4",fragment4);
                startActivity(intent);

                finish();
            }
            else if(parsedData != null && parsedData[0][0].equals("failed")){
                Snackbar.make(myview,"아이디 패스워드를 확인해주세요.",Snackbar.LENGTH_SHORT).show();
            }
            else{
                Snackbar.make(myview,"서버와의 접속에 실패하였습니다.",Snackbar.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }
    }

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
        intent.putExtra("Approach",Approach);
        intent.putExtra("NewsFeed_Num",NewsFeed_Num);
        intent.putExtra("fragment1",fragment1);
        intent.putExtra("fragment2",fragment2);
        intent.putExtra("fragment3",fragment3);
        intent.putExtra("fragment4",fragment4);
        startActivity(intent);
        finish();
    }
}