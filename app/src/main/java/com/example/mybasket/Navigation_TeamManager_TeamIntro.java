package com.example.mybasket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by park on 2016-05-17.
 */
public class Navigation_TeamManager_TeamIntro extends Activity implements
        Navigation_TeamManager_TeamIntro_ColorPicker.OnColorChangedListener{

    ImageView TeamManager_TeamIntro_ImageView_Emblem,TeamManager_TeamIntro_ImageView_Image1,TeamManager_TeamIntro_ImageView_Image2,TeamManager_TeamIntro_ImageView_Image3;
    EditText TeamManager_TeamIntro_EditText_HomeCourt, TeamManager_TeamIntro_EditText_Time,TeamManager_TeamIntro_EditText_TeamIntro;
    Button TeamManager_TeamIntro_Button_Save,TeamManager_TeamIntro_Button_Cancel;
    Button TeamManager_TeamIntro_Button_UniformTop;
    Button TeamManager_TeamIntro_EditText_TeamName, TeamManager_TeamIntro_EditText_TeamAddress;
    static String Id="";
    String[][] parsedData;
    String ImageUrl1,ImageUrl2,ImageUrl3,ImageUrl4;
    Bitmap bmImg;
    static String choice="";

    String TeamName = "";
    String TeamAddress_do = "";
    String TeamAddress_se = "";
    String HomeCourt = "";
    String Time = "";
    String TeamIntro = "";
    String UniformTop = "";
    String UniformBottom = "";
    String Image1 = "";
    String Image2 = "";
    String Image3 = "";
    String Emblem = "";
    boolean Top_Color, Bottom_Color;

    final int REQ_SELECT=0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_teammanager_teamintro);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        this.activity = this;
        TeamManager_TeamIntro_ImageView_Emblem = (ImageView)findViewById(R.id.TeamManager_TeamIntro_ImageView_Emblem);
        TeamManager_TeamIntro_ImageView_Image1 = (ImageView)findViewById(R.id.TeamManager_TeamIntro_ImageView_Image1);
        TeamManager_TeamIntro_ImageView_Image2 = (ImageView)findViewById(R.id.TeamManager_TeamIntro_ImageView_Image2);
        TeamManager_TeamIntro_ImageView_Image3 = (ImageView)findViewById(R.id.TeamManager_TeamIntro_ImageView_Image3);
        TeamManager_TeamIntro_EditText_TeamName = (Button)findViewById(R.id.TeamManager_TeamIntro_EditText_TeamName);
        TeamManager_TeamIntro_EditText_TeamAddress = (Button)findViewById(R.id.TeamManager_TeamIntro_EditText_TeamAddress);
        TeamManager_TeamIntro_EditText_HomeCourt = (EditText)findViewById(R.id.TeamManager_TeamIntro_EditText_HomeCourt);
        TeamManager_TeamIntro_EditText_Time = (EditText)findViewById(R.id.TeamManager_TeamIntro_EditText_Time);
        TeamManager_TeamIntro_EditText_TeamIntro = (EditText)findViewById(R.id.TeamManager_TeamIntro_EditText_TeamIntro);
        TeamManager_TeamIntro_Button_UniformTop = (Button)findViewById(R.id.TeamManager_TeamIntro_Button_UniformTop);
        TeamManager_TeamIntro_Button_Save = (Button)findViewById(R.id.TeamManager_TeamIntro_Button_Save);
        TeamManager_TeamIntro_Button_Cancel = (Button)findViewById(R.id.TeamManager_TeamIntro_Button_Cancel);
        Intent intent1 = getIntent();
        Id = intent1.getStringExtra("Id");
        Log.i("Team",Id);
        String result="";
        try {
            HttpClient client = new DefaultHttpClient();
            String postURL = "http://210.122.7.195:8080/Web_basket/NaviTeamInfo.jsp";
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

        TeamName = parsedData[0][0];
        TeamAddress_do = parsedData[0][1];
        TeamAddress_se = parsedData[0][2];
        HomeCourt= parsedData[0][3];
        Time =parsedData[0][10];
        TeamIntro = parsedData[0][4];
        UniformTop = parsedData[0][5];
        Image1 = parsedData[0][6];
        Image2 = parsedData[0][7];
        Image3 = parsedData[0][8];
        Emblem = parsedData[0][9];

        TeamManager_TeamIntro_EditText_TeamName.setText(TeamName);
        TeamManager_TeamIntro_EditText_TeamAddress.setText(TeamAddress_do + " " + TeamAddress_se);
        TeamManager_TeamIntro_EditText_HomeCourt.setText(HomeCourt);
        TeamManager_TeamIntro_EditText_Time.setText(Time);
        TeamManager_TeamIntro_EditText_TeamIntro.setText(TeamIntro);
        //URI 한글 인코딩
        try{
            String En_Image1 = URLEncoder.encode(Image1, "utf-8");
            String En_Image2 = URLEncoder.encode(Image2, "utf-8");
            String En_Image3 = URLEncoder.encode(Image3, "utf-8");
            String En_Emblem = URLEncoder.encode(Emblem, "utf-8");
            if(!Image1.equals(".")) {
                Glide.with(Navigation_TeamManager_TeamIntro.this).load("http://210.122.7.195:8080/Web_basket/imgs/Team/" + En_Image1 + ".jpg")
                        .into(TeamManager_TeamIntro_ImageView_Image1);
            }
            if(!Image2.equals(".")) {
                Glide.with(Navigation_TeamManager_TeamIntro.this).load("http://210.122.7.195:8080/Web_basket/imgs/Team/" + En_Image2 + ".jpg")
                        .into(TeamManager_TeamIntro_ImageView_Image2);
            }
            if(!Image3.equals(".")) {
                Glide.with(Navigation_TeamManager_TeamIntro.this).load("http://210.122.7.195:8080/Web_basket/imgs/Team/" + En_Image3 + ".jpg")
                        .into(TeamManager_TeamIntro_ImageView_Image3);
            }
            if(!Emblem.equals(".")) {
                Glide.with(Navigation_TeamManager_TeamIntro.this).load("http://210.122.7.195:8080/Web_basket/imgs/Emblem/" + En_Emblem + ".jpg")
                        .into(TeamManager_TeamIntro_ImageView_Emblem);
            }
            TeamManager_TeamIntro_ImageView_Image1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
        catch (UnsupportedEncodingException e){

        }
        int UniformTop_color = Integer.parseInt(UniformTop);
        Navigation_TeamManager_TeamIntro.this.findViewById(R.id.TeamManager_TeamIntro_Button_UniformTop).setBackgroundColor(UniformTop_color);
        TeamManager_TeamIntro_ImageView_Emblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choice = "emblem";
                if (Emblem.equals(".")) {
                    //사진 읽어오기위한 uri 작성하기.
                    Uri uri = Uri.parse("content://media/external/images/media");
                    //무언가 보여달라는 암시적 인텐트 객체 생성하기.
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    //인텐트에 요청을 덛붙인다.
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    //모든 이미지
                    intent.setType("image/*");
                    //결과값을 받아오는 액티비티를 실행한다.
                    startActivityForResult(intent, REQ_SELECT);
                    Emblem = TeamName;
                } else {
                    LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                    View layout = inflater.inflate(R.layout.layout_customdialog_album, (ViewGroup) view.findViewById(R.id.Layout_CustomDialog_Album_Root));
                    final Button Layout_CustomDialog_Album_BasicImage = (Button) layout.findViewById(R.id.Layout_CustomDialog_Album_BasicImage);
                    final Button Layout_CustomDialog_Album_AlbumImage = (Button) layout.findViewById(R.id.Layout_CustomDialog_Album_AlbumImage);
                    final Button Layout_CustomDialog_Album_Cancel = (Button) layout.findViewById(R.id.Layout_CustomDialog_Album_Cancel);
                    final AlertDialog.Builder aDialog = new AlertDialog.Builder(view.getContext());
                    aDialog.setTitle("이미지 변경");
                    aDialog.setView(layout);
                    final AlertDialog ad = aDialog.create();
                    ad.show();
                    Layout_CustomDialog_Album_Cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ad.dismiss();
                        }
                    });
                    Layout_CustomDialog_Album_BasicImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            TeamManager_TeamIntro_ImageView_Emblem.setImageResource(R.drawable.basic_image);
                            Emblem = ".";
                            ad.dismiss();
                        }
                    });
                    Layout_CustomDialog_Album_AlbumImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //사진 읽어오기위한 uri 작성하기.
                            Uri uri = Uri.parse("content://media/external/images/media");
                            //무언가 보여달라는 암시적 인텐트 객체 생성하기.
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            //인텐트에 요청을 덛붙인다.
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            //모든 이미지
                            intent.setType("image/*");
                            //결과값을 받아오는 액티비티를 실행한다.
                            startActivityForResult(intent, REQ_SELECT);
                            ad.dismiss();
                        }
                    });
                    Emblem = TeamName;
                }
            }
        });
        TeamManager_TeamIntro_ImageView_Image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choice = "image1";
                if (Image1.equals(".")) {
                    //사진 읽어오기위한 uri 작성하기.
                    Uri uri = Uri.parse("content://media/external/images/media");
                    //무언가 보여달라는 암시적 인텐트 객체 생성하기.
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    //인텐트에 요청을 덛붙인다.
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    //모든 이미지
                    intent.setType("image/*");
                    //결과값을 받아오는 액티비티를 실행한다.
                    startActivityForResult(intent, REQ_SELECT);
                    Image1=TeamName+"1";
                } else {
                    LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                    View layout = inflater.inflate(R.layout.layout_customdialog_album, (ViewGroup) view.findViewById(R.id.Layout_CustomDialog_Album_Root));
                    final Button Layout_CustomDialog_Album_BasicImage = (Button) layout.findViewById(R.id.Layout_CustomDialog_Album_BasicImage);
                    final Button Layout_CustomDialog_Album_AlbumImage = (Button) layout.findViewById(R.id.Layout_CustomDialog_Album_AlbumImage);
                    final Button Layout_CustomDialog_Album_Cancel = (Button) layout.findViewById(R.id.Layout_CustomDialog_Album_Cancel);
                    final AlertDialog.Builder aDialog = new AlertDialog.Builder(view.getContext());
                    aDialog.setTitle("이미지 변경");
                    aDialog.setView(layout);
                    final AlertDialog ad = aDialog.create();
                    ad.show();
                    Layout_CustomDialog_Album_Cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ad.dismiss();
                        }
                    });
                    Layout_CustomDialog_Album_BasicImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            TeamManager_TeamIntro_ImageView_Image1.setImageResource(R.drawable.basic_image);
                            Image1 = ".";
                            ad.dismiss();
                        }
                    });
                    Layout_CustomDialog_Album_AlbumImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //사진 읽어오기위한 uri 작성하기.
                            Uri uri = Uri.parse("content://media/external/images/media");
                            //무언가 보여달라는 암시적 인텐트 객체 생성하기.
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            //인텐트에 요청을 덛붙인다.
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            //모든 이미지
                            intent.setType("image/*");
                            //결과값을 받아오는 액티비티를 실행한다.
                            startActivityForResult(intent, REQ_SELECT);
                            ad.dismiss();
                        }
                    });
                    Image1=TeamName+"1";
                }
            }
        });
        TeamManager_TeamIntro_ImageView_Image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choice = "image2";
                if (Image1.equals(".")) {
                    //사진 읽어오기위한 uri 작성하기.
                    Uri uri = Uri.parse("content://media/external/images/media");
                    //무언가 보여달라는 암시적 인텐트 객체 생성하기.
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    //인텐트에 요청을 덛붙인다.
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    //모든 이미지
                    intent.setType("image/*");
                    //결과값을 받아오는 액티비티를 실행한다.
                    startActivityForResult(intent, REQ_SELECT);
                    Image1=TeamName+"2";
                } else {
                    LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                    View layout = inflater.inflate(R.layout.layout_customdialog_album, (ViewGroup) view.findViewById(R.id.Layout_CustomDialog_Album_Root));
                    final Button Layout_CustomDialog_Album_BasicImage = (Button) layout.findViewById(R.id.Layout_CustomDialog_Album_BasicImage);
                    final Button Layout_CustomDialog_Album_AlbumImage = (Button) layout.findViewById(R.id.Layout_CustomDialog_Album_AlbumImage);
                    final Button Layout_CustomDialog_Album_Cancel = (Button) layout.findViewById(R.id.Layout_CustomDialog_Album_Cancel);
                    final AlertDialog.Builder aDialog = new AlertDialog.Builder(view.getContext());
                    aDialog.setTitle("이미지 변경");
                    aDialog.setView(layout);
                    final AlertDialog ad = aDialog.create();
                    ad.show();
                    Layout_CustomDialog_Album_Cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ad.dismiss();
                        }
                    });
                    Layout_CustomDialog_Album_BasicImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            TeamManager_TeamIntro_ImageView_Image2.setImageResource(R.drawable.basic_image);
                            Image2 = ".";
                            ad.dismiss();
                        }
                    });
                    Layout_CustomDialog_Album_AlbumImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //사진 읽어오기위한 uri 작성하기.
                            Uri uri = Uri.parse("content://media/external/images/media");
                            //무언가 보여달라는 암시적 인텐트 객체 생성하기.
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            //인텐트에 요청을 덛붙인다.
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            //모든 이미지
                            intent.setType("image/*");
                            //결과값을 받아오는 액티비티를 실행한다.
                            startActivityForResult(intent, REQ_SELECT);
                            ad.dismiss();
                        }
                    });
                    Image2=TeamName+"2";
                }
            }
        });
        TeamManager_TeamIntro_ImageView_Image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choice = "image3";
                if (Image1.equals(".")) {
                    //사진 읽어오기위한 uri 작성하기.
                    Uri uri = Uri.parse("content://media/external/images/media");
                    //무언가 보여달라는 암시적 인텐트 객체 생성하기.
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    //인텐트에 요청을 덛붙인다.
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    //모든 이미지
                    intent.setType("image/*");
                    //결과값을 받아오는 액티비티를 실행한다.
                    startActivityForResult(intent, REQ_SELECT);
                    Image1=TeamName+"3";
                }
                else {
                    LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                    View layout = inflater.inflate(R.layout.layout_customdialog_album, (ViewGroup) view.findViewById(R.id.Layout_CustomDialog_Album_Root));
                    final Button Layout_CustomDialog_Album_BasicImage = (Button) layout.findViewById(R.id.Layout_CustomDialog_Album_BasicImage);
                    final Button Layout_CustomDialog_Album_AlbumImage = (Button) layout.findViewById(R.id.Layout_CustomDialog_Album_AlbumImage);
                    final Button Layout_CustomDialog_Album_Cancel = (Button) layout.findViewById(R.id.Layout_CustomDialog_Album_Cancel);
                    final AlertDialog.Builder aDialog = new AlertDialog.Builder(view.getContext());
                    aDialog.setTitle("이미지 변경");
                    aDialog.setView(layout);
                    final AlertDialog ad = aDialog.create();
                    ad.show();
                    Layout_CustomDialog_Album_Cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ad.dismiss();
                        }
                    });
                    Layout_CustomDialog_Album_BasicImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            TeamManager_TeamIntro_ImageView_Image3.setImageResource(R.drawable.basic_image);
                            Image3 = ".";
                            ad.dismiss();
                        }
                    });
                    Layout_CustomDialog_Album_AlbumImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //사진 읽어오기위한 uri 작성하기.
                            Uri uri = Uri.parse("content://media/external/images/media");
                            //무언가 보여달라는 암시적 인텐트 객체 생성하기.
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            //인텐트에 요청을 덛붙인다.
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            //모든 이미지
                            intent.setType("image/*");
                            //결과값을 받아오는 액티비티를 실행한다.
                            startActivityForResult(intent, REQ_SELECT);
                            ad.dismiss();
                        }
                    });
                    Image3=TeamName+"3";
                }
            }
        });
       TeamManager_TeamIntro_Button_UniformTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Top_Color = true;
                Bottom_Color = false;
                getColor(view);
            }
        });

        TeamManager_TeamIntro_Button_Save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                HomeCourt = TeamManager_TeamIntro_EditText_HomeCourt.getText().toString();
                Time = TeamManager_TeamIntro_EditText_Time.getText().toString();
                TeamIntro = TeamManager_TeamIntro_EditText_TeamIntro.getText().toString();
                String result="";
                try {
                    HttpClient client = new DefaultHttpClient();
                    String postURL = "http://210.122.7.195:8080/Web_basket/Navi_TeamManager_TeamIntro_Update.jsp";
                    HttpPost post = new HttpPost(postURL);

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("TeamName", TeamName));
                    params.add(new BasicNameValuePair("HomeCourt", HomeCourt));
                    params.add(new BasicNameValuePair("Time", Time));
                    params.add(new BasicNameValuePair("TeamIntro", TeamIntro));
                    params.add(new BasicNameValuePair("UniformTop", UniformTop));
                    params.add(new BasicNameValuePair("UniformBottom", UniformBottom));
                    params.add(new BasicNameValuePair("Image1", Image1));
                    params.add(new BasicNameValuePair("Image2", Image2));
                    params.add(new BasicNameValuePair("Image3", Image3));
                    params.add(new BasicNameValuePair("Emblem", Emblem));
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
                parsedData = jsonParserList_save(result);
                if(parsedData[0][0].equals("succed"))
                {
                    Toast.makeText(Navigation_TeamManager_TeamIntro.this,"수정완료",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(Navigation_TeamManager_TeamIntro.this,"잠시 후 다시 시도해주세요.",Toast.LENGTH_SHORT).show();
                }
            }
        });
        TeamManager_TeamIntro_Button_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    /////네비 탭 - 매 팀정보수정 : 받아온 json 파싱합니다.//////////////////////////////////////////////////////////
    public String[][] jsonParserList(String pRecvServerPage){
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try{
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");

            String[] jsonName = {"msg1","msg2","msg3","msg4","msg5","msg6","msg7","msg8","msg9", "msg10","msg11"};
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
    /////네비 탭 - 매 팀정보 : 받아온 json 파싱합니다.//////////////////////////////////////////////////////////
    public String[][] jsonParserList_save(String pRecvServerPage){
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
    public void colorChanged(String str,int color)
    {
        if(Top_Color == true)
        {
            Navigation_TeamManager_TeamIntro.this.findViewById(R.id.TeamManager_TeamIntro_Button_UniformTop).setBackgroundColor(color);
            UniformTop = Integer.toString(color);
        }
    }

    Activity activity;

    public void getColor(View v) {
        new Navigation_TeamManager_TeamIntro_ColorPicker(activity, this, "", Color.BLACK, Color.WHITE).show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        try {
//인텐트에 데이터가 담겨 왔다면
            if (!intent.getData().equals(null)) {
//해당경로의 이미지를 intent에 담긴 이미지 uri를 이용해서 Bitmap형태로 읽어온다.
                Bitmap selPhoto = MediaStore.Images.Media.getBitmap(getContentResolver(), intent.getData());
                //이미지의 크기 조절하기.
                selPhoto = Bitmap.createScaledBitmap(selPhoto, 100, 100, true);
                //image_bt.setImageBitmap(selPhoto);//썸네일
                //화면에 출력해본다.
                if(choice.equals("emblem")) {
                    TeamManager_TeamIntro_ImageView_Emblem.setImageBitmap(selPhoto);
                }
                else if(choice.equals("image1")) {
                    TeamManager_TeamIntro_ImageView_Image1.setImageBitmap(selPhoto);
                }
                else if(choice.equals("image2")) {
                    TeamManager_TeamIntro_ImageView_Image2.setImageBitmap(selPhoto);
                }
                else if(choice.equals("image3")) {
                    TeamManager_TeamIntro_ImageView_Image3.setImageBitmap(selPhoto);
                }

                Log.e("선택 된 이미지 ", "selPhoto : " + selPhoto);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
//선택한 이미지의 uri를 읽어온다.
        Uri selPhotoUri = intent.getData();
        Log.e("전송", "시~~작 ~~~~~!");
//업로드할 서버의 url 주소
        String urlString="";
        if(choice.equals("emblem")) {
            urlString = "http://210.122.7.195:8080/Web_basket/Upload_Emblem.jsp";
        }
        else if(choice.equals("image1")) {
            urlString = "http://210.122.7.195:8080/Web_basket/Upload_image1.jsp";
        }
        else if(choice.equals("image2")) {
            urlString = "http://210.122.7.195:8080/Web_basket/Upload_image2.jsp";
        }
        else if(choice.equals("image3")) {
            urlString = "http://210.122.7.195:8080/Web_basket/Upload_image3.jsp";
        }
        //절대경로를 획득한다!!! 중요~
        Cursor c = getContentResolver().query(Uri.parse(selPhotoUri.toString()), null, null, null,null);
        c.moveToNext();
        //업로드할 파일의 절대경로 얻어오기("_data") 로 해도 된다.
        String absolutePath = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
        Log.e("###파일의 절대 경로###", absolutePath);
        //파일 업로드 시작!
        HttpFileUpload(urlString ,"", absolutePath);


    }

    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";

    public void HttpFileUpload(String urlString, String params, String fileName) {
       // fileName=TeamName;
        try{
            //선택한 파일의 절대 경로를 이용해서 파일 입력 스트림 객체를 얻어온다.
            FileInputStream mFileInputStream = new FileInputStream(fileName);
            //파일을 업로드할 서버의 url 주소를이용해서 URL 객체 생성하기.
            URL connectUrl = new URL(urlString);
            //Connection 객체 얻어오기.
            HttpURLConnection conn = (HttpURLConnection)connectUrl.openConnection();
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
            if(choice.equals("emblem")) {
                dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + URLEncoder.encode(TeamName, "utf-8")+".jpg"+"\"" + lineEnd);
            }
            else if(choice.equals("image1")) {
                dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + URLEncoder.encode(TeamName+"1", "utf-8")+".jpg"+"\"" + lineEnd);
            }
            else if(choice.equals("image2")) {
                dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + URLEncoder.encode(TeamName+"2", "utf-8")+".jpg"+"\"" + lineEnd);
            }
            else if(choice.equals("image3")) {
                dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + URLEncoder.encode(TeamName+"3", "utf-8")+".jpg"+"\"" + lineEnd);
            }
            dos.writeBytes(lineEnd);
            //한번에 읽어들일수있는 스트림의 크기를 얻어온다.
            int bytesAvailable = mFileInputStream.available();
            //byte단위로 읽어오기 위하여 byte 배열 객체를 준비한다.
            byte[] buffer = new byte[bytesAvailable];
            int bytesRead = 0;
            // read image
            while (bytesRead!=-1) {
                //파일에서 바이트단위로 읽어온다.
                bytesRead = mFileInputStream.read(buffer);
                if(bytesRead==-1)break; //더이상 읽을 데이터가 없다면 빠저나온다.
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
            StringBuffer b =new StringBuffer();
            while( ( ch = is.read() ) != -1 ){
                b.append( (char)ch );
            }
            String s=b.toString();
            Log.e("Test", "result = " + s);

        } catch (Exception e) {
            Log.d("Test", "exception " + e.getMessage());
            Toast.makeText(this,"업로드중 에러발생!", Toast.LENGTH_SHORT).show();
        }
    }
}
