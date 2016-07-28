package com.example.mybasket;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

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
 * Created by 박효근 on 2016-06-24.
 */
public class Navigation_TeamManager_Member extends AppCompatActivity{
    FrameLayout TeamManager_Member_Layout_TeamPlayer,TeamManager_Member_Layout_Joiner;
    SwipeMenuListView TeamManager_Member_ListView_JoinerList,TeamManager_Member_ListView_TeamPlayerList;
    static String Id, Team;
    String[][] parsedData_TeamPlayer,parsedData_Joiner, parsedData_TeamPlayer_Delete;
    //팀원 리스트 선언
    Navigation_TeamManager_Member_Customlist_TeamPlayer_MyAdapter navigation_TeamManager_Member_Customlist_TeamPlayer_MyAdapter;
    ArrayList<Navigation_TeamManager_Member_Customlist_TeamPlayer_MyData> navigation_TeamManager_Member_Customlist_TeamPlayer_MyData;
    //가입신청자 리스트 선언
    Navigation_TeamManager_Member_CustomList_Joiner_MyAdapter navigation_TeamManager_Member_CustomListJoiner_MyAdapter;
    ArrayList<Navigation_TeamManager_Member_CustomList_Joiner_MyData> navigation_TeamManager_Member_CustomListJoiner_MyData;
    ArrayList<Navigation_TeamManager_Member_Customlist_TeamPlayer_MyData> arrData;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_teammanager_player);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.addTab(tabLayout.newTab().setText("TeamPlayer"));
        tabLayout.addTab(tabLayout.newTab().setText("Joiner"));
        TeamManager_Member_Layout_TeamPlayer = (FrameLayout)findViewById(R.id.TeamManager_Member_Layout_TeamPlayer);
        TeamManager_Member_Layout_Joiner = (FrameLayout)findViewById(R.id.TeamManager_Member_Layout_Joiner);
        TeamManager_Member_ListView_TeamPlayerList = (SwipeMenuListView)findViewById(R.id.TeamManager_Member_ListView_TeamPlayerList);
        TeamManager_Member_ListView_JoinerList = (SwipeMenuListView)findViewById(R.id.TeamManager_Member_ListView_JoinerList);

        Intent intent1 = getIntent();
        Id = intent1.getStringExtra("Id");
        Team = intent1.getStringExtra("Team");
        String result="";
        try {
            HttpClient client = new DefaultHttpClient();
            String postURL = "http://210.122.7.195:8080/Web_basket/NaviTeamManager_TeamPlayer.jsp";
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
        parsedData_TeamPlayer = jsonParserList_TeamPlayerList(result);
        setData_TeamPlayer();
        navigation_TeamManager_Member_Customlist_TeamPlayer_MyAdapter = new Navigation_TeamManager_Member_Customlist_TeamPlayer_MyAdapter(Navigation_TeamManager_Member.this, navigation_TeamManager_Member_Customlist_TeamPlayer_MyData);
        //리스트뷰에 어댑터 연결

        TeamManager_Member_ListView_TeamPlayerList.setAdapter(navigation_TeamManager_Member_Customlist_TeamPlayer_MyAdapter);


        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //가입신청자 받아오기
      result="";
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
        parsedData_Joiner = jsonParserList_JoinList(result);
        setData_Joiner();
        navigation_TeamManager_Member_CustomListJoiner_MyAdapter = new Navigation_TeamManager_Member_CustomList_Joiner_MyAdapter(Navigation_TeamManager_Member.this, navigation_TeamManager_Member_CustomListJoiner_MyData);
        //리스트뷰에 어댑터 연결
        TeamManager_Member_ListView_JoinerList.setAdapter(navigation_TeamManager_Member_CustomListJoiner_MyAdapter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==0){
                    TeamManager_Member_Layout_TeamPlayer.setVisibility(View.VISIBLE);
                    TeamManager_Member_Layout_Joiner.setVisibility(View.GONE);
                }
                else if(tab.getPosition()==1){
                    TeamManager_Member_Layout_TeamPlayer.setVisibility(View.GONE);
                    TeamManager_Member_Layout_Joiner.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        SwipeMenuCreator creator_TeamPlayer = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(180);
                // set a icon
                deleteItem.setIcon(R.drawable.delete1);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

// set creator
        TeamManager_Member_ListView_TeamPlayerList.setMenuCreator(creator_TeamPlayer);
        TeamManager_Member_ListView_TeamPlayerList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, final SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // delete
                        final MaterialDialog TeamPlayerDialog = new MaterialDialog(Navigation_TeamManager_Member.this);

                        TeamPlayerDialog
                                .setTitle("팀원 정보")
                                .setMessage("팀원 삭제 시 팀에서 방출됩니다.\n"+"팀에서 삭제하시겠습니까?")
                                .setNegativeButton("OK", new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v){

                                        String TeamPlayer_Delete_result="";
                                        try {
                                            HttpClient client = new DefaultHttpClient();
                                            String postURL = "http://210.122.7.195:8080/Web_basket/TeamPlayer_Delete.jsp";
                                            HttpPost post = new HttpPost(postURL);

                                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                                            params.add(new BasicNameValuePair("Id", navigation_TeamManager_Member_Customlist_TeamPlayer_MyData.get(position).getId()));

                                            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                            post.setEntity(ent);

                                            HttpResponse response = client.execute(post);
                                            BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));

                                            String line = null;
                                            while ((line = bufreader.readLine()) != null) {
                                                TeamPlayer_Delete_result += line;
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        parsedData_TeamPlayer_Delete = jsonParserList_TeamPlayerDelete(TeamPlayer_Delete_result);
                                        if(parsedData_TeamPlayer_Delete[0][0].equals("succed")){
                                            Snackbar.make(v,"팀원이 삭제되었습니다.",Snackbar.LENGTH_SHORT).show();
                                            TeamPlayerDialog.dismiss();
                                        }
                                        else {
                                            Snackbar.make(v,"잠시 후 다시 시도해주세요.",Snackbar.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .setPositiveButton("Cancel", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                TeamPlayerDialog.dismiss();
                                            }
                                        });

                                TeamPlayerDialog.show();

                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        //////////////////////////////////////////////////////////////////////////////////////////////
        //가입신청자 - 스와이프
        SwipeMenuCreator creator_Joiner = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(180);
                // set item title
                openItem.setTitle("Agree");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(180);
                // set item title
                deleteItem.setTitle("Refuse");
                // set item width
                deleteItem.setTitleSize(18);
                // set item title font color
                deleteItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(deleteItem);

            }
        };
        TeamManager_Member_ListView_JoinerList.setMenuCreator(creator_Joiner);
        TeamManager_Member_ListView_JoinerList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, final SwipeMenu menu, int index) {
                switch (index) {
                    case 0:

                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

    }
    private void setData_TeamPlayer()
    {
        navigation_TeamManager_Member_Customlist_TeamPlayer_MyData = new ArrayList<Navigation_TeamManager_Member_Customlist_TeamPlayer_MyData>();
        for(int i =0; i<parsedData_TeamPlayer.length; i++)
        {
            navigation_TeamManager_Member_Customlist_TeamPlayer_MyData.add(new Navigation_TeamManager_Member_Customlist_TeamPlayer_MyData(
                    parsedData_TeamPlayer[i][7],"01073225945",parsedData_TeamPlayer[i][0],parsedData_TeamPlayer[i][1],parsedData_TeamPlayer[i][2],parsedData_TeamPlayer[i][3],parsedData_TeamPlayer[i][5],parsedData_TeamPlayer[i][6],"10",parsedData_TeamPlayer[i][4]));
        }
    }
    private void setData_Joiner()
    {
        navigation_TeamManager_Member_CustomListJoiner_MyData = new ArrayList<Navigation_TeamManager_Member_CustomList_Joiner_MyData>();
        for(int i =0; i<parsedData_Joiner.length; i++)
        {
            navigation_TeamManager_Member_CustomListJoiner_MyData.add(new Navigation_TeamManager_Member_CustomList_Joiner_MyData(parsedData_Joiner[i][0],parsedData_Joiner[i][1],parsedData_Joiner[i][2],parsedData_Joiner[i][3],parsedData_Joiner[i][4],parsedData_Joiner[i][5],parsedData_Joiner[i][6],parsedData_Joiner[i][7],"1",Team));
        }
    }
    public String[][] jsonParserList_TeamPlayerList(String pRecvServerPage){
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try{
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");

            String[] jsonName = {"msg1","msg2","msg3","msg4","msg5","msg6","msg7","msg8"};
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
    public String[][] jsonParserList_JoinList(String pRecvServerPage){
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try{
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");

            String[] jsonName = {"msg1","msg2","msg3","msg4","msg5","msg6","msg7","msg8"};
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
    public String[][] jsonParserList_TeamPlayerDelete(String pRecvServerPage){
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
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
}
