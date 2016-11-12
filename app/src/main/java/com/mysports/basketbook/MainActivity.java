package com.mysports.basketbook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.iid.FirebaseInstanceId;

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
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import me.drakeet.materialdialog.MaterialDialog;

public class MainActivity extends AppCompatActivity {
    static String Id = "";
    static String Profile;
    static String Height="";
    static String Weight="";
    static String fragment1,fragment2,fragment3,fragment4;
    static String yourTeamStatus = "reset";
    static String Approach="";
    static String NewsFeed_Num="";
    static String now_Date="";
    static String realTime = "";
    static String Team1 = "";
    static int MaxNum_out;
    static int in_MinNum;
    static String Token = "";
    static String MyTeam = "";
    static String interestArea_do = ""; static String interestArea_si = "";

    View rootView;
    static String Alarm = "";
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout tabLayout;
    //게임 관련 선언
    static LinearLayout League_Layout_1, League_Layout_2;
    static LinearLayout Contest_Layout_1, Contest_Layout_2;
    static String[][] recommendparsedData;
    static String[][] parsedData_BasicSetting;
    static String[][] parsedData_gameStatus;
    static String[][] parsedData_gameRefuse;
    static String[][] parsedData_gameScoreInfo;
    static String[][] parsedData_gameScoreInfo_succed;
    static String[][] parsedData_Profile;
    static String[][] parsedData_CommentDirect;
    static int allowtime = 0;
    static String HomeTeam, AwayTeam, Authority;
    static Button Contest_Button_MyTeam, Contest_Button_YourTeam,Contest_Button_Start, Contest_Button_Cancel;
    static TimerTask myTask;
    static Timer timer;
    static String[][] parsedData_gameDelete,parsedData_gameGenerate;
    ////
    static ImageView Profile_ImageVIew_Profile;
    static ListView NewsFeed_List;
    static ListView Match_In_CustomList;
    static LinearLayout Match_Layout_Tab;
    static LinearLayout Match_Layout_Out_Address;
    static LinearLayout Match_Layout_In_Address;
    static String out_in_choice = "out";
    static View layout;
    static View layout_GameAdd;
    static View layout_GameScoreInfo;
    ///////탭 아이콘 불러오기/////////////////
    private int[] tabIcons_match = {
            R.drawable.basketball_clicked,
            R.drawable.trophy,
            R.drawable.document,
            R.drawable.user
    };
    private int[] tabIcons = {
            R.drawable.basketball,
            R.drawable.trophy,
            R.drawable.document,
            R.drawable.user
    };
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    GestureDetector gd = new GestureDetector(new GestureDetector.SimpleOnGestureListener());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
// 현재 날짜

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        now_Date = df.format(new Date());
       // now_Date = formatter1.format(cal.getTime());
        ////////////////
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        setupTabIcons_match();
////// 리그 탭 다이얼로그 사용을 위한 커스텀 다이얼로그 선언
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        layout = inflater.inflate(R.layout.layout_customdialog_teamchoice, (ViewGroup) findViewById(R.id.Layout_CustomDialog_teamChoice_Root));
        //////    ////// 리그 탭 다이얼로그_시합종료 사용을 위한 커스텀 다이얼로그 선언
        layout_GameAdd = inflater.inflate(R.layout.layout_customdialog_gameadd, (ViewGroup) findViewById(R.id.Layout_CustomDialog_GameAdd_Root));
        ///스코어 점수 확인을 위한 커스텀 다이얼로그 선언
        layout_GameScoreInfo = inflater.inflate(R.layout.layout_customdialog_gamescoreinfo, (ViewGroup) findViewById(R.id.Layout_CustomDialog_GameScoreInfo_Root));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int a = tab.getPosition();
                //매칭 탭 클릭시
                if (a == 0) {
                    tab.setIcon(R.drawable.basketball_clicked);
                    mViewPager.setCurrentItem(tab.getPosition());
                } else if (a == 1) {
                    tab.setIcon(R.drawable.trophy_clicked);
                    mViewPager.setCurrentItem(tab.getPosition());
                } else if (a == 2) {
                    tab.setIcon(R.drawable.document_clicked);
                    mViewPager.setCurrentItem(tab.getPosition());
                } else if (a == 3) {
                    tab.setIcon(R.drawable.user_clicked);
                    mViewPager.setCurrentItem(tab.getPosition());

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                setupTabIcons();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                if (out_in_choice.equals("out")) {
                    NewsFeed_List.setSelectionFromTop(0, 0);
                    new Handler().postDelayed(new Runnable() {// 1 초 후에 실행
                        @Override
                        public void run() {
                            Match_Layout_Tab.setVisibility(View.VISIBLE);
                            Match_Layout_Out_Address.setVisibility(View.VISIBLE);
                        }
                    }, 500);
                } else if (out_in_choice.equals("in")) {
                    Match_In_CustomList.setSelectionFromTop(0, 0);
                    new Handler().postDelayed(new Runnable() {// 1 초 후에 실행
                        @Override
                        public void run() {
                            Match_Layout_Tab.setVisibility(View.VISIBLE);
                            Match_Layout_In_Address.setVisibility(View.VISIBLE);
                        }
                    }, 500);
                }
            }
        });
        SharedPreferences prefs_user = getSharedPreferences("basketball_user", MODE_PRIVATE);

        Intent intent1 = getIntent();
        Id = intent1.getStringExtra("Id");
        fragment1 = intent1.getStringExtra("fragment1");
        fragment2 = intent1.getStringExtra("fragment2");
        fragment3 = intent1.getStringExtra("fragment3");
        fragment4 = intent1.getStringExtra("fragment4");
        if(intent1.hasExtra("Approach")){
            Approach = intent1.getStringExtra("Approach");
            NewsFeed_Num = intent1.getStringExtra("NewsFeed_Num");
        }
        realTime = new SimpleDateFormat("HHmm").format(new java.sql.Date(System.currentTimeMillis()));

        //gcm 데이터 등록
        Token = FirebaseInstanceId.getInstance().getToken();
        Log.i("Id", Id);
        Log.i("token", Token);
        String result = "";
        try {
            HttpClient client = new DefaultHttpClient();
            String postURL = "http://210.122.7.193:8080/Web_basket/Gcm_IdAdd_Alarm.jsp";
            HttpPost post = new HttpPost(postURL);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Id", Id));
            params.add(new BasicNameValuePair("Token", Token));

            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);

            HttpResponse response = client.execute(post);
            BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));

            String line = null;
            while ((line = bufreader.readLine()) != null) {
                result += line;
            }
            parsedData_BasicSetting = jsonParserList_BasicSetting(result);
            Log.i("결과", result);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("실패", "실패");
        }
        ///////////////////////////////////////////////////////////////////

        ////////////////////////////////건의사항 체크소스/////////////////////////////////////////////////

        result = "";
        try {
            HttpClient client = new DefaultHttpClient();
            String postURL = "http://210.122.7.193:8080/gg/recommend_download.jsp";
            HttpPost post = new HttpPost(postURL);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("User_Id", Id));
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);
            HttpResponse response = client.execute(post);
            BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
            String line = null;
            while ((line = bufreader.readLine()) != null) {
                result += line;
            }
            recommendparsedData = recommend_jsonParserList(result);
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (!(recommendparsedData[0][0].equals(null))) {
                final MaterialDialog recommendDialog = new MaterialDialog(MainActivity.this);
                recommendDialog
                        .setTitle("건의사항답변")
                        .setMessage(recommendparsedData[0][0])
                        .setPositiveButton("확인", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                recommendDialog.dismiss();
                                try {
                                    HttpClient client = new DefaultHttpClient();
                                    String postURL = "http://210.122.7.193:8080/gg/recommend_delete.jsp";
                                    HttpPost post = new HttpPost(postURL);
                                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                                    params.add(new BasicNameValuePair("User_Id", Id));
                                    UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                    post.setEntity(ent);
                                    HttpResponse response = null;
                                    response = client.execute(post);
                                    BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                recommendDialog.show();
            }
        } catch (ArrayIndexOutOfBoundsException e) {

        }
        ///////////////////////////////////////////////////////////////////////////////////////

        String result_profile = "";
        try {
            HttpClient client = new DefaultHttpClient();
            String postURL = "http://210.122.7.193:8080/Web_basket/Profile.jsp";
            HttpPost post = new HttpPost(postURL);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Id", Id));

            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);

            HttpResponse response = client.execute(post);
            BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));

            String line = null;
            while ((line = bufreader.readLine()) != null) {
                result_profile += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        parsedData_Profile = jsonParserList_UserInfo(result_profile);
        MyTeam = parsedData_Profile[0][6];
        Profile = parsedData_Profile[0][7];
        Height = parsedData_Profile[0][8];
        Weight = parsedData_Profile[0][9];
        interestArea_do = parsedData_Profile[0][10];
        interestArea_si = parsedData_Profile[0][11];
        /////////////////////////////////////////
        //일반 접근인 경우
        ///일반적 접근일 경우 처리
        ///jsp문 돌려서 어떤 접근인지 check
        String result_GameStatus = "";
        try {
            HttpClient client = new DefaultHttpClient();
            String postURL = "http://210.122.7.193:8080/Web_basket/GameStatus.jsp";
            HttpPost post = new HttpPost(postURL);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("MyTeam", MyTeam));
            params.add(new BasicNameValuePair("MyId", Id));
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);

            HttpResponse response = client.execute(post);
            BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));

            String line = null;
            while ((line = bufreader.readLine()) != null) {
                result_GameStatus += line;
            }
            parsedData_gameStatus = jsonParserList_GameStatus(result_GameStatus);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HomeTeam = parsedData_gameStatus[0][1];
        AwayTeam = parsedData_gameStatus[0][2];
        Authority = parsedData_gameStatus[0][6];
        //시합 신청한 팀으로 접근하는 경우
        if (HomeTeam.equals(MyTeam)) {
            if (parsedData_gameStatus[0][0].equals("NotGame")) {

            } else if (parsedData_gameStatus[0][0].equals(".")) {
                yourTeamStatus = "suggest";

                allowtime = Integer.parseInt(realTime) - Integer.parseInt(parsedData_gameStatus[0][5]);
                Log.i("realTime", Integer.toString(allowtime));
            } else if (parsedData_gameStatus[0][0].equals("allow")) {
                yourTeamStatus = "allow";
            } else if (parsedData_gameStatus[0][0].equals("ing")) {
                yourTeamStatus = "ing";
            }
            else if (parsedData_gameStatus[0][0].equals("refuse")) {
                yourTeamStatus = "refuse";
            }
//            else if (parsedData_gameStatus[0][0].equals("ScoreCheck")) {
//                yourTeamStatus = "ScoreCheck";
//            }
        }
        //시합 신청받은 팀으로 접근하는 경우
        else {
            if (parsedData_gameStatus[0][0].equals(".")) {
                if(parsedData_gameStatus[0][6].equals("1")){
                    final MaterialDialog DropOutDialog = new MaterialDialog(MainActivity.this);
                    DropOutDialog
                            .setTitle("시합신청")
                            .setMessage(HomeTeam + "팀에서 시합신청하였습니다.")
                            .setNegativeButton("취소", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String result = "";
                                    try {
                                        HttpClient client = new DefaultHttpClient();
                                        String postURL = "http://210.122.7.193:8080/Web_basket/GameRefuse.jsp";
                                        HttpPost post = new HttpPost(postURL);
                                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                                        params.add(new BasicNameValuePair("SendTeam", HomeTeam));
                                        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                        post.setEntity(ent);
                                        HttpResponse response = client.execute(post);
                                        BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                        String line = null;
                                        while ((line = bufreader.readLine()) != null) {
                                            result += line;
                                        }
                                        parsedData_gameRefuse = jsonParserList_gameDelete(result);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if (parsedData_gameRefuse[0][0].equals("succed")) {
                                        yourTeamStatus = "reset";
                                        DropOutDialog.dismiss();
                                    } else {
                                    }

                                }
                            })
                            .setPositiveButton("수락", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String result = "";
                                    try {
                                        HttpClient client = new DefaultHttpClient();
                                        String postURL = "http://210.122.7.193:8080/Web_basket/GameSucced.jsp";
                                        HttpPost post = new HttpPost(postURL);
                                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                                        params.add(new BasicNameValuePair("SendTeam", HomeTeam));
                                        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                        post.setEntity(ent);
                                        HttpResponse response = client.execute(post);
                                        BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                        String line = null;
                                        while ((line = bufreader.readLine()) != null) {
                                            result += line;
                                        }
                                        parsedData_gameRefuse = jsonParserList_gameDelete(result);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if (parsedData_gameRefuse[0][0].equals("succed")) {
                                        yourTeamStatus = "allow_away";
                                        DropOutDialog.dismiss();
                                        mViewPager.setCurrentItem(1);
                                        Contest_Layout_1.setVisibility(View.GONE);
                                        Contest_Layout_2.setVisibility(View.VISIBLE);
                                       /* Contest_Button_MyTeam.setText(HomeTeam);
                                        Contest_Button_YourTeam.setText(AwayTeam);
                                        Contest_Button_Start.setText("신청 접수중");
                                        Contest_Button_Cancel.setVisibility(View.VISIBLE);*/
                                        Log.i("asdf","asdf");
                                        Contest_Button_Start.setText("신청 접수중");
                                        Contest_Button_Cancel.setVisibility(View.VISIBLE);
                                        Contest_Button_MyTeam.setText(HomeTeam);
                                        Contest_Button_YourTeam.setText(MyTeam);
                                        Contest_Button_YourTeam.setEnabled(false);
                                        myTask = new TimerTask() {
                                            int i = 300;
                                            public void run() {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Contest_Button_Start.setText("신청 접수중 " + i);
                                                        if (i % 3 == 0) {
                                                            String result = "";
                                                            try {
                                                                HttpClient client = new DefaultHttpClient();
                                                                String postURL = "http://210.122.7.193:8080/Web_basket/GameStatusCheck.jsp";
                                                                HttpPost post = new HttpPost(postURL);
                                                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                                                params.add(new BasicNameValuePair("SendTeam", HomeTeam));
                                                                params.add(new BasicNameValuePair("ReceiveTeam", MyTeam));
                                                                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                                                post.setEntity(ent);
                                                                HttpResponse response = client.execute(post);
                                                                BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                                                String line = null;
                                                                while ((line = bufreader.readLine()) != null) {
                                                                    result += line;
                                                                }
                                                                parsedData_gameStatus = jsonParserList_gameDelete(result);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                            if (parsedData_gameStatus[0][0].equals(".")) {
                                                                Log.i("bbbbb", "bbbbb");
                                                            } else if (parsedData_gameStatus[0][0].equals("ing")) {
                                                                timer.cancel();
                                                                Contest_Button_Start.setText("시합 중");
                                                                Contest_Button_Start.setTextColor(Color.WHITE);
                                                                Contest_Button_Start.setBackgroundColor(Color.RED);
                                                                yourTeamStatus = "ing";
                                                            }
                                                        }
                                                    }
                                                });
                                                i--;
                                                //시간이 초과된 경우 game 내 데이터 삭제 및 초기화.
                                                if (i == 1) {
                                                    timer.cancel();
                                                    String result = "";
                                                    try {
                                                        HttpClient client = new DefaultHttpClient();
                                                        String postURL = "http://210.122.7.193:8080/Web_basket/GameDelete.jsp";
                                                        HttpPost post = new HttpPost(postURL);
                                                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                                                        params.add(new BasicNameValuePair("SendTeam", MyTeam));
                                                        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                                        post.setEntity(ent);
                                                        HttpResponse response = client.execute(post);
                                                        BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                                        String line = null;
                                                        while ((line = bufreader.readLine()) != null) {
                                                            result += line;
                                                        }
                                                        parsedData_gameDelete = jsonParserList_gameDelete(result);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                    if (parsedData_gameDelete[0][0].equals("succed")) {
                                                       runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                yourTeamStatus = "reset";
                                                                Contest_Button_Cancel.setVisibility(View.GONE);
                                                                Contest_Button_MyTeam.setText(Team1);
                                                                Contest_Button_YourTeam.setText("팀 찾기");
                                                                Contest_Button_YourTeam.setEnabled(true);
                                                                Contest_Button_Start.setText("시합신청");
                                                                Contest_Button_Start.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                                            }
                                                        });
                                                    }
                                                    if (layout != null) {
                                                        ViewGroup parentViewGroup = (ViewGroup) layout.getParent();
                                                        if (null != parentViewGroup) {
                                                            parentViewGroup.removeView(layout);
                                                        }
                                                    }
                                                }
                                            }
                                        };
                                        timer = new Timer();
                                        //timer.schedule(myTask, 5000);  // 5초후 실행하고 종료
                                        timer.schedule(myTask, 500, 1000); // 5초후 첫실행, 3초마다 계속실행
                                    } else {

                                    }
                                    DropOutDialog.dismiss();
                                }
                            });
                    DropOutDialog.show();
                }
            }
            else if (parsedData_gameStatus[0][0].equals("allow")) {
                yourTeamStatus = "allow_away";
            }
            else if (parsedData_gameStatus[0][0].equals("ing")) {
                yourTeamStatus = "ing";
            }
        }
       if (parsedData_gameStatus[0][0].equals("ScoreCheck")) {
           if(parsedData_gameStatus[0][6].equals("1")) {
               String result_ScoreCheck = "";
               try {
                   HttpClient client = new DefaultHttpClient();
                   String postURL = "http://210.122.7.193:8080/Web_basket/GameScoreInfo.jsp";
                   HttpPost post = new HttpPost(postURL);
                   List<NameValuePair> params = new ArrayList<NameValuePair>();
                   params.add(new BasicNameValuePair("SendTeam", parsedData_Profile[0][6]));
                   UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                   post.setEntity(ent);
                   HttpResponse response = client.execute(post);
                   BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                   String line = null;
                   while ((line = bufreader.readLine()) != null) {
                       result_ScoreCheck += line;
                   }
                   parsedData_gameScoreInfo = jsonParserList_gameScoreInfo(result_ScoreCheck);
               } catch (Exception e) {
                   e.printStackTrace();
               }
               if (parsedData_gameScoreInfo[0][4].equals(MyTeam)) {
                   yourTeamStatus = "ScoreCheck";
               } else {
                   final String HomeTeam = parsedData_gameScoreInfo[0][0];
                   final String AwayTeam = parsedData_gameScoreInfo[0][1];
                   final String HomeScore = parsedData_gameScoreInfo[0][2];
                   final String AwayScore = parsedData_gameScoreInfo[0][3];

                   final Button Layout_CustomDialog_GameScoreInfo_Button_HomeTeam = (Button) layout_GameScoreInfo.findViewById(R.id.Layout_CustomDialog_GameScoreInfo_Button_HomeTeam);
                   final TextView Layout_CustomDialog_GameScoreInfo_TextView_HomeTeam = (TextView) layout_GameScoreInfo.findViewById(R.id.Layout_CustomDialog_GameScoreInfo_TextView_HomeTeam);
                   final Button Layout_CustomDialog_GameScoreInfo_Button_AwayTeam = (Button) layout_GameScoreInfo.findViewById(R.id.Layout_CustomDialog_GameScoreInfo_Button_AwayTeam);
                   final TextView Layout_CustomDialog_GameScoreInfo_TextView_AwayTeam = (TextView) layout_GameScoreInfo.findViewById(R.id.Layout_CustomDialog_GameScoreInfo_TextView_AwayTeam);

                   Layout_CustomDialog_GameScoreInfo_Button_HomeTeam.setText(HomeTeam);
                   Layout_CustomDialog_GameScoreInfo_TextView_HomeTeam.setText(HomeScore);
                   Layout_CustomDialog_GameScoreInfo_Button_AwayTeam.setText(AwayTeam);
                   Layout_CustomDialog_GameScoreInfo_TextView_AwayTeam.setText(AwayScore);

                   final MaterialDialog DropOutDialog = new MaterialDialog(MainActivity.this);
                   DropOutDialog
                           .setTitle("시합점수 확인")
                           .setView(layout_GameScoreInfo)
                           .setNegativeButton("취소", new View.OnClickListener() {
                               @Override
                               public void onClick(View v) {
                                   String result_ScoreCheck_refuse = "";
                                   String Orderteam = "";
                                   try {
                                       if (HomeTeam.equals(MyTeam)) {
                                           Orderteam = AwayTeam;
                                       }
                                       if (AwayTeam.equals(MyTeam)) {
                                           Orderteam = HomeTeam;
                                       }
                                       HttpClient client = new DefaultHttpClient();
                                       String postURL = "http://210.122.7.193:8080/Web_basket/GameScoreInfoRefuse.jsp";
                                       HttpPost post = new HttpPost(postURL);
                                       List<NameValuePair> params = new ArrayList<NameValuePair>();
                                       params.add(new BasicNameValuePair("Myteam", parsedData_Profile[0][6]));
                                       Log.i("OrderTema",Orderteam);
                                       params.add(new BasicNameValuePair("Orderteam", Orderteam));
                                       UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                       post.setEntity(ent);
                                       HttpResponse response = client.execute(post);
                                       BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                       String line = null;
                                       while ((line = bufreader.readLine()) != null) {
                                           result_ScoreCheck_refuse += line;
                                       }
                                       parsedData_gameScoreInfo_succed = jsonParserList_BasicSetting(result_ScoreCheck_refuse);
                                   } catch (Exception e) {
                                       e.printStackTrace();
                                   }
                                   DropOutDialog.dismiss();
                               }
                           })
                           .setPositiveButton("확인", new View.OnClickListener() {
                               @Override
                               public void onClick(View view) {
                                   String result_ScoreCheck_agree = "";
                                   try {
                                       HttpClient client = new DefaultHttpClient();
                                       String postURL = "http://210.122.7.193:8080/Web_basket/GameScoreInfoAgree.jsp";
                                       HttpPost post = new HttpPost(postURL);
                                       List<NameValuePair> params = new ArrayList<NameValuePair>();
                                       params.add(new BasicNameValuePair("hometeamName", HomeTeam));
                                       params.add(new BasicNameValuePair("awayteamName", AwayTeam));
                                       params.add(new BasicNameValuePair("hometeamScore", HomeScore));
                                       params.add(new BasicNameValuePair("awayteamScore", AwayScore));
                                       params.add(new BasicNameValuePair("myteam", parsedData_Profile[0][6]));
                                       params.add(new BasicNameValuePair("Date", now_Date));
                                       UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                       post.setEntity(ent);
                                       HttpResponse response = client.execute(post);
                                       BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                       String line = null;
                                       while ((line = bufreader.readLine()) != null) {
                                           result_ScoreCheck_agree += line;
                                       }
                                       parsedData_gameScoreInfo_succed = jsonParserList_BasicSetting(result_ScoreCheck_agree);
                                   } catch (Exception e) {
                                       e.printStackTrace();
                                   }
                                   DropOutDialog.dismiss();
                               }
                           }).show();
               }
           }
        }
        if(Approach.equals("comment")) {
            Log.i("test",NewsFeed_Num);
            try {
                HttpClient client = new DefaultHttpClient();
                String postURL = "http://210.122.7.193:8080/pp/Comment_Direct.jsp";
                HttpPost post = new HttpPost(postURL);
                List<NameValuePair> params1 = new ArrayList<NameValuePair>();
                params1.add(new BasicNameValuePair("NewsFeed_Num", NewsFeed_Num));

                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params1, HTTP.UTF_8);
                post.setEntity(ent);
                HttpResponse response = client.execute(post);
                BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));

                String line = null;
                String result_comment = "";
                while ((line = bufreader.readLine()) != null) {
                    result_comment += line;
                }
                parsedData_CommentDirect = jsonParserList_CommentDirect(result_comment);
            } catch (IOException e) {
            }
            String NewsFeed_Do = parsedData_CommentDirect[0][0];
            String NewsFeed_Si = parsedData_CommentDirect[0][1];
            String NewsFeed_Court = parsedData_CommentDirect[0][2];
            String NewsFeed_Name = parsedData_CommentDirect[0][3];
            String NewsFeed_User = parsedData_CommentDirect[0][4];
            String NewsFeed_Data = parsedData_CommentDirect[0][5];
            String NewsFeed_Month = parsedData_CommentDirect[0][6];
            String NewsFeed_Day = parsedData_CommentDirect[0][7];
            String NewsFeed_Hour = parsedData_CommentDirect[0][8];
            String NewsFeed_Minute = parsedData_CommentDirect[0][9];
            String NewsFeed_Image = parsedData_CommentDirect[0][10];
            Intent CommentIntent = new Intent(MainActivity.this, Match_Out_NewsFeed_Comment.class);
            CommentIntent.putExtra("Num", NewsFeed_Num);
            CommentIntent.putExtra("Court", NewsFeed_Court);
            CommentIntent.putExtra("Name", NewsFeed_Name);
            CommentIntent.putExtra("Data", NewsFeed_Data);
            CommentIntent.putExtra("Time", NewsFeed_Month + "월 " + NewsFeed_Day + "일 " + NewsFeed_Hour + "시 " + NewsFeed_Minute + "분 ");
            CommentIntent.putExtra("Id", Id);
            CommentIntent.putExtra("profile", Profile);
            CommentIntent.putExtra("Image", NewsFeed_Image);
            startActivity(CommentIntent);
        }
    }
////건의사항 체크 파서리스트
public String[][] recommend_jsonParserList(String pRecvServerPage) {
    Log.i("recommend에서 받은 전체 내용", pRecvServerPage);
    try {
        JSONObject json = new JSONObject(pRecvServerPage);
        JSONArray jArr = json.getJSONArray("List");

        String[] jsonName = {"recommend_answer"};
        json = new JSONObject(pRecvServerPage);
        recommendparsedData = new String[jArr.length()][jsonName.length];
        for (int i = 0; i < jArr.length(); i++) {
            json = jArr.getJSONObject(i);
            for (int j = 0; j < jsonName.length; j++) {
                recommendparsedData[i][j] = json.getString(jsonName[j]);
            }
        }
        return recommendparsedData;
    } catch (JSONException e) {
        return null;
    }
}
    ///////game상태 접근법 파서
    /////프로필 탭 사용자정보를 파싱합니다.//////////////////////////////////////////////////////////
    public String[][] jsonParserList_GameStatus(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");

            String[] jsonName = {"msg1", "msg2", "msg3", "msg4", "msg5", "msg6","msg7"};
            String[][] parseredData = new String[jArr.length()][jsonName.length];
            for (int i = 0; i < jArr.length(); i++) {
                json = jArr.getJSONObject(i);
                for (int j = 0; j < jsonName.length; j++) {
                    parseredData[i][j] = json.getString(jsonName[j]);
                }
            }
            return parseredData;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /////프로필 탭 사용자정보를 파싱합니다.//////////////////////////////////////////////////////////
    public String[][] jsonParserList_UserInfo(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");

            String[] jsonName = {"msg1", "msg2", "msg3", "msg4", "msg5", "msg6", "msg7", "msg8","msg9","msg10","msg11","msg12"};
            String[][] parseredData = new String[jArr.length()][jsonName.length];
            for (int i = 0; i < jArr.length(); i++) {
                json = jArr.getJSONObject(i);
                for (int j = 0; j < jsonName.length; j++) {
                    parseredData[i][j] = json.getString(jsonName[j]);
                }
            }
            return parseredData;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String[][] jsonParserList_gameDelete(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");

            String[] jsonName = {"msg1"};
            String[][] parseredData = new String[jArr.length()][jsonName.length];
            for (int i = 0; i < jArr.length(); i++) {
                json = jArr.getJSONObject(i);
                for (int j = 0; j < jsonName.length; j++) {
                    parseredData[i][j] = json.getString(jsonName[j]);
                }
            }
            return parseredData;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String[][] jsonParserList_BasicSetting(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");

            String[] jsonName = {"msg1"};
            String[][] parseredData = new String[jArr.length()][jsonName.length];
            for (int i = 0; i < jArr.length(); i++) {
                json = jArr.getJSONObject(i);
                for (int j = 0; j < jsonName.length; j++) {
                    parseredData[i][j] = json.getString(jsonName[j]);
                }
            }
            return parseredData;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String[][] jsonParserList_gameScoreInfo(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");

            String[] jsonName = {"msg1", "msg2", "msg3", "msg4", "msg5"};
            String[][] parseredData = new String[jArr.length()][jsonName.length];
            for (int i = 0; i < jArr.length(); i++) {
                json = jArr.getJSONObject(i);
                for (int j = 0; j < jsonName.length; j++) {
                    parseredData[i][j] = json.getString(jsonName[j]);
                }
            }
            return parseredData;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    /////댓글 푸시알림으로 접근한 경우, 데이터를 파싱합니다.//////////////////////////////////////////////////////////
    public String[][] jsonParserList_CommentDirect(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");

            String[] jsonName = {"Do", "Si", "Court", "Name", "User", "Data","Month","Day","Hour","Minute","Image"};
            String[][] parseredData = new String[jArr.length()][jsonName.length];
            for (int i = 0; i < jArr.length(); i++) {
                json = jArr.getJSONObject(i);
                for (int j = 0; j < jsonName.length; j++) {
                    parseredData[i][j] = json.getString(jsonName[j]);
                }
            }
            return parseredData;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                new AlertDialog.Builder(this)
                        .setTitle("바스켓북")
                        .setMessage("어플리케이션을 종료 하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                finish();
                            }
                        })
                        .setNegativeButton("아니오", null).show();
                return false;
            default:
                return false;
        }
    }

    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            return rootView;
        }
    }

    ////탭 아이콘 탭에 저장.//////////////////////////////////
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }

    private void setupTabIcons_match() {
        tabLayout.getTabAt(0).setIcon(tabIcons_match[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons_match[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons_match[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons_match[3]);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            // return PlaceholderFragment.newInstance(position + 1);
            Fragment fragment = null;
            Bundle args = null;
            switch (position) {
                case 0:
                    fragment = new SectionsFragment1();
                    args = new Bundle();
                    break;
                case 1:
                    fragment = new SectionsFragment2();
                    args = new Bundle();
                    break;
                case 2:
                    fragment = new SectionsFragment3();
                    args = new Bundle();
                    break;
                case 3:
                    fragment = new SectionsFragment4();
                    args = new Bundle();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }

    }

    public String[][] jsonParserList(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");

            String[] jsonName = {"msg1"};
            String[][] parseredData = new String[jArr.length()][jsonName.length];
            for (int i = 0; i < jArr.length(); i++) {
                json = jArr.getJSONObject(i);
                for (int j = 0; j < jsonName.length; j++) {
                    parseredData[i][j] = json.getString(jsonName[j]);
                }
            }
            return parseredData;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        try {
            //인텐트에 데이터가 담겨 왔다면
            if (!intent.getData().equals(null)) {
                //해당경로의 이미지를 intent에 담긴 이미지 uri를 이용해서 Bitmap형태로 읽어온다.
                Bitmap selPhoto = MediaStore.Images.Media.getBitmap(getContentResolver(), intent.getData());
                //이미지의 크기 조절하기.
                selPhoto = Bitmap.createScaledBitmap(selPhoto, 100, 100, true);
                //image_bt.setImageBitmap(selPhoto);//썸네일
                //화면에 출력해본다.
                //Profile_ImageVIew_Profile.setImageBitmap(selPhoto);
                Log.e("선택 된 이미지 ", "selPhoto : " + selPhoto);

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                //선택한 이미지의 uri를 읽어온다.
                Uri selPhotoUri = intent.getData();
                Log.e("전송", "시~~작 ~~~~~!");
                //업로드할 서버의 url 주소
                String urlString = "";
                urlString = "http://210.122.7.193:8080/Web_basket/Upload_Profile.jsp";
                //절대경로를 획득한다!!! 중요~
                Cursor c = getContentResolver().query(Uri.parse(selPhotoUri.toString()), null, null, null, null);
                c.moveToNext();
                //업로드할 파일의 절대경로 얻어오기("_data") 로 해도 된다.
                String absolutePath = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
                Log.e("###파일의 절대 경로###", absolutePath);
                //파일 업로드 시작!
                HttpFileUpload(urlString, "", absolutePath);
                String En_Profile = URLEncoder.encode(Id, "utf-8");
                Glide.with(MainActivity.this).load("http://210.122.7.193:8080/Web_basket/imgs/Profile/" + En_Profile + ".jpg").bitmapTransform(new CropCircleTransformation(Glide.get(MainActivity.this).getBitmapPool()))
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(Profile_ImageVIew_Profile);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {

        }

    }

    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";

    public void HttpFileUpload(String urlString, String params, String fileName) {
        // fileName=TeamName;
        try {
            //선택한 파일의 절대 경로를 이용해서 파일 입력 스트림 객체를 얻어온다.
            FileInputStream mFileInputStream = new FileInputStream(fileName);
            //파일을 업로드할 서버의 url 주소를이용해서 URL 객체 생성하기.
            URL connectUrl = new URL(urlString);
            //Connection 객체 얻어오기.
            HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
            conn.setDoInput(true);//입력할수 있도록
            conn.setDoOutput(true); //출력할수 있도록
            conn.setUseCaches(false);  //캐쉬 사용하지 않음

            //post 전송
            conn.setRequestMethod("POST");
            //파일 업로드 할수 있도록 설정하기.
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            //DataOutputStream 객체 생성하기.
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            //전송할 데이터의 시작임을 알린다.
            //String En_TeamName = URLEncoder.encode(TeamName, "utf-8");
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + URLEncoder.encode(Id, "utf-8") + ".jpg" + "\"" + lineEnd);
            dos.writeBytes(lineEnd);
            //한번에 읽어들일수있는 스트림의 크기를 얻어온다.
            int bytesAvailable = mFileInputStream.available();
            //byte단위로 읽어오기 위하여 byte 배열 객체를 준비한다.
            byte[] buffer = new byte[bytesAvailable];
            int bytesRead = 0;
            // read image
            while (bytesRead != -1) {
                //파일에서 바이트단위로 읽어온다.
                bytesRead = mFileInputStream.read(buffer);
                if (bytesRead == -1) break; //더이상 읽을 데이터가 없다면 빠저나온다.
                Log.d("Test", "image byte is " + bytesRead);
                //읽은만큼 출력한다.
                dos.write(buffer, 0, bytesRead);
                //출력한 데이터 밀어내기
                dos.flush();
            }
            //전송할 데이터의 끝임을 알린다.
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            //flush() 타이밍??
            //dos.flush();
            dos.close();//스트림 닫아주기
            mFileInputStream.close();//스트림 닫아주기.
            // get response
            int ch;
            //입력 스트림 객체를 얻어온다.
            InputStream is = conn.getInputStream();
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            String s = b.toString();
            Log.e("Test", "result = " + s);

        } catch (Exception e) {
            Log.d("Test", "exception " + e.getMessage());
            Snackbar.make(rootView, "업로드중 에러발생!.", Snackbar.LENGTH_SHORT).show();

        }
    }

}
