package com.mysports.basketbook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.melnykov.fab.FloatingActionButton;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by ldong on 2016-11-12.
 */
public class SectionsFragment4 extends Fragment {
    Button Profile_Button_Name, Profile_Button_Position, Profile_Button_Age_Physical, Profile_Button_TeamName;
    Button Profile_Button_TeamMake, Profile_Button_TeamManager, Profile_Button_TeamSearch, Profile_Button_Logout;
    Button Profile_Button_interestArea;
    Button Profile_Button_Password;
    LinearLayout layout_profile_Root;
    TextView update_textview;
    Button Profile_Button_setting;
    String[][] parsedData_overLap, parsedData_TeamCheck, parsedData_Alarm;
    String ProfileUrl;
    Bitmap bmImg;
    String Position, Name, Sex, Age;
    final int REQ_SELECT = 0;
    ArrayAdapter<CharSequence> adspin1, adspin2;

    public SectionsFragment4() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.layout_profile, container, false);
        Profile_Button_Name = (Button) rootView.findViewById(R.id.Profile_Button_Name);
        Profile_Button_Position = (Button) rootView.findViewById(R.id.Profile_Button_Position);
        Profile_Button_Age_Physical = (Button) rootView.findViewById(R.id.Profile_Button_Age_Physical);
        MainActivity.Profile_ImageVIew_Profile = (ImageView) rootView.findViewById(R.id.Profile_ImageVIew_Profile);
        Profile_Button_TeamName = (Button) rootView.findViewById(R.id.Profile_Button_TeamName);
        layout_profile_Root = (LinearLayout) rootView.findViewById(R.id.layout_profile_Root);
        update_textview = (TextView) rootView.findViewById(R.id.update_textview);
        Profile_Button_interestArea = (Button) rootView.findViewById(R.id.Profile_Button_interestArea);
        ///네비게이션 메뉴 선언
        Profile_Button_TeamMake = (Button) rootView.findViewById(R.id.Profile_Button_TeamMake);
        Profile_Button_TeamManager = (Button) rootView.findViewById(R.id.Profile_Button_TeamManager);
        Profile_Button_TeamSearch = (Button) rootView.findViewById(R.id.Profile_Button_TeamSearch);
        Profile_Button_Password = (Button) rootView.findViewById(R.id.Profile_Button_Password);
        Profile_Button_Logout = (Button) rootView.findViewById(R.id.Profile_Button_Logout);
        Profile_Button_setting = (Button) rootView.findViewById(R.id.Profile_Button_setting);
        if (Boolean.parseBoolean(MainActivity.fragment4)) {
            update_textview.setVisibility(View.GONE);
            layout_profile_Root.setVisibility(View.VISIBLE);
        } else {
            update_textview.setVisibility(View.VISIBLE);
            layout_profile_Root.setVisibility(View.GONE);
        }
        Profile_Button_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingIntent = new Intent(getContext(), Setting.class);
                settingIntent.putExtra("Id", MainActivity.Id);
                settingIntent.putExtra("Token", MainActivity.Token);
                startActivity(settingIntent);
            }
        });

        Name = MainActivity.parsedData_Profile[0][2];
        Position = MainActivity.parsedData_Profile[0][5];
        MainActivity.Profile = MainActivity.parsedData_Profile[0][7];
        Sex = MainActivity.parsedData_Profile[0][3];
        MainActivity.Team1 = MainActivity.parsedData_Profile[0][6];
        Age = ChangeAge(MainActivity.parsedData_Profile[0][4]);

        MainActivity.MyTeam = MainActivity.parsedData_Profile[0][6];
        MainActivity.Profile = MainActivity.parsedData_Profile[0][7];
        MainActivity.Height = MainActivity.parsedData_Profile[0][8];
        MainActivity.Weight = MainActivity.parsedData_Profile[0][9];
        MainActivity.interestArea_do = MainActivity.parsedData_Profile[0][10];
        MainActivity.interestArea_si = MainActivity.parsedData_Profile[0][11];

        Profile_Button_Name.setText(Name + "(" + Age + ")");
        Profile_Button_Position.setText(Position);
        Profile_Button_Age_Physical.setText(MainActivity.Height + " / " + MainActivity.Weight);
        Profile_Button_TeamName.setText(MainActivity.Team1);
        Profile_Button_interestArea.setText(MainActivity.interestArea_do + " / " + MainActivity.interestArea_si);
        //유저 개인 이미지를 서버에서 받아옵니다.
        try {
            String En_Profile = URLEncoder.encode(MainActivity.Profile, "utf-8");
            if (MainActivity.Profile.equals(".")) {
                Glide.with(rootView.getContext()).load(R.drawable.profile_basic_image).into(MainActivity.Profile_ImageVIew_Profile);
            } else {
                Glide.with(rootView.getContext()).load("http://210.122.7.193:8080/Web_basket/imgs/Profile/" + En_Profile + ".jpg").bitmapTransform(new CropCircleTransformation(Glide.get(rootView.getContext()).getBitmapPool()))
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(MainActivity.Profile_ImageVIew_Profile);
            }
        } catch (UnsupportedEncodingException e) {

        }
        MainActivity.Profile_ImageVIew_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.Profile.equals(".")) {
                    String result = "";
                    try {
                        HttpClient client = new DefaultHttpClient();
                        String postURL = "http://210.122.7.193:8080/Web_basket/Profile_Image.jsp";
                        HttpPost post = new HttpPost(postURL);

                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("Id", MainActivity.Id));
                        params.add(new BasicNameValuePair("Image", MainActivity.Id));
                        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                        post.setEntity(ent);

                        HttpResponse response = client.execute(post);
                        BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));

                        String line = null;
                        while ((line = bufreader.readLine()) != null) {
                            result += line;
                        }
                        MainActivity.Profile = "exist";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //사진 읽어오기위한 uri 작성하기.
                    Uri uri = Uri.parse("content://media/external/images/media");
                    //무언가 보여달라는 암시적 인텐트 객체 생성하기.
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    //인텐트에 요청을 덛붙인다.
                    intent.setAction(Intent.ACTION_PICK);
                    //모든 이미지
                    intent.setType("image/*");
                    //결과값을 받아오는 액티비티를 실행한다.
                    startActivityForResult(intent, REQ_SELECT);
                } else {
                    LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                            String result = "";
                            try {
                                HttpClient client = new DefaultHttpClient();
                                String postURL = "http://210.122.7.193:8080/Web_basket/Profile_Image.jsp";
                                HttpPost post = new HttpPost(postURL);

                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                params.add(new BasicNameValuePair("Id", MainActivity.Id));
                                params.add(new BasicNameValuePair("Image", "."));
                                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                post.setEntity(ent);

                                HttpResponse response = client.execute(post);
                                BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));

                                String line = null;
                                while ((line = bufreader.readLine()) != null) {
                                    result += line;
                                }
                                MainActivity.Profile_ImageVIew_Profile.setImageResource(R.drawable.profile_basic_image);
                                ad.dismiss();
                                MainActivity.Profile = ".";
                            } catch (Exception e) {
                                e.printStackTrace();
                                Snackbar.make(view, "다시 시도해 주시기 바랍니다.", Snackbar.LENGTH_SHORT).show();
                            }

                        }
                    });
                    Layout_CustomDialog_Album_AlbumImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String result = "";
                            try {
                                HttpClient client = new DefaultHttpClient();
                                String postURL = "http://210.122.7.193:8080/Web_basket/Profile_Image.jsp";
                                HttpPost post = new HttpPost(postURL);

                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                params.add(new BasicNameValuePair("Id", MainActivity.Id));
                                params.add(new BasicNameValuePair("Image", MainActivity.Id));
                                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                post.setEntity(ent);

                                HttpResponse response = client.execute(post);
                                BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));

                                String line = null;
                                while ((line = bufreader.readLine()) != null) {
                                    result += line;
                                }
                                //사진 읽어오기위한 uri 작성하기.
                                Uri uri = Uri.parse("content://media/external/images/media");
                                //무언가 보여달라는 암시적 인텐트 객체 생성하기.
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                //인텐트에 요청을 덛붙인다.
                                intent.setAction(Intent.ACTION_PICK);
                                //모든 이미지
                                intent.setType("image/*");
                                //결과값을 받아오는 액티비티를 실행한다.
                                startActivityForResult(intent, REQ_SELECT);
                                ad.dismiss();
                                MainActivity.Profile = "exist";
                            } catch (Exception e) {
                                e.printStackTrace();
                                Snackbar.make(view, "다시 시도해 주시기 바랍니다.", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        //////회원정보 수정
        Profile_Button_Position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View layout = inflater.inflate(R.layout.layout_customdialog_change_position, null);
                final RadioGroup rg = (RadioGroup) layout.findViewById(R.id.Positon_Root);

                final RadioButton Positon_Center = (RadioButton) layout.findViewById(R.id.Positon_Center);
                final RadioButton Positon_PowerFoward = (RadioButton) layout.findViewById(R.id.Positon_PowerFoward);
                final RadioButton Positon_SmallFoward = (RadioButton) layout.findViewById(R.id.Positon_SmallFoward);
                final RadioButton Positon_ShootingGuard = (RadioButton) layout.findViewById(R.id.Positon_ShootingGuard);
                final RadioButton Positon_PointGuard = (RadioButton) layout.findViewById(R.id.Positon_PointGuard);
                final Spinner Positon_Spinner_Height = (Spinner) layout.findViewById(R.id.Positon_Spinner_Height);
                final Spinner Positon_Spinner_Weight = (Spinner) layout.findViewById(R.id.Positon_Spinner_Weight);


                if (Position.equals("센터")) {
                    Positon_Center.setChecked(true);
                }
                if (Position.equals("파워포워드")) {
                    Positon_PowerFoward.setChecked(true);
                }
                if (Position.equals("스몰포워드")) {
                    Positon_SmallFoward.setChecked(true);
                }
                if (Position.equals("포인트가드")) {
                    Positon_PointGuard.setChecked(true);
                }
                if (Position.equals("슈팅가드")) {
                    Positon_ShootingGuard.setChecked(true);
                }


                adspin1 = ArrayAdapter.createFromResource(getContext(), R.array.height, R.layout.zfile_spinner_test);
                adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                Positon_Spinner_Height.setAdapter(adspin1);
                adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.weight, R.layout.zfile_spinner_test);
                adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Positon_Spinner_Weight.setAdapter(adspin2);

                Positon_Spinner_Height.setSelection(Integer.parseInt(MainActivity.Height) - 120, false);
                Positon_Spinner_Weight.setSelection(Integer.parseInt(MainActivity.Weight) - 40, false);
                AlertDialog.Builder buider = new AlertDialog.Builder(view.getContext()); //AlertDialog.Builder 객체 생성
                buider.setTitle("변경"); //Dialog 제목
                buider.setIcon(android.R.drawable.ic_menu_add); //제목옆의 아이콘 이미지(원하는 이미지 설정)
                buider.setView(layout);
                buider.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String CheckID = String.valueOf(rg.getCheckedRadioButtonId());

                        if (CheckID.equals(String.valueOf(Positon_Center.getId()))) {
                            Position = "센터";
                            Profile_Button_Position.setText(Position);
                        } else if (CheckID.equals(String.valueOf(Positon_PowerFoward.getId()))) {
                            Position = "파워포워드";
                            Profile_Button_Position.setText(Position);
                        } else if (CheckID.equals(String.valueOf(Positon_SmallFoward.getId()))) {
                            Position = "스몰포워드";
                            Profile_Button_Position.setText(Position);
                        } else if (CheckID.equals(String.valueOf(Positon_ShootingGuard.getId()))) {
                            Position = "슛팅가드";
                            Profile_Button_Position.setText(Position);
                        } else if (CheckID.equals(String.valueOf(Positon_PointGuard.getId()))) {
                            Position = "포인트가드";
                            Profile_Button_Position.setText(Position);
                        }

                        try {
                            HttpClient client = new DefaultHttpClient();
                            String postURL = "http://210.122.7.193:8080/gg/user_information_update.jsp";

                            HttpPost post = new HttpPost(postURL);
                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                            params.add(new BasicNameValuePair("id", MainActivity.Id));
                            params.add(new BasicNameValuePair("Position", Position));
                            params.add(new BasicNameValuePair("Height", String.valueOf(Positon_Spinner_Height.getSelectedItem())));
                            params.add(new BasicNameValuePair("Weight", String.valueOf(Positon_Spinner_Weight.getSelectedItem())));
                            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                            post.setEntity(ent);
                            HttpResponse response = client.execute(post);
                        } catch (Exception e) {
                            Snackbar.make(view, "다시시도해주세요.", Snackbar.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        Profile_Button_Age_Physical.setText(String.valueOf(Positon_Spinner_Height.getSelectedItem()) + " / " + String.valueOf(Positon_Spinner_Weight.getSelectedItem()));

                        String result_profile = "";
                        try {
                            HttpClient client = new DefaultHttpClient();
                            String postURL = "http://210.122.7.193:8080/Web_basket/Profile.jsp";
                            HttpPost post = new HttpPost(postURL);

                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                            params.add(new BasicNameValuePair("Id", MainActivity.Id));

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
                        MainActivity.parsedData_Profile = jsonParserList_UserInfo(result_profile);

                        Name = MainActivity.parsedData_Profile[0][2];
                        Position = MainActivity.parsedData_Profile[0][5];
                        Sex = MainActivity.parsedData_Profile[0][3];
                        MainActivity.Team1 = MainActivity.parsedData_Profile[0][6];
                        Age = ChangeAge(MainActivity.parsedData_Profile[0][4]);
                        MainActivity.MyTeam = MainActivity.parsedData_Profile[0][6];
                        MainActivity.Profile = MainActivity.parsedData_Profile[0][7];
                        MainActivity.Height = MainActivity.parsedData_Profile[0][8];
                        MainActivity.Weight = MainActivity.parsedData_Profile[0][9];
                        MainActivity.interestArea_do = MainActivity.parsedData_Profile[0][10];
                        MainActivity.interestArea_si = MainActivity.parsedData_Profile[0][11];
                        Profile_Button_interestArea.setText(MainActivity.interestArea_do + " / " + MainActivity.interestArea_si);

                        Profile_Button_Age_Physical.setText(String.valueOf(Positon_Spinner_Height.getSelectedItem()) + " / " + String.valueOf(Positon_Spinner_Weight.getSelectedItem()));
                    }
                });
                buider.show();
            }
        });


        Profile_Button_TeamName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View layout = inflater.inflate(R.layout.layout_customdialog_change_position, null);
                final RadioGroup rg = (RadioGroup) layout.findViewById(R.id.Positon_Root);

                final RadioButton Positon_Center = (RadioButton) layout.findViewById(R.id.Positon_Center);
                final RadioButton Positon_PowerFoward = (RadioButton) layout.findViewById(R.id.Positon_PowerFoward);
                final RadioButton Positon_SmallFoward = (RadioButton) layout.findViewById(R.id.Positon_SmallFoward);
                final RadioButton Positon_ShootingGuard = (RadioButton) layout.findViewById(R.id.Positon_ShootingGuard);
                final RadioButton Positon_PointGuard = (RadioButton) layout.findViewById(R.id.Positon_PointGuard);
                final Spinner Positon_Spinner_Height = (Spinner) layout.findViewById(R.id.Positon_Spinner_Height);
                final Spinner Positon_Spinner_Weight = (Spinner) layout.findViewById(R.id.Positon_Spinner_Weight);


                if (Position.equals("센터")) {
                    Positon_Center.setChecked(true);
                }
                if (Position.equals("파워포워드")) {
                    Positon_PowerFoward.setChecked(true);
                }
                if (Position.equals("스몰포워드")) {
                    Positon_SmallFoward.setChecked(true);
                }
                if (Position.equals("포인트가드")) {
                    Positon_PointGuard.setChecked(true);
                }
                if (Position.equals("슈팅가드")) {
                    Positon_ShootingGuard.setChecked(true);
                }
                //Positon_Spinner_Weight.setSelection(2);
                //멤버의 세부내역 입력 Dialog 생성 및 보이기


                adspin1 = ArrayAdapter.createFromResource(getContext(), R.array.height, R.layout.zfile_spinner_test);
                adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                Positon_Spinner_Height.setAdapter(adspin1);
                adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.weight, R.layout.zfile_spinner_test);
                adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Positon_Spinner_Weight.setAdapter(adspin2);

                Positon_Spinner_Height.setSelection(Integer.parseInt(MainActivity.Height) - 120, false);
                Positon_Spinner_Weight.setSelection(Integer.parseInt(MainActivity.Weight) - 40, false);

                AlertDialog.Builder buider = new AlertDialog.Builder(view.getContext()); //AlertDialog.Builder 객체 생성
                buider.setTitle("변경"); //Dialog 제목
                buider.setIcon(android.R.drawable.ic_menu_add); //제목옆의 아이콘 이미지(원하는 이미지 설정)
                buider.setView(layout);
                buider.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String CheckID = String.valueOf(rg.getCheckedRadioButtonId());

                        if (CheckID.equals(String.valueOf(Positon_Center.getId()))) {
                            Position = "센터";
                            Profile_Button_Position.setText(Position);
                        } else if (CheckID.equals(String.valueOf(Positon_PowerFoward.getId()))) {
                            Position = "파워포워드";
                            Profile_Button_Position.setText(Position);
                        } else if (CheckID.equals(String.valueOf(Positon_SmallFoward.getId()))) {
                            Position = "스몰포워드";
                            Profile_Button_Position.setText(Position);
                        } else if (CheckID.equals(String.valueOf(Positon_ShootingGuard.getId()))) {
                            Position = "슛팅가드";
                            Profile_Button_Position.setText(Position);
                        } else if (CheckID.equals(String.valueOf(Positon_PointGuard.getId()))) {
                            Position = "포인트가드";
                            Profile_Button_Position.setText(Position);
                        }

                        try {
                            HttpClient client = new DefaultHttpClient();
                            String postURL = "http://210.122.7.193:8080/gg/user_information_update.jsp";

                            HttpPost post = new HttpPost(postURL);
                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                            params.add(new BasicNameValuePair("id", MainActivity.Id));
                            params.add(new BasicNameValuePair("Position", Position));
                            params.add(new BasicNameValuePair("Height", String.valueOf(Positon_Spinner_Height.getSelectedItem())));
                            params.add(new BasicNameValuePair("Weight", String.valueOf(Positon_Spinner_Weight.getSelectedItem())));
                            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                            post.setEntity(ent);
                            HttpResponse response = client.execute(post);
                        } catch (Exception e) {
                            Snackbar.make(view, "다시시도해주세요.", Snackbar.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        Profile_Button_Age_Physical.setText(String.valueOf(Positon_Spinner_Height.getSelectedItem()) + " / " + String.valueOf(Positon_Spinner_Weight.getSelectedItem()));


                        String result_profile = "";
                        try {
                            HttpClient client = new DefaultHttpClient();
                            String postURL = "http://210.122.7.193:8080/Web_basket/Profile.jsp";
                            HttpPost post = new HttpPost(postURL);

                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                            params.add(new BasicNameValuePair("Id", MainActivity.Id));

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

                        MainActivity.parsedData_Profile = jsonParserList_UserInfo(result_profile);

                        Name = MainActivity.parsedData_Profile[0][2];
                        Position = MainActivity.parsedData_Profile[0][5];
                        Sex = MainActivity.parsedData_Profile[0][3];
                        MainActivity.Team1 = MainActivity.parsedData_Profile[0][6];
                        Age = ChangeAge(MainActivity.parsedData_Profile[0][4]);

                        MainActivity.MyTeam = MainActivity.parsedData_Profile[0][6];
                        MainActivity.Profile = MainActivity.parsedData_Profile[0][7];
                        MainActivity.Height = MainActivity.parsedData_Profile[0][8];
                        MainActivity.Weight = MainActivity.parsedData_Profile[0][9];
                        MainActivity.interestArea_do = MainActivity.parsedData_Profile[0][10];
                        MainActivity.interestArea_si = MainActivity.parsedData_Profile[0][11];
                        Profile_Button_interestArea.setText(MainActivity.interestArea_do + " / " + MainActivity.interestArea_si);

                        Profile_Button_Age_Physical.setText(String.valueOf(Positon_Spinner_Height.getSelectedItem()) + " / " + String.valueOf(Positon_Spinner_Weight.getSelectedItem()));


                    }
                });
                buider.show();
            }
        });

        Profile_Button_Age_Physical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View layout = inflater.inflate(R.layout.layout_customdialog_change_position, null);
                final RadioGroup rg = (RadioGroup) layout.findViewById(R.id.Positon_Root);

                final RadioButton Positon_Center = (RadioButton) layout.findViewById(R.id.Positon_Center);
                final RadioButton Positon_PowerFoward = (RadioButton) layout.findViewById(R.id.Positon_PowerFoward);
                final RadioButton Positon_SmallFoward = (RadioButton) layout.findViewById(R.id.Positon_SmallFoward);
                final RadioButton Positon_ShootingGuard = (RadioButton) layout.findViewById(R.id.Positon_ShootingGuard);
                final RadioButton Positon_PointGuard = (RadioButton) layout.findViewById(R.id.Positon_PointGuard);
                final Spinner Positon_Spinner_Height = (Spinner) layout.findViewById(R.id.Positon_Spinner_Height);
                final Spinner Positon_Spinner_Weight = (Spinner) layout.findViewById(R.id.Positon_Spinner_Weight);


                if (Position.equals("센터")) {
                    Positon_Center.setChecked(true);
                }
                if (Position.equals("파워포워드")) {
                    Positon_PowerFoward.setChecked(true);
                }
                if (Position.equals("스몰포워드")) {
                    Positon_SmallFoward.setChecked(true);
                }
                if (Position.equals("포인트가드")) {
                    Positon_PointGuard.setChecked(true);
                }
                if (Position.equals("슈팅가드")) {
                    Positon_ShootingGuard.setChecked(true);
                }
                //Positon_Spinner_Weight.setSelection(2);
                //멤버의 세부내역 입력 Dialog 생성 및 보이기


                ArrayAdapter<CharSequence> adspin1, adspin2;
                adspin1 = ArrayAdapter.createFromResource(getContext(), R.array.height, R.layout.zfile_spinner_test);
                adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                Positon_Spinner_Height.setAdapter(adspin1);
                adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.weight, R.layout.zfile_spinner_test);
                adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Positon_Spinner_Weight.setAdapter(adspin2);

                Positon_Spinner_Height.setSelection(Integer.parseInt(MainActivity.Height) - 120, false);
                Positon_Spinner_Weight.setSelection(Integer.parseInt(MainActivity.Weight) - 40, false);

                AlertDialog.Builder buider = new AlertDialog.Builder(view.getContext()); //AlertDialog.Builder 객체 생성
                buider.setTitle("변경"); //Dialog 제목
                buider.setIcon(android.R.drawable.ic_menu_add); //제목옆의 아이콘 이미지(원하는 이미지 설정)
                buider.setView(layout);
                buider.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String CheckID = String.valueOf(rg.getCheckedRadioButtonId());

                        if (CheckID.equals(String.valueOf(Positon_Center.getId()))) {
                            Position = "센터";
                            Profile_Button_Position.setText(Position);
                        } else if (CheckID.equals(String.valueOf(Positon_PowerFoward.getId()))) {
                            Position = "파워포워드";
                            Profile_Button_Position.setText(Position);
                        } else if (CheckID.equals(String.valueOf(Positon_SmallFoward.getId()))) {
                            Position = "스몰포워드";
                            Profile_Button_Position.setText(Position);
                        } else if (CheckID.equals(String.valueOf(Positon_ShootingGuard.getId()))) {
                            Position = "슛팅가드";
                            Profile_Button_Position.setText(Position);
                        } else if (CheckID.equals(String.valueOf(Positon_PointGuard.getId()))) {
                            Position = "포인트가드";
                            Profile_Button_Position.setText(Position);
                        }

                        try {
                            HttpClient client = new DefaultHttpClient();
                            String postURL = "http://210.122.7.193:8080/gg/user_information_update.jsp";

                            HttpPost post = new HttpPost(postURL);
                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                            params.add(new BasicNameValuePair("id", MainActivity.Id));
                            params.add(new BasicNameValuePair("Position", Position));
                            params.add(new BasicNameValuePair("Height", String.valueOf(Positon_Spinner_Height.getSelectedItem())));
                            params.add(new BasicNameValuePair("Weight", String.valueOf(Positon_Spinner_Weight.getSelectedItem())));
                            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                            post.setEntity(ent);
                            HttpResponse response = client.execute(post);
                        } catch (Exception e) {
                            Snackbar.make(view, "다시시도해주세요.", Snackbar.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        Profile_Button_Age_Physical.setText(String.valueOf(Positon_Spinner_Height.getSelectedItem()) + " / " + String.valueOf(Positon_Spinner_Weight.getSelectedItem()));


                        String result_profile = "";
                        try {
                            HttpClient client = new DefaultHttpClient();
                            String postURL = "http://210.122.7.193:8080/Web_basket/Profile.jsp";
                            HttpPost post = new HttpPost(postURL);

                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                            params.add(new BasicNameValuePair("Id", MainActivity.Id));

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

                        MainActivity.parsedData_Profile = jsonParserList_UserInfo(result_profile);

                        Name = MainActivity.parsedData_Profile[0][2];
                        Position = MainActivity.parsedData_Profile[0][5];
                        Sex = MainActivity.parsedData_Profile[0][3];
                        MainActivity.Team1 = MainActivity.parsedData_Profile[0][6];
                        Age = ChangeAge(MainActivity.parsedData_Profile[0][4]);

                        MainActivity.MyTeam = MainActivity.parsedData_Profile[0][6];
                        MainActivity.Profile = MainActivity.parsedData_Profile[0][7];
                        MainActivity.Height = MainActivity.parsedData_Profile[0][8];
                        MainActivity.Weight = MainActivity.parsedData_Profile[0][9];
                        MainActivity.interestArea_do = MainActivity.parsedData_Profile[0][10];
                        MainActivity.interestArea_si = MainActivity.parsedData_Profile[0][11];
                        Profile_Button_interestArea.setText(MainActivity.interestArea_do + " / " + MainActivity.interestArea_si);

                        Profile_Button_Age_Physical.setText(String.valueOf(Positon_Spinner_Height.getSelectedItem()) + " / " + String.valueOf(Positon_Spinner_Weight.getSelectedItem()));
                    }
                });
                buider.show();
            }
        });

        Profile_Button_interestArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View layout = inflater.inflate(R.layout.layout_customdialog_change_interestarea, null);
                final Spinner Layout_Spinner_Change_interestarea_Do = (Spinner) layout.findViewById(R.id.Layout_Spinner_Change_interestarea_Do);
                final Spinner Layout_Spinner_Change_interestarea_Si = (Spinner) layout.findViewById(R.id.Layout_Spinner_Change_interestarea_Si);

                if (MainActivity.interestArea_do.equals("서울")) {
                    adspin1 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do, R.layout.zfile_spinner_test);
                    adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_Spinner_Change_interestarea_Do.setAdapter(adspin1);
                    Layout_Spinner_Change_interestarea_Do.setSelection(0);
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_seoul_notall, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_Spinner_Change_interestarea_Si.setAdapter(adspin2);

                } else if (MainActivity.interestArea_do.equals("인천")) {
                    adspin1 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do, R.layout.zfile_spinner_test);
                    adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_Spinner_Change_interestarea_Do.setAdapter(adspin1);
                    Layout_Spinner_Change_interestarea_Do.setSelection(1);
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_incheon_notall, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_Spinner_Change_interestarea_Si.setAdapter(adspin2);

                } else if (MainActivity.interestArea_do.equals("광주")) {
                    adspin1 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do, R.layout.zfile_spinner_test);
                    adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_Spinner_Change_interestarea_Do.setAdapter(adspin1);
                    Layout_Spinner_Change_interestarea_Do.setSelection(2);
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_gwangju_notall, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_Spinner_Change_interestarea_Si.setAdapter(adspin2);

                } else if (MainActivity.interestArea_do.equals("대구")) {
                    adspin1 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do, R.layout.zfile_spinner_test);
                    adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_Spinner_Change_interestarea_Do.setAdapter(adspin1);
                    Layout_Spinner_Change_interestarea_Do.setSelection(3);
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_DaeGu_notall, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_Spinner_Change_interestarea_Si.setAdapter(adspin2);

                } else if (MainActivity.interestArea_do.equals("울산")) {
                    adspin1 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do, R.layout.zfile_spinner_test);
                    adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_Spinner_Change_interestarea_Do.setAdapter(adspin1);
                    Layout_Spinner_Change_interestarea_Do.setSelection(4);
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Ulsan_notall, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_Spinner_Change_interestarea_Si.setAdapter(adspin2);

                } else if (MainActivity.interestArea_do.equals("대전")) {
                    adspin1 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do, R.layout.zfile_spinner_test);
                    adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_Spinner_Change_interestarea_Do.setAdapter(adspin1);
                    Layout_Spinner_Change_interestarea_Do.setSelection(5);
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_DaeJeon_notall, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_Spinner_Change_interestarea_Si.setAdapter(adspin2);

                } else if (MainActivity.interestArea_do.equals("부산")) {
                    adspin1 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do, R.layout.zfile_spinner_test);
                    adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_Spinner_Change_interestarea_Do.setAdapter(adspin1);
                    Layout_Spinner_Change_interestarea_Do.setSelection(6);
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Busan_notall, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_Spinner_Change_interestarea_Si.setAdapter(adspin2);

                } else if (MainActivity.interestArea_do.equals("강원도")) {
                    adspin1 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do, R.layout.zfile_spinner_test);
                    adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_Spinner_Change_interestarea_Do.setAdapter(adspin1);
                    Layout_Spinner_Change_interestarea_Do.setSelection(7);
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Gangwondo_notall, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_Spinner_Change_interestarea_Si.setAdapter(adspin2);

                } else if (MainActivity.interestArea_do.equals("경기도")) {
                    adspin1 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do, R.layout.zfile_spinner_test);
                    adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_Spinner_Change_interestarea_Do.setAdapter(adspin1);
                    Layout_Spinner_Change_interestarea_Do.setSelection(8);
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Gyeonggido_notall, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_Spinner_Change_interestarea_Si.setAdapter(adspin2);

                } else if (MainActivity.interestArea_do.equals("충청북도")) {
                    adspin1 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do, R.layout.zfile_spinner_test);
                    adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_Spinner_Change_interestarea_Do.setAdapter(adspin1);
                    Layout_Spinner_Change_interestarea_Do.setSelection(9);
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Chungcheongbukdo_notall, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_Spinner_Change_interestarea_Si.setAdapter(adspin2);

                } else if (MainActivity.interestArea_do.equals("충청남도")) {
                    adspin1 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do, R.layout.zfile_spinner_test);
                    adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_Spinner_Change_interestarea_Do.setAdapter(adspin1);
                    Layout_Spinner_Change_interestarea_Do.setSelection(10);
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Chungcheongnamdo_notall, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_Spinner_Change_interestarea_Si.setAdapter(adspin2);

                } else if (MainActivity.interestArea_do.equals("전라북도")) {
                    adspin1 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do, R.layout.zfile_spinner_test);
                    adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_Spinner_Change_interestarea_Do.setAdapter(adspin1);
                    Layout_Spinner_Change_interestarea_Do.setSelection(11);
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Jeolabukdo_notall, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_Spinner_Change_interestarea_Si.setAdapter(adspin2);

                } else if (MainActivity.interestArea_do.equals("전라남도")) {
                    adspin1 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do, R.layout.zfile_spinner_test);
                    adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_Spinner_Change_interestarea_Do.setAdapter(adspin1);
                    Layout_Spinner_Change_interestarea_Do.setSelection(12);
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Jeolanamdo_notall, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_Spinner_Change_interestarea_Si.setAdapter(adspin2);

                } else if (MainActivity.interestArea_do.equals("경상북도")) {
                    adspin1 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do, R.layout.zfile_spinner_test);
                    adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_Spinner_Change_interestarea_Do.setAdapter(adspin1);
                    Layout_Spinner_Change_interestarea_Do.setSelection(13);
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Gyeongsangbukdo_notall, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_Spinner_Change_interestarea_Si.setAdapter(adspin2);

                } else if (MainActivity.interestArea_do.equals("경상남도")) {
                    adspin1 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do, R.layout.zfile_spinner_test);
                    adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_Spinner_Change_interestarea_Do.setAdapter(adspin1);
                    Layout_Spinner_Change_interestarea_Do.setSelection(14);
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Gyeongsangnamdo_notall, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_Spinner_Change_interestarea_Si.setAdapter(adspin2);

                } else if (MainActivity.interestArea_do.equals("제주도")) {
                    adspin1 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do, R.layout.zfile_spinner_test);
                    adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_Spinner_Change_interestarea_Do.setAdapter(adspin1);
                    Layout_Spinner_Change_interestarea_Do.setSelection(15);
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Jejudo_notall, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Layout_Spinner_Change_interestarea_Si.setAdapter(adspin2);
                }


                AlertDialog.Builder buider = new AlertDialog.Builder(v.getContext()); //AlertDialog.Builder 객체 생성
                buider.setTitle("변경"); //Dialog 제목
                buider.setIcon(android.R.drawable.ic_menu_add); //제목옆의 아이콘 이미지(원하는 이미지 설정)
                buider.setView(layout);
                buider.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.interestArea_do = (String) Layout_Spinner_Change_interestarea_Do.getSelectedItem();
                        MainActivity.interestArea_si = (String) Layout_Spinner_Change_interestarea_Si.getSelectedItem();
                        try {
                            HttpClient client = new DefaultHttpClient();
                            String postURL = "http://210.122.7.193:8080/gg/user_interestarea_update.jsp";

                            HttpPost post = new HttpPost(postURL);
                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                            params.add(new BasicNameValuePair("id", MainActivity.Id));
                            params.add(new BasicNameValuePair("interestArea_do", MainActivity.interestArea_do));
                            params.add(new BasicNameValuePair("interestArea_si", MainActivity.interestArea_si));
                            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                            post.setEntity(ent);
                            HttpResponse response = client.execute(post);
                        } catch (Exception e) {
                            Snackbar.make(v, "다시시도해주세요.", Snackbar.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        Profile_Button_interestArea.setText(MainActivity.interestArea_do + " / " + MainActivity.interestArea_si);
                        Snackbar.make(v, "재접속시적용됩니다.", Snackbar.LENGTH_SHORT).show();
                    }
                });
                buider.show();

                Layout_Spinner_Change_interestarea_Do.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (adspin1.getItem(position).equals("서울")) {
                            MainActivity.interestArea_do = adspin1.getItem(position).toString();
                            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_seoul_notall, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Layout_Spinner_Change_interestarea_Si.setAdapter(adspin2);
                            Layout_Spinner_Change_interestarea_Si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    MainActivity.interestArea_si = adspin2.getItem(i).toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                        } else if (adspin1.getItem(position).equals("인천")) {
                            MainActivity.interestArea_do = adspin1.getItem(position).toString();
                            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_incheon_notall, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Layout_Spinner_Change_interestarea_Si.setAdapter(adspin2);
                            Layout_Spinner_Change_interestarea_Si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    MainActivity.interestArea_si = adspin2.getItem(i).toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                        } else if (adspin1.getItem(position).equals("광주")) {
                            MainActivity.interestArea_do = adspin1.getItem(position).toString();
                            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_gwangju_notall, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Layout_Spinner_Change_interestarea_Si.setAdapter(adspin2);
                            Layout_Spinner_Change_interestarea_Si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    MainActivity.interestArea_si = adspin2.getItem(i).toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                        } else if (adspin1.getItem(position).equals("대구")) {
                            MainActivity.interestArea_do = adspin1.getItem(position).toString();
                            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_DaeGu_notall, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Layout_Spinner_Change_interestarea_Si.setAdapter(adspin2);
                            Layout_Spinner_Change_interestarea_Si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    MainActivity.interestArea_si = adspin2.getItem(i).toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                        } else if (adspin1.getItem(position).equals("울산")) {
                            MainActivity.interestArea_do = adspin1.getItem(position).toString();
                            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Ulsan_notall, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Layout_Spinner_Change_interestarea_Si.setAdapter(adspin2);
                            Layout_Spinner_Change_interestarea_Si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    MainActivity.interestArea_si = adspin2.getItem(i).toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                        } else if (adspin1.getItem(position).equals("대전")) {
                            MainActivity.interestArea_do = adspin1.getItem(position).toString();
                            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_DaeJeon_notall, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Layout_Spinner_Change_interestarea_Si.setAdapter(adspin2);
                            Layout_Spinner_Change_interestarea_Si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    MainActivity.interestArea_si = adspin2.getItem(i).toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                        } else if (adspin1.getItem(position).equals("부산")) {
                            MainActivity.interestArea_do = adspin1.getItem(position).toString();
                            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Busan_notall, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Layout_Spinner_Change_interestarea_Si.setAdapter(adspin2);
                            Layout_Spinner_Change_interestarea_Si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    MainActivity.interestArea_si = adspin2.getItem(i).toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                        } else if (adspin1.getItem(position).equals("강원도")) {
                            MainActivity.interestArea_do = adspin1.getItem(position).toString();
                            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Gangwondo_notall, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Layout_Spinner_Change_interestarea_Si.setAdapter(adspin2);
                            Layout_Spinner_Change_interestarea_Si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    MainActivity.interestArea_si = adspin2.getItem(i).toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                        } else if (adspin1.getItem(position).equals("경기도")) {
                            MainActivity.interestArea_do = adspin1.getItem(position).toString();
                            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Gyeonggido_notall, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Layout_Spinner_Change_interestarea_Si.setAdapter(adspin2);
                            Layout_Spinner_Change_interestarea_Si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    MainActivity.interestArea_si = adspin2.getItem(i).toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                        } else if (adspin1.getItem(position).equals("충청북도")) {
                            MainActivity.interestArea_do = adspin1.getItem(position).toString();
                            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Chungcheongbukdo_notall, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Layout_Spinner_Change_interestarea_Si.setAdapter(adspin2);
                            Layout_Spinner_Change_interestarea_Si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    MainActivity.interestArea_si = adspin2.getItem(i).toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                        } else if (adspin1.getItem(position).equals("충청남도")) {
                            MainActivity.interestArea_do = adspin1.getItem(position).toString();
                            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Chungcheongnamdo_notall, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Layout_Spinner_Change_interestarea_Si.setAdapter(adspin2);
                            Layout_Spinner_Change_interestarea_Si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    MainActivity.interestArea_si = adspin2.getItem(i).toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                        } else if (adspin1.getItem(position).equals("전라북도")) {
                            MainActivity.interestArea_do = adspin1.getItem(position).toString();
                            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Jeolabukdo_notall, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Layout_Spinner_Change_interestarea_Si.setAdapter(adspin2);
                            Layout_Spinner_Change_interestarea_Si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    MainActivity.interestArea_si = adspin2.getItem(i).toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                        } else if (adspin1.getItem(position).equals("전라남도")) {
                            MainActivity.interestArea_do = adspin1.getItem(position).toString();
                            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Jeolanamdo_notall, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Layout_Spinner_Change_interestarea_Si.setAdapter(adspin2);
                            Layout_Spinner_Change_interestarea_Si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    MainActivity.interestArea_si = adspin2.getItem(i).toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                        } else if (adspin1.getItem(position).equals("경상북도")) {
                            MainActivity.interestArea_do = adspin1.getItem(position).toString();
                            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Gyeongsangbukdo_notall, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Layout_Spinner_Change_interestarea_Si.setAdapter(adspin2);
                            Layout_Spinner_Change_interestarea_Si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    MainActivity.interestArea_si = adspin2.getItem(i).toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                        } else if (adspin1.getItem(position).equals("경상남도")) {
                            MainActivity.interestArea_do = adspin1.getItem(position).toString();
                            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Gyeongsangnamdo_notall, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Layout_Spinner_Change_interestarea_Si.setAdapter(adspin2);
                            Layout_Spinner_Change_interestarea_Si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    MainActivity.interestArea_si = adspin2.getItem(i).toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                        } else if (adspin1.getItem(position).equals("제주도")) {
                            MainActivity.interestArea_do = adspin1.getItem(position).toString();
                            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Jejudo_notall, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Layout_Spinner_Change_interestarea_Si.setAdapter(adspin2);
                            Layout_Spinner_Change_interestarea_Si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    MainActivity.interestArea_si = adspin2.getItem(i).toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });
        ////////////////////////////////////////////////////////////////회원정보 수정 끝
        ///팀 만들기 버튼
        Profile_Button_TeamMake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //이미 팀 존재하는 지 확인 후 중복 제거
                String result = "";
                try {
                    HttpClient client = new DefaultHttpClient();
                    String postURL = "http://210.122.7.193:8080/Web_basket/TeamMake_OverLap.jsp";
                    HttpPost post = new HttpPost(postURL);

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("Id", MainActivity.Id));

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
                parsedData_overLap = jsonParserList_TeamMake_OverLap(result);
                if (parsedData_overLap[0][0].equals("overLap")) {
                    Snackbar.make(view, "이미 다른 팀에 가입 중이십니다.", Snackbar.LENGTH_SHORT).show();
                } else {
                    Intent intent_TeamMake = new Intent(rootView.getContext(), Navigation_TeamManager_TeamMake1.class);
                    intent_TeamMake.putExtra("Id", MainActivity.Id);
                    startActivity(intent_TeamMake);
                }
            }
        });
        Profile_Button_TeamManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String TeamCheck_result = "";
                try {
                    HttpClient client = new DefaultHttpClient();
                    String postURL = "http://210.122.7.193:8080/Web_basket/TeamCheck.jsp";
                    HttpPost post = new HttpPost(postURL);

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("Id", MainActivity.Id));

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
                parsedData_TeamCheck = jsonParserList_TeamCheck(TeamCheck_result);
                if (parsedData_TeamCheck[0][0].equals("Unexist")) {
                    Snackbar.make(view, "관리할 팀이 없습니다.", Snackbar.LENGTH_SHORT).show();
                } else {
                    Intent intent_TeamManager = new Intent(rootView.getContext(), Navigation_TeamManager.class);
                    intent_TeamManager.putExtra("Id", MainActivity.Id);
                    startActivity(intent_TeamManager);
                }
            }
        });
        Profile_Button_TeamSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_TeamIntro = new Intent(rootView.getContext(), Navigation_TeamIntro.class);
                intent_TeamIntro.putExtra("Id", MainActivity.Id);
                startActivity(intent_TeamIntro);
            }
        });
        Profile_Button_Password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_Changepw = new Intent(rootView.getContext(), ChangePw1Activity.class);
                intent_Changepw.putExtra("Id", MainActivity.Id);
                startActivity(intent_Changepw);
            }
        });
        Profile_Button_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences prefs1 = rootView.getContext().getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = prefs1.edit();
                editor1.putString("id", "");
                editor1.putString("pw", "");
                editor1.putString("auto", "false");
                editor1.commit();

                onClickLogout();
            }
        });


        return rootView;
    }

    private void onClickLogout() {
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                redirectLoginActivity();
            }
        });
    }

    protected void redirectLoginActivity() {
        Log.i("fragment1", MainActivity.fragment1);
        final Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.putExtra("fragment1", MainActivity.fragment1);
        intent.putExtra("fragment2", MainActivity.fragment2);
        intent.putExtra("fragment3", MainActivity.fragment3);
        intent.putExtra("fragment4", MainActivity.fragment4);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        startActivity(intent);
        getActivity().finish();
    }

    //date 입력받아 나이 구하는 함수
    public String ChangeAge(String Age) {
        Calendar cal = Calendar.getInstance();
        String[] str = new String(Age).split(" \\/ ");
        String[] str_day = new String(str[2]).split(" ");
        int year = Integer.parseInt(str[0]);
        int month = Integer.parseInt(str[1]);
        int day = Integer.parseInt(str_day[0]);

        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DATE, day);

        Calendar now = Calendar.getInstance();

        int age = now.get(Calendar.YEAR) - cal.get(Calendar.YEAR);
        if ((cal.get(Calendar.MONTH) > now.get(Calendar.MONTH))
                || (cal.get(Calendar.MONTH) == now.get(Calendar.MONTH)
                && cal.get(Calendar.DAY_OF_MONTH) > now.get(Calendar.DAY_OF_MONTH))
                ) {
            age--;
        }
        String Str_age = Integer.toString(age);
        return Str_age;
    }


    /////프로필 탭 사용자정보를 파싱합니다.//////////////////////////////////////////////////////////
    public String[][] jsonParserList_TeamMake_OverLap(String pRecvServerPage) {
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

    public String[][] jsonParserList_UserInfo(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");

            String[] jsonName = {"msg1", "msg2", "msg3", "msg4", "msg5", "msg6", "msg7", "msg8", "msg9", "msg10", "msg11", "msg12","msg13"};
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

    /////팀이 존재하는지 체크합니다.
    public String[][] jsonParserList_TeamCheck(String pRecvServerPage) {
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

}
