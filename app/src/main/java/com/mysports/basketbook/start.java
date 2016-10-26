package com.mysports.basketbook;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
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
import java.util.ArrayList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by 박지훈 on 2016-10-24.
 */

public class start extends AppCompatActivity {
    static String Approach=".";
    static String NewsFeed_Num=".";
    int threadtime = 0;
    JSONObject json_out;
    JSONArray jArr_out;
    String[][] parsedData;
    String version = "1.0.0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_start);
        AnimationSet set = new AnimationSet(true);
        set.setInterpolator(new AccelerateInterpolator());
        Intent intent1 = getIntent();
        if(intent1.hasExtra("Approach")){
            Approach = intent1.getStringExtra("Approach");
            NewsFeed_Num = intent1.getStringExtra("NewsFeed_Num");
        }
        ImageView imgLava = (ImageView) findViewById(R.id.imgLava);
        final TextView TextView_VersionCheck = (TextView) findViewById(R.id.TextView_VersionCheck);
        Animation ani = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        ani.setRepeatCount(-1);
        ani.setDuration(1000);

        set.addAnimation(ani);
        imgLava.setAnimation(set);

        String result = "";
        try {
            HttpClient client = new DefaultHttpClient();
            String postURL = "http://210.122.7.195:8080/gg/versioncheck_download.jsp";
            HttpPost post = new HttpPost(postURL);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);
            HttpResponse response = client.execute(post);
            BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
            String line = null;
            while ((line = bufreader.readLine()) != null) {
                result += line;
            }
            parsedData = jsonParserList(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(!(parsedData[0][0].equals(version))){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    threadtime += 500;
                    TextView_VersionCheck.setText(TextView_VersionCheck.getText() + ".");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            threadtime += 500;
                            TextView_VersionCheck.setText(TextView_VersionCheck.getText() + ".");
                            final MaterialDialog DropOutDialog = new MaterialDialog(start.this);
                            DropOutDialog
                                    .setTitle("바스켓북")
                                    .setMessage("업데이트 후 이용해주시기 바랍니다.")
                                    .setNegativeButton("취소", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            finish();
                                        }
                                    })
                                    .setPositiveButton("업데이트", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String url ="https://play.google.com/store/apps/details?id=com.mysports.basketbook";
                                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                            startActivity(intent);
                                        }
                                    });
                            DropOutDialog.show();
                           // Intent marketLaunch = new Intent(Intent.ACTION_VIEW);
                            //marketLaunch.setData(Uri.parse("market://search?q=네이트온"));
                            //startActivity(marketLaunch);
                        }
                    }, 1000);// 0.5초 정도 딜레이를 준 후 시작
                }
            }, 1000);// 0.5초 정도 딜레이를 준 후 시작
        }else {


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    threadtime += 500;
                    TextView_VersionCheck.setText(TextView_VersionCheck.getText() + ".");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            threadtime += 500;
                            TextView_VersionCheck.setText(TextView_VersionCheck.getText() + ".");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    threadtime += 500;
                                    TextView_VersionCheck.setText(TextView_VersionCheck.getText() + ".");
                                    if (threadtime == 1500) {
                                        Intent LoginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                                        LoginIntent.putExtra("fragment1", parsedData[0][1]);
                                        LoginIntent.putExtra("fragment2", parsedData[0][2]);
                                        LoginIntent.putExtra("fragment3", parsedData[0][3]);
                                        LoginIntent.putExtra("fragment4", parsedData[0][4]);
                                        LoginIntent.putExtra("Approach", Approach);
                                        LoginIntent.putExtra("NewsFeed_Num", NewsFeed_Num);
                                        startActivity(LoginIntent);
                                    }
                                }
                            }, 500);// 0.5초 정도 딜레이를 준 후 시작
                        }
                    }, 500);// 0.5초 정도 딜레이를 준 후 시작
                }
            }, 500);// 0.5초 정도 딜레이를 준 후 시작
        }
    }

    public String[][] jsonParserList(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try {
            String[] jsonName = {"version", "fragment1", "fragment2", "fragment3", "fragment4"};

            json_out = new JSONObject(pRecvServerPage);
            jArr_out = json_out.getJSONArray("List");
            parsedData = new String[jArr_out.length()][jsonName.length];
            for (int i = 0; i < jArr_out.length(); i++) {
                json_out = jArr_out.getJSONObject(i);
                for (int j = 0; j < jsonName.length; j++) {
                    parsedData[i][j] = json_out.getString(jsonName[j]);

                }
            }
            return parsedData;
        } catch (JSONException e) {
            return null;
        }
    }

} // end class