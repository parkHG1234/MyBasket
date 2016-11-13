package com.mysports.basketbook;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

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
import java.util.Timer;
import java.util.TimerTask;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by ldong on 2016-11-12.
 */
public class SectionsFragment2 extends Fragment {
    Button Contest_Button_Tab1, Contest_Button_Tab2;

//교류전 게임 선언

    String TabChoice = "1";
    String Do, Si, Team;
    int spinnum1, spinnum2, spinnum3;
    ArrayAdapter<CharSequence> adspin1, adspin2, adspin3;
    String[][] parsedData, parsedData_gameGenerate, parsedData_gameDelete, parsedData_gameStatus, parsedData_gameScoreAdd, parsedData_TeamPersonCount;
    ArrayList arr;
    static TimerTask myTask;
    static Timer timer;
    ListView Contest_ListView_contest;

    //////////////////////////////////////////////
    public SectionsFragment2() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.layout_contest, container, false);
        /////////////탭 선언 연결
        Contest_Button_Tab1 = (Button) rootView.findViewById(R.id.Contest_Button_Tab1);
        Contest_Button_Tab2 = (Button) rootView.findViewById(R.id.Contest_Button_Tab2);
        MainActivity.Contest_Layout_1 = (LinearLayout) rootView.findViewById(R.id.Contest_Layout_1);
        MainActivity.Contest_Layout_2 = (LinearLayout) rootView.findViewById(R.id.Contest_Layout_2);
////////////////게임 선언 연결
        MainActivity.Contest_Button_MyTeam = (Button) rootView.findViewById(R.id.Contest_Button_MyTeam);
        MainActivity.Contest_Button_YourTeam = (Button) rootView.findViewById(R.id.Contest_Button_YourTeam);
        MainActivity.Contest_Button_Start = (Button) rootView.findViewById(R.id.Contest_Button_Start);
        MainActivity.Contest_Button_Cancel = (Button) rootView.findViewById(R.id.Contest_Button_Cancel);
        //////////////////////////

        Contest_Button_Tab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.Contest_Layout_1.setVisibility(View.VISIBLE);
                MainActivity.Contest_Layout_2.setVisibility(View.GONE);

                String result = "";
                try {
                    HttpClient client = new DefaultHttpClient();
                    String postURL = "http://210.122.7.193:8080/pp/Contests_Customlist_all.jsp";
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String[][] ContestsParsedList = jsonParserList_getContestsList(result);

                ArrayList<Contests_Customlist_MyData> Contests_Customlist_MyData;
                Contests_Customlist_MyData = new ArrayList<Contests_Customlist_MyData>();
                for (int i = 0; i < ContestsParsedList.length; i++) {
                    Contests_Customlist_MyData.add(new Contests_Customlist_MyData(ContestsParsedList[i][1], ContestsParsedList[i][3], ContestsParsedList[i][2], ContestsParsedList[i][4], ContestsParsedList[i][5], ContestsParsedList[i][6], ContestsParsedList[i][0]));
                }
                Contest_ListView_contest = (ListView) rootView.findViewById(R.id.Contest_ListView_contests);
                Contests_Customlist_Adapter Adapter = new Contests_Customlist_Adapter(rootView.getContext(), Contests_Customlist_MyData);
                Contest_ListView_contest.setAdapter(Adapter);

                Contest_ListView_contest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Object o = Contest_ListView_contest.getItemAtPosition(i);
                        String b = String.valueOf(o);
                        System.out.println(i);

                        Intent intent = new Intent(getContext(), Contest_Detail.class);
                        String Pk = Integer.toString(i+1);
                        intent.putExtra("position", Pk);
                        startActivity(intent);
                    }
                });


            }
        });
        Contest_Button_Tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.Contest_Layout_1.setVisibility(View.GONE);
                MainActivity.Contest_Layout_2.setVisibility(View.VISIBLE);

            }
        });


        ///교류전 코딩
        final Spinner Layout_CustomDialog_teamChoice_Do = (Spinner) MainActivity.layout.findViewById(R.id.Layout_CustomDialog_teamChoice_Do);
        final Spinner Layout_CustomDialog_teamChoice_Se = (Spinner) MainActivity.layout.findViewById(R.id.Layout_CustomDialog_teamChoice_Se);
        final Spinner Layout_CustomDialog_teamChoice_team = (Spinner) MainActivity.layout.findViewById(R.id.Layout_CustomDialog_teamChoice_team);

        MainActivity.Contest_Button_MyTeam.setText(MainActivity.MyTeam);
        Log.i("asdf", "asdf");
        if (MainActivity.Authority.equals("1")) {
            if (MainActivity.yourTeamStatus.equals("allow_away")) {
                Log.i("asdf", "asdf");
                Team = MainActivity.HomeTeam;
                MainActivity.Contest_Button_Start.setText("신청 접수중");
                MainActivity.Contest_Button_Cancel.setVisibility(View.VISIBLE);
                MainActivity.Contest_Button_MyTeam.setText(Team);
                MainActivity.Contest_Button_YourTeam.setText(MainActivity.MyTeam);
                MainActivity.Contest_Button_YourTeam.setEnabled(false);
                myTask = new TimerTask() {
                    int i = 300;

                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MainActivity.Contest_Button_Start.setText("신청 접수중 " + i);
                                if (i % 3 == 0) {
                                    String result = "";
                                    try {
                                        HttpClient client = new DefaultHttpClient();
                                        String postURL = "http://210.122.7.193:8080/Web_basket/GameStatusCheck.jsp";
                                        HttpPost post = new HttpPost(postURL);
                                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                                        params.add(new BasicNameValuePair("SendTeam", Team));
                                        params.add(new BasicNameValuePair("ReceiveTeam", MainActivity.MyTeam));
                                        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                        post.setEntity(ent);
                                        HttpResponse response = client.execute(post);
                                        BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                        String line = null;
                                        while ((line = bufreader.readLine()) != null) {
                                            result += line;
                                        }
                                        parsedData_gameStatus = jsonParserList_gameGenerate(result);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if (parsedData_gameStatus[0][0].equals(".")) {
                                        Log.i("bbbbb", "bbbbb");
                                    } else if (parsedData_gameStatus[0][0].equals("ing")) {
                                        timer.cancel();
                                        MainActivity.Contest_Button_Start.setText("시합 중");
                                        MainActivity.Contest_Button_Start.setTextColor(Color.WHITE);
                                        MainActivity.Contest_Button_Start.setBackgroundColor(Color.RED);
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
                                params.add(new BasicNameValuePair("SendTeam", MainActivity.MyTeam));
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
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        MainActivity.yourTeamStatus = "reset";
                                        MainActivity.Contest_Button_Cancel.setVisibility(View.GONE);
                                        MainActivity.Contest_Button_MyTeam.setText(MainActivity.Team1);
                                        MainActivity.Contest_Button_YourTeam.setText("팀 찾기");
                                        MainActivity.Contest_Button_YourTeam.setEnabled(true);
                                        MainActivity.Contest_Button_Start.setText("시합신청");
                                        MainActivity.Contest_Button_Start.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                    }
                                });
                            }
                            if (MainActivity.layout != null) {
                                ViewGroup parentViewGroup = (ViewGroup) MainActivity.layout.getParent();
                                if (null != parentViewGroup) {
                                    parentViewGroup.removeView(MainActivity.layout);
                                }
                            }
                        }
                    }
                };
                timer = new Timer();
                //timer.schedule(myTask, 5000);  // 5초후 실행하고 종료
                timer.schedule(myTask, 500, 1000); // 5초후 첫실행, 3초마다 계속실행
            }
            // int a=300
            else if (MainActivity.yourTeamStatus.equals("suggest")) {
                Team = MainActivity.AwayTeam;
                MainActivity.Contest_Button_Cancel.setVisibility(View.VISIBLE);
                MainActivity.Contest_Button_MyTeam.setText(MainActivity.HomeTeam);
                MainActivity.Contest_Button_YourTeam.setText(MainActivity.AwayTeam);
                MainActivity.Contest_Button_YourTeam.setEnabled(false);
                myTask = new TimerTask() {
                    int i = 300 - (MainActivity.allowtime * 60);

                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 해당 작업을 처리함
                                MainActivity.Contest_Button_Start.setText("신청 중 " + i);
                                if (i % 3 == 0) {
                                    String result = "";
                                    try {
                                        HttpClient client = new DefaultHttpClient();
                                        String postURL = "http://210.122.7.193:8080/Web_basket/GameStatusCheck.jsp";
                                        HttpPost post = new HttpPost(postURL);
                                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                                        params.add(new BasicNameValuePair("SendTeam", MainActivity.MyTeam));
                                        params.add(new BasicNameValuePair("ReceiveTeam", Team));
                                        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                        post.setEntity(ent);
                                        HttpResponse response = client.execute(post);
                                        BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                        String line = null;
                                        while ((line = bufreader.readLine()) != null) {
                                            result += line;
                                        }
                                        parsedData_gameStatus = jsonParserList_gameGenerate(result);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if (parsedData_gameStatus[0][0].equals(".")) {
                                        Log.i("bbbbb", "bbbbb");
                                    }
                                    ///시합 신청이 수락된 경우
                                    else if (parsedData_gameStatus[0][0].equals("allow")) {
                                        timer.cancel();
                                        final MaterialDialog DropOutDialog = new MaterialDialog(rootView.getContext());
                                        DropOutDialog
                                                .setTitle("시합신청")
                                                .setMessage(Team + "팀에서 시합신청을 수락하였습니다.")
                                                .setNegativeButton("시합하기", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        String result = "";
                                                        try {
                                                            HttpClient client = new DefaultHttpClient();
                                                            String postURL = "http://210.122.7.193:8080/Web_basket/Gameing.jsp";
                                                            HttpPost post = new HttpPost(postURL);
                                                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                                                            params.add(new BasicNameValuePair("SendTeam", MainActivity.MyTeam));
                                                            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                                            post.setEntity(ent);
                                                            HttpResponse response = client.execute(post);
                                                            BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                                            String line = null;
                                                            while ((line = bufreader.readLine()) != null) {
                                                                result += line;
                                                            }
                                                            parsedData_gameStatus = jsonParserList_gameGenerate(result);
                                                        } catch (Exception e) {

                                                            e.printStackTrace();
                                                        }
                                                        if (parsedData_gameStatus[0][0].equals("succed")) {
                                                            MainActivity.Contest_Button_Start.setText("시합 중");
                                                            MainActivity.Contest_Button_Start.setTextColor(Color.WHITE);
                                                            MainActivity.Contest_Button_Start.setBackgroundColor(Color.RED);
                                                            DropOutDialog.dismiss();
                                                        }
                                                    }
                                                });
                                        DropOutDialog.show();
                                    }
                                    //시합 신청이 거절된 경우
                                    else if (parsedData_gameStatus[0][0].equals("refuse")) {
                                        timer.cancel();
                                        final MaterialDialog DropOutDialog = new MaterialDialog(rootView.getContext());
                                        DropOutDialog
                                                .setTitle("시합신청")
                                                .setMessage(Team + "팀에서 시합신청을 거절하였습니다.")
                                                .setNegativeButton("확인", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        String result = "";
                                                        try {
                                                            HttpClient client = new DefaultHttpClient();
                                                            String postURL = "http://210.122.7.193:8080/Web_basket/GameDelete.jsp";
                                                            HttpPost post = new HttpPost(postURL);
                                                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                                                            params.add(new BasicNameValuePair("SendTeam", MainActivity.MyTeam));
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
                                                            getActivity().runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    MainActivity.yourTeamStatus = "reset";
                                                                    MainActivity.Contest_Button_Cancel.setVisibility(View.GONE);
                                                                    MainActivity.Contest_Button_YourTeam.setText("팀 찾기");
                                                                    MainActivity.Contest_Button_YourTeam.setEnabled(true);
                                                                    MainActivity.Contest_Button_Start.setText("시합신청");
                                                                    MainActivity.Contest_Button_Start.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                                                }
                                                            });
                                                        }
                                                        if (MainActivity.layout != null) {
                                                            ViewGroup parentViewGroup = (ViewGroup) MainActivity.layout.getParent();
                                                            if (null != parentViewGroup) {
                                                                parentViewGroup.removeView(MainActivity.layout);
                                                            }
                                                        }
                                                        DropOutDialog.dismiss();
                                                    }
                                                });
                                        DropOutDialog.show();
                                    }
                                }
                            }
                        });

                        Log.d("myTask", Integer.toString(i));
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
                                params.add(new BasicNameValuePair("SendTeam", MainActivity.MyTeam));
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
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        MainActivity.yourTeamStatus = "reset";
                                        MainActivity.Contest_Button_Cancel.setVisibility(View.GONE);
                                        MainActivity.Contest_Button_YourTeam.setText("팀 찾기");
                                        MainActivity.Contest_Button_YourTeam.setEnabled(true);
                                        MainActivity.Contest_Button_Start.setText("시합신청");
                                        MainActivity.Contest_Button_Start.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                    }
                                });
                            }
                            if (MainActivity.layout != null) {
                                ViewGroup parentViewGroup = (ViewGroup) MainActivity.layout.getParent();
                                if (null != parentViewGroup) {
                                    parentViewGroup.removeView(MainActivity.layout);
                                }
                            }
                        }
                    }
                };
                timer = new Timer();
                //timer.schedule(myTask, 5000);  // 5초후 실행하고 종료
                timer.schedule(myTask, 500, 1000); // 5초후 첫실행, 3초마다 계속실행
            } else if (MainActivity.yourTeamStatus.equals("allow")) {
                final MaterialDialog DropOutDialog = new MaterialDialog(rootView.getContext());
                DropOutDialog
                        .setTitle("시합신청")
                        .setMessage(Team + "팀에서 시합신청을 수락하였습니다.")
                        .setNegativeButton("시합하기", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String result = "";
                                try {
                                    HttpClient client = new DefaultHttpClient();
                                    String postURL = "http://210.122.7.193:8080/Web_basket/Gameing.jsp";
                                    HttpPost post = new HttpPost(postURL);
                                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                                    params.add(new BasicNameValuePair("SendTeam", MainActivity.MyTeam));
                                    UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                    post.setEntity(ent);
                                    HttpResponse response = client.execute(post);
                                    BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                    String line = null;
                                    while ((line = bufreader.readLine()) != null) {
                                        result += line;
                                    }
                                    parsedData_gameStatus = jsonParserList_gameGenerate(result);
                                } catch (Exception e) {

                                    e.printStackTrace();
                                }
                                if (parsedData_gameStatus[0][0].equals("succed")) {
                                    MainActivity.Contest_Button_Start.setText("시합 중");
                                    MainActivity.Contest_Button_Start.setTextColor(Color.WHITE);
                                    MainActivity.Contest_Button_Start.setBackgroundColor(Color.RED);
                                    DropOutDialog.dismiss();
                                }
                            }
                        });
                DropOutDialog.show();
            } else if (MainActivity.yourTeamStatus.equals("ing")) {
                MainActivity.Contest_Button_Cancel.setVisibility(View.VISIBLE);
                MainActivity.Contest_Button_MyTeam.setText(MainActivity.HomeTeam);
                MainActivity.Contest_Button_YourTeam.setText(MainActivity.AwayTeam);
                MainActivity.Contest_Button_YourTeam.setEnabled(false);
                MainActivity.Contest_Button_Start.setText("시합 중");
                MainActivity.Contest_Button_Start.setTextColor(Color.WHITE);
                MainActivity.Contest_Button_Start.setBackgroundColor(Color.RED);
            } else if (MainActivity.yourTeamStatus.equals("ScoreCheck")) {
                MainActivity.Contest_Button_Cancel.setVisibility(View.GONE);
                MainActivity.Contest_Button_MyTeam.setText(MainActivity.HomeTeam);
                MainActivity.Contest_Button_YourTeam.setText(MainActivity.AwayTeam);
                MainActivity.Contest_Button_YourTeam.setEnabled(false);
                MainActivity.Contest_Button_Start.setText("완료 대기중");
                MainActivity.Contest_Button_Start.setTextColor(Color.WHITE);
                MainActivity.Contest_Button_Start.setBackgroundColor(Color.RED);
            } else if (MainActivity.yourTeamStatus.equals("refuse")) {
                final MaterialDialog DropOutDialog = new MaterialDialog(rootView.getContext());
                DropOutDialog
                        .setTitle("시합신청")
                        .setMessage(MainActivity.AwayTeam + "팀에서 시합신청을 거절하였습니다.")
                        .setNegativeButton("확인", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String result = "";
                                try {
                                    HttpClient client = new DefaultHttpClient();
                                    String postURL = "http://210.122.7.193:8080/Web_basket/GameDelete.jsp";
                                    HttpPost post = new HttpPost(postURL);
                                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                                    params.add(new BasicNameValuePair("SendTeam", MainActivity.MyTeam));
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
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            MainActivity.yourTeamStatus = "reset";
                                            MainActivity.Contest_Button_Cancel.setVisibility(View.GONE);
                                            MainActivity.Contest_Button_YourTeam.setText("팀 찾기");
                                            MainActivity.Contest_Button_YourTeam.setEnabled(true);
                                            MainActivity.Contest_Button_Start.setText("시합신청");
                                            MainActivity.Contest_Button_Start.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                        }
                                    });
                                }
                                if (MainActivity.layout != null) {
                                    ViewGroup parentViewGroup = (ViewGroup) MainActivity.layout.getParent();
                                    if (null != parentViewGroup) {
                                        parentViewGroup.removeView(MainActivity.layout);
                                    }
                                }
                                DropOutDialog.dismiss();
                            }
                        });
                DropOutDialog.show();
            }
        } else {

        }
        final Button Layout_CustomDialog_GameAdd_Button_HomeTeam = (Button) MainActivity.layout_GameAdd.findViewById(R.id.Layout_CustomDialog_GameAdd_Button_HomeTeam);
        final EditText Layout_CustomDialog_GameAdd_EditText_HomeTeam = (EditText) MainActivity.layout_GameAdd.findViewById(R.id.Layout_CustomDialog_GameAdd_EditText_HomeTeam);
        final Button Layout_CustomDialog_GameAdd_Button_AwayTeam = (Button) MainActivity.layout_GameAdd.findViewById(R.id.Layout_CustomDialog_GameAdd_Button_AwayTeam);
        final EditText Layout_CustomDialog_GameAdd_EditText_AwayTeam = (EditText) MainActivity.layout_GameAdd.findViewById(R.id.Layout_CustomDialog_GameAdd_EditText_AwayTeam);

        MainActivity.Contest_Button_Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.Contest_Button_Start.getText().toString().equals("완료 대기중")) {

                } else {
                    Log.i("MyTeam", MainActivity.MyTeam);
                    Log.i("now_Date", MainActivity.now_Date);
                    String result_personCount = "";           //8명 이하일 경우 처리
                    try {
                        HttpClient client = new DefaultHttpClient();
                        String postURL = "http://210.122.7.193:8080/Web_basket/TeamPersonCount.jsp";
                        HttpPost post = new HttpPost(postURL);
                        List<NameValuePair> params = new ArrayList<NameValuePair>();

                        params.add(new BasicNameValuePair("MyTeam", MainActivity.MyTeam));
                        params.add(new BasicNameValuePair("now_Date", MainActivity.now_Date));
                        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                        post.setEntity(ent);
                        HttpResponse response = client.execute(post);
                        BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                        String line = null;
                        while ((line = bufreader.readLine()) != null) {
                            result_personCount += line;
                        }
                        parsedData_TeamPersonCount = jsonParserList_gameGenerate(result_personCount);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //시합 완료 처리
                    if (parsedData_TeamPersonCount[0][0].equals("not enough")) {
                        Snackbar.make(view, "팀원이 부족합니다.(8)", Snackbar.LENGTH_SHORT).show();
                    } else if (parsedData_TeamPersonCount[0][0].equals("already")) {
                        Snackbar.make(view, "이미 오늘 경기하셨습니다.", Snackbar.LENGTH_SHORT).show();
                    } else {
                        if (MainActivity.Authority.equals("1")) {
                            if (MainActivity.Contest_Button_YourTeam.getText().toString().equals("팀 찾기")) {
                                Snackbar.make(view, "팀 찾기 후 이용해주시기 바랍니다.", Snackbar.LENGTH_SHORT).show();
                            } else if (MainActivity.Contest_Button_Start.getText().toString().equals("시합 중")) {
                                ///경기가 도중에 취소됐는지 체크
                                String result = "";
                                try {
                                    HttpClient client = new DefaultHttpClient();
                                    String postURL = "http://210.122.7.193:8080/Web_basket/GameStatusCheck.jsp";
                                    HttpPost post = new HttpPost(postURL);
                                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                                    params.add(new BasicNameValuePair("SendTeam", MainActivity.HomeTeam));
                                    params.add(new BasicNameValuePair("ReceiveTeam", MainActivity.AwayTeam));
                                    UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                    post.setEntity(ent);
                                    HttpResponse response = client.execute(post);
                                    BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                    String line = null;
                                    while ((line = bufreader.readLine()) != null) {
                                        result += line;
                                    }
                                    parsedData_gameStatus = jsonParserList_gameGenerate(result);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Log.i("호호", parsedData_gameStatus[0][0]);
                                if (parsedData_gameStatus[0][0].equals("not exist")) {
                                    Snackbar.make(view, "시합이 취소되었습니다.", Snackbar.LENGTH_SHORT).show();
                                    MainActivity.Contest_Button_Cancel.setVisibility(View.GONE);
                                    MainActivity.Contest_Button_Start.setText(MainActivity.MyTeam);
                                    MainActivity.Contest_Button_YourTeam.setText("팀 찾기");
                                    MainActivity.Contest_Button_YourTeam.setEnabled(true);
                                    MainActivity.Contest_Button_Start.setText("시합신청");
                                    MainActivity.Contest_Button_Start.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                } else {
                                    Layout_CustomDialog_GameAdd_Button_HomeTeam.setText(MainActivity.Contest_Button_MyTeam.getText().toString());
                                    Layout_CustomDialog_GameAdd_Button_AwayTeam.setText(MainActivity.Contest_Button_YourTeam.getText().toString());
                                    final MaterialDialog GameAddDialog = new MaterialDialog(rootView.getContext());
                                    GameAddDialog
                                            .setTitle("시합 종료")
                                            .setView(MainActivity.layout_GameAdd)
                                            .setNegativeButton("취소", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    GameAddDialog.dismiss();
                                                    if (MainActivity.layout_GameAdd != null) {
                                                        ViewGroup parentViewGroup = (ViewGroup) MainActivity.layout_GameAdd.getParent();
                                                        if (null != parentViewGroup) {
                                                            parentViewGroup.removeView(MainActivity.layout_GameAdd);
                                                        }
                                                    }
                                                }
                                            })
                                            .setPositiveButton("확인", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    if (Layout_CustomDialog_GameAdd_EditText_HomeTeam.equals("")) {
                                                        Snackbar.make(v, "점수를 입력해주세요.", Snackbar.LENGTH_SHORT);
                                                    } else if (Layout_CustomDialog_GameAdd_EditText_AwayTeam.equals("")) {
                                                        Snackbar.make(v, "점수를 입력해주세요.", Snackbar.LENGTH_SHORT);
                                                    } else {
                                                        String HomeScore = Layout_CustomDialog_GameAdd_EditText_HomeTeam.getText().toString();
                                                        String AwayScore = Layout_CustomDialog_GameAdd_EditText_AwayTeam.getText().toString();
                                                        String result = "";
                                                        try {
                                                            HttpClient client = new DefaultHttpClient();
                                                            String postURL = "http://210.122.7.193:8080/Web_basket/GameScoreCheck.jsp";
                                                            HttpPost post = new HttpPost(postURL);
                                                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                                                            params.add(new BasicNameValuePair("SendTeam", Layout_CustomDialog_GameAdd_Button_HomeTeam.getText().toString()));
                                                            params.add(new BasicNameValuePair("AwayTeam", Layout_CustomDialog_GameAdd_Button_AwayTeam.getText().toString()));
                                                            params.add(new BasicNameValuePair("HomeScore", HomeScore));
                                                            params.add(new BasicNameValuePair("AwayScore", AwayScore));
                                                            params.add(new BasicNameValuePair("MyTeam", MainActivity.MyTeam));
                                                            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                                            post.setEntity(ent);
                                                            HttpResponse response = client.execute(post);
                                                            BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                                            String line = null;
                                                            while ((line = bufreader.readLine()) != null) {
                                                                result += line;
                                                            }
                                                            parsedData_gameScoreAdd = jsonParserList_gameGenerate(result);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                        //시합 완료 처리
                                                        if (parsedData_gameScoreAdd[0][0].equals("succed")) {
                                                            MainActivity.Contest_Button_Start.setText("완료 대기중");
                                                            MainActivity.Contest_Button_Cancel.setVisibility(View.GONE);
                                                        }
                                                    }
                                                    GameAddDialog.dismiss();
                                                }
                                            });
                                    GameAddDialog.show();
                                }
                                //
                            } else {
                                MainActivity.Contest_Button_Cancel.setVisibility(View.VISIBLE);
                                MainActivity.Contest_Button_YourTeam.setEnabled(false);
                                if (MainActivity.yourTeamStatus.equals("teamchoice")) {
                                    String result = "";
                                    try {
                                        HttpClient client = new DefaultHttpClient();
                                        String postURL = "http://210.122.7.193:8080/Web_basket/GameGenerate.jsp";
                                        HttpPost post = new HttpPost(postURL);
                                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                                        params.add(new BasicNameValuePair("SendTeam", MainActivity.MyTeam));
                                        params.add(new BasicNameValuePair("ReceiveTeam", Team));
                                        params.add(new BasicNameValuePair("Time", MainActivity.realTime));
                                        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                        post.setEntity(ent);
                                        HttpResponse response = client.execute(post);
                                        BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                        String line = null;
                                        while ((line = bufreader.readLine()) != null) {
                                            result += line;
                                        }
                                        parsedData_gameGenerate = jsonParserList_gameGenerate(result);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    ///게임생성.
                                    if (parsedData_gameGenerate[0][0].equals("succed")) {
                                        myTask = new TimerTask() {
                                            int i = 300;

                                            public void run() {
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        // 해당 작업을 처리함
                                                        MainActivity.Contest_Button_Start.setText("신청 중 " + i);
                                                        if (i % 3 == 0) {
                                                            String result = "";
                                                            try {
                                                                HttpClient client = new DefaultHttpClient();
                                                                String postURL = "http://210.122.7.193:8080/Web_basket/GameStatusCheck.jsp";
                                                                HttpPost post = new HttpPost(postURL);
                                                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                                                params.add(new BasicNameValuePair("SendTeam", MainActivity.MyTeam));
                                                                params.add(new BasicNameValuePair("ReceiveTeam", Team));
                                                                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                                                post.setEntity(ent);
                                                                HttpResponse response = client.execute(post);
                                                                BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                                                String line = null;
                                                                while ((line = bufreader.readLine()) != null) {
                                                                    result += line;
                                                                }
                                                                parsedData_gameStatus = jsonParserList_gameGenerate(result);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                            if (parsedData_gameStatus[0][0].equals(".")) {
                                                                Log.i("bbbbb", "bbbbb");
                                                            }
                                                            ///시합 신청이 수락된 경우
                                                            else if (parsedData_gameStatus[0][0].equals("allow")) {
                                                                timer.cancel();
                                                                final MaterialDialog DropOutDialog = new MaterialDialog(rootView.getContext());
                                                                DropOutDialog
                                                                        .setTitle("시합신청")
                                                                        .setMessage(Team + "팀에서 시합신청을 수락하였습니다.")
                                                                        .setNegativeButton("시합하기", new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View v) {
                                                                                String result = "";
                                                                                try {
                                                                                    HttpClient client = new DefaultHttpClient();
                                                                                    String postURL = "http://210.122.7.193:8080/Web_basket/Gameing.jsp";
                                                                                    HttpPost post = new HttpPost(postURL);
                                                                                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                                                                                    params.add(new BasicNameValuePair("SendTeam", MainActivity.MyTeam));
                                                                                    UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                                                                    post.setEntity(ent);
                                                                                    HttpResponse response = client.execute(post);
                                                                                    BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                                                                    String line = null;
                                                                                    while ((line = bufreader.readLine()) != null) {
                                                                                        result += line;
                                                                                    }
                                                                                    parsedData_gameStatus = jsonParserList_gameGenerate(result);
                                                                                } catch (Exception e) {

                                                                                    e.printStackTrace();
                                                                                }
                                                                                if (parsedData_gameStatus[0][0].equals("succed")) {
                                                                                    MainActivity.Contest_Button_Start.setText("시합 중");
                                                                                    MainActivity.Contest_Button_Start.setTextColor(Color.WHITE);
                                                                                    MainActivity.Contest_Button_Start.setBackgroundColor(Color.RED);
                                                                                    DropOutDialog.dismiss();
                                                                                }
                                                                            }
                                                                        });
                                                                DropOutDialog.show();
                                                            }
                                                            //시합 신청이 거절된 경우
                                                            else if (parsedData_gameStatus[0][0].equals("refuse")) {
                                                                timer.cancel();
                                                                final MaterialDialog DropOutDialog = new MaterialDialog(rootView.getContext());
                                                                DropOutDialog
                                                                        .setTitle("시합신청")
                                                                        .setMessage(Team + "팀에서 시합신청을 거절하였습니다.")
                                                                        .setNegativeButton("확인", new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View v) {
                                                                                String result = "";
                                                                                try {
                                                                                    HttpClient client = new DefaultHttpClient();
                                                                                    String postURL = "http://210.122.7.193:8080/Web_basket/GameDelete.jsp";
                                                                                    HttpPost post = new HttpPost(postURL);
                                                                                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                                                                                    params.add(new BasicNameValuePair("SendTeam", MainActivity.MyTeam));
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
                                                                                    getActivity().runOnUiThread(new Runnable() {
                                                                                        @Override
                                                                                        public void run() {
                                                                                            MainActivity.yourTeamStatus = "reset";
                                                                                            MainActivity.Contest_Button_Cancel.setVisibility(View.GONE);
                                                                                            MainActivity.Contest_Button_YourTeam.setText("팀 찾기");
                                                                                            MainActivity.Contest_Button_YourTeam.setEnabled(true);
                                                                                            MainActivity.Contest_Button_Start.setText("시합신청");
                                                                                            MainActivity.Contest_Button_Start.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                                                                        }
                                                                                    });
                                                                                }
                                                                                if (MainActivity.layout != null) {
                                                                                    ViewGroup parentViewGroup = (ViewGroup) MainActivity.layout.getParent();
                                                                                    if (null != parentViewGroup) {
                                                                                        parentViewGroup.removeView(MainActivity.layout);
                                                                                    }
                                                                                }
                                                                                DropOutDialog.dismiss();
                                                                            }
                                                                        });
                                                                DropOutDialog.show();
                                                            }
                                                        }
                                                    }
                                                });

                                                Log.d("myTask", Integer.toString(i));
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
                                                        params.add(new BasicNameValuePair("SendTeam", MainActivity.MyTeam));
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
                                                        getActivity().runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                MainActivity.yourTeamStatus = "reset";
                                                                MainActivity.Contest_Button_Cancel.setVisibility(View.GONE);
                                                                MainActivity.Contest_Button_YourTeam.setText("팀 찾기");
                                                                MainActivity.Contest_Button_YourTeam.setEnabled(true);
                                                                MainActivity.Contest_Button_Start.setText("시합신청");
                                                                MainActivity.Contest_Button_Start.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                                            }
                                                        });
                                                    }
                                                    if (MainActivity.layout != null) {
                                                        ViewGroup parentViewGroup = (ViewGroup) MainActivity.layout.getParent();
                                                        if (null != parentViewGroup) {
                                                            parentViewGroup.removeView(MainActivity.layout);
                                                        }
                                                    }
                                                }
                                            }
                                        };
                                    }
                                }
                                timer = new Timer();
                                //timer.schedule(myTask, 5000);  // 5초후 실행하고 종료
                                timer.schedule(myTask, 500, 1000); // 5초후 첫실행, 3초마다 계속실행
                            }
                        } else {
                            Snackbar.make(view, "게임 권한이 없습니다.", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        MainActivity.Contest_Button_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MainActivity.yourTeamStatus.equals("ScoreCheck") && !MainActivity.yourTeamStatus.equals("ing")) {
                    timer.cancel();
                }
                new Handler().postDelayed(new Runnable() {// 1 초 후에 실행
                    @Override
                    public void run() {
                        String result = "";
                        try {
                            HttpClient client = new DefaultHttpClient();
                            String postURL = "http://210.122.7.193:8080/Web_basket/GameDelete.jsp";
                            HttpPost post = new HttpPost(postURL);
                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                            params.add(new BasicNameValuePair("SendTeam", MainActivity.MyTeam));
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
                            MainActivity.yourTeamStatus = "reset";
                            MainActivity.Contest_Button_Cancel.setVisibility(View.GONE);
                            MainActivity.Contest_Button_MyTeam.setText(MainActivity.MyTeam);
                            MainActivity.Contest_Button_YourTeam.setText("팀 찾기");
                            MainActivity.Contest_Button_YourTeam.setEnabled(true);
                            MainActivity.Contest_Button_Start.setText("시합신청");
                            MainActivity.Contest_Button_Start.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        }
                        if (MainActivity.layout != null) {
                            ViewGroup parentViewGroup = (ViewGroup) MainActivity.layout.getParent();
                            if (null != parentViewGroup) {
                                parentViewGroup.removeView(MainActivity.layout);
                            }
                        }
                    }
                }, 1000);
            }
        });
        MainActivity.Contest_Button_YourTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.Contest_Button_YourTeam.getText().toString().equals("팀 찾기")) {
                    Team = "팀 선택";
                    final MaterialDialog TeamSearchDialog = new MaterialDialog(view.getContext());
                    TeamSearchDialog
                            .setTitle("팀 선택")
                            .setView(MainActivity.layout)
                            .setNegativeButton("취소", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    MainActivity.Contest_Button_YourTeam.setText("팀 찾기");
                                    TeamSearchDialog.dismiss();
                                    if (MainActivity.layout != null) {
                                        ViewGroup parentViewGroup = (ViewGroup) MainActivity.layout.getParent();
                                        if (null != parentViewGroup) {
                                            parentViewGroup.removeView(MainActivity.layout);
                                        }
                                    }
                                }
                            })
                            .setPositiveButton("선택 완료", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (Team.equals("팀 선택")) {
                                        Snackbar.make(v, "팀을 선택해주세요.", Snackbar.LENGTH_SHORT).show();
                                    } else {
                                        TeamSearchDialog.dismiss();
                                        MainActivity.yourTeamStatus = "teamchoice";
                                        if (MainActivity.layout != null) {
                                            ViewGroup parentViewGroup = (ViewGroup) MainActivity.layout.getParent();
                                            if (null != parentViewGroup) {
                                                parentViewGroup.removeView(MainActivity.layout);
                                            }
                                        }
                                    }
                                }
                            });
                    TeamSearchDialog.show();
                }
            }
        });
        //////////일반적 접근 - 신청 중인 경우.
        adspin1 = ArrayAdapter.createFromResource(rootView.getContext(), R.array.spinner_do, R.layout.zfile_spinner_test);
        adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Layout_CustomDialog_teamChoice_Do.setAdapter(adspin1);

        Layout_CustomDialog_teamChoice_Do.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnum1 = i;
                Do = adspin1.getItem(i).toString();
                if (adspin1.getItem(i).equals("서울")) {
                    adspin2 = ArrayAdapter.createFromResource(rootView.getContext(), R.array.spinner_do_seoul, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_CustomDialog_teamChoice_Se.setAdapter(adspin2);
                    Layout_CustomDialog_teamChoice_Se.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    spinnum2 = i;
                                    Si = adspin2.getItem(i).toString();
                                    String result = "";
                                    try {
                                        HttpClient client = new DefaultHttpClient();
                                        String postURL = "http://210.122.7.193:8080/Web_basket/TeamInformation.jsp";
                                        HttpPost post = new HttpPost(postURL);
                                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                                        params.add(new BasicNameValuePair("TeamSearch_Do", (String) adspin1.getItem(spinnum1)));
                                        params.add(new BasicNameValuePair("TeamSearch_Si", (String) adspin2.getItem(spinnum2)));
                                        params.add(new BasicNameValuePair("MyTeam", MainActivity.MyTeam));
                                        params.add(new BasicNameValuePair("now_Date", MainActivity.now_Date));
                                        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                        post.setEntity(ent);
                                        HttpResponse response = client.execute(post);
                                        BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                        String line = null;
                                        while ((line = bufreader.readLine()) != null) {
                                            result += line;
                                        }
                                        parsedData = jsonParserList_TeamSearch(result);
                                        arr = new ArrayList();
                                        arr.add("팀 선택");
                                        for (int a = 0; a < parsedData.length; a++) {
                                            arr.add(parsedData[a][0]);
                                        }
                                        adspin3 = new ArrayAdapter<CharSequence>(rootView.getContext(), android.R.layout.simple_spinner_item, arr);
                                        adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        Layout_CustomDialog_teamChoice_team.setAdapter(adspin3);
                                        Layout_CustomDialog_teamChoice_team.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                                Team = adspin3.getItem(i).toString();
                                                MainActivity.Contest_Button_YourTeam.setText(Team);
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            }
                    );
                } else if (adspin1.getItem(i).equals("인천")) {
                    adspin2 = ArrayAdapter.createFromResource(rootView.getContext(), R.array.spinner_do_incheon, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_CustomDialog_teamChoice_Se.setAdapter(adspin2);
                    Layout_CustomDialog_teamChoice_Se.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    spinnum2 = i;
                                    Si = adspin2.getItem(i).toString();
                                    String result = "";
                                    try {
                                        HttpClient client = new DefaultHttpClient();
                                        String postURL = "http://210.122.7.193:8080/Web_basket/TeamInformation.jsp";
                                        HttpPost post = new HttpPost(postURL);
                                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                                        params.add(new BasicNameValuePair("TeamSearch_Do", (String) adspin1.getItem(spinnum1)));
                                        params.add(new BasicNameValuePair("TeamSearch_Si", (String) adspin2.getItem(spinnum2)));
                                        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                        post.setEntity(ent);
                                        HttpResponse response = client.execute(post);
                                        BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                        String line = null;
                                        while ((line = bufreader.readLine()) != null) {
                                            result += line;
                                        }
                                        parsedData = jsonParserList_TeamSearch(result);
                                        arr = new ArrayList();
                                        arr.add("팀 선택");
                                        for (int a = 0; a < parsedData.length; a++) {
                                            arr.add(parsedData[a][0]);
                                        }
                                        adspin3 = new ArrayAdapter<CharSequence>(rootView.getContext(), android.R.layout.simple_spinner_item, arr);
                                        adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        Layout_CustomDialog_teamChoice_team.setAdapter(adspin3);
                                        Layout_CustomDialog_teamChoice_team.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                Team = adspin3.getItem(i).toString();
                                                MainActivity.Contest_Button_YourTeam.setText(Team);
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            }
                    );
                } else if (adspin1.getItem(i).equals("광주")) {
                    adspin2 = ArrayAdapter.createFromResource(rootView.getContext(), R.array.spinner_do_gwangju, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_CustomDialog_teamChoice_Se.setAdapter(adspin2);
                    Layout_CustomDialog_teamChoice_Se.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    spinnum2 = i;
                                    Si = adspin2.getItem(i).toString();
                                    String result = "";
                                    try {
                                        HttpClient client = new DefaultHttpClient();
                                        String postURL = "http://210.122.7.193:8080/Web_basket/TeamInformation.jsp";
                                        HttpPost post = new HttpPost(postURL);
                                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                                        params.add(new BasicNameValuePair("TeamSearch_Do", (String) adspin1.getItem(spinnum1)));
                                        params.add(new BasicNameValuePair("TeamSearch_Si", (String) adspin2.getItem(spinnum2)));
                                        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                        post.setEntity(ent);
                                        HttpResponse response = client.execute(post);
                                        BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                        String line = null;
                                        while ((line = bufreader.readLine()) != null) {
                                            result += line;
                                        }
                                        parsedData = jsonParserList_TeamSearch(result);
                                        arr = new ArrayList();
                                        arr.add("팀 선택");
                                        for (int a = 0; a < parsedData.length; a++) {
                                            arr.add(parsedData[a][0]);
                                        }
                                        adspin3 = new ArrayAdapter<CharSequence>(rootView.getContext(), android.R.layout.simple_spinner_item, arr);
                                        adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        Layout_CustomDialog_teamChoice_team.setAdapter(adspin3);
                                        Layout_CustomDialog_teamChoice_team.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                Team = adspin3.getItem(i).toString();
                                                MainActivity.Contest_Button_YourTeam.setText(Team);
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            }
                    );
                } else if (adspin1.getItem(i).equals("대구")) {
                    adspin2 = ArrayAdapter.createFromResource(rootView.getContext(), R.array.spinner_do_DaeGu, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_CustomDialog_teamChoice_Se.setAdapter(adspin2);
                    Layout_CustomDialog_teamChoice_Se.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    spinnum2 = i;
                                    Si = adspin2.getItem(i).toString();
                                    String result = "";
                                    try {
                                        HttpClient client = new DefaultHttpClient();
                                        String postURL = "http://210.122.7.193:8080/Web_basket/TeamInformation.jsp";
                                        HttpPost post = new HttpPost(postURL);
                                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                                        params.add(new BasicNameValuePair("TeamSearch_Do", (String) adspin1.getItem(spinnum1)));
                                        params.add(new BasicNameValuePair("TeamSearch_Si", (String) adspin2.getItem(spinnum2)));
                                        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                        post.setEntity(ent);
                                        HttpResponse response = client.execute(post);
                                        BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                        String line = null;
                                        while ((line = bufreader.readLine()) != null) {
                                            result += line;
                                        }
                                        parsedData = jsonParserList_TeamSearch(result);
                                        arr = new ArrayList();
                                        arr.add("팀 선택");
                                        for (int a = 0; a < parsedData.length; a++) {
                                            arr.add(parsedData[a][0]);
                                        }
                                        adspin3 = new ArrayAdapter<CharSequence>(rootView.getContext(), android.R.layout.simple_spinner_item, arr);
                                        adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        Layout_CustomDialog_teamChoice_team.setAdapter(adspin3);
                                        Layout_CustomDialog_teamChoice_team.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                Team = adspin3.getItem(i).toString();
                                                MainActivity.Contest_Button_YourTeam.setText(Team);
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            }
                    );
                } else if (adspin1.getItem(i).equals("울산")) {
                    adspin2 = ArrayAdapter.createFromResource(rootView.getContext(), R.array.spinner_do_Ulsan, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_CustomDialog_teamChoice_Se.setAdapter(adspin2);
                    Layout_CustomDialog_teamChoice_Se.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    spinnum2 = i;
                                    Si = adspin2.getItem(i).toString();
                                    String result = "";
                                    try {
                                        HttpClient client = new DefaultHttpClient();
                                        String postURL = "http://210.122.7.193:8080/Web_basket/TeamInformation.jsp";
                                        HttpPost post = new HttpPost(postURL);
                                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                                        params.add(new BasicNameValuePair("TeamSearch_Do", (String) adspin1.getItem(spinnum1)));
                                        params.add(new BasicNameValuePair("TeamSearch_Si", (String) adspin2.getItem(spinnum2)));
                                        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                        post.setEntity(ent);
                                        HttpResponse response = client.execute(post);
                                        BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                        String line = null;
                                        while ((line = bufreader.readLine()) != null) {
                                            result += line;
                                        }
                                        parsedData = jsonParserList_TeamSearch(result);
                                        arr = new ArrayList();
                                        arr.add("팀 선택");
                                        for (int a = 0; a < parsedData.length; a++) {
                                            arr.add(parsedData[a][0]);
                                        }
                                        adspin3 = new ArrayAdapter<CharSequence>(rootView.getContext(), android.R.layout.simple_spinner_item, arr);
                                        adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        Layout_CustomDialog_teamChoice_team.setAdapter(adspin3);
                                        Layout_CustomDialog_teamChoice_team.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                Team = adspin3.getItem(i).toString();
                                                MainActivity.Contest_Button_YourTeam.setText(Team);
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            }
                    );
                } else if (adspin1.getItem(i).equals("대전")) {
                    adspin2 = ArrayAdapter.createFromResource(rootView.getContext(), R.array.spinner_do_DaeJeon, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_CustomDialog_teamChoice_Se.setAdapter(adspin2);
                    Layout_CustomDialog_teamChoice_Se.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    spinnum2 = i;
                                    Si = adspin2.getItem(i).toString();
                                    String result = "";
                                    try {
                                        HttpClient client = new DefaultHttpClient();
                                        String postURL = "http://210.122.7.193:8080/Web_basket/TeamInformation.jsp";
                                        HttpPost post = new HttpPost(postURL);
                                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                                        params.add(new BasicNameValuePair("TeamSearch_Do", (String) adspin1.getItem(spinnum1)));
                                        params.add(new BasicNameValuePair("TeamSearch_Si", (String) adspin2.getItem(spinnum2)));
                                        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                        post.setEntity(ent);
                                        HttpResponse response = client.execute(post);
                                        BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                        String line = null;
                                        while ((line = bufreader.readLine()) != null) {
                                            result += line;
                                        }
                                        parsedData = jsonParserList_TeamSearch(result);
                                        arr = new ArrayList();
                                        arr.add("팀 선택");
                                        for (int a = 0; a < parsedData.length; a++) {
                                            arr.add(parsedData[a][0]);
                                        }
                                        adspin3 = new ArrayAdapter<CharSequence>(rootView.getContext(), android.R.layout.simple_spinner_item, arr);
                                        adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        Layout_CustomDialog_teamChoice_team.setAdapter(adspin3);
                                        Layout_CustomDialog_teamChoice_team.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                Team = adspin3.getItem(i).toString();
                                                MainActivity.Contest_Button_YourTeam.setText(Team);
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            }
                    );
                } else if (adspin1.getItem(i).equals("부산")) {
                    adspin2 = ArrayAdapter.createFromResource(rootView.getContext(), R.array.spinner_do_Busan, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_CustomDialog_teamChoice_Se.setAdapter(adspin2);
                    Layout_CustomDialog_teamChoice_Se.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    spinnum2 = i;
                                    Si = adspin2.getItem(i).toString();
                                    String result = "";
                                    try {
                                        HttpClient client = new DefaultHttpClient();
                                        String postURL = "http://210.122.7.193:8080/Web_basket/TeamInformation.jsp";
                                        HttpPost post = new HttpPost(postURL);
                                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                                        params.add(new BasicNameValuePair("TeamSearch_Do", (String) adspin1.getItem(spinnum1)));
                                        params.add(new BasicNameValuePair("TeamSearch_Si", (String) adspin2.getItem(spinnum2)));
                                        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                        post.setEntity(ent);
                                        HttpResponse response = client.execute(post);
                                        BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                        String line = null;
                                        while ((line = bufreader.readLine()) != null) {
                                            result += line;
                                        }
                                        parsedData = jsonParserList_TeamSearch(result);
                                        arr = new ArrayList();
                                        arr.add("팀 선택");
                                        for (int a = 0; a < parsedData.length; a++) {
                                            arr.add(parsedData[a][0]);
                                        }
                                        adspin3 = new ArrayAdapter<CharSequence>(rootView.getContext(), android.R.layout.simple_spinner_item, arr);
                                        adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        Layout_CustomDialog_teamChoice_team.setAdapter(adspin3);
                                        Layout_CustomDialog_teamChoice_team.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                Team = adspin3.getItem(i).toString();
                                                MainActivity.Contest_Button_YourTeam.setText(Team);
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            }
                    );
                } else if (adspin1.getItem(i).equals("강원도")) {
                    adspin2 = ArrayAdapter.createFromResource(rootView.getContext(), R.array.spinner_do_Gangwondo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_CustomDialog_teamChoice_Se.setAdapter(adspin2);
                    Layout_CustomDialog_teamChoice_Se.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    spinnum2 = i;
                                    Si = adspin2.getItem(i).toString();
                                    String result = "";
                                    try {
                                        HttpClient client = new DefaultHttpClient();
                                        String postURL = "http://210.122.7.193:8080/Web_basket/TeamInformation.jsp";
                                        HttpPost post = new HttpPost(postURL);
                                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                                        params.add(new BasicNameValuePair("TeamSearch_Do", (String) adspin1.getItem(spinnum1)));
                                        params.add(new BasicNameValuePair("TeamSearch_Si", (String) adspin2.getItem(spinnum2)));
                                        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                        post.setEntity(ent);
                                        HttpResponse response = client.execute(post);
                                        BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                        String line = null;
                                        while ((line = bufreader.readLine()) != null) {
                                            result += line;
                                        }
                                        parsedData = jsonParserList_TeamSearch(result);
                                        arr = new ArrayList();
                                        arr.add("팀 선택");
                                        for (int a = 0; a < parsedData.length; a++) {
                                            arr.add(parsedData[a][0]);
                                        }
                                        adspin3 = new ArrayAdapter<CharSequence>(rootView.getContext(), android.R.layout.simple_spinner_item, arr);
                                        adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        Layout_CustomDialog_teamChoice_team.setAdapter(adspin3);
                                        Layout_CustomDialog_teamChoice_team.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                Team = adspin3.getItem(i).toString();
                                                MainActivity.Contest_Button_YourTeam.setText(Team);
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            }
                    );
                } else if (adspin1.getItem(i).equals("경기도")) {
                    adspin2 = ArrayAdapter.createFromResource(rootView.getContext(), R.array.spinner_do_Gyeonggido, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_CustomDialog_teamChoice_Se.setAdapter(adspin2);
                    Layout_CustomDialog_teamChoice_Se.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    spinnum2 = i;
                                    Si = adspin2.getItem(i).toString();
                                    String result = "";
                                    try {
                                        HttpClient client = new DefaultHttpClient();
                                        String postURL = "http://210.122.7.193:8080/Web_basket/TeamInformation.jsp";
                                        HttpPost post = new HttpPost(postURL);
                                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                                        params.add(new BasicNameValuePair("TeamSearch_Do", (String) adspin1.getItem(spinnum1)));
                                        params.add(new BasicNameValuePair("TeamSearch_Si", (String) adspin2.getItem(spinnum2)));
                                        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                        post.setEntity(ent);
                                        HttpResponse response = client.execute(post);
                                        BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                        String line = null;
                                        while ((line = bufreader.readLine()) != null) {
                                            result += line;
                                        }
                                        parsedData = jsonParserList_TeamSearch(result);
                                        arr = new ArrayList();
                                        arr.add("팀 선택");
                                        for (int a = 0; a < parsedData.length; a++) {
                                            arr.add(parsedData[a][0]);
                                        }
                                        adspin3 = new ArrayAdapter<CharSequence>(rootView.getContext(), android.R.layout.simple_spinner_item, arr);
                                        adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        Layout_CustomDialog_teamChoice_team.setAdapter(adspin3);
                                        Layout_CustomDialog_teamChoice_team.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                Team = adspin3.getItem(i).toString();
                                                MainActivity.Contest_Button_YourTeam.setText(Team);
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            }
                    );
                } else if (adspin1.getItem(i).equals("충청북도")) {
                    adspin2 = ArrayAdapter.createFromResource(rootView.getContext(), R.array.spinner_do_Chungcheongbukdo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_CustomDialog_teamChoice_Se.setAdapter(adspin2);
                    Layout_CustomDialog_teamChoice_Se.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    spinnum2 = i;
                                    Si = adspin2.getItem(i).toString();
                                    String result = "";
                                    try {
                                        HttpClient client = new DefaultHttpClient();
                                        String postURL = "http://210.122.7.193:8080/Web_basket/TeamInformation.jsp";
                                        HttpPost post = new HttpPost(postURL);
                                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                                        params.add(new BasicNameValuePair("TeamSearch_Do", (String) adspin1.getItem(spinnum1)));
                                        params.add(new BasicNameValuePair("TeamSearch_Si", (String) adspin2.getItem(spinnum2)));
                                        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                        post.setEntity(ent);
                                        HttpResponse response = client.execute(post);
                                        BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                        String line = null;
                                        while ((line = bufreader.readLine()) != null) {
                                            result += line;
                                        }
                                        parsedData = jsonParserList_TeamSearch(result);
                                        arr = new ArrayList();
                                        arr.add("팀 선택");
                                        for (int a = 0; a < parsedData.length; a++) {
                                            arr.add(parsedData[a][0]);
                                        }
                                        adspin3 = new ArrayAdapter<CharSequence>(rootView.getContext(), android.R.layout.simple_spinner_item, arr);
                                        adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        Layout_CustomDialog_teamChoice_team.setAdapter(adspin3);
                                        Layout_CustomDialog_teamChoice_team.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                Team = adspin3.getItem(i).toString();
                                                MainActivity.Contest_Button_YourTeam.setText(Team);
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            }
                    );
                } else if (adspin1.getItem(i).equals("충청남도")) {
                    adspin2 = ArrayAdapter.createFromResource(rootView.getContext(), R.array.spinner_do_Chungcheongnamdo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_CustomDialog_teamChoice_Se.setAdapter(adspin2);
                    Layout_CustomDialog_teamChoice_Se.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    spinnum2 = i;
                                    Si = adspin2.getItem(i).toString();
                                    String result = "";
                                    try {
                                        HttpClient client = new DefaultHttpClient();
                                        String postURL = "http://210.122.7.193:8080/Web_basket/TeamInformation.jsp";
                                        HttpPost post = new HttpPost(postURL);
                                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                                        params.add(new BasicNameValuePair("TeamSearch_Do", (String) adspin1.getItem(spinnum1)));
                                        params.add(new BasicNameValuePair("TeamSearch_Si", (String) adspin2.getItem(spinnum2)));
                                        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                        post.setEntity(ent);
                                        HttpResponse response = client.execute(post);
                                        BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                        String line = null;
                                        while ((line = bufreader.readLine()) != null) {
                                            result += line;
                                        }
                                        parsedData = jsonParserList_TeamSearch(result);
                                        arr = new ArrayList();
                                        arr.add("팀 선택");
                                        for (int a = 0; a < parsedData.length; a++) {
                                            arr.add(parsedData[a][0]);
                                        }
                                        adspin3 = new ArrayAdapter<CharSequence>(rootView.getContext(), android.R.layout.simple_spinner_item, arr);
                                        adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        Layout_CustomDialog_teamChoice_team.setAdapter(adspin3);
                                        Layout_CustomDialog_teamChoice_team.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                Team = adspin3.getItem(i).toString();
                                                MainActivity.Contest_Button_YourTeam.setText(Team);
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            }
                    );
                } else if (adspin1.getItem(i).equals("전라북도")) {
                    adspin2 = ArrayAdapter.createFromResource(rootView.getContext(), R.array.spinner_do_Jeolabukdo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_CustomDialog_teamChoice_Se.setAdapter(adspin2);
                    Layout_CustomDialog_teamChoice_Se.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    spinnum2 = i;
                                    Si = adspin2.getItem(i).toString();
                                    String result = "";
                                    try {
                                        HttpClient client = new DefaultHttpClient();
                                        String postURL = "http://210.122.7.193:8080/Web_basket/TeamInformation.jsp";
                                        HttpPost post = new HttpPost(postURL);
                                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                                        params.add(new BasicNameValuePair("TeamSearch_Do", (String) adspin1.getItem(spinnum1)));
                                        params.add(new BasicNameValuePair("TeamSearch_Si", (String) adspin2.getItem(spinnum2)));
                                        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                        post.setEntity(ent);
                                        HttpResponse response = client.execute(post);
                                        BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                        String line = null;
                                        while ((line = bufreader.readLine()) != null) {
                                            result += line;
                                        }
                                        parsedData = jsonParserList_TeamSearch(result);
                                        arr = new ArrayList();
                                        arr.add("팀 선택");
                                        for (int a = 0; a < parsedData.length; a++) {
                                            arr.add(parsedData[a][0]);
                                        }
                                        adspin3 = new ArrayAdapter<CharSequence>(rootView.getContext(), android.R.layout.simple_spinner_item, arr);
                                        adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        Layout_CustomDialog_teamChoice_team.setAdapter(adspin3);
                                        Layout_CustomDialog_teamChoice_team.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                Team = adspin3.getItem(i).toString();
                                                MainActivity.Contest_Button_YourTeam.setText(Team);
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            }
                    );
                } else if (adspin1.getItem(i).equals("전라남도")) {
                    adspin2 = ArrayAdapter.createFromResource(rootView.getContext(), R.array.spinner_do_Jeolanamdo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_CustomDialog_teamChoice_Se.setAdapter(adspin2);
                    Layout_CustomDialog_teamChoice_Se.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    spinnum2 = i;
                                    Si = adspin2.getItem(i).toString();
                                    String result = "";
                                    try {
                                        HttpClient client = new DefaultHttpClient();
                                        String postURL = "http://210.122.7.193:8080/Web_basket/TeamInformation.jsp";
                                        HttpPost post = new HttpPost(postURL);
                                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                                        params.add(new BasicNameValuePair("TeamSearch_Do", (String) adspin1.getItem(spinnum1)));
                                        params.add(new BasicNameValuePair("TeamSearch_Si", (String) adspin2.getItem(spinnum2)));
                                        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                        post.setEntity(ent);
                                        HttpResponse response = client.execute(post);
                                        BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                        String line = null;
                                        while ((line = bufreader.readLine()) != null) {
                                            result += line;
                                        }
                                        parsedData = jsonParserList_TeamSearch(result);
                                        arr = new ArrayList();
                                        arr.add("팀 선택");
                                        for (int a = 0; a < parsedData.length; a++) {
                                            arr.add(parsedData[a][0]);
                                        }
                                        adspin3 = new ArrayAdapter<CharSequence>(rootView.getContext(), android.R.layout.simple_spinner_item, arr);
                                        adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        Layout_CustomDialog_teamChoice_team.setAdapter(adspin3);
                                        Layout_CustomDialog_teamChoice_team.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                Team = adspin3.getItem(i).toString();
                                                MainActivity.Contest_Button_YourTeam.setText(Team);
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            }
                    );
                } else if (adspin1.getItem(i).equals("경상북도")) {
                    adspin2 = ArrayAdapter.createFromResource(rootView.getContext(), R.array.spinner_do_Gyeongsangbukdo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_CustomDialog_teamChoice_Se.setAdapter(adspin2);
                    Layout_CustomDialog_teamChoice_Se.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    spinnum2 = i;
                                    Si = adspin2.getItem(i).toString();
                                    String result = "";
                                    try {
                                        HttpClient client = new DefaultHttpClient();
                                        String postURL = "http://210.122.7.193:8080/Web_basket/TeamInformation.jsp";
                                        HttpPost post = new HttpPost(postURL);
                                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                                        params.add(new BasicNameValuePair("TeamSearch_Do", (String) adspin1.getItem(spinnum1)));
                                        params.add(new BasicNameValuePair("TeamSearch_Si", (String) adspin2.getItem(spinnum2)));
                                        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                        post.setEntity(ent);
                                        HttpResponse response = client.execute(post);
                                        BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                        String line = null;
                                        while ((line = bufreader.readLine()) != null) {
                                            result += line;
                                        }
                                        parsedData = jsonParserList_TeamSearch(result);
                                        arr = new ArrayList();
                                        arr.add("팀 선택");
                                        for (int a = 0; a < parsedData.length; a++) {
                                            arr.add(parsedData[a][0]);
                                        }
                                        adspin3 = new ArrayAdapter<CharSequence>(rootView.getContext(), android.R.layout.simple_spinner_item, arr);
                                        adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        Layout_CustomDialog_teamChoice_team.setAdapter(adspin3);
                                        Layout_CustomDialog_teamChoice_team.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                Team = adspin3.getItem(i).toString();
                                                MainActivity.Contest_Button_YourTeam.setText(Team);
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            }
                    );
                } else if (adspin1.getItem(i).equals("경상남도")) {
                    adspin2 = ArrayAdapter.createFromResource(rootView.getContext(), R.array.spinner_do_Gyeongsangnamdo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_CustomDialog_teamChoice_Se.setAdapter(adspin2);
                    Layout_CustomDialog_teamChoice_Se.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    spinnum2 = i;
                                    Si = adspin2.getItem(i).toString();
                                    String result = "";
                                    try {
                                        HttpClient client = new DefaultHttpClient();
                                        String postURL = "http://210.122.7.193:8080/Web_basket/TeamInformation.jsp";
                                        HttpPost post = new HttpPost(postURL);
                                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                                        params.add(new BasicNameValuePair("TeamSearch_Do", (String) adspin1.getItem(spinnum1)));
                                        params.add(new BasicNameValuePair("TeamSearch_Si", (String) adspin2.getItem(spinnum2)));
                                        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                        post.setEntity(ent);
                                        HttpResponse response = client.execute(post);
                                        BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                        String line = null;
                                        while ((line = bufreader.readLine()) != null) {
                                            result += line;
                                        }
                                        parsedData = jsonParserList_TeamSearch(result);
                                        arr = new ArrayList();
                                        arr.add("팀 선택");
                                        for (int a = 0; a < parsedData.length; a++) {
                                            arr.add(parsedData[a][0]);
                                        }
                                        adspin3 = new ArrayAdapter<CharSequence>(rootView.getContext(), android.R.layout.simple_spinner_item, arr);
                                        adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        Layout_CustomDialog_teamChoice_team.setAdapter(adspin3);
                                        Layout_CustomDialog_teamChoice_team.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                Team = adspin3.getItem(i).toString();
                                                MainActivity.Contest_Button_YourTeam.setText(Team);
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            }
                    );
                } else if (adspin1.getItem(i).equals("제주도")) {
                    adspin2 = ArrayAdapter.createFromResource(rootView.getContext(), R.array.spinner_do_Jejudo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_CustomDialog_teamChoice_Se.setAdapter(adspin2);
                    Layout_CustomDialog_teamChoice_Se.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    spinnum2 = i;
                                    Si = adspin2.getItem(i).toString();
                                    String result = "";
                                    try {
                                        HttpClient client = new DefaultHttpClient();
                                        String postURL = "http://210.122.7.193:8080/Web_basket/TeamInformation.jsp";
                                        HttpPost post = new HttpPost(postURL);
                                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                                        params.add(new BasicNameValuePair("TeamSearch_Do", (String) adspin1.getItem(spinnum1)));
                                        params.add(new BasicNameValuePair("TeamSearch_Si", (String) adspin2.getItem(spinnum2)));
                                        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                        post.setEntity(ent);
                                        HttpResponse response = client.execute(post);
                                        BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                        String line = null;
                                        while ((line = bufreader.readLine()) != null) {
                                            result += line;
                                        }
                                        parsedData = jsonParserList_TeamSearch(result);
                                        arr = new ArrayList();
                                        arr.add("팀 선택");
                                        for (int a = 0; a < parsedData.length; a++) {
                                            arr.add(parsedData[a][0]);
                                        }
                                        adspin3 = new ArrayAdapter<CharSequence>(rootView.getContext(), android.R.layout.simple_spinner_item, arr);
                                        adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        Layout_CustomDialog_teamChoice_team.setAdapter(adspin3);
                                        Layout_CustomDialog_teamChoice_team.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                Team = adspin3.getItem(i).toString();
                                                MainActivity.Contest_Button_YourTeam.setText(Team);
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            }
                    );
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return rootView;
    }

    public String[][] jsonParserList_TeamSearch(String pRecvServerPage) {
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

    public String[][] jsonParserList_gameGenerate(String pRecvServerPage) {
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

    public String[][] jsonParserList_getContestsList(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");

            String[] jsonName = {"Pk", "Title", "Date", "Image", "currentNum", "maxNum", "Point"};
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
}
