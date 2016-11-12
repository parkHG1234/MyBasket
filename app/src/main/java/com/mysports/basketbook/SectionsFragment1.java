package com.mysports.basketbook;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ldong on 2016-11-12.
 */
public class SectionsFragment1 extends Fragment {
    //매칭 out선언
    Spinner spinner_Address_Do, spinner_Address_si;
    Button NewsFeed_Select_Button;

    String address1 = "", address2 = "전 체";
    ImageView NewsFeed_Writing;

    Match_Out_NewsFeed_Data_Adapter dataadapter;
    ArrayList<Match_Out_NewsFeed_Data_Setting> arrData;

    ImageView NewsFeed_Emblem;
    TextView NewsFeed_Court, NewsFeed_Data, update_textview;
    ;
    String Choice_Tab = "out";
    int cnt_out, cnt_in, pos;
    static int Position = 0;
    private int MonthGap[] = {-30, -30, -27, -30, -29, -30, -29, -30, -30, -29, -30, -29};


    JSONObject json_out, json_in;
    JSONArray jArr_out, jArr_in;
    String[] jsonName = {"NewsFeed_Num", "NewsFeed_User", "NewsFeed_Do", "NewsFeed_Si", "NewsFeed_Name", "NewsFeed_Court", "NewsFeed_Data", "NewsFeed_Month", "NewsFeed_Day", "NewsFeed_Hour", "NewsFeed_Minute", "NewsFeed_Image", "Name", "Birth", "Sex", "Position", "Team", "Profile", "Height", "Weight", "Phone", "Comment_Count"};
    ProgressBar NewsFeed_ProgressBar;
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    LinearLayout layout_match_Root;
    ImageButton Match_Button_Out;
    Button Match_Button_In, Match_In_Button_Search;
    FloatingActionButton Match_In_FloatingActionButton_fab;
    LinearLayout Match_Layout_Out, Match_Layout_In;
    Match_In_CustomList_MyAdapter match_In_CustomList_MyAdapter;
    ArrayList<Match_In_CustomList_MyData> match_In_CustomList_MyData;
    Spinner Match_In_Spinner_Address_do, Match_In_Spinner_Address_se;
    ArrayAdapter<CharSequence> adspin1, adspin2;
    String[][] parsedData_out, parsedData_TeamCheck;
    String[][] parsedData_in;
    String choice_do = "", choice_se = "전 체";
    int in_minScheduleId = 10000000;
    int out_minScheduleId = 10000000;
    boolean lastitemVisibleFlag_out = false;        //화면에 리스트의 마지막 아이템이 보여지는지 체크
    boolean firstitemVIsibleFlag_out = false;
    boolean lastitemVisibleFlag_in = false;        //화면에 리스트의 마지막 아이템이 보여지는지 체크
    boolean firstitemVIsibleFlag_in = false;

    public SectionsFragment1() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.layout_match, container, false);

        layout_match_Root = (LinearLayout) rootView.findViewById(R.id.layout_match_Root);
        update_textview = (TextView) rootView.findViewById(R.id.update_textview);
        MainActivity.Match_Layout_Tab = (LinearLayout) rootView.findViewById(R.id.Match_Layout_Tab);
        MainActivity.Match_Layout_Out_Address = (LinearLayout) rootView.findViewById(R.id.Match_Layout_Out_Address);
        MainActivity.Match_Layout_In_Address = (LinearLayout) rootView.findViewById(R.id.Match_Layout_In_Address);
        MainActivity.Match_In_CustomList = (ListView) rootView.findViewById(R.id.Match_In_CustomList);
        Match_Button_Out = (ImageButton) rootView.findViewById(R.id.Match_Button_Out);
        Match_Layout_Out = (LinearLayout) rootView.findViewById(R.id.Match_Layout_Out);
        Match_Layout_In = (LinearLayout) rootView.findViewById(R.id.Match_Layout_In);

        //매칭 out id매칭칭
        NewsFeed_Emblem = (ImageView) rootView.findViewById(R.id.NewsFeed_CustomList_Emblem);
        NewsFeed_Court = (TextView) rootView.findViewById(R.id.NewsFeed_CustomList_Court);
        NewsFeed_Data = (TextView) rootView.findViewById(R.id.NewsFeed_CustomList_Data);
        NewsFeed_Writing = (ImageView) rootView.findViewById(R.id.NewsFeed_Writing);
        MainActivity.NewsFeed_List = (ListView) rootView.findViewById(R.id.NewsFeed_List);

        NewsFeed_Select_Button = (Button) rootView.findViewById(R.id.NewsFeed_Select_Button);
        spinner_Address_Do = (Spinner) rootView.findViewById(R.id.NewsFeed_Spinner_Do);
        spinner_Address_si = (Spinner) rootView.findViewById(R.id.NewsFeed_Spinner_Si);
///////////////////////////////////////////////////////////////////////////////////////////////
        if (Boolean.parseBoolean(MainActivity.fragment1)) {
            update_textview.setVisibility(View.GONE);
            layout_match_Root.setVisibility(View.VISIBLE);
        } else {
            update_textview.setVisibility(View.VISIBLE);
            layout_match_Root.setVisibility(View.GONE);
        }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //매칭 out 뉴스피드 코딩
        adspin1 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do, R.layout.zfile_spinner_test);
        adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Address_Do.setAdapter(adspin1);
        spinner_Address_Do.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                address1 = adspin1.getItem(i).toString();
                if (adspin1.getItem(i).equals("서울")) {
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_seoul, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_Address_si.setAdapter(adspin2);
                    spinner_Address_si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            address2 = adspin2.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
                if (adspin1.getItem(i).equals("인천")) {
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_incheon, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_Address_si.setAdapter(adspin2);
                    spinner_Address_si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            address2 = adspin2.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
                if (adspin1.getItem(i).equals("광주")) {
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_gwangju, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_Address_si.setAdapter(adspin2);
                    spinner_Address_si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            address2 = adspin2.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
                if (adspin1.getItem(i).equals("대구")) {
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_DaeGu, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_Address_si.setAdapter(adspin2);
                    spinner_Address_si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            address2 = adspin2.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
                if (adspin1.getItem(i).equals("울산")) {
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Ulsan, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_Address_si.setAdapter(adspin2);
                    spinner_Address_si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            address2 = adspin2.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
                if (adspin1.getItem(i).equals("대전")) {
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_DaeJeon, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_Address_si.setAdapter(adspin2);
                    spinner_Address_si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            address2 = adspin2.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
                if (adspin1.getItem(i).equals("부산")) {
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Busan, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_Address_si.setAdapter(adspin2);
                    spinner_Address_si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            address2 = adspin2.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
                if (adspin1.getItem(i).equals("강원도")) {
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Gangwondo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_Address_si.setAdapter(adspin2);
                    spinner_Address_si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            address2 = adspin2.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
                if (adspin1.getItem(i).equals("경기도")) {
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Gyeonggido, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_Address_si.setAdapter(adspin2);
                    spinner_Address_si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            address2 = adspin2.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
                if (adspin1.getItem(i).equals("충청북도")) {
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Chungcheongbukdo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_Address_si.setAdapter(adspin2);
                    spinner_Address_si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            address2 = adspin2.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
                if (adspin1.getItem(i).equals("충청남도")) {
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Chungcheongnamdo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_Address_si.setAdapter(adspin2);
                    spinner_Address_si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            address2 = adspin2.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
                if (adspin1.getItem(i).equals("전라북도")) {
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Jeolabukdo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_Address_si.setAdapter(adspin2);
                    spinner_Address_si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            address2 = adspin2.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
                if (adspin1.getItem(i).equals("전라남도")) {
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Jeolanamdo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_Address_si.setAdapter(adspin2);
                    spinner_Address_si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            address2 = adspin2.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
                if (adspin1.getItem(i).equals("경상북도")) {
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Gyeongsangbukdo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_Address_si.setAdapter(adspin2);
                    spinner_Address_si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            address2 = adspin2.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
                if (adspin1.getItem(i).equals("경상남도")) {
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Gyeongsangnamdo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_Address_si.setAdapter(adspin2);
                    spinner_Address_si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            address2 = adspin2.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
                if (adspin1.getItem(i).equals("제주도")) {
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Jejudo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_Address_si.setAdapter(adspin2);
                    spinner_Address_si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            address2 = adspin2.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        NewsFeed_Select_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Http_Out_Search Http_Out_Search = new Http_Out_Search();
                Http_Out_Search.execute(address1, address2);

            }
        });


        NewsFeed_Writing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent DataIntent = new Intent(getContext(), Match_Out_NewsFeed_Writing.class);
                DataIntent.putExtra("Id", MainActivity.Id);
                startActivity(DataIntent);
            }
        });

        address1 = MainActivity.interestArea_do;
        choice_do = MainActivity.interestArea_do;

        String result = "";
        try {
            HttpClient client = new DefaultHttpClient();
            String postURL = "http://210.122.7.193:8080/gg/newsfeed_data_download.jsp";
            HttpPost post = new HttpPost(postURL);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("NewsFeed_Do", (String) address1));
            params.add(new BasicNameValuePair("NewsFeed_Si", (String) address2));
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);
            HttpResponse response = client.execute(post);
            BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
            String line = null;
            while ((line = bufreader.readLine()) != null) {
                result += line;
            }
            parsedData_out = jsonParserList(result);
            setData();
            dataadapter = new Match_Out_NewsFeed_Data_Adapter(getContext(), arrData, MainActivity.Id, MainActivity.MaxNum_out);
            dataadapter.listview(MainActivity.NewsFeed_List);
            MainActivity.NewsFeed_List.setAdapter(dataadapter);
        } catch (Exception e) {
            e.printStackTrace();
        }


        adspin1 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do, R.layout.zfile_spinner_test);
        adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Address_Do.setAdapter(adspin1);
        if (address1.equals("서울")) {
            spinner_Address_Do.setSelection(0);
            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_seoul, R.layout.zfile_spinner_test);
            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_Address_si.setAdapter(adspin2);

        } else if (address1.equals("인천")) {
            spinner_Address_Do.setSelection(1);
            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_incheon, R.layout.zfile_spinner_test);
            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_Address_si.setAdapter(adspin2);

        } else if (address1.equals("광주")) {
            spinner_Address_Do.setSelection(2);
            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_gwangju, R.layout.zfile_spinner_test);
            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_Address_si.setAdapter(adspin2);

        } else if (address1.equals("대구")) {
            spinner_Address_Do.setSelection(3);
            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_DaeGu, R.layout.zfile_spinner_test);
            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_Address_si.setAdapter(adspin2);

        } else if (address1.equals("울산")) {
            spinner_Address_Do.setSelection(4);
            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Ulsan, R.layout.zfile_spinner_test);
            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_Address_si.setAdapter(adspin2);

        } else if (address1.equals("대전")) {
            spinner_Address_Do.setSelection(5);
            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Ulsan, R.layout.zfile_spinner_test);
            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_Address_si.setAdapter(adspin2);

        } else if (address1.equals("부산")) {
            spinner_Address_Do.setSelection(6);
            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Ulsan, R.layout.zfile_spinner_test);
            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_Address_si.setAdapter(adspin2);

        } else if (address1.equals("강원도")) {
            spinner_Address_Do.setSelection(7);
            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Ulsan, R.layout.zfile_spinner_test);
            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_Address_si.setAdapter(adspin2);

        } else if (address1.equals("경기도")) {
            spinner_Address_Do.setSelection(8);
            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Gyeonggido, R.layout.zfile_spinner_test);
            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_Address_si.setAdapter(adspin2);

        } else if (address1.equals("충청북도")) {
            spinner_Address_Do.setSelection(9);
            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Chungcheongbukdo, R.layout.zfile_spinner_test);
            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_Address_si.setAdapter(adspin2);

        } else if (address1.equals("충청남도")) {
            spinner_Address_Do.setSelection(10);
            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Chungcheongnamdo, R.layout.zfile_spinner_test);
            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_Address_si.setAdapter(adspin2);

        } else if (address1.equals("전라북도")) {
            spinner_Address_Do.setSelection(11);
            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Jeolabukdo, R.layout.zfile_spinner_test);
            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_Address_si.setAdapter(adspin2);

        } else if (address1.equals("전라남도")) {
            spinner_Address_Do.setSelection(12);
            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Jeolanamdo, R.layout.zfile_spinner_test);
            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_Address_si.setAdapter(adspin2);

        } else if (address1.equals("경상북도")) {
            spinner_Address_Do.setSelection(13);
            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Gyeongsangbukdo, R.layout.zfile_spinner_test);
            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_Address_si.setAdapter(adspin2);

        } else if (address1.equals("경상남도")) {
            spinner_Address_Do.setSelection(14);
            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Gyeongsangnamdo, R.layout.zfile_spinner_test);
            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_Address_si.setAdapter(adspin2);

        } else if (address1.equals("제주도")) {
            spinner_Address_Do.setSelection(15);
            adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Jejudo, R.layout.zfile_spinner_test);
            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_Address_si.setAdapter(adspin2);

        }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Match_In_Spinner_Address_do = (Spinner) rootView.findViewById(R.id.Match_In_Spinner_Address_do);
        Match_In_Spinner_Address_se = (Spinner) rootView.findViewById(R.id.Match_In_Spinner_Address_se);
        Match_In_Button_Search = (Button) rootView.findViewById(R.id.Match_In_Button_Search);
        Match_In_FloatingActionButton_fab = (FloatingActionButton) rootView.findViewById(R.id.Match_In_FloatingActionButton_fab);
        Match_Button_Out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.out_in_choice = "out";
                Match_Layout_Out.setVisibility(View.VISIBLE);
                Match_Layout_In.setVisibility(View.GONE);
                Choice_Tab = "out";
                out_minScheduleId = 10000;
                try {
                    HttpClient client = new DefaultHttpClient();
                    String postURL = "http://210.122.7.193:8080/gg/newsfeed_data_download.jsp";
                    HttpPost post = new HttpPost(postURL);
                    List<NameValuePair> params1 = new ArrayList<NameValuePair>();
                    params1.add(new BasicNameValuePair("NewsFeed_Do", (String) address1));
                    params1.add(new BasicNameValuePair("NewsFeed_Si", (String) address2));

                    UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params1, HTTP.UTF_8);
                    post.setEntity(ent);
                    HttpResponse response = client.execute(post);
                    BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));

                    String line = null;
                    String result = "";
                    while ((line = bufreader.readLine()) != null) {
                        result += line;
                    }
                    parsedData_out = jsonParserList(result);
                    setData();
                    dataadapter = new Match_Out_NewsFeed_Data_Adapter(getContext(), arrData, MainActivity.Id, MainActivity.MaxNum_out);
                    dataadapter.listview(MainActivity.NewsFeed_List);
                    MainActivity.NewsFeed_List.setAdapter(dataadapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ////////////////////////////////리스트 뷰 구현////////////////////////////////////////////////
            }
        });
        MainActivity.NewsFeed_List.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //현재 화면에 보이는 첫번째 리스트 아이템의 번호(firstVisibleItem) + 현재 화면에 보이는 리스트 아이템의 갯수(visibleItemCount)가 리스트 전체의 갯수(totalItemCount) -1 보다 크거나 같을때
                lastitemVisibleFlag_out = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
                firstitemVIsibleFlag_out = (totalItemCount > 0) && (firstVisibleItem == 0);
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //OnScrollListener.SCROLL_STATE_IDLE은 스크롤이 이동하다가 멈추었을때 발생되는 스크롤 상태입니다.
                //즉 스크롤이 바닦에 닿아 멈춘 상태에 처리를 하겠다는 뜻
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastitemVisibleFlag_out) {
                    Http_Out_Search_Scroll Http_Out_Search_Scroll = new Http_Out_Search_Scroll();
                    Http_Out_Search_Scroll.execute(address1, address2);
                }
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && firstitemVIsibleFlag_out) {
                    MainActivity.Match_Layout_Tab.setVisibility(View.VISIBLE);
                    MainActivity.Match_Layout_Out_Address.setVisibility(View.VISIBLE);
                }
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    MainActivity.Match_Layout_Tab.setVisibility(View.GONE);
                    MainActivity.Match_Layout_Out_Address.setVisibility(View.GONE);
                }
            }
        });
        ////////////////////////////////리스트 뷰 구현////////////////////////////////////////////////

////////////////////////////////            /////매칭 -In 구현/////////////////////////////////////////////////////////////////////////////////
        Match_Button_In = (Button) rootView.findViewById(R.id.Match_Button_In);

        Match_Button_In.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                in_minScheduleId = 10000;
                Match_Layout_Out.setVisibility(View.GONE);
                Match_Layout_In.setVisibility(View.VISIBLE);
                MainActivity.out_in_choice = "in";
                Choice_Tab = "in";

                adspin1 = ArrayAdapter.createFromResource(rootView.getContext(), R.array.spinner_do, R.layout.zfile_spinner_test);
                adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Match_In_Spinner_Address_do.setAdapter(adspin1);
                Match_In_Spinner_Address_do.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        if (adspin1.getItem(i).equals("서울")) {
                            choice_do = "서울";
                            //두번째 스피너 이벤트
                            adspin2 = ArrayAdapter.createFromResource(rootView.getContext(), R.array.spinner_do_seoul, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Match_In_Spinner_Address_se.setAdapter(adspin2);
                            Match_In_Spinner_Address_se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    choice_se = adspin2.getItem(i).toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                        } else if (adspin1.getItem(i).equals("인천")) {
                            choice_do = "인천";
                            adspin2 = ArrayAdapter.createFromResource(rootView.getContext(), R.array.spinner_do_incheon, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Match_In_Spinner_Address_se.setAdapter(adspin2);
                            Match_In_Spinner_Address_se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    choice_se = adspin2.getItem(i).toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                        } else if (adspin1.getItem(i).equals("광주")) {
                            choice_do = "광주";
                            adspin2 = ArrayAdapter.createFromResource(rootView.getContext(), R.array.spinner_do_gwangju, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Match_In_Spinner_Address_se.setAdapter(adspin2);
                            Match_In_Spinner_Address_se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    choice_se = adspin2.getItem(i).toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                        } else if (adspin1.getItem(i).equals("대구")) {
                            choice_do = "대구";
                            adspin2 = ArrayAdapter.createFromResource(rootView.getContext(), R.array.spinner_do_DaeGu, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Match_In_Spinner_Address_se.setAdapter(adspin2);
                            Match_In_Spinner_Address_se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    choice_se = adspin2.getItem(i).toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                        } else if (adspin1.getItem(i).equals("울산")) {
                            choice_do = "울산";
                            adspin2 = ArrayAdapter.createFromResource(rootView.getContext(), R.array.spinner_do_Ulsan, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Match_In_Spinner_Address_se.setAdapter(adspin2);
                            Match_In_Spinner_Address_se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    choice_se = adspin2.getItem(i).toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                        } else if (adspin1.getItem(i).equals("대전")) {
                            choice_do = "대전";
                            adspin2 = ArrayAdapter.createFromResource(rootView.getContext(), R.array.spinner_do_DaeJeon, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Match_In_Spinner_Address_se.setAdapter(adspin2);
                            Match_In_Spinner_Address_se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    choice_se = adspin2.getItem(i).toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                        } else if (adspin1.getItem(i).equals("부산")) {
                            choice_do = "부산";
                            adspin2 = ArrayAdapter.createFromResource(rootView.getContext(), R.array.spinner_do_Busan, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Match_In_Spinner_Address_se.setAdapter(adspin2);
                            Match_In_Spinner_Address_se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    choice_se = adspin2.getItem(i).toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                        } else if (adspin1.getItem(i).equals("강원도")) {
                            choice_do = "강원도";
                            adspin2 = ArrayAdapter.createFromResource(rootView.getContext(), R.array.spinner_do_Gangwondo, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Match_In_Spinner_Address_se.setAdapter(adspin2);
                            Match_In_Spinner_Address_se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    choice_se = adspin2.getItem(i).toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                        } else if (adspin1.getItem(i).equals("경기도")) {
                            choice_do = "경기도";
                            adspin2 = ArrayAdapter.createFromResource(rootView.getContext(), R.array.spinner_do_Gyeonggido, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Match_In_Spinner_Address_se.setAdapter(adspin2);
                            Match_In_Spinner_Address_se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    choice_se = adspin2.getItem(i).toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                        } else if (adspin1.getItem(i).equals("충청남도")) {
                            choice_do = "충청남도";
                            adspin2 = ArrayAdapter.createFromResource(rootView.getContext(), R.array.spinner_do_Chungcheongnamdo, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Match_In_Spinner_Address_se.setAdapter(adspin2);
                            Match_In_Spinner_Address_se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    choice_se = adspin2.getItem(i).toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                        } else if (adspin1.getItem(i).equals("충청북도")) {
                            choice_do = "충청북도";
                            adspin2 = ArrayAdapter.createFromResource(rootView.getContext(), R.array.spinner_do_Chungcheongbukdo, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Match_In_Spinner_Address_se.setAdapter(adspin2);
                            Match_In_Spinner_Address_se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    choice_se = adspin2.getItem(i).toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                        } else if (adspin1.getItem(i).equals("전라북도")) {
                            choice_do = "전라북도";
                            adspin2 = ArrayAdapter.createFromResource(rootView.getContext(), R.array.spinner_do_Jeolabukdo, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Match_In_Spinner_Address_se.setAdapter(adspin2);
                            Match_In_Spinner_Address_se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    choice_se = adspin2.getItem(i).toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                        } else if (adspin1.getItem(i).equals("전라남도")) {
                            choice_do = "전라남도";
                            adspin2 = ArrayAdapter.createFromResource(rootView.getContext(), R.array.spinner_do_Jeolanamdo, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Match_In_Spinner_Address_se.setAdapter(adspin2);
                            Match_In_Spinner_Address_se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    choice_se = adspin2.getItem(i).toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                        } else if (adspin1.getItem(i).equals("경상북도")) {
                            choice_do = "경상북도";
                            adspin2 = ArrayAdapter.createFromResource(rootView.getContext(), R.array.spinner_do_Gyeongsangbukdo, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Match_In_Spinner_Address_se.setAdapter(adspin2);
                            Match_In_Spinner_Address_se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    choice_se = adspin2.getItem(i).toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                        } else if (adspin1.getItem(i).equals("경상남도")) {
                            choice_do = "경상남도";
                            adspin2 = ArrayAdapter.createFromResource(rootView.getContext(), R.array.spinner_do_Gyeongsangnamdo, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Match_In_Spinner_Address_se.setAdapter(adspin2);
                            Match_In_Spinner_Address_se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    choice_se = adspin2.getItem(i).toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                        } else if (adspin1.getItem(i).equals("제주도")) {
                            choice_do = "제주도";
                            adspin2 = ArrayAdapter.createFromResource(rootView.getContext(), R.array.spinner_do_Jejudo, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Match_In_Spinner_Address_se.setAdapter(adspin2);
                            Match_In_Spinner_Address_se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    choice_se = adspin2.getItem(i).toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });

                ///////////////////////////////////////////////////////////////////


                address1 = MainActivity.interestArea_do;


                adspin1 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do, R.layout.zfile_spinner_test);
                adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Match_In_Spinner_Address_do.setAdapter(adspin1);
                if (address1.equals("서울")) {
                    Match_In_Spinner_Address_do.setSelection(0);
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_seoul, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Match_In_Spinner_Address_se.setAdapter(adspin2);

                } else if (address1.equals("인천")) {
                    Match_In_Spinner_Address_do.setSelection(1);
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_incheon, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Match_In_Spinner_Address_se.setAdapter(adspin2);

                } else if (address1.equals("광주")) {
                    Match_In_Spinner_Address_do.setSelection(2);
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_gwangju, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Match_In_Spinner_Address_se.setAdapter(adspin2);

                } else if (address1.equals("대구")) {
                    Match_In_Spinner_Address_do.setSelection(3);
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_DaeGu, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Match_In_Spinner_Address_se.setAdapter(adspin2);

                } else if (address1.equals("울산")) {
                    Match_In_Spinner_Address_do.setSelection(4);
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Ulsan, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Match_In_Spinner_Address_se.setAdapter(adspin2);

                } else if (address1.equals("대전")) {
                    Match_In_Spinner_Address_do.setSelection(5);
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Ulsan, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Match_In_Spinner_Address_se.setAdapter(adspin2);

                } else if (address1.equals("부산")) {
                    Match_In_Spinner_Address_do.setSelection(6);
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Ulsan, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Match_In_Spinner_Address_se.setAdapter(adspin2);

                } else if (address1.equals("강원도")) {
                    Match_In_Spinner_Address_do.setSelection(7);
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Ulsan, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Match_In_Spinner_Address_se.setAdapter(adspin2);

                } else if (address1.equals("경기도")) {
                    Match_In_Spinner_Address_do.setSelection(8);
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Gyeonggido, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Match_In_Spinner_Address_se.setAdapter(adspin2);

                } else if (address1.equals("충청북도")) {
                    Match_In_Spinner_Address_do.setSelection(9);
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Chungcheongbukdo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Match_In_Spinner_Address_se.setAdapter(adspin2);

                } else if (address1.equals("충청남도")) {
                    Match_In_Spinner_Address_do.setSelection(10);
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Chungcheongnamdo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Match_In_Spinner_Address_se.setAdapter(adspin2);

                } else if (address1.equals("전라북도")) {
                    Match_In_Spinner_Address_do.setSelection(11);
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Jeolabukdo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Match_In_Spinner_Address_se.setAdapter(adspin2);

                } else if (address1.equals("전라남도")) {
                    Match_In_Spinner_Address_do.setSelection(12);
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Jeolanamdo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Match_In_Spinner_Address_se.setAdapter(adspin2);

                } else if (address1.equals("경상북도")) {
                    Match_In_Spinner_Address_do.setSelection(13);
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Gyeongsangbukdo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Match_In_Spinner_Address_se.setAdapter(adspin2);

                } else if (address1.equals("경상남도")) {
                    Match_In_Spinner_Address_do.setSelection(14);
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Gyeongsangnamdo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Match_In_Spinner_Address_se.setAdapter(adspin2);

                } else if (address1.equals("제주도")) {
                    Match_In_Spinner_Address_do.setSelection(15);
                    adspin2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_do_Jejudo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Match_In_Spinner_Address_se.setAdapter(adspin2);

                }
                ///처음에 서울 전체 리스트 불러옵니다.////////////////////////////////////////////////////////
                try {
                    HttpClient client = new DefaultHttpClient();
                    String postURL = "http://210.122.7.193:8080/Web_basket/Match_InList.jsp";
                    HttpPost post = new HttpPost(postURL);

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("do", address1));
                    params.add(new BasicNameValuePair("se", address2));

                    UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                    post.setEntity(ent);

                    HttpResponse response = client.execute(post);
                    BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));

                    String line = null;
                    String result = "";
                    while ((line = bufreader.readLine()) != null) {
                        result += line;
                    }
                    parsedData_in = inList_jsonParserList(result);
                    inList_setData();
                    match_In_CustomList_MyAdapter = new Match_In_CustomList_MyAdapter(rootView.getContext(), match_In_CustomList_MyData);
                    MainActivity.Match_In_CustomList.setAdapter(match_In_CustomList_MyAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //검색 버튼 클릭 이벤트/////////////////////////////////////////////////////////////////////////
                Match_In_Button_Search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        in_minScheduleId = 10000;
                        Http_In_Search Http_In_Search = new Http_In_Search();
                        Http_In_Search.execute(choice_do, choice_se);

                    }
                });

                /////////////////////////////////////////////////////////////////////////////////////////////////////
                //플로팅버튼을 리스트에 뜨도록 매칭
                Match_In_FloatingActionButton_fab.attachToListView(MainActivity.Match_In_CustomList);
                ///in리스트를 등록합니다.////////////////////////////////////////////////////////
                Match_In_FloatingActionButton_fab.setOnClickListener(new View.OnClickListener() {
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
                            Snackbar.make(view, "팀 등록 후 이용해주시기 바랍니다.", Snackbar.LENGTH_SHORT).show();
                        } else {
                            Activity activity = (Activity) rootView.getContext();
                            Intent intent_In_Register = new Intent(rootView.getContext(), Match_In_Register.class);
                            intent_In_Register.putExtra("Id", MainActivity.Id);
                            activity.startActivity(intent_In_Register);
                        }
                    }
                });

                MainActivity.Match_In_CustomList.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        //현재 화면에 보이는 첫번째 리스트 아이템의 번호(firstVisibleItem) + 현재 화면에 보이는 리스트 아이템의 갯수(visibleItemCount)가 리스트 전체의 갯수(totalItemCount) -1 보다 크거나 같을때
                        lastitemVisibleFlag_in = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
                        firstitemVIsibleFlag_in = (totalItemCount > 0) && (firstVisibleItem == 0);
                    }

                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                        //OnScrollListener.SCROLL_STATE_IDLE은 스크롤이 이동하다가 멈추었을때 발생되는 스크롤 상태입니다.
                        //즉 스크롤이 바닦에 닿아 멈춘 상태에 처리를 하겠다는 뜻
                        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastitemVisibleFlag_in) {
                            Http_In_Scroll Http_In_Scroll = new Http_In_Scroll();
                            Http_In_Scroll.execute();

                        }
                        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && firstitemVIsibleFlag_in) {
                            MainActivity.Match_Layout_Tab.setVisibility(View.VISIBLE);
                            MainActivity.Match_Layout_In_Address.setVisibility(View.VISIBLE);
                        }
                        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                            MainActivity.Match_Layout_Tab.setVisibility(View.GONE);
                            MainActivity.Match_Layout_In_Address.setVisibility(View.GONE);
                        }
                    }

                });
            }
        });
        /////////////////어댑터에 값 넣음./////////////////////////


        return rootView;
    }

    private void inList_setData() {
        match_In_CustomList_MyData = new ArrayList<Match_In_CustomList_MyData>();
        for (int i = 0; i < parsedData_in.length; i++) {
            match_In_CustomList_MyData.add(new Match_In_CustomList_MyData(parsedData_in[i][0], parsedData_in[i][1], parsedData_in[i][2], parsedData_in[i][3], parsedData_in[i][4], parsedData_in[i][5], parsedData_in[i][6], parsedData_in[i][7], parsedData_in[i][8], parsedData_in[i][9], parsedData_in[i][10], parsedData_in[i][11], parsedData_in[i][12], MainActivity.Id, parsedData_in[i][13]));
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private void setData() {
        arrData = new ArrayList<Match_Out_NewsFeed_Data_Setting>();
        for (int a = 0; a < parsedData_out.length; a++) {
            arrData.add(new Match_Out_NewsFeed_Data_Setting(parsedData_out[a][0], parsedData_out[a][1], parsedData_out[a][2], parsedData_out[a][3], parsedData_out[a][4], parsedData_out[a][5], parsedData_out[a][6], parsedData_out[a][7], parsedData_out[a][8], parsedData_out[a][9], parsedData_out[a][10], parsedData_out[a][11], parsedData_out[a][12], parsedData_out[a][13], parsedData_out[a][14], parsedData_out[a][15], parsedData_out[a][16], parsedData_out[a][17], parsedData_out[a][18], parsedData_out[a][19], parsedData_out[a][20], parsedData_out[a][21]));

        }
    }

    public class Http_Out_Search extends AsyncTask<String, Void, String> {
        ProgressDialog asyncDialog = new ProgressDialog(getContext());
        String[][] parsedData;

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("잠시만 기다려주세요..");
            // show dialog
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try {
                HttpClient client = new DefaultHttpClient();
                String postURL = "http://210.122.7.193:8080/gg/newsfeed_data_download.jsp";
                HttpPost post = new HttpPost(postURL);
                List<NameValuePair> params1 = new ArrayList<NameValuePair>();
                params1.add(new BasicNameValuePair("NewsFeed_Do", address1));
                params1.add(new BasicNameValuePair("NewsFeed_Si", address2));
                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params1, HTTP.UTF_8);
                post.setEntity(ent);
                HttpResponse response = client.execute(post);
                BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                String line = null;
                while ((line = bufreader.readLine()) != null) {
                    result += line;
                }
                parsedData_out = jsonParserList(result);

                return "succed";
            } catch (Exception e) {
                e.printStackTrace();
                return "failed";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            setData();
            dataadapter = new Match_Out_NewsFeed_Data_Adapter(getContext(), arrData, MainActivity.Id, MainActivity.MaxNum_out);
            dataadapter.listview(MainActivity.NewsFeed_List);
            MainActivity.NewsFeed_List.setAdapter(dataadapter);
            asyncDialog.dismiss();
            super.onPostExecute(result);
        }
    }

    public class Http_Out_Search_Scroll extends AsyncTask<String, Void, String> {
        ProgressDialog asyncDialog = new ProgressDialog(getContext());
        String[][] parsedData;

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("잠시만 기다려주세요..");
            // show dialog
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try {
                HttpClient client = new DefaultHttpClient();
                String postURL = "http://210.122.7.193:8080/gg/newsfeed_data_download_scroll.jsp";
                HttpPost post = new HttpPost(postURL);
                List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                params2.add(new BasicNameValuePair("NewsFeed_Do", (String) address1));
                params2.add(new BasicNameValuePair("NewsFeed_Si", (String) address2));
                params2.add(new BasicNameValuePair("minScheduleId", Integer.toString(out_minScheduleId)));
                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params2, HTTP.UTF_8);
                post.setEntity(ent);
                HttpResponse response = client.execute(post);
                BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                String line = null;
                while ((line = bufreader.readLine()) != null) {
                    result += line;
                }
                parsedData_out = jsonParserList(result);
                return "succed";
            } catch (Exception e) {
                e.printStackTrace();
                return "failed";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            for (int a = 0; a < parsedData_out.length; a++) {
                arrData.add(new Match_Out_NewsFeed_Data_Setting(parsedData_out[a][0], parsedData_out[a][1], parsedData_out[a][2], parsedData_out[a][3], parsedData_out[a][4], parsedData_out[a][5], parsedData_out[a][6], parsedData_out[a][7], parsedData_out[a][8], parsedData_out[a][9], parsedData_out[a][10], parsedData_out[a][11], parsedData_out[a][12], parsedData_out[a][13], parsedData_out[a][14], parsedData_out[a][15], parsedData_out[a][16], parsedData_out[a][17], parsedData_out[a][18], parsedData_out[a][19], parsedData_out[a][20], parsedData_out[a][21]));
            }
            dataadapter.notifyDataSetChanged();
            asyncDialog.dismiss();
            super.onPostExecute(result);
        }
    }

    public class Http_In_Search extends AsyncTask<String, Void, String> {
        ProgressDialog asyncDialog = new ProgressDialog(getContext());
        String[][] parsedData;

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("잠시만 기다려주세요..");
            // show dialog
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                HttpClient client = new DefaultHttpClient();
                String postURL = "http://210.122.7.193:8080/Web_basket/Match_InList.jsp";
                HttpPost post = new HttpPost(postURL);

                List<NameValuePair> params1 = new ArrayList<NameValuePair>();
                params1.add(new BasicNameValuePair("do", choice_do));
                params1.add(new BasicNameValuePair("se", choice_se));

                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params1, HTTP.UTF_8);
                post.setEntity(ent);

                HttpResponse response = client.execute(post);
                BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));

                String line = null;
                String result = "";
                while ((line = bufreader.readLine()) != null) {
                    result += line;
                }
                parsedData_in = inList_jsonParserList(result);

                return "succed";
            } catch (Exception e) {
                e.printStackTrace();
                return "failed";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            inList_setData();
            match_In_CustomList_MyAdapter = new Match_In_CustomList_MyAdapter(getContext(), match_In_CustomList_MyData);
            MainActivity.Match_In_CustomList.setAdapter(match_In_CustomList_MyAdapter);
            asyncDialog.dismiss();
            super.onPostExecute(result);
        }
    }

    public class Http_In_Scroll extends AsyncTask<String, Void, String> {
        ProgressDialog asyncDialog = new ProgressDialog(getContext());
        String[][] parsedData;

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("잠시만 기다려주세요..");
            // show dialog
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                HttpClient client = new DefaultHttpClient();
                String postURL = "http://210.122.7.193:8080/Web_basket/Match_InList_Scroll.jsp";
                HttpPost post = new HttpPost(postURL);

                List<NameValuePair> params1 = new ArrayList<NameValuePair>();
                params1.add(new BasicNameValuePair("do", choice_do));
                params1.add(new BasicNameValuePair("se", choice_se));
                params1.add(new BasicNameValuePair("minScheduleId", Integer.toString(in_minScheduleId)));

                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params1, HTTP.UTF_8);
                post.setEntity(ent);

                HttpResponse response = client.execute(post);
                BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));

                String line = null;
                String result = "";
                while ((line = bufreader.readLine()) != null) {
                    result += line;
                }
                parsedData_in = inList_jsonParserList(result);
                for (int i = 0; i < parsedData_in.length; i++) {
                    match_In_CustomList_MyData.add(new Match_In_CustomList_MyData(parsedData_in[i][0], parsedData_in[i][1], parsedData_in[i][2], parsedData_in[i][3], parsedData_in[i][4], parsedData_in[i][5], parsedData_in[i][6], parsedData_in[i][7], parsedData_in[i][8], parsedData_in[i][9], parsedData_in[i][10], parsedData_in[i][11], parsedData_in[i][12], MainActivity.Id, parsedData_in[i][13]));
                }

                return "succed";
            } catch (Exception e) {
                e.printStackTrace();
                return "failed";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            match_In_CustomList_MyAdapter.notifyDataSetChanged();
            asyncDialog.dismiss();
            super.onPostExecute(result);
        }
    }

    public String[][] jsonParserList(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try {
            json_out = new JSONObject(pRecvServerPage);
            jArr_out = json_out.getJSONArray("List");
            parsedData_out = new String[jArr_out.length()][jsonName.length];
            for (int i = 0; i < jArr_out.length(); i++) {
                json_out = jArr_out.getJSONObject(i);
                for (int j = 0; j < jsonName.length; j++) {
                    parsedData_out[i][j] = json_out.getString(jsonName[j]);
                    if (MainActivity.MaxNum_out < Integer.valueOf(parsedData_out[i][0])) {
                        MainActivity.MaxNum_out = Integer.valueOf(parsedData_out[i][0]);
                    }
                }
                if (out_minScheduleId > Integer.parseInt(parsedData_out[i][0])) {
                    out_minScheduleId = Integer.parseInt(parsedData_out[i][0]);
                }
            }
            return parsedData_out;
        } catch (JSONException e) {
            return null;
        }
    }

    /////매칭 탭 - in : 받아온 json 파싱합니다.//////////////////////////////////////////////////////////
    public String[][] inList_jsonParserList(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try {
            json_in = new JSONObject(pRecvServerPage);
            jArr_in = json_in.getJSONArray("List");
            String[] jsonName = {"msg1", "msg2", "msg3", "msg4", "msg5", "msg6", "msg7", "msg8", "msg9", "msg10", "msg11", "msg12", "msg13", "msg14"};
            parsedData_in = new String[jArr_in.length()][jsonName.length];
            for (int i = 0; i < jArr_in.length(); i++) {
                json_in = jArr_in.getJSONObject(i);
                for (int j = 0; j < jsonName.length; j++) {
                    parsedData_in[i][j] = json_in.getString(jsonName[j]);
                }
                if (in_minScheduleId > Integer.parseInt(parsedData_in[i][4])) {
                    in_minScheduleId = Integer.parseInt(parsedData_in[i][4]);
                }
                Log.i("minScheduleId", Integer.toString(in_minScheduleId));
            }
            return parsedData_in;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String GetTime(int position) {
        String Time;
        Integer Month, Day, Hour, Minute;

        Month = (Integer.parseInt(new SimpleDateFormat("MM").format(new java.sql.Date(System.currentTimeMillis())))) - Integer.parseInt(arrData.get(position).getMonth());
        Day = (Integer.parseInt(new SimpleDateFormat("dd").format(new java.sql.Date(System.currentTimeMillis())))) - Integer.parseInt(arrData.get(position).getDay());
        Hour = (Integer.parseInt(new SimpleDateFormat("kk").format(new java.sql.Date(System.currentTimeMillis())))) - Integer.parseInt(arrData.get(position).getHour());
        Minute = (Integer.parseInt(new SimpleDateFormat("mm").format(new java.sql.Date(System.currentTimeMillis())))) - Integer.parseInt(arrData.get(position).getMinute());
        if (Month > 0) {
            //매달 1일 일 경우
            if (Day == MonthGap[(Integer.parseInt(new SimpleDateFormat("MM").format(new java.sql.Date(System.currentTimeMillis()))))]) {
                if (Hour > 1) {
                    return Hour + "시간전";
                } else if (Hour == 1 && Minute >= 0) {
                    return Hour + "시간전";
                } else if (Hour > 0 && Minute <= 0) {
                    return 60 + Minute + "분전";
                }
            } else {
                Month = Integer.parseInt(arrData.get(position).getMonth());
                Day = Integer.parseInt(arrData.get(position).getDay());
                Hour = Integer.parseInt(arrData.get(position).getHour());
                Minute = Integer.parseInt(arrData.get(position).getMinute());
                Time = Month + "월 " + Day + "일 " + Hour + "시 " + Minute + "분 ";
                return Time;
            }
        } else {
            if (Day > 0) {
                return Day + "일전";
            } else {
                if (Hour > 1 && Minute > 0) {
                    return Hour + "시간전";
                } else if (Hour > 1 && Minute < 0) {
                    return Hour - 1 + "시간전";
                } else if (Hour == 1 && Minute >= 0) {
                    return Hour + "시간전";
                } else if (Hour == 1 && Minute < 0) {
                    return 60 + Minute + "분전";
                } else if (Hour == 0 && Minute > 0) {
                    return Minute + "분전";
                } else if (Hour == 0 && Minute < 0) {
                    return 60 + Minute + "분전";
                } else {
                    return "방금";
                }
            }
        }
        return "Time Error";
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
}
