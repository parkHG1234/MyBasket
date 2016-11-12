package com.mysports.basketbook;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.bumptech.glide.Glide;
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
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by ldong on 2016-11-12.
 */
public class SectionsFragment3 extends Fragment {
    Button League_Button_1, League_Button_2;
    ////////////////랭크 탭 선언
    String choice_do = "", choice_si = "전 체";
    String[][] help_parsedData;
    String[][] helpData;
    String[][] myteamData;
    TextView league_Rank_TextView_myrank;
    TextView league_Rank_TextView_myteam;
    TextView league_Rank_TextView_myteampoint;
    ImageView league_Rank_Image_myteam;
    SwipeMenuListView League_Rank_List;
    FloatingActionButton League_Rank_Help;

    League_Rank_Customlist_Adapter dataadapter;
    ArrayList<League_Rank_Customlist_Setting> arrData;

    LinearLayout layout_league_Root;
    TextView update_textview;
    ArrayAdapter<CharSequence> adspin1, adspin2, adspin3;
    ///////////////////////////////내 팀 게임 탭 선언
   /* Button League_Button_MyTeam, League_Button_YourTeam, League_Button_Start, League_Button_Cancel;
    String TabChoice = "1";
    String Do, Si, Team;
    int spinnum1, spinnum2, spinnum3;

    String[][] parsedData, parsedData_gameGenerate, parsedData_gameDelete, parsedData_gameStatus, parsedData_gameScoreAdd,parsedData_TeamPersonCount;
    ArrayList arr;
    static TimerTask myTask;
    static Timer timer;*/
///////////내 팀 기록 탭 선언//////////////////////////////////////////////////////////
    ListView Leauge_myRecord_ListView;

    ////////////////////////////////////////////////
    public SectionsFragment3() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.layout_league, container, false);
        League_Button_1 = (Button) rootView.findViewById(R.id.League_Button_1);
        League_Button_2 = (Button) rootView.findViewById(R.id.League_Button_2);
        MainActivity.League_Layout_1 = (LinearLayout) rootView.findViewById(R.id.League_Layout_1);
        MainActivity.League_Layout_2 = (LinearLayout) rootView.findViewById(R.id.League_Layout_2);
        layout_league_Root = (LinearLayout) rootView.findViewById(R.id.layout_league_Root);
        update_textview = (TextView) rootView.findViewById(R.id.update_textview);
////////////게임 탭 선언
     /*   League_Button_MyTeam = (Button) rootView.findViewById(R.id.League_Button_MyTeam);
        League_Button_YourTeam = (Button) rootView.findViewById(R.id.League_Button_YourTeam);
        League_Button_Start = (Button) rootView.findViewById(R.id.League_Button_Start);
        League_Button_Cancel = (Button) rootView.findViewById(R.id.League_Button_Cancel);*/
        ////랭크 탭 선언///////////////////////////////////////

        Spinner League_Rank_Spinner_Do = (Spinner) rootView.findViewById(R.id.League_Rank_Spinner_Do);
        final Spinner League_Rank_Spinner_Si = (Spinner) rootView.findViewById(R.id.League_Rank_Spinner_Si);
        Button League_Rank_Search_Button = (Button) rootView.findViewById(R.id.League_Rank_Search_Button);
        LinearLayout league_Rank_mylayout = (LinearLayout) rootView.findViewById(R.id.league_Rank_mylayout);

        league_Rank_TextView_myrank = (TextView) rootView.findViewById(R.id.league_Rank_TextView_myrank);
        league_Rank_TextView_myteam = (TextView) rootView.findViewById(R.id.league_Rank_TextView_myteam);
        league_Rank_TextView_myteampoint = (TextView) rootView.findViewById(R.id.league_Rank_TextView_myteampoint);
        league_Rank_Image_myteam = (ImageView) rootView.findViewById(R.id.league_Rank_Image_myteam);
        League_Rank_List = (SwipeMenuListView) rootView.findViewById(R.id.League_Rank_List);
        League_Rank_Help = (FloatingActionButton) rootView.findViewById(R.id.League_Rank_Help);
        //////////내 팀 기록 선언
        //////////////////////////////////////////////
        //화면 visible

        if (Boolean.parseBoolean(MainActivity.fragment3)) {
            update_textview.setVisibility(View.GONE);
            layout_league_Root.setVisibility(View.VISIBLE);
        } else {
            layout_league_Root.setVisibility(View.GONE);
            update_textview.setVisibility(View.VISIBLE);
        }
        ///////////////////랭크 탭
        adspin1 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do, R.layout.zfile_spinner_test);
        adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        League_Rank_Spinner_Do.setAdapter(adspin1);
        League_Rank_Spinner_Do.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (adspin1.getItem(position).toString().equals("서울")) {
                    choice_do = adspin1.getItem(position).toString();
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_seoul, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    League_Rank_Spinner_Si.setAdapter(adspin2);
                    League_Rank_Spinner_Si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_si = adspin2.getItem(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (adspin1.getItem(position).toString().equals("인천")) {
                    choice_do = adspin1.getItem(position).toString();
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_incheon, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    League_Rank_Spinner_Si.setAdapter(adspin2);
                    League_Rank_Spinner_Si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_si = adspin2.getItem(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (adspin1.getItem(position).toString().equals("광주")) {
                    choice_do = adspin1.getItem(position).toString();
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_gwangju, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    League_Rank_Spinner_Si.setAdapter(adspin2);
                    League_Rank_Spinner_Si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_si = adspin2.getItem(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (adspin1.getItem(position).toString().equals("대구")) {
                    choice_do = adspin1.getItem(position).toString();
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_DaeGu, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    League_Rank_Spinner_Si.setAdapter(adspin2);
                    League_Rank_Spinner_Si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_si = adspin2.getItem(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (adspin1.getItem(position).toString().equals("울산")) {
                    choice_do = adspin1.getItem(position).toString();
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Ulsan, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    League_Rank_Spinner_Si.setAdapter(adspin2);
                    League_Rank_Spinner_Si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_si = adspin2.getItem(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (adspin1.getItem(position).toString().equals("대전")) {
                    choice_do = adspin1.getItem(position).toString();
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_DaeJeon, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    League_Rank_Spinner_Si.setAdapter(adspin2);
                    League_Rank_Spinner_Si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_si = adspin2.getItem(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (adspin1.getItem(position).toString().equals("부산")) {
                    choice_do = adspin1.getItem(position).toString();
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Busan, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    League_Rank_Spinner_Si.setAdapter(adspin2);
                    League_Rank_Spinner_Si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_si = adspin2.getItem(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (adspin1.getItem(position).toString().equals("강원도")) {
                    choice_do = adspin1.getItem(position).toString();
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Gangwondo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    League_Rank_Spinner_Si.setAdapter(adspin2);
                    League_Rank_Spinner_Si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_si = adspin2.getItem(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (adspin1.getItem(position).toString().equals("경기도")) {
                    choice_do = adspin1.getItem(position).toString();
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Gyeonggido, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    League_Rank_Spinner_Si.setAdapter(adspin2);
                    League_Rank_Spinner_Si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_si = adspin2.getItem(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (adspin1.getItem(position).toString().equals("충청북도")) {
                    choice_do = adspin1.getItem(position).toString();
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Chungcheongbukdo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    League_Rank_Spinner_Si.setAdapter(adspin2);
                    League_Rank_Spinner_Si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_si = adspin2.getItem(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (adspin1.getItem(position).toString().equals("충청남도")) {
                    choice_do = adspin1.getItem(position).toString();
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Chungcheongnamdo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    League_Rank_Spinner_Si.setAdapter(adspin2);
                    League_Rank_Spinner_Si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_si = adspin2.getItem(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (adspin1.getItem(position).toString().equals("전라북도")) {
                    choice_do = adspin1.getItem(position).toString();
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Jeolabukdo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    League_Rank_Spinner_Si.setAdapter(adspin2);
                    League_Rank_Spinner_Si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_si = adspin2.getItem(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (adspin1.getItem(position).toString().equals("전라남도")) {
                    choice_do = adspin1.getItem(position).toString();
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Jeolanamdo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    League_Rank_Spinner_Si.setAdapter(adspin2);
                    League_Rank_Spinner_Si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_si = adspin2.getItem(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (adspin1.getItem(position).toString().equals("경상북도")) {
                    choice_do = adspin1.getItem(position).toString();
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Gyeongsangbukdo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    League_Rank_Spinner_Si.setAdapter(adspin2);
                    League_Rank_Spinner_Si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_si = adspin2.getItem(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (adspin1.getItem(position).toString().equals("경상남도")) {
                    choice_do = adspin1.getItem(position).toString();
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Gyeongsangnamdo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    League_Rank_Spinner_Si.setAdapter(adspin2);
                    League_Rank_Spinner_Si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_si = adspin2.getItem(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (adspin1.getItem(position).toString().equals("제주도")) {
                    choice_do = adspin1.getItem(position).toString();
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Jejudo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    League_Rank_Spinner_Si.setAdapter(adspin2);
                    League_Rank_Spinner_Si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_si = adspin2.getItem(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        choice_do = MainActivity.interestArea_do;

        String result = "";
        try {
            result = "";
            HttpClient client = new DefaultHttpClient();
            String postURL = "http://210.122.7.193:8080/gg/myteam_information_download.jsp";
            HttpPost post = new HttpPost(postURL);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("ID", MainActivity.Id));
            params.add(new BasicNameValuePair("Rank_Do", choice_do));
            params.add(new BasicNameValuePair("Rank_Si", choice_si));
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);
            HttpResponse response = client.execute(post);
            BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
            String line = null;
            while ((line = bufreader.readLine()) != null) {
                result += line;
            }
            myteamData = rank_jsonParserList(result);

            result = "";

            client = new DefaultHttpClient();
            postURL = "http://210.122.7.193:8080/gg/team_information_download.jsp";
            post = new HttpPost(postURL);
            params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Rank_Do", choice_do));
            params.add(new BasicNameValuePair("Rank_Si", choice_si));
            ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);
            response = client.execute(post);
            bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
            line = null;
            while ((line = bufreader.readLine()) != null) {
                result += line;
            }
            help_parsedData = rank_jsonParserList(result);
            setData();
            dataadapter = new League_Rank_Customlist_Adapter(getContext(), arrData);
            dataadapter.listview(League_Rank_List);
            League_Rank_List.setAdapter(dataadapter);
        } catch (Exception e) {
            e.printStackTrace();
        }


        String En_Profile = null;
        try {
            En_Profile = URLEncoder.encode(myteamData[0][4], "utf-8");
            if (myteamData[0][4].equals(".")) {
                Glide.with(rootView.getContext()).load(R.drawable.profile_basic_image).into(league_Rank_Image_myteam);
            } else {
                Glide.with(rootView.getContext()).load("http://210.122.7.193:8080/Web_basket/imgs/Emblem/" + En_Profile + ".jpg").bitmapTransform(new CropCircleTransformation(Glide.get(rootView.getContext()).getBitmapPool()))
                        .into(league_Rank_Image_myteam);
            }
        } catch (UnsupportedEncodingException e) {

        }
        ///관심지역 설정

        adspin1 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do, R.layout.zfile_spinner_test);
        adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        League_Rank_Spinner_Do.setAdapter(adspin1);
        if (choice_do.equals("서울")) {
            League_Rank_Spinner_Do.setSelection(0);
            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_seoul, R.layout.zfile_spinner_test);
            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            League_Rank_Spinner_Si.setAdapter(adspin2);

        } else if (choice_do.equals("인천")) {
            League_Rank_Spinner_Do.setSelection(1);
            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_incheon, R.layout.zfile_spinner_test);
            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            League_Rank_Spinner_Si.setAdapter(adspin2);

        } else if (choice_do.equals("광주")) {
            League_Rank_Spinner_Do.setSelection(2);
            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_gwangju, R.layout.zfile_spinner_test);
            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            League_Rank_Spinner_Si.setAdapter(adspin2);

        } else if (choice_do.equals("대구")) {
            League_Rank_Spinner_Do.setSelection(3);
            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_DaeGu, R.layout.zfile_spinner_test);
            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            League_Rank_Spinner_Si.setAdapter(adspin2);

        } else if (choice_do.equals("울산")) {
            League_Rank_Spinner_Do.setSelection(4);
            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Ulsan, R.layout.zfile_spinner_test);
            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            League_Rank_Spinner_Si.setAdapter(adspin2);

        } else if (choice_do.equals("대전")) {
            League_Rank_Spinner_Do.setSelection(5);
            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Ulsan, R.layout.zfile_spinner_test);
            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            League_Rank_Spinner_Si.setAdapter(adspin2);

        } else if (choice_do.equals("부산")) {
            League_Rank_Spinner_Do.setSelection(6);
            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Ulsan, R.layout.zfile_spinner_test);
            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            League_Rank_Spinner_Si.setAdapter(adspin2);

        } else if (choice_do.equals("강원도")) {
            League_Rank_Spinner_Do.setSelection(7);
            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Ulsan, R.layout.zfile_spinner_test);
            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            League_Rank_Spinner_Si.setAdapter(adspin2);

        } else if (choice_do.equals("경기도")) {
            League_Rank_Spinner_Do.setSelection(8);
            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Gyeonggido, R.layout.zfile_spinner_test);
            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            League_Rank_Spinner_Si.setAdapter(adspin2);

        } else if (choice_do.equals("충청북도")) {
            League_Rank_Spinner_Do.setSelection(9);
            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Chungcheongbukdo, R.layout.zfile_spinner_test);
            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            League_Rank_Spinner_Si.setAdapter(adspin2);

        } else if (choice_do.equals("충청남도")) {
            League_Rank_Spinner_Do.setSelection(10);
            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Chungcheongnamdo, R.layout.zfile_spinner_test);
            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            League_Rank_Spinner_Si.setAdapter(adspin2);

        } else if (choice_do.equals("전라북도")) {
            League_Rank_Spinner_Do.setSelection(11);
            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Jeolabukdo, R.layout.zfile_spinner_test);
            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            League_Rank_Spinner_Si.setAdapter(adspin2);

        } else if (choice_do.equals("전라남도")) {
            League_Rank_Spinner_Do.setSelection(12);
            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Jeolanamdo, R.layout.zfile_spinner_test);
            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            League_Rank_Spinner_Si.setAdapter(adspin2);

        } else if (choice_do.equals("경상북도")) {
            League_Rank_Spinner_Do.setSelection(13);
            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Gyeongsangbukdo, R.layout.zfile_spinner_test);
            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            League_Rank_Spinner_Si.setAdapter(adspin2);

        } else if (choice_do.equals("경상남도")) {
            League_Rank_Spinner_Do.setSelection(14);
            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Gyeongsangnamdo, R.layout.zfile_spinner_test);
            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            League_Rank_Spinner_Si.setAdapter(adspin2);

        } else if (choice_do.equals("제주도")) {
            League_Rank_Spinner_Do.setSelection(15);
            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Jejudo, R.layout.zfile_spinner_test);
            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            League_Rank_Spinner_Si.setAdapter(adspin2);

        }

        /////////////////////////////////////////////////////////////////////////////////////////
        if (myteamData[0][0].equals(".")) {
            league_Rank_TextView_myrank.setText("");
            league_Rank_TextView_myteam.setText("팀가입후이용해주세요.");
            league_Rank_TextView_myteam.setGravity(2);
            league_Rank_TextView_myteampoint.setText("");
            League_Button_2.setEnabled(false);
        } else if (myteamData[0][2].equals(choice_do) && myteamData[0][3].equals(choice_si)) {
            league_Rank_TextView_myrank.setText(myteamData[0][0]);
            league_Rank_TextView_myteam.setText(myteamData[0][1]);
            league_Rank_TextView_myteampoint.setText(myteamData[0][5]);
        } else if (myteamData[0][2].equals(choice_do) && choice_si.equals("전 체")) {
            league_Rank_TextView_myrank.setText(myteamData[0][0]);
            league_Rank_TextView_myteam.setText(myteamData[0][1]);
            league_Rank_TextView_myteampoint.setText(myteamData[0][5]);
        } else {
            league_Rank_TextView_myrank.setText("-");
            league_Rank_TextView_myteampoint.setText("-");
        }


        League_Rank_Help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.layout_customdialog_rank_help, (ViewGroup) rootView.findViewById(R.id.Layout_CustomDialog_help_Root));

                final Button Layout_CustomDialog_help_Button1 = (Button) layout.findViewById(R.id.Layout_CustomDialog_help_Button1);
                final Button Layout_CustomDialog_help_Button2 = (Button) layout.findViewById(R.id.Layout_CustomDialog_help_Button2);
                final Button Layout_CustomDialog_help_Button3 = (Button) layout.findViewById(R.id.Layout_CustomDialog_help_Button3);
                final TextView Layout_CustomDialog_help_Scroll1 = (TextView) layout.findViewById(R.id.Layout_CustomDialog_help_Scroll1);
                final TextView Layout_CustomDialog_help_Scroll2 = (TextView) layout.findViewById(R.id.Layout_CustomDialog_help_Scroll2);
                final TextView Layout_CustomDialog_help_Scroll3 = (TextView) layout.findViewById(R.id.Layout_CustomDialog_help_Scroll3);
                final AlertDialog.Builder aDialog = new AlertDialog.Builder(getContext());
                aDialog.setView(layout);
                final AlertDialog ad = aDialog.create();
                ad.show();
                String result = "";
                try {
                    HttpClient client = new DefaultHttpClient();
                    String postURL = "http://210.122.7.193:8080/gg/help_download.jsp";
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
                    helpData = help_jsonParserList(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < helpData.length; i++) {
                    if (!(helpData[i][0].equals(" "))) {
                        Layout_CustomDialog_help_Scroll1.setText(Layout_CustomDialog_help_Scroll1.getText() + helpData[i][0].toString() + "\n");
                    }
                    if (!(helpData[i][1].equals(" "))) {
                        Layout_CustomDialog_help_Scroll2.setText(Layout_CustomDialog_help_Scroll2.getText() + helpData[i][1].toString() + "\n");
                    }
                    if (!(helpData[i][2].equals(" "))) {
                        Layout_CustomDialog_help_Scroll3.setText(Layout_CustomDialog_help_Scroll3.getText() + helpData[i][2].toString() + "\n");
                    }
                }


                Layout_CustomDialog_help_Button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Layout_CustomDialog_help_Button1.setBackgroundColor(Color.parseColor("#2C9FD3"));
                        Layout_CustomDialog_help_Button2.setBackgroundColor(Color.WHITE);
                        Layout_CustomDialog_help_Button3.setBackgroundColor(Color.WHITE);
                        Layout_CustomDialog_help_Scroll1.setVisibility(View.VISIBLE);
                        Layout_CustomDialog_help_Scroll2.setVisibility(View.GONE);
                        Layout_CustomDialog_help_Scroll3.setVisibility(View.GONE);
                    }
                });
                Layout_CustomDialog_help_Button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Layout_CustomDialog_help_Button1.setBackgroundColor(Color.WHITE);
                        Layout_CustomDialog_help_Button2.setBackgroundColor(Color.parseColor("#2C9FD3"));
                        Layout_CustomDialog_help_Button3.setBackgroundColor(Color.WHITE);
                        Layout_CustomDialog_help_Scroll1.setVisibility(View.GONE);
                        Layout_CustomDialog_help_Scroll2.setVisibility(View.VISIBLE);
                        Layout_CustomDialog_help_Scroll3.setVisibility(View.GONE);
                    }
                });
                Layout_CustomDialog_help_Button3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Layout_CustomDialog_help_Button1.setBackgroundColor(Color.WHITE);
                        Layout_CustomDialog_help_Button2.setBackgroundColor(Color.WHITE);
                        Layout_CustomDialog_help_Button3.setBackgroundColor(Color.parseColor("#2C9FD3"));
                        Layout_CustomDialog_help_Scroll1.setVisibility(View.GONE);
                        Layout_CustomDialog_help_Scroll2.setVisibility(View.GONE);
                        Layout_CustomDialog_help_Scroll3.setVisibility(View.VISIBLE);
                    }
                });
            }
        });


        League_Rank_Search_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = "";
                try {
                    result = "";
                    HttpClient client = new DefaultHttpClient();
                    String postURL = "http://210.122.7.193:8080/gg/myteam_information_download.jsp";
                    HttpPost post = new HttpPost(postURL);
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("ID", MainActivity.Id));
                    params.add(new BasicNameValuePair("Rank_Do", choice_do));
                    params.add(new BasicNameValuePair("Rank_Si", choice_si));
                    UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                    post.setEntity(ent);
                    HttpResponse response = client.execute(post);
                    BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                    String line = null;
                    while ((line = bufreader.readLine()) != null) {
                        result += line;
                    }
                    myteamData = rank_jsonParserList(result);

                    result = "";

                    client = new DefaultHttpClient();
                    postURL = "http://210.122.7.193:8080/gg/team_information_download.jsp";
                    post = new HttpPost(postURL);
                    params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("Rank_Do", choice_do));
                    params.add(new BasicNameValuePair("Rank_Si", choice_si));
                    ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                    post.setEntity(ent);
                    response = client.execute(post);
                    bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                    line = null;
                    while ((line = bufreader.readLine()) != null) {
                        result += line;
                    }
                    help_parsedData = rank_jsonParserList(result);
                    setData();
                    dataadapter = new League_Rank_Customlist_Adapter(getContext(), arrData);
                    dataadapter.listview(League_Rank_List);
                    League_Rank_List.setAdapter(dataadapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                String En_Profile = null;
                try {
                    En_Profile = URLEncoder.encode(myteamData[0][4], "utf-8");
                    if (myteamData[0][4].equals(".")) {
                        Glide.with(rootView.getContext()).load(R.drawable.profile_basic_image).into(league_Rank_Image_myteam);
                    } else {
                        Glide.with(rootView.getContext()).load("http://210.122.7.193:8080/Web_basket/imgs/Emblem/" + En_Profile + ".jpg").bitmapTransform(new CropCircleTransformation(Glide.get(rootView.getContext()).getBitmapPool()))
                                .into(league_Rank_Image_myteam);
                    }
                } catch (UnsupportedEncodingException e) {

                }

                if (myteamData[0][0].equals(".")) {
                    league_Rank_TextView_myrank.setText("");
                    league_Rank_TextView_myteam.setText("팀가입후이용해주세요.");
                    league_Rank_TextView_myteam.setGravity(2);
                    league_Rank_TextView_myteampoint.setText("");
                    League_Button_2.setEnabled(false);
                } else if (myteamData[0][2].equals(choice_do) && myteamData[0][3].equals(choice_si)) {
                    league_Rank_TextView_myrank.setText(myteamData[0][0]);
                    league_Rank_TextView_myteam.setText(myteamData[0][1]);
                    league_Rank_TextView_myteampoint.setText(myteamData[0][5]);
                } else if (myteamData[0][2].equals(choice_do) && choice_si.equals("전 체")) {
                    league_Rank_TextView_myrank.setText(myteamData[0][0]);
                    league_Rank_TextView_myteam.setText(myteamData[0][1]);
                    league_Rank_TextView_myteampoint.setText(myteamData[0][5]);
                } else {
                    league_Rank_TextView_myrank.setText("-");
                    league_Rank_TextView_myteampoint.setText("-");
                }
            }

        });


//./////////////////////////////////탭 구분.//////////////////////////////////////////////////////////////////////////
        League_Button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TabChoice = "1";
                MainActivity.League_Layout_1.setVisibility(View.VISIBLE);
                MainActivity.League_Layout_2.setVisibility(View.GONE);
            }
        });
        League_Button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a = "테스트";
                if (a.equals("테스트")) {
                    //TabChoice = "2";
                    MainActivity.League_Layout_1.setVisibility(View.GONE);
                    MainActivity.League_Layout_2.setVisibility(View.VISIBLE);

                    ////////////내 팀 기록 //////////////////////////////////////////////////////////

                    String result = "";
                    try {
                        HttpClient client = new DefaultHttpClient();
                        String postURL = "http://210.122.7.193:8080/pp/getMyTeamName.jsp";
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
                    Log.i("서버에서 받아온 팀 이름", result);
                    String[][] getTeamParsedData = jsonParserList_getTeamName(result);
                    if (getTeamParsedData != null) {
                        String myTeamName = getTeamParsedData[0][0];
                        result = "";
                        try {
                            HttpClient client = new DefaultHttpClient();
                            String postURL = "http://210.122.7.193:8080/pp/Leuage_myrecord.jsp";
                            HttpPost post = new HttpPost(postURL);

                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                            params.add(new BasicNameValuePair("Id", MainActivity.Id));
                            params.add(new BasicNameValuePair("TeamName", MainActivity.Team1));

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
                        String[][] MyRecordParsedList = jsonParserList_getMyRecord(result);

                        ArrayList<Leauge_MyRecord_MyData> Leauge_Myrecord_MyData;
                        Leauge_Myrecord_MyData = new ArrayList<Leauge_MyRecord_MyData>();
                        String isWin;
                        if (MyRecordParsedList != null) {
                            for (int i = 0; i < MyRecordParsedList.length; i++) {
                                if (MyRecordParsedList[i][6].equals(getTeamParsedData[0][0])) {
                                    isWin = "win";
                                } else {
                                    isWin = "lose";
                                }
                                Leauge_Myrecord_MyData.add(new Leauge_MyRecord_MyData(isWin, MyRecordParsedList[i][5], MyRecordParsedList[i][4], MyRecordParsedList[i][3], MyRecordParsedList[i][2], MyRecordParsedList[i][1], MyRecordParsedList[i][0], MyRecordParsedList[i][7], MyRecordParsedList[i][8]));
                            }
                            Leauge_myRecord_ListView = (ListView) rootView.findViewById(R.id.Leauge_myrecord_listview);
                            Leauge_MyRecord_MyAdapter Adapter = new Leauge_MyRecord_MyAdapter(rootView.getContext(), Leauge_Myrecord_MyData);
                            Leauge_myRecord_ListView.setAdapter(Adapter);
                        } else {
                            //정보 없다 표시
                        }
                    } else {


                    }
                }
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

    ///////////////기록 탭 함수 선언
    public String[][] rank_jsonParserList(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");
            String[] jsonName = {"Rank", "TeamName", "TeamAddress_do", "TeamAddress_si", "Emblem", "Point"};
            help_parsedData = new String[jArr.length()][jsonName.length];
            for (int i = 0; i < jArr.length(); i++) {
                json = jArr.getJSONObject(i);
                for (int j = 0; j < jsonName.length; j++) {
                    help_parsedData[i][j] = json.getString(jsonName[j]);

                }

            }
            return help_parsedData;
        } catch (JSONException e) {
            return null;
        }
    }

    public String[][] help_jsonParserList(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");

            String[] jsonName = {"help1", "help2", "help3"};
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

    /////////내 팀 관리 함수 선언
    public String[][] jsonParserList_getTeamName(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");

            String[] jsonName = {"teamName"};
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

    public String[][] jsonParserList_getMyRecord(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");

            String[] jsonName = {"hometeamname", "awayteamname", "hometeamscore", "awayteamscore", "matchdate", "matchtime", "result", "homeEmblem", "awayEmblem"};
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

    //////////////////////////////////////////////////////////////
    private void setData() {
        arrData = new ArrayList<League_Rank_Customlist_Setting>();
        for (int a = 0; a < help_parsedData.length; a++) {
            arrData.add(new League_Rank_Customlist_Setting(help_parsedData[a][0], help_parsedData[a][1], help_parsedData[a][2], help_parsedData[a][3], help_parsedData[a][4], help_parsedData[a][5], MainActivity.Id));
        }
    }
}
