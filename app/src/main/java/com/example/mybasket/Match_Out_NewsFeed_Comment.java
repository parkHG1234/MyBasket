package com.example.mybasket;

/**
 * Created by 박효근 on 2016-07-22.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by 박지훈 on 2016-06-27.
 */
public class Match_Out_NewsFeed_Comment extends Activity implements AbsListView.OnScrollListener {

    ImageView NewsFeed_Comment_Emblem;
    TextView NewsFeed_Comment_User;
    TextView NewsFeed_Comment_Court;
    TextView NewsFeed_Comment_Time;
    TextView NewsFeed_Comment_Data;
    ListView NewSpeed_Comment_List;
    EditText NewsFeed_Comment_EditText;
    Button NewsFeed_Comment_Button;
    ImageView NewSpeed_Comment_ImageView;


    Match_Out_NewsFeed_Comment_Adapter CommentAdapter;
    ArrayList<Match_Out_NewsFeed_Comment_Setting> arrComment;
    String NewsFeed_Num;
    String[][] parsedData;
    String Comment_User,Comment_Emblem=null;
    String[] jsonName = {"Comment_Num", "NewsFeed_Num", "Comment_User", "Comment_Data", "Comment_Month", "Comment_Day", "Comment_Hour", "Comment_Minute", "Name", "Birth", "Sex", "Position", "Team", "Profile", "Height", "Weight", "Phone"};

    Handler handler = new Handler();

    boolean VisibleFlag = false;
    String En_Profile;
    int cnt, pos;
    JSONArray jArr;
    JSONObject json;
    ProgressBar NewsFeed_Comment_ProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_match_out_newsfeed_comment);

        NewsFeed_Comment_Emblem = (ImageView) findViewById(R.id.NewsFeed_Comment_Emblem);
        NewsFeed_Comment_User =  (TextView) findViewById(R.id.NewsFeed_Comment_User);
        NewsFeed_Comment_Court = (TextView) findViewById(R.id.NewsFeed_Comment_Court);
        NewsFeed_Comment_Time = (TextView) findViewById(R.id.NewsFeed_Comment_Time);
        NewsFeed_Comment_Data = (TextView) findViewById(R.id.NewsFeed_Comment_Data);
        NewSpeed_Comment_List = (ListView) findViewById(R.id.NewSpeed_Comment_List);
        NewsFeed_Comment_EditText = (EditText) findViewById(R.id.NewsFeed_Comment_EditText);
        NewsFeed_Comment_Button = (Button) findViewById(R.id.NewsFeed_Comment_Button);
        NewsFeed_Comment_ProgressBar = (ProgressBar) findViewById(R.id.NewsFeed_Comment_ProgressBar);
        NewSpeed_Comment_ImageView=(ImageView)findViewById(R.id.NewSpeed_Comment_ImageView);

        final Intent CommentIntent = getIntent();
        NewsFeed_Num = CommentIntent.getExtras().getString("Num");
        Comment_User = CommentIntent.getExtras().getString("Id");
        NewsFeed_Comment_User.setText(CommentIntent.getExtras().getString("Id"));
        NewsFeed_Comment_Court.setText(CommentIntent.getExtras().getString("Court"));
        NewsFeed_Comment_Data.setText(CommentIntent.getExtras().getString("Data"));
        NewsFeed_Comment_Time.setText(CommentIntent.getExtras().getString("Time"));
        Comment_Emblem=CommentIntent.getExtras().getString("profile");
        if (Comment_Emblem.equals(".")) {
            Glide.with(getApplicationContext()).load(R.drawable.basic_image).into(NewsFeed_Comment_Emblem);
        } else {
            Glide.with(getApplicationContext()).load("http://210.122.7.195:8080/Web_basket/imgs/Profile/" + Comment_Emblem + ".jpg").bitmapTransform(new CropCircleTransformation(Glide.get(getApplicationContext()).getBitmapPool()))
                    .into(NewsFeed_Comment_Emblem);
        }

        String result = "";
        try {
            En_Profile = URLEncoder.encode(CommentIntent.getExtras().getString("Image"), "utf-8");
            HttpClient client = new DefaultHttpClient();
            String postURL = "http://210.122.7.195:8080/gg/newsfeed_comment_download.jsp";
            HttpPost post = new HttpPost(postURL);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("NewsFeed_Num", NewsFeed_Num));
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);
            HttpResponse response = client.execute(post);
            BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
            String line = null;
            while ((line = bufreader.readLine()) != null) {
                result += line;
            }
            parsedData = jsonParserList(result);
            setData();

            CommentAdapter = new Match_Out_NewsFeed_Comment_Adapter(Match_Out_NewsFeed_Comment.this, arrComment,Comment_User);
            CommentAdapter.listview(NewSpeed_Comment_List);
            NewSpeed_Comment_List.setAdapter(CommentAdapter);
            NewSpeed_Comment_List.setOnScrollListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        NewsFeed_Comment_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = "";
                if(!(NewsFeed_Comment_EditText.getText().equals("null"))){
                try {
                    HttpClient client = new DefaultHttpClient();
                    String postURL = "http://210.122.7.195:8080/gg/newsfeed_comment_upload.jsp";
                    HttpPost post = new HttpPost(postURL);
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("NewsFeed_Num", NewsFeed_Num));
                    params.add(new BasicNameValuePair("Comment_User",Comment_User ));
                    params.add(new BasicNameValuePair("Comment_Data", NewsFeed_Comment_EditText.getText().toString()));
                    params.add(new BasicNameValuePair("Comment_Month", new SimpleDateFormat("MM").format(new java.sql.Date(System.currentTimeMillis()))));
                    params.add(new BasicNameValuePair("Comment_Day", new SimpleDateFormat("dd").format(new java.sql.Date(System.currentTimeMillis()))));
                    params.add(new BasicNameValuePair("Comment_Hour", new SimpleDateFormat("kk").format(new java.sql.Date(System.currentTimeMillis()))));
                    params.add(new BasicNameValuePair("Comment_Minute", new SimpleDateFormat("mm").format(new java.sql.Date(System.currentTimeMillis()))));
                    UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                    post.setEntity(ent);
                    HttpResponse response = client.execute(post);
                    BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));

                    client = new DefaultHttpClient();
                    postURL = "http://210.122.7.195:8080/gg/newsfeed_comment_download.jsp";
                    post = new HttpPost(postURL);
                    params.add(new BasicNameValuePair("NewsFeed_Num", NewsFeed_Num));
                    ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                    post.setEntity(ent);
                    response = client.execute(post);
                    bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));

                    String line = null;
                    while ((line = bufreader.readLine()) != null) {
                        result += line;
                    }
                    parsedData = jsonParserList(result);
                    setData();
                    CommentAdapter = new Match_Out_NewsFeed_Comment_Adapter(Match_Out_NewsFeed_Comment.this, arrComment,Comment_User);

                    CommentAdapter.listview(NewSpeed_Comment_List);
                    NewSpeed_Comment_List.setAdapter(CommentAdapter);
                    NewsFeed_Comment_EditText.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                }}
            }
        });

        if (En_Profile.equals(".")) {
            NewSpeed_Comment_ImageView.setVisibility(View.GONE);
        } else {
            NewSpeed_Comment_ImageView.setVisibility(View.VISIBLE);
            Glide.with(getApplicationContext()).load("http://210.122.7.195:8080/gg/imgs1/" + En_Profile + ".jpg").into(NewSpeed_Comment_ImageView);
            Log.i("data_adapter",En_Profile);
        }
    }
    private void setData() {
        arrComment = new ArrayList<Match_Out_NewsFeed_Comment_Setting>();
        for (int a = 0; a < cnt; a++) {
            arrComment.add(new Match_Out_NewsFeed_Comment_Setting(parsedData[a][0], parsedData[a][1], parsedData[a][2], parsedData[a][3], parsedData[a][4], parsedData[a][5], parsedData[a][6], parsedData[a][7],
                    parsedData[a][8], parsedData[a][9], parsedData[a][10], parsedData[a][11], parsedData[a][12], parsedData[a][13], parsedData[a][14], parsedData[a][15], parsedData[a][16]));
        }
    }
    public String[][] jsonParserList(String pRecvServerPage) {
        Log.i("댓글에서 받은 전체 내용", pRecvServerPage);
        try {
            json = new JSONObject(pRecvServerPage);
            jArr = json.getJSONArray("List");

            if (jArr.length() >9) {
                cnt = 10;
            } else {
                cnt = jArr.length();
                VisibleFlag = false;
            }
            parsedData = new String[jArr.length()][jsonName.length];
            for (int i = 0; i < cnt; i++) {
                json = jArr.getJSONObject(i);
                for (int j = 0; j < jsonName.length; j++) {
                    parsedData[i][j] = json.getString(jsonName[j]);
                }
            }
            return parsedData;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && VisibleFlag) {
            if (jArr.length() <= cnt) {
                cnt = jArr.length();
            }  else{
                cnt = cnt + 10;
            }
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    NewsFeed_Comment_ProgressBar.setVisibility(View.GONE);
                }
            }, 1000);
            try {
                for (int i = 0; i < cnt; i++) {
                    json = jArr.getJSONObject(i);
                    for (int j = 0; j < jsonName.length; j++) {
                        parsedData[i][j] = json.getString(jsonName[j]);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            setData();
            CommentAdapter = new Match_Out_NewsFeed_Comment_Adapter(Match_Out_NewsFeed_Comment.this, arrComment,Comment_User);
            CommentAdapter.listview(NewSpeed_Comment_List);
            NewSpeed_Comment_List = (ListView) findViewById(R.id.NewSpeed_Comment_List);
            NewSpeed_Comment_List.setAdapter(CommentAdapter);
            NewSpeed_Comment_List.setSelection(pos + 3);
        }
    }
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        VisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
        pos = totalItemCount - visibleItemCount;
    }
}