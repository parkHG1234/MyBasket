package com.mysports.playbasket;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.util.ArrayList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by park on 2016-04-04.
 */
public class Navigation_TeamManager extends AppCompatActivity {

    String[][] parsedData;
    static String Id="",Team="",Duty="";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_teammanager);
        Button TeamManager_Button_TeamMake = (Button)findViewById(R.id.TeamManager_Button_TeamMake);
        Button TeamManager_Button_MemberManager = (Button)findViewById(R.id.TeamManager_Button_MemberManager);
        Button TeamManager_Button_ScheduleManager = (Button)findViewById(R.id.TeamManager_Button_ScheduleManager);
        Button TeamManager_Button_TeamIntroManager = (Button)findViewById(R.id.TeamManager_Button_TeamIntroManager);
        Button TeamManager_Button_NoticeManager = (Button)findViewById(R.id.TeamManager_Button_NoticeManager);
        Button TeamManager_Button_Withdraw = (Button)findViewById(R.id.TeamManager_Button_Withdraw);
        TextView TeamManager_TextView_TeamName = (TextView)findViewById(R.id.TeamManager_TextView_TeamName);
        TextView TeamManager_TextView_Duty = (TextView)findViewById(R.id.TeamManager_TextView_Duty);
        LinearLayout TeamManager_layout1 = (LinearLayout)findViewById(R.id.TeamManager_layout1);
        LinearLayout TeamManager_layout2 = (LinearLayout)findViewById(R.id.TeamManager_layout2);

        Intent intent1 = getIntent();
        Id = intent1.getStringExtra("Id");
        //관리할 팀이 있는지 확인 후 중복 제거
        String TeamCheck_result="";
        try {
            HttpClient client = new DefaultHttpClient();
            String postURL = "http://210.122.7.195:8080/Web_basket/TeamCheck.jsp";
            HttpPost post = new HttpPost(postURL);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Id", Id));

            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);

            HttpResponse response = client.execute(post);
            BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));

            String line = null;
            while ((line = bufreader.readLine()) != null) {
                TeamCheck_result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        parsedData = TeamCheck_jsonParserList(TeamCheck_result);

        if(parsedData[0][0].equals("Unexist")){
            TeamManager_layout1.setVisibility(View.GONE);
            TeamManager_layout2.setVisibility(View.VISIBLE);
        }
        else{
            TeamManager_layout1.setVisibility(View.VISIBLE);
            TeamManager_layout2.setVisibility(View.GONE);
            String result="";
            try {
                HttpClient client = new DefaultHttpClient();
                String postURL = "http://210.122.7.195:8080/Web_basket/TeamManager.jsp";
                HttpPost post = new HttpPost(postURL);

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("Id", Id));

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
            parsedData = jsonParserList(result);
            if(parsedData[0][0].equals("succed")){
                TeamManager_Button_TeamMake.setVisibility(View.GONE);
                Team = parsedData[0][1];
                TeamManager_TextView_TeamName.setText(Team);
                Duty = parsedData[0][2];
                TeamManager_TextView_Duty.setText(Duty);
                if(parsedData[0][3].equals("1")){
                    TeamManager_Button_MemberManager.setVisibility(View.VISIBLE);
                }
                if(parsedData[0][4].equals("1")){
                    TeamManager_Button_ScheduleManager.setVisibility(View.VISIBLE);
                }
                if(parsedData[0][5].equals("1")){
                    TeamManager_Button_TeamIntroManager.setVisibility(View.VISIBLE);
                }
                if(parsedData[0][6].equals("1")){
                    TeamManager_Button_NoticeManager.setVisibility(View.VISIBLE);
                }
            }else if(parsedData[0][0].equals("failed")){
                TeamManager_Button_TeamMake.setVisibility(View.VISIBLE);
            }

            TeamManager_Button_TeamMake.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent_TeamMake = new Intent(Navigation_TeamManager.this, Navigation_TeamManager_TeamMake1.class);
                    startActivity(intent_TeamMake);
                }
            });
            TeamManager_Button_ScheduleManager.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view,"준비중입니다.", Snackbar.LENGTH_SHORT).show();
                   /* Intent intent_Schedule = new Intent(Navigation_TeamManager.this, Navigation_TeamManager_Schedule.class);
                    startActivity(intent_Schedule);*/
                }
            });
            TeamManager_Button_TeamIntroManager.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent_TeamIntro = new Intent(Navigation_TeamManager.this, Navigation_TeamManager_TeamIntro.class);
                    intent_TeamIntro.putExtra("Id", Id);
                    startActivity(intent_TeamIntro);
                }
            });
            TeamManager_Button_MemberManager.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent_TeamMember = new Intent(Navigation_TeamManager.this, Navigation_TeamManager_Member.class);
                    intent_TeamMember.putExtra("Id", Id);
                    intent_TeamMember.putExtra("Team", Team);
                    intent_TeamMember.putExtra("MyDuty", Duty);
                    startActivity(intent_TeamMember);
                }
            });
            TeamManager_Button_NoticeManager.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view,"준비중입니다.", Snackbar.LENGTH_SHORT).show();
                }
            });
            TeamManager_Button_Withdraw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Duty.equals("팀대표")){
                        final MaterialDialog TeamPlayerDialog = new MaterialDialog(Navigation_TeamManager.this);
                        TeamPlayerDialog
                                .setTitle("팀 탈퇴")
                                .setMessage("직책 인수인계 후 탈퇴해주시기 바랍니다.")
                                .setPositiveButton("확인", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        TeamPlayerDialog.dismiss();
                                    }
                                });
                        TeamPlayerDialog.show();
                    }
                    else{
                        final MaterialDialog TeamPlayerDialog = new MaterialDialog(Navigation_TeamManager.this);
                        TeamPlayerDialog
                                .setTitle("팀원 정보")
                                .setMessage("팀을 탈퇴하시겠습니까?")
                                .setNegativeButton("탈퇴", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String result="";
                                        try {
                                            HttpClient client = new DefaultHttpClient();
                                            String postURL = "http://210.122.7.195:8080/Web_basket/TeamManager_WithDraw.jsp";
                                            HttpPost post = new HttpPost(postURL);

                                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                                            params.add(new BasicNameValuePair("Id", Id));
                                            params.add(new BasicNameValuePair("TeamName", Team));

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
                                        parsedData = jsonParserList(result);
                                        if(parsedData[0][0].equals("succed")){
                                            TeamPlayerDialog.dismiss();
                                        }
                                    }
                                })
                                .setPositiveButton("취소", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        TeamPlayerDialog.dismiss();
                                    }
                                });
                        TeamPlayerDialog.show();
                    }
                }
            });
        }

    }
    /////팀이 존재하는지 체크합니다.
    public String[][] TeamCheck_jsonParserList(String pRecvServerPage){
        Log.i("팀관리- 팀존재하는지", pRecvServerPage);
        try{
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");

            String[] jsonName = {"msg1"};
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
    /////매칭 탭 - out : 받아온 json 파싱합니다.//////////////////////////////////////////////////////////
    public String[][] jsonParserList(String pRecvServerPage){
        Log.i("팀 권한", pRecvServerPage);
        try{
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");

            String[] jsonName = {"msg1","msg2","msg3","msg4","msg5","msg6","msg7"};
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
}
