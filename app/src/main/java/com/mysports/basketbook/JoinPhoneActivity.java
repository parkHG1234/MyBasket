package com.mysports.basketbook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.InflateException;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by ldong on 2016-08-11.
 */
public class JoinPhoneActivity extends Activity {
    LinearLayout join_phone_layout_root;
    EditText join_phone_layout_access_EditText, join_phone_layout_phone_EditText;
    Button join_phone_layout_correct_Button, join_phone_layout_next_Button;
    TextView join_phone_layout_time_TextView, join_phone_layout_count_TextView;
    String phone, date, msg;
    static TimerTask myTask;
    static Timer timer;
    boolean flag;
    int cnt = 3, time = 0;
    Random random = new Random();
    int rnd = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_phone_layout);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        final BackThread thread = new BackThread();
        join_phone_layout_root = (LinearLayout) findViewById(R.id.join_phone_layout_root);
        join_phone_layout_phone_EditText = (EditText) findViewById(R.id.join_phone_layout_phone_EditText);
        join_phone_layout_correct_Button = (Button) findViewById(R.id.join_phone_layout_correct_Button);
        join_phone_layout_access_EditText = (EditText) findViewById(R.id.join_phone_layout_access_EditText);
        join_phone_layout_next_Button = (Button) findViewById(R.id.join_phone_layout_next_Button);
        join_phone_layout_time_TextView = (TextView) findViewById(R.id.join_phone_layout_time_TextView);
        join_phone_layout_count_TextView = (TextView) findViewById(R.id.join_phone_layout_count_TextView);

        join_phone_layout_phone_EditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = true;
            }
        });
        join_phone_layout_access_EditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=false;
            }
        });
        join_phone_layout_correct_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                try {
                    phone = join_phone_layout_phone_EditText.getText().toString();

                    if (phone.length() == 11) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault());
                        Date d = new Date();
                        date = dateFormat.format(d);
                        rnd = Math.abs(random.nextInt(899999) + 100000);
                        msg = "바스켓북 인증번호는 [" + String.valueOf(rnd) + "] 입니다.감사합니다.";

                        HttpClient client = new DefaultHttpClient();
                        String postURL = "http://210.122.7.193:8080/InetSMSExample/example.jsp";
                        HttpPost post = new HttpPost(postURL);
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("sms_msg", msg));
                        params.add(new BasicNameValuePair("sms_to", phone));
                        params.add(new BasicNameValuePair("sms_from", phone));
                        params.add(new BasicNameValuePair("sms_date", date));
                        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                        post.setEntity(ent);
                        client.execute(post);

                        join_phone_layout_correct_Button.setVisibility(View.GONE);
                        join_phone_layout_access_EditText.setVisibility(View.VISIBLE);
                        join_phone_layout_next_Button.setVisibility(View.VISIBLE);
                        join_phone_layout_time_TextView.setVisibility(View.VISIBLE);
                        join_phone_layout_count_TextView.setVisibility(View.VISIBLE);

                        join_phone_layout_count_TextView.setText("남은횟수 : " + String.valueOf(cnt));
                        myTask = new TimerTask() {
                            int i = 300;

                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // 해당 작업을 처리함
                                        join_phone_layout_time_TextView.setText("제한시간 : " + i);
                                    }
                                });
                                i--;
                                //시간이 초과된 경우 game 내 데이터 삭제 및 초기화.
                                if (i == 0) {
                                    timer.cancel();
                                    Snackbar.make(v, "처음부터 다시 시도해주세요.", Snackbar.LENGTH_SHORT).show();
                                    thread.start();
                                }
                            }
                        };
                        timer = new Timer();
                        timer.schedule(myTask, 0, 1000); // 5초후 첫실행, 1초마다 계속실행
                    } else {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(join_phone_layout_phone_EditText.getWindowToken(), 0);
                        Snackbar.make(v, "핸드폰번호를 확인해주세요.", Snackbar.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        join_phone_layout_next_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(join_phone_layout_access_EditText.getWindowToken(), 0);
                if (join_phone_layout_access_EditText.getText().toString().equals(String.valueOf(rnd))) {
                    myTask.cancel();
                    timer.cancel();
                    cnt = 3;
                    join_phone_layout_phone_EditText.setText("");
                    join_phone_layout_correct_Button.setVisibility(View.VISIBLE);
                    join_phone_layout_access_EditText.setText("");
                    join_phone_layout_access_EditText.setVisibility(View.GONE);
                    join_phone_layout_next_Button.setVisibility(View.GONE);
                    join_phone_layout_time_TextView.setVisibility(View.GONE);
                    join_phone_layout_count_TextView.setVisibility(View.GONE);
                    Intent intent = new Intent(JoinPhoneActivity.this, JoinIdActivity.class);
                    startActivity(intent);
                } else {
                    cnt--;
                    join_phone_layout_count_TextView.setText("남은횟수 : " + String.valueOf(cnt));
                    Snackbar.make(v, "인증번호를 확인해주세요.", Snackbar.LENGTH_SHORT).show();
                }
                if (cnt == 0) {
                    Snackbar.make(v, "처음부터 다시 시도해주세요.", Snackbar.LENGTH_SHORT).show();
                    thread.start();
                }
            }
        });
        join_phone_layout_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if(flag) {
                    imm.hideSoftInputFromWindow(join_phone_layout_phone_EditText.getWindowToken(), 0);
                }else{
                    imm.hideSoftInputFromWindow(join_phone_layout_access_EditText.getWindowToken(), 0);
                }
            }
        });
    }
    class BackThread extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                myTask.cancel();
                timer.cancel();
                finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } // end run()
    } // end class BackThread
}
