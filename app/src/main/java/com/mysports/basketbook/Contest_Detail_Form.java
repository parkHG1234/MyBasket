package com.mysports.basketbook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by 박효근 on 2016-11-13.
 */

public class Contest_Detail_Form extends AppCompatActivity {
    static String Id;
    static String Pk;
    static String MyTeam;
    static String Phone;
    static String Name;
    static int Player=0;
    static int JoinerCount=0;
    static String[] JoinerId;
    String[][] parsedData_Profile,parsedData_Player,parsedData_Joiner;
    Button Contest_Detail_Form_Button_TeamName;
    Button Contest_Detail_Form_Button_TeamLeader;
    Button Contest_Detail_Form_Button_TeamPhone;
    Button Contest_Detail_Form_Input;
    ListView Contest_Detail_Form_ListView;
    TextView Contest_Detail_Form_JoinerCount;
    Contest_Detail_Form_Customlist_Adapter Contest_Detail_Form_Customlist_Adapter;
    ArrayList<Contest_Detail_Form_Customlist_MyData> Contest_Detail_Form_Customlist_MyData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_contest_detail_form);

        Contest_Detail_Form_Button_TeamName = (Button)findViewById(R.id.Contest_Detail_Form_Button_TeamName);
        Contest_Detail_Form_Button_TeamLeader = (Button)findViewById(R.id.Contest_Detail_Form_Button_TeamLeader);
        Contest_Detail_Form_Button_TeamPhone = (Button)findViewById(R.id.Contest_Detail_Form_Button_TeamPhone);
        Contest_Detail_Form_JoinerCount = (TextView)findViewById(R.id.Contest_Detail_Form_JoinerCount);
        Contest_Detail_Form_ListView = (ListView)findViewById(R.id.Contest_Detail_Form_ListView);
        Contest_Detail_Form_Input = (Button)findViewById(R.id.Contest_Detail_Form_Input);
        
        Intent intent = getIntent();
        Pk = intent.getStringExtra("Pk");
        Id = intent.getStringExtra("Id");
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
        Phone = parsedData_Profile[0][12];
        Name = parsedData_Profile[0][2];
        Contest_Detail_Form_Button_TeamName.setText(MyTeam);
        Contest_Detail_Form_Button_TeamLeader.setText(Name);
        Contest_Detail_Form_Button_TeamPhone.setText(Phone);


        String result_Player="";
        try {
            HttpClient client = new DefaultHttpClient();
            String postURL = "http://210.122.7.193:8080/Web_basket/Contest_Detail_Fomr_Player.jsp";
            HttpPost post = new HttpPost(postURL);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("TeamName", MyTeam));

            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);

            HttpResponse response = client.execute(post);
            BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));

            String line = null;
            while ((line = bufreader.readLine()) != null) {
                result_Player += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        parsedData_Player = jsonParserList_Player(result_Player);
        setData_Player();
        Contest_Detail_Form_Customlist_Adapter = new Contest_Detail_Form_Customlist_Adapter(Contest_Detail_Form.this, Contest_Detail_Form_Customlist_MyData);
        //리스트뷰에 어댑터 연결
        Contest_Detail_Form_ListView.setAdapter(Contest_Detail_Form_Customlist_Adapter);
        Log.i("qwer11",Integer.toString(Player));
        JoinerId = new String[Player];
        for(int i=0; i<Player;i++){
            JoinerId[i]= "false";
        }
        Contest_Detail_Form_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.buildDrawingCache();
                Bitmap bitmap = view.getDrawingCache();
                int color = bitmap.getPixel(0, 0);
                Log.e("ChecktedText","Background Color: " + color);
                view.destroyDrawingCache();
                if(color == getResources().getColor(R.color.Red))
                {
                    JoinerCount--;
                    Contest_Detail_Form_JoinerCount.setText(Integer.toString(JoinerCount)+"명 참가");
                    view.setBackgroundColor(getResources().getColor(R.color.White));
                    JoinerId[i]="false";
                }
                else
                {
                    JoinerCount++;
                    Contest_Detail_Form_JoinerCount.setText(Integer.toString(JoinerCount)+"명 참가");
                    view.setBackgroundColor(getResources().getColor(R.color.Red));
                    JoinerId[i]=Contest_Detail_Form_Customlist_MyData.get(i).getId();
                }
                Log.i("aa",Integer.toString(i));
            }
        });
//
        Contest_Detail_Form_Input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0;i<Player;i++){
                    Log.i("qwe1",JoinerId[i]);
                    if(!JoinerId[i].equals("false")){
                        String result="";
                        try {
                            HttpClient client = new DefaultHttpClient();
                            String postURL = "http://210.122.7.193:8080/Web_basket/Contest_Detail_Form_Joiner.jsp";
                            HttpPost post = new HttpPost(postURL);

                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                            params.add(new BasicNameValuePair("TeamName", MyTeam));
                            params.add(new BasicNameValuePair("ContestId", Pk));
                            params.add(new BasicNameValuePair("Id", JoinerId[i]));
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
                    }
                }
                final MaterialDialog recommendDialog = new MaterialDialog(Contest_Detail_Form.this);
                    recommendDialog
                            .setTitle("참가신청서")
                            .setMessage("참가신청이 완료되었습니다.")
                            .setPositiveButton("확인", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    recommendDialog.dismiss();
                                    finish();
                                }
                            });
                    recommendDialog.show();
//                if(parsedData_Joiner[0][0].equals("succed")){
//                    final MaterialDialog recommendDialog = new MaterialDialog(Contest_Detail_Form.this);
//                    recommendDialog
//                            .setTitle("참가신청서")
//                            .setMessage("참가신청이 완료되었습니다.")
//                            .setPositiveButton("확인", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    recommendDialog.dismiss();
//                                }
//                            });
//                    recommendDialog.show();
//                }
            }
        });
    }
    /////프로필 탭 사용자정보를 파싱합니다.//////////////////////////////////////////////////////////
    public String[][] jsonParserList_UserInfo(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");

            String[] jsonName = {"msg1", "msg2", "msg3", "msg4", "msg5", "msg6", "msg7", "msg8","msg9","msg10","msg11","msg12","msg13"};
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
    public String[][] jsonParserList_Player(String pRecvServerPage){
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try{
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");

            String[] jsonName = {"msg1","msg2","msg3","msg4", "msg5", "msg6","msg7"};
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
    public String[][] jsonParserList_Joiner(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");

            String[] jsonName = {"msg1"};
            String[][] parseredData = new String[jArr.length()][jsonName.length];
            Player = jsonName.length;
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
    private void setData_Player()
    {
        Contest_Detail_Form_Customlist_MyData = new ArrayList<Contest_Detail_Form_Customlist_MyData>();
        for(int i =0; i<parsedData_Player.length; i++)
        {
            Contest_Detail_Form_Customlist_MyData.add(new Contest_Detail_Form_Customlist_MyData(parsedData_Player[i][0],parsedData_Player[i][1],parsedData_Player[i][2],parsedData_Player[i][3],parsedData_Player[i][4],parsedData_Player[i][5],MyTeam,parsedData_Player[i][6]));
        }
        Player = parsedData_Player.length;
    }
}
