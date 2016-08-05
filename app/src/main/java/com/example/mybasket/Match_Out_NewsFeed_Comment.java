package com.example.mybasket;

/**
 * Created by 박효근 on 2016-07-22.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by 박지훈 on 2016-06-27.
 */
public class Match_Out_NewsFeed_Comment extends AppCompatActivity implements AbsListView.OnScrollListener, NavigationView.OnNavigationItemSelectedListener {

    ImageView NewsFeed_Comment_Emblem;
    TextView NewsFeed_Comment_Court;
    TextView NewsFeed_Comment_Person;
    TextView NewsFeed_Comment_Time;
    TextView NewsFeed_Comment_Data;
    ListView NewSpeed_Comment_List;
    EditText NewsFeed_Comment_EditText;
    Button NewsFeed_Comment_Button;


    Match_Out_NewsFeed_Comment_Adapter CommentAdapter;
    ArrayList<Match_Out_NewsFeed_Comment_Setting> arrComment;
    String NewsFeed_Num;
    String[][] parsedData,parsedData_CourtInfo;
    String[] jsonName = {"Comment_Num", "NewsFeed_Num", "Comment_Person", "Comment_Data", "Comment_Month", "Comment_Day", "Comment_Hour", "Comment_Minute"};

    Handler handler = new Handler();

    boolean VisibleFlag = false;
    int cnt, pos;
    JSONArray jArr;
    JSONObject json;
    ProgressBar NewsFeed_Comment_ProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_123);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar123);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        final View aa = navigationView.inflateHeaderView(R.layout.layout_courtinfo_navheader);
        final TextView CourtInfo_Nav_CourtName = (TextView) aa.findViewById(R.id.CourtInfo_Nav_CourtName);
        final TextView CourtInfo_Nav_CourtFloor = (TextView) aa.findViewById(R.id.CourtInfo_Nav_CourtFloor);
        final TextView CourtInfo_Nav_CourtAddress = (TextView) aa.findViewById(R.id.CourtInfo_Nav_CourtAddress);
        final TextView CourtInfo_Nav_CourtCount = (TextView) aa.findViewById(R.id.CourtInfo_Nav_CourtCount);

        final ImageView CourtInfo_Nav_Img1 = (ImageView)aa.findViewById(R.id.CourtInfo_Nav_Img1);
        final ImageView CourtInfo_Nav_Img2 = (ImageView)aa.findViewById(R.id.CourtInfo_Nav_Img2);
        final ImageView CourtInfo_Nav_Img3 = (ImageView)aa.findViewById(R.id.CourtInfo_Nav_Img3);

        NewsFeed_Comment_Emblem = (ImageView) findViewById(R.id.NewsFeed_Comment_Emblem);
        NewsFeed_Comment_Court = (TextView) findViewById(R.id.NewsFeed_Comment_Court);
        NewsFeed_Comment_Person = (TextView) findViewById(R.id.NewsFeed_Comment_Person);
        NewsFeed_Comment_Time = (TextView) findViewById(R.id.NewsFeed_Comment_Time);
        NewsFeed_Comment_Data = (TextView) findViewById(R.id.NewsFeed_Comment_Data);
        NewSpeed_Comment_List = (ListView) findViewById(R.id.NewSpeed_Comment_List);
        NewsFeed_Comment_EditText = (EditText) findViewById(R.id.NewsFeed_Comment_EditText);
        NewsFeed_Comment_Button = (Button) findViewById(R.id.NewsFeed_Comment_Button);
        NewsFeed_Comment_ProgressBar = (ProgressBar) findViewById(R.id.NewsFeed_Comment_ProgressBar);

        final Intent CommentIntent = getIntent();
        NewsFeed_Num = CommentIntent.getExtras().getString("Num");
        NewsFeed_Comment_Court.setText(CommentIntent.getExtras().getString("Court"));
        NewsFeed_Comment_Person.setText(CommentIntent.getExtras().getString("Person"));
        NewsFeed_Comment_Data.setText(CommentIntent.getExtras().getString("Data"));
        NewsFeed_Comment_Time.setText(CommentIntent.getExtras().getString("Time"));

        String result_CourtInfo="";
        try {
            HttpClient client = new DefaultHttpClient();
            String postURL = "http://210.122.7.195:8080/Web_basket/CourtInfo.jsp";
            HttpPost post = new HttpPost(postURL);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("CourtName", CommentIntent.getExtras().getString("Court")));

            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);

            HttpResponse response = client.execute(post);
            BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));

            String line = null;
            while ((line = bufreader.readLine()) != null) {
                result_CourtInfo += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        parsedData_CourtInfo = jsonParserList_CourtInfo(result_CourtInfo);
        String CourtName =parsedData_CourtInfo[0][0];
        String CourtAddress =parsedData_CourtInfo[0][1];
        String CourtCount =parsedData_CourtInfo[0][2];
        String CourtFloor =parsedData_CourtInfo[0][3];
        String CourtMapX =parsedData_CourtInfo[0][4];
        String CourtMapy =parsedData_CourtInfo[0][5];
        String Image1 =parsedData_CourtInfo[0][6];
        String Image2 =parsedData_CourtInfo[0][7];
        String Image3 =parsedData_CourtInfo[0][8];

        CourtInfo_Nav_CourtName.setText(CourtName);
        CourtInfo_Nav_CourtAddress.setText(CourtAddress);
        CourtInfo_Nav_CourtCount.setText(CourtCount);
        CourtInfo_Nav_CourtFloor.setText(CourtFloor);
//URI 한글 인코딩
        try{
            String En_Image1 = URLEncoder.encode(Image1, "utf-8");
            String En_Image2 = URLEncoder.encode(Image2, "utf-8");
            String En_Image3 = URLEncoder.encode(Image3, "utf-8");

            Glide.with(Match_Out_NewsFeed_Comment.this).load("http://210.122.7.195:8080/Web_basket/imgs/Court/"+En_Image1+".jpg")
                    .into(CourtInfo_Nav_Img1);
            Glide.with(Match_Out_NewsFeed_Comment.this).load("http://210.122.7.195:8080/Web_basket/imgs/Court/"+En_Image2+".jpg")
                    .into(CourtInfo_Nav_Img2);
            Glide.with(Match_Out_NewsFeed_Comment.this).load("http://210.122.7.195:8080/Web_basket/imgs/Court/"+En_Image3+".jpg")
                    .into(CourtInfo_Nav_Img3);

        }catch (UnsupportedEncodingException e){

        }
        String result = "";
        try {
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

            CommentAdapter = new Match_Out_NewsFeed_Comment_Adapter(Match_Out_NewsFeed_Comment.this, arrComment);
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
                try {
                    HttpClient client = new DefaultHttpClient();
                    String postURL = "http://210.122.7.195:8080/gg/newsfeed_comment_upload.jsp";
                    HttpPost post = new HttpPost(postURL);
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("NewsFeed_Num", NewsFeed_Num));
                    params.add(new BasicNameValuePair("Comment_Person", NewsFeed_Comment_Person.getText().toString()));
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
                    CommentAdapter = new Match_Out_NewsFeed_Comment_Adapter(Match_Out_NewsFeed_Comment.this, arrComment);
                    CommentAdapter.listview(NewSpeed_Comment_List);
                    NewSpeed_Comment_List.setAdapter(CommentAdapter);
                    NewsFeed_Comment_EditText.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setData() {
        arrComment = new ArrayList<Match_Out_NewsFeed_Comment_Setting>();
        for (int a = 0; a < cnt; a++) {
            arrComment.add(new Match_Out_NewsFeed_Comment_Setting(parsedData[a][0], parsedData[a][1], parsedData[a][2], parsedData[a][3], parsedData[a][4], parsedData[a][5], parsedData[a][6], parsedData[a][7]));
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
    public String[][] jsonParserList_CourtInfo(String pRecvServerPage){
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try{
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");

            String[] jsonName = {"msg1","msg2","msg3","msg4","msg5","msg6","msg7","msg8","msg9"};
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
    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {

        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && VisibleFlag) {

            if (cnt>=10 && cnt+10>jArr.length()) {
                cnt = jArr.length();
            }  else{
                cnt = cnt + 10;
            }

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
            CommentAdapter = new Match_Out_NewsFeed_Comment_Adapter(Match_Out_NewsFeed_Comment.this, arrComment);
            CommentAdapter.listview(NewSpeed_Comment_List);
            NewSpeed_Comment_List = (ListView) findViewById(R.id.NewSpeed_Comment_List);
            NewSpeed_Comment_List.setAdapter(CommentAdapter);
            NewSpeed_Comment_List.setSelection(pos + 1);

        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        VisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
        pos = totalItemCount - visibleItemCount;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}