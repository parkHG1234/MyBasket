package com.mysports.basketbook;

/**
 * Created by 박효근 on 2016-07-22.
 */

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.mysports.basketbook.R;
import com.rengwuxian.materialedittext.MaterialEditText;

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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by 박지훈 on 2016-06-21.
 */
public class Setting extends Activity {

    String Id;
    String Alarm;
    String Token;
    String msg = "2016 / 01 / 01";

    View view;
    ImageView Setting_ImageView;
    Button Setting_Button_notice;
    Button Setting_Button_recommend;
    Button Setting_Button_Alarm;
    LinearLayout Setting_LinearLayout_Alarm;
    Switch Setting_Switch_Alarm;
    String Setting_Choice = "close";
    Button Setting_Button_DropOut;
    TextView Layout_Setting_TextView_Dropout;

    int year, month, day;
    ExpandableListView notice_ListView;
    MaterialEditText recommend_EditText;
    Button recommend_Button;
    boolean flag = true;

    private ArrayList<String> mGroupList = null;
    private ArrayList<ArrayList<String>> mChildList = null;
    private ArrayList<String> mChildListContent = null;
    Setting_Notice_Adapter noticeAdapter;
    ArrayList<Setting_Notice_Setting> arrData;
    String[][] parsedData, parsedData_Alarm, parsedData_BasicSetting, parsedData_dropout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting);
        Intent intent1 = getIntent();
        Id = intent1.getStringExtra("Id");
        Token = intent1.getStringExtra("Token");

        Setting_ImageView = (ImageView) findViewById(R.id.Setting_ImageView);
        Setting_Button_notice = (Button) findViewById(R.id.Setting_Button_notice);
        notice_ListView = (ExpandableListView) findViewById(R.id.notice_ListView);
        Setting_Button_recommend = (Button) findViewById(R.id.Setting_Button_recommend);
        recommend_EditText = (MaterialEditText) findViewById(R.id.recommend_EditText);
        recommend_Button = (Button) findViewById(R.id.recommend_Button);
        Setting_Button_Alarm = (Button) findViewById(R.id.Setting_Button_Alarm);
        Setting_LinearLayout_Alarm = (LinearLayout) findViewById(R.id.Setting_LinearLayout_Alarm);
        Setting_Switch_Alarm = (Switch) findViewById(R.id.Setting_Switch_Alarm);
        Setting_Button_DropOut = (Button) findViewById(R.id.Setting_Button_DropOut);


        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        GregorianCalendar calendar = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);


        String result = "";
        try {
            HttpClient client = new DefaultHttpClient();
            String postURL = "http://210.122.7.195:8080/Web_basket/Gcm_IdAdd_Alarm.jsp";
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
            Log.i("결과", result);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("실패", "실패");
        }
        parsedData_BasicSetting = jsonParserList_BasicSetting(result);
        if (parsedData_BasicSetting[0][0].equals("on")) {
            Setting_Switch_Alarm.setChecked(true);
        } else if (parsedData_BasicSetting[0][0].equals("off")) {
            Setting_Switch_Alarm.setChecked(false);
        }
        Setting_ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view =v;
                finish();
            }
        });


        Setting_Button_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = "";

                if (flag) {
                    notice_ListView.setVisibility(View.VISIBLE);
                    notice_ListView.setVisibility(View.VISIBLE);

                    try {
                        HttpClient client = new DefaultHttpClient();
                        String postURL = "http://210.122.7.195:8080/gg/notice_download.jsp";
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
                        setData();

                        noticeAdapter = new Setting_Notice_Adapter(Setting.this, arrData);
                        notice_ListView.setAdapter(noticeAdapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    flag = false;
                } else {
                    notice_ListView.setVisibility(View.GONE);
                    notice_ListView.setVisibility(View.GONE);
                    flag = true;
                }
            }
        });

        Setting_Button_recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flag) {
                    recommend_EditText.setVisibility(View.VISIBLE);
                    recommend_Button.setVisibility(View.VISIBLE);
                    flag = false;
                } else {
                    recommend_EditText.setVisibility(View.GONE);
                    recommend_Button.setVisibility(View.GONE);
                    flag = true;
                }
            }
        });
        recommend_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recommend_EditText.setHorizontallyScrolling(false);
                recommend_EditText.setVisibility(View.GONE);
                recommend_Button.setVisibility(View.GONE);
                flag = true;


                try {
                    HttpClient client = new DefaultHttpClient();
                    String postURL = "http://210.122.7.195:8080/gg/recommend_upload.jsp";
                    HttpPost post = new HttpPost(postURL);
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("recommend", String.valueOf(recommend_EditText.getText())));
                    params.add(new BasicNameValuePair("id", Id));
                    UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                    post.setEntity(ent);
                    HttpResponse response = client.execute(post);

                    recommend_EditText.setText("");

                } catch (Exception e) {
                    Snackbar.make(v, "다시시도해주세요.", Snackbar.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        Setting_Button_Alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Setting_Choice.equals("close")) {
                    Setting_LinearLayout_Alarm.setVisibility(View.VISIBLE);
                    Setting_Choice = "open";

                } else if (Setting_Choice.equals("open")) {
                    Setting_LinearLayout_Alarm.setVisibility(View.GONE);
                    Setting_Choice = "close";
                }
            }
        });
        Setting_Switch_Alarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked == true) {
                    String Alarm_off_result = "";
                    try {
                        HttpClient client = new DefaultHttpClient();
                        String postURL = "http://210.122.7.195:8080/Web_basket/Alarm.jsp";
                        HttpPost post = new HttpPost(postURL);

                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("Id", Id));
                        params.add(new BasicNameValuePair("Alarm", "on"));
                        params.add(new BasicNameValuePair("Token", Token));
                        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                        post.setEntity(ent);

                        HttpResponse response = client.execute(post);
                        BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));

                        String line = null;
                        while ((line = bufreader.readLine()) != null) {
                            Alarm_off_result += line;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    parsedData_Alarm = jsonParserList_Alarm(Alarm_off_result);
                    if (parsedData_Alarm[0][0].equals("succed")) {
                        Setting_Switch_Alarm.setChecked(true);
                    }
                } else {

                    String Alarm_on_result = "";
                    try {
                        HttpClient client = new DefaultHttpClient();
                        String postURL = "http://210.122.7.195:8080/Web_basket/Alarm.jsp";
                        HttpPost post = new HttpPost(postURL);

                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("Id", Id));
                        params.add(new BasicNameValuePair("Alarm", "off"));
                        params.add(new BasicNameValuePair("Token", Token));
                        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                        post.setEntity(ent);

                        HttpResponse response = client.execute(post);
                        BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));

                        String line = null;
                        while ((line = bufreader.readLine()) != null) {
                            Alarm_on_result += line;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    parsedData_Alarm = jsonParserList_Alarm(Alarm_on_result);
                    if (parsedData_Alarm[0][0].equals("succed")) {
                        Setting_Switch_Alarm.setChecked(false);
                    }
                }
            }
        });

        final View layout = inflater.inflate(R.layout.layout_setting_dropout_dialog, (ViewGroup) findViewById(R.id.Layout_Setting_Dropout_Root));
        Layout_Setting_TextView_Dropout = (TextView) layout.findViewById(R.id.Layout_Setting_TextView_Dropout);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("회원탈퇴")        // 제목 설정
                .setMessage("탈퇴하시겠습니까?")        // 메세지 설정
                .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    // 확인 버튼 클릭시 설정
                    public void onClick(DialogInterface dialog, int whichButton) {
                        try {
                            HttpClient client = new DefaultHttpClient();
                            String postURL = "http://210.122.7.195:8080/gg/user_dropout.jsp";
                            HttpPost post = new HttpPost(postURL);
                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                            params.add(new BasicNameValuePair("Dropout_Id", Id));
                            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                            post.setEntity(ent);
                            client.execute(post);


                            Toast.makeText(getApplicationContext(),"회원탈퇴되었습니다.",Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {

                        }

                        moveTaskToBack(true);
                        finish();
                        android.os.Process.killProcess(android.os.Process.myPid());

                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    // 취소 버튼 클릭시 설정
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });


        final MaterialDialog DropOutDialog = new MaterialDialog(Setting.this);
        final AlertDialog dialog = builder.create();    // 알림창 객체 생성
        DropOutDialog
                .setTitle("비밀번호변경")
                .setView(layout)
                .setNegativeButton("취소", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DropOutDialog.dismiss();
                    }
                })
                .setPositiveButton("완료", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("aaaaaaaaa",parsedData_dropout[0][0]);
                        Log.i("aaaaaaaaa",msg);
                        if (msg.equals(parsedData_dropout[0][0])) {
                            dialog.show();
                        } else {

                            Snackbar.make(v, "생년월일을확인해주세요.", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
        Setting_Button_DropOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Dropout_result = "";
                try {
                    HttpClient client = new DefaultHttpClient();
                    String postURL = "http://210.122.7.195:8080/gg/dropout_confirm.jsp";
                    HttpPost post = new HttpPost(postURL);
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("Dropout_Id", Id));
                    UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                    post.setEntity(ent);
                    HttpResponse response = client.execute(post);
                    BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                    String line = null;
                    while ((line = bufreader.readLine()) != null) {
                        Dropout_result += line;
                    }
                    parsedData_dropout = jsonParserList_DropOut(Dropout_result);


                } catch (Exception e) {
                    e.printStackTrace();
                }
                DropOutDialog.show();
            }
        });


        Layout_Setting_TextView_Dropout.setOnClickListener(new View.OnClickListener() {
                                                               @Override
                                                               public void onClick(View v) {
                                                                   new DatePickerDialog(Setting.this, dateSetListener, year, month, day).show();
                                                               }
                                                           }
        );
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            msg = String.format("%d / %d / %d", year, monthOfYear + 1, dayOfMonth);
            Layout_Setting_TextView_Dropout.setText(msg);
        }
    };


    private void setData() {
        arrData = new ArrayList<Setting_Notice_Setting>();
        for (int a = 0; a < parsedData.length; a++) {
            arrData.add(new Setting_Notice_Setting(parsedData[a][0], parsedData[a][1], parsedData[a][2]));
        }
    }

    public String[][] jsonParserList(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");

            String[] jsonName = {"notice_num", "notice_title", "notice_content"};
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

    public String[][] jsonParserList_DropOut(String pRecvServerPage) {
        Log.i("탈퇴에서 받은 전체 내용", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");

            String[] jsonName = {"Birth"};
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


    public String[][] jsonParserList_Alarm(String pRecvServerPage) {
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
}