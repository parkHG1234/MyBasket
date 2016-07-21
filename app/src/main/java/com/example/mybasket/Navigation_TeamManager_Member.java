package com.example.mybasket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

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
 * Created by 박효근 on 2016-06-24.
 */
public class Navigation_TeamManager_Member extends Activity{
    Button TeamManager_Member_Button_TeamPlayer, TeamManager_Member_Button_Joiner;
    ListView TeamManager_Member_ListView_JoinerList,TeamManager_Member_ListView_TeamPlayerList;
    static String Id, Team;
    String[][] parsedData;
    //팀원 리스트 선언
    Navigation_TeamManager_Member_Customlist_TeamPlayer_MyAdapter navigation_TeamManager_Member_Customlist_TeamPlayer_MyAdapter;
    ArrayList<Navigation_TeamManager_Member_Customlist_TeamPlayer_MyData> navigation_TeamManager_Member_Customlist_TeamPlayer_MyData;
    //가입신청자 리스트 선언
    Navigation_TeamManager_Member_CustomList_Joiner_MyAdapter navigation_TeamManager_Member_CustomListJoiner_MyAdapter;
    ArrayList<Navigation_TeamManager_Member_CustomList_Joiner_MyData> navigation_TeamManager_Member_CustomListJoiner_MyData;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_teammanager_player);

        TeamManager_Member_Button_TeamPlayer = (Button)findViewById(R.id.TeamManager_Member_Button_TeamPlayer);
        TeamManager_Member_Button_Joiner = (Button)findViewById(R.id.TeamManager_Member_Button_Joiner);
        TeamManager_Member_ListView_TeamPlayerList = (ListView)findViewById(R.id.TeamManager_Member_ListView_TeamPlayerList);
        TeamManager_Member_ListView_JoinerList = (ListView)findViewById(R.id.TeamManager_Member_ListView_JoinerList);

        Intent intent1 = getIntent();
        Id = intent1.getStringExtra("Id");
        Team = intent1.getStringExtra("Team");

        TeamManager_Member_Button_Joiner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result="";
                try {
                    HttpClient client = new DefaultHttpClient();
                    String postURL = "http://210.122.7.195:8080/Web_basket/NaviTeamManager_Joiner.jsp";
                    HttpPost post = new HttpPost(postURL);

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("Team", Team));

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
                parsedData = jsonParserList_JoinList(result);
                setData();
                navigation_TeamManager_Member_CustomListJoiner_MyAdapter = new Navigation_TeamManager_Member_CustomList_Joiner_MyAdapter(Navigation_TeamManager_Member.this, navigation_TeamManager_Member_CustomListJoiner_MyData);
                //리스트뷰에 어댑터 연결
                TeamManager_Member_ListView_JoinerList.setVisibility(View.VISIBLE);
                TeamManager_Member_ListView_JoinerList.setAdapter(navigation_TeamManager_Member_CustomListJoiner_MyAdapter);


            }
        });
    }
    private void setData()
    {
        navigation_TeamManager_Member_CustomListJoiner_MyData = new ArrayList<Navigation_TeamManager_Member_CustomList_Joiner_MyData>();
        for(int i =0; i<parsedData.length; i++)
        {
            navigation_TeamManager_Member_CustomListJoiner_MyData.add(new Navigation_TeamManager_Member_CustomList_Joiner_MyData(parsedData[i][0],parsedData[i][1],Team));
        }
    }
    public String[][] jsonParserList_JoinList(String pRecvServerPage){
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try{
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");

            String[] jsonName = {"msg1","msg2"};
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
