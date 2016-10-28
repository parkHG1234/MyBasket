package com.mysports.basketbook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

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
import org.xdty.preference.colorpicker.ColorPickerDialog;
import org.xdty.preference.colorpicker.ColorPickerSwatch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by park on 2016-04-04.
 */
public class Navigation_TeamManager_TeamMake1 extends AppCompatActivity {
    LinearLayout layout_teammanager_teammake1_root;
    LinearLayout TeamManager_TeamMake_Layout_Maker;
    LinearLayout TeamManager_TeamMake_Layout_OverLap;
    String Str_TeamManager_TeamMake_EditText_TeamName="";
    String Str_TeamManager_TeamMake_EditText_TeamAddress_do="";
    String Str_TeamManager_TeamMake_EditText_TeamAddress_se="전 체";
    String Str_TeamManager_TeamMake_EditText_HomeCourt;
    String Str_TeamManager_TeamMake_EditText_Time;
    String Str_TeamManager_TeamMake_EditText_TeamIntro;
    MaterialEditText TeamManager_TeamMake_EditText_TeamName;
    MaterialEditText TeamManager_TeamMake_EditText_HomeCourt;
    MaterialEditText TeamManager_TeamMake_EditText_Time;
    Button TeamManger_TeamMake_Button_UniformColor;
    EditText TeamManager_TeamMake_EditText_TeamIntro;
    Spinner TeamManager_TeamMake_Spinner_Address_Do;
    Spinner TeamManager_TeamMake_Spinner_Address_Se;
    static String UniformTop = "-10395295";
    private int mSelectedColor;
    ColorPickerDialog dialog;
    static String Id="";
    ArrayAdapter<CharSequence> adspin1, adspin2;
    String[][] parsedData,parsedData_TeamName;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_teammanager_teammake1);
        layout_teammanager_teammake1_root = (LinearLayout)findViewById(R.id.layout_teammanager_teammake1_root);
        TeamManager_TeamMake_EditText_TeamName = (MaterialEditText)findViewById(R.id.TeamManager_TeamMake_EditText_TeamName);
        TeamManager_TeamMake_EditText_HomeCourt = (MaterialEditText)findViewById(R.id.TeamManager_TeamMake_EditText_HomeCourt);
        TeamManager_TeamMake_EditText_Time = (MaterialEditText)findViewById(R.id.TeamManager_TeamMake_EditText_Time);
        TeamManger_TeamMake_Button_UniformColor = (Button)findViewById(R.id.TeamManger_TeamMake_Button_UniformColor);
        TeamManager_TeamMake_EditText_TeamIntro = (EditText)findViewById(R.id.TeamManager_TeamMake_EditText_TeamIntro);
        TeamManager_TeamMake_Spinner_Address_Do = (Spinner)findViewById(R.id.TeamManager_TeamMake_Spinner_Address_Do);
        TeamManager_TeamMake_Spinner_Address_Se = (Spinner)findViewById(R.id.TeamManager_TeamMake_Spinner_Address_Se);
        TeamManager_TeamMake_Layout_Maker = (LinearLayout)findViewById(R.id.TeamManager_TeamMake_Layout_Maker);
        TeamManager_TeamMake_Layout_OverLap = (LinearLayout)findViewById(R.id.TeamManager_TeamMake_Layout_OverLap);
        Button TeamManager_TeamMake1_Button_Next = (Button)findViewById(R.id.TeamManager_TeamMake_Button_Next);


        layout_teammanager_teammake1_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(TeamManager_TeamMake_EditText_TeamName.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(TeamManager_TeamMake_EditText_HomeCourt.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(TeamManager_TeamMake_EditText_Time.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(TeamManager_TeamMake_EditText_TeamIntro.getWindowToken(), 0);

            }
        });

        mSelectedColor = ContextCompat.getColor(this, R.color.flamingo);
        int[] mColors = getResources().getIntArray(R.array.default_rainbow);
        dialog = ColorPickerDialog.newInstance(R.string.color_picker_default_title,
                mColors,
                mSelectedColor,
                5, // Number of columns
                ColorPickerDialog.SIZE_SMALL);

        dialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {

            @Override
            public void onColorSelected(int color) {
                mSelectedColor = color;
                TeamManger_TeamMake_Button_UniformColor.setBackgroundColor(mSelectedColor);
                UniformTop = Integer.toString(mSelectedColor);
            }

        });

        Intent intent1 = getIntent();
        Id = intent1.getStringExtra("Id");
        TeamManager_TeamMake_Layout_Maker.setVisibility(View.VISIBLE);
        TeamManager_TeamMake_Layout_OverLap.setVisibility(View.GONE);
        adspin1 = ArrayAdapter.createFromResource(Navigation_TeamManager_TeamMake1.this, R.array.spinner_do, R.layout.zfile_spinner_test);
        adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TeamManager_TeamMake_Spinner_Address_Do.setAdapter(adspin1);
        TeamManager_TeamMake_Spinner_Address_Do.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (adspin1.getItem(i).equals("서울")) {
                    Str_TeamManager_TeamMake_EditText_TeamAddress_do = "서울";
                    //두번째 스피너 이벤트
                    adspin2 = ArrayAdapter.createFromResource(Navigation_TeamManager_TeamMake1.this, R.array.spinner_do_seoul, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    TeamManager_TeamMake_Spinner_Address_Se.setAdapter(adspin2);
                    TeamManager_TeamMake_Spinner_Address_Se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            Str_TeamManager_TeamMake_EditText_TeamAddress_se = adspin2.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                } else if (adspin1.getItem(i).equals("인천")) {
                    Str_TeamManager_TeamMake_EditText_TeamAddress_do = "인천";
                    adspin2 = ArrayAdapter.createFromResource(Navigation_TeamManager_TeamMake1.this
                            , R.array.spinner_do_incheon, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    TeamManager_TeamMake_Spinner_Address_Se.setAdapter(adspin2);
                    TeamManager_TeamMake_Spinner_Address_Se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            Str_TeamManager_TeamMake_EditText_TeamAddress_se = adspin2.getItem(i).toString();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
                else if (adspin1.getItem(i).equals("광주")) {
                    Str_TeamManager_TeamMake_EditText_TeamAddress_do = "광주";
                    adspin2 = ArrayAdapter.createFromResource(Navigation_TeamManager_TeamMake1.this
                            , R.array.spinner_do_gwangju, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    TeamManager_TeamMake_Spinner_Address_Se.setAdapter(adspin2);
                    TeamManager_TeamMake_Spinner_Address_Se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            Str_TeamManager_TeamMake_EditText_TeamAddress_se = adspin2.getItem(i).toString();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
                else if (adspin1.getItem(i).equals("대구")) {
                    Str_TeamManager_TeamMake_EditText_TeamAddress_do = "대구";
                    adspin2 = ArrayAdapter.createFromResource(Navigation_TeamManager_TeamMake1.this
                            , R.array.spinner_do_DaeGu, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    TeamManager_TeamMake_Spinner_Address_Se.setAdapter(adspin2);
                    TeamManager_TeamMake_Spinner_Address_Se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            Str_TeamManager_TeamMake_EditText_TeamAddress_se = adspin2.getItem(i).toString();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
                else if (adspin1.getItem(i).equals("울산")) {
                    Str_TeamManager_TeamMake_EditText_TeamAddress_do = "울산";
                    adspin2 = ArrayAdapter.createFromResource(Navigation_TeamManager_TeamMake1.this
                            , R.array.spinner_do_Ulsan, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    TeamManager_TeamMake_Spinner_Address_Se.setAdapter(adspin2);
                    TeamManager_TeamMake_Spinner_Address_Se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            Str_TeamManager_TeamMake_EditText_TeamAddress_se = adspin2.getItem(i).toString();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
                else if (adspin1.getItem(i).equals("대전")) {
                    Str_TeamManager_TeamMake_EditText_TeamAddress_do = "대전";
                    adspin2 = ArrayAdapter.createFromResource(Navigation_TeamManager_TeamMake1.this
                            , R.array.spinner_do_DaeJeon, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    TeamManager_TeamMake_Spinner_Address_Se.setAdapter(adspin2);
                    TeamManager_TeamMake_Spinner_Address_Se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            Str_TeamManager_TeamMake_EditText_TeamAddress_se = adspin2.getItem(i).toString();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
                else if (adspin1.getItem(i).equals("부산")) {
                    Str_TeamManager_TeamMake_EditText_TeamAddress_do = "부산";
                    adspin2 = ArrayAdapter.createFromResource(Navigation_TeamManager_TeamMake1.this
                            , R.array.spinner_do_Busan, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    TeamManager_TeamMake_Spinner_Address_Se.setAdapter(adspin2);
                    TeamManager_TeamMake_Spinner_Address_Se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            Str_TeamManager_TeamMake_EditText_TeamAddress_se = adspin2.getItem(i).toString();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
                else if (adspin1.getItem(i).equals("강원도")) {
                    Str_TeamManager_TeamMake_EditText_TeamAddress_do = "강원도";
                    adspin2 = ArrayAdapter.createFromResource(Navigation_TeamManager_TeamMake1.this
                            , R.array.spinner_do_Gangwondo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    TeamManager_TeamMake_Spinner_Address_Se.setAdapter(adspin2);
                    TeamManager_TeamMake_Spinner_Address_Se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            Str_TeamManager_TeamMake_EditText_TeamAddress_se = adspin2.getItem(i).toString();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
                else if (adspin1.getItem(i).equals("경기도")) {
                    Str_TeamManager_TeamMake_EditText_TeamAddress_do = "경기도";
                    adspin2 = ArrayAdapter.createFromResource(Navigation_TeamManager_TeamMake1.this
                            , R.array.spinner_do_Gyeonggido, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    TeamManager_TeamMake_Spinner_Address_Se.setAdapter(adspin2);
                    TeamManager_TeamMake_Spinner_Address_Se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            Str_TeamManager_TeamMake_EditText_TeamAddress_se = adspin2.getItem(i).toString();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
                else if (adspin1.getItem(i).equals("충청북도")) {
                    Str_TeamManager_TeamMake_EditText_TeamAddress_do = "충청북도";
                    adspin2 = ArrayAdapter.createFromResource(Navigation_TeamManager_TeamMake1.this
                            , R.array.spinner_do_Chungcheongbukdo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    TeamManager_TeamMake_Spinner_Address_Se.setAdapter(adspin2);
                    TeamManager_TeamMake_Spinner_Address_Se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            Str_TeamManager_TeamMake_EditText_TeamAddress_se = adspin2.getItem(i).toString();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
                else if (adspin1.getItem(i).equals("충청남도")) {
                    Str_TeamManager_TeamMake_EditText_TeamAddress_do = "충청남도";
                    adspin2 = ArrayAdapter.createFromResource(Navigation_TeamManager_TeamMake1.this
                            , R.array.spinner_do_Chungcheongnamdo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    TeamManager_TeamMake_Spinner_Address_Se.setAdapter(adspin2);
                    TeamManager_TeamMake_Spinner_Address_Se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            Str_TeamManager_TeamMake_EditText_TeamAddress_se = adspin2.getItem(i).toString();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
                else if (adspin1.getItem(i).equals("전라북도")) {
                    Str_TeamManager_TeamMake_EditText_TeamAddress_do = "전라북도";
                    adspin2 = ArrayAdapter.createFromResource(Navigation_TeamManager_TeamMake1.this
                            , R.array.spinner_do_Jeolabukdo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    TeamManager_TeamMake_Spinner_Address_Se.setAdapter(adspin2);
                    TeamManager_TeamMake_Spinner_Address_Se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            Str_TeamManager_TeamMake_EditText_TeamAddress_se = adspin2.getItem(i).toString();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
                else if (adspin1.getItem(i).equals("전라남도")) {
                    Str_TeamManager_TeamMake_EditText_TeamAddress_do = "전라남도";
                    adspin2 = ArrayAdapter.createFromResource(Navigation_TeamManager_TeamMake1.this
                            , R.array.spinner_do_Jeolanamdo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    TeamManager_TeamMake_Spinner_Address_Se.setAdapter(adspin2);
                    TeamManager_TeamMake_Spinner_Address_Se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            Str_TeamManager_TeamMake_EditText_TeamAddress_se = adspin2.getItem(i).toString();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
                else if (adspin1.getItem(i).equals("경상북도")) {
                    Str_TeamManager_TeamMake_EditText_TeamAddress_do = "경상북도";
                    adspin2 = ArrayAdapter.createFromResource(Navigation_TeamManager_TeamMake1.this
                            , R.array.spinner_do_Gyeongsangbukdo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    TeamManager_TeamMake_Spinner_Address_Se.setAdapter(adspin2);
                    TeamManager_TeamMake_Spinner_Address_Se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            Str_TeamManager_TeamMake_EditText_TeamAddress_se = adspin2.getItem(i).toString();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
                else if (adspin1.getItem(i).equals("경상남도")) {
                    Str_TeamManager_TeamMake_EditText_TeamAddress_do = "경상남도";
                    adspin2 = ArrayAdapter.createFromResource(Navigation_TeamManager_TeamMake1.this
                            , R.array.spinner_do_Gyeongsangnamdo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    TeamManager_TeamMake_Spinner_Address_Se.setAdapter(adspin2);
                    TeamManager_TeamMake_Spinner_Address_Se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            Str_TeamManager_TeamMake_EditText_TeamAddress_se = adspin2.getItem(i).toString();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
                else if (adspin1.getItem(i).equals("제주도")) {
                    Str_TeamManager_TeamMake_EditText_TeamAddress_do = "제주도";
                    adspin2 = ArrayAdapter.createFromResource(Navigation_TeamManager_TeamMake1.this
                            , R.array.spinner_do_Jejudo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    TeamManager_TeamMake_Spinner_Address_Se.setAdapter(adspin2);
                    TeamManager_TeamMake_Spinner_Address_Se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            Str_TeamManager_TeamMake_EditText_TeamAddress_se = adspin2.getItem(i).toString();
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
        TeamManger_TeamMake_Button_UniformColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show(getFragmentManager(), "color_dialog_test");
            }
        });
        TeamManager_TeamMake1_Button_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Str_TeamManager_TeamMake_EditText_TeamName = TeamManager_TeamMake_EditText_TeamName.getText().toString();
                Str_TeamManager_TeamMake_EditText_HomeCourt = TeamManager_TeamMake_EditText_HomeCourt.getText().toString();
                Str_TeamManager_TeamMake_EditText_Time = TeamManager_TeamMake_EditText_Time.getText().toString();
                Str_TeamManager_TeamMake_EditText_TeamIntro = TeamManager_TeamMake_EditText_TeamIntro.getText().toString();
                if(Str_TeamManager_TeamMake_EditText_TeamName.equals(""))
                {
                    Snackbar.make(view,"팀명을 입력해주세요.",Snackbar.LENGTH_SHORT).show();
                }
                else{
                    //팀명 중복검사
                    String result="";
                    try {
                        HttpClient client = new DefaultHttpClient();
                        String postURL = "http://210.122.7.193:8080/Web_basket/Navi_Teamanager_TeamMake.jsp";
                        HttpPost post = new HttpPost(postURL);

                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("TeamName", Str_TeamManager_TeamMake_EditText_TeamName));

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
                    parsedData_TeamName = jsonParserList(result);
                    if(Str_TeamManager_TeamMake_EditText_TeamAddress_se.equals("전 체")) {
                        Snackbar.make(view, "지역을 선택해주세요.", Snackbar.LENGTH_SHORT).show();
                    }
                    else{
                        if(parsedData_TeamName[0][0].equals("succed")){
                          /*  Intent intent1 = new Intent(getApplicationContext(), Navigation_TeamManager_TeamMake2.class);
                            intent1.putExtra("Id", Id);
                            intent1.putExtra("TeamName", Str_TeamManager_TeamMake_EditText_TeamName);
                            intent1.putExtra("TeamAddress_do", Str_TeamManager_TeamMake_EditText_TeamAddress_do);
                            intent1.putExtra("TeamAddress_se", Str_TeamManager_TeamMake_EditText_TeamAddress_se);
                            intent1.putExtra("HomeCourt", Str_TeamManager_TeamMake_EditText_HomeCourt);
                            intent1.putExtra("Time", Str_TeamManager_TeamMake_EditText_Time);
                            intent1.putExtra("TeamIntro", Str_TeamManager_TeamMake_EditText_TeamIntro);
                            startActivity(intent1);
                            finish();*/
                            String TeamName =  Str_TeamManager_TeamMake_EditText_TeamName;
                            String TeamAddress_do = Str_TeamManager_TeamMake_EditText_TeamAddress_do;
                            String TeamAddress_se = Str_TeamManager_TeamMake_EditText_TeamAddress_se;
                            String HomeCourt = Str_TeamManager_TeamMake_EditText_HomeCourt;
                            String Time = Str_TeamManager_TeamMake_EditText_Time;
                            String TeamIntro = Str_TeamManager_TeamMake_EditText_TeamIntro;
                            try {
                                HttpClient client = new DefaultHttpClient();
                                String postURL = "http://210.122.7.193:8080/Web_basket/TeamMake.jsp";
                                HttpPost post = new HttpPost(postURL);

                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                params.add(new BasicNameValuePair("Id", Id));
                                params.add(new BasicNameValuePair("TeamName", TeamName));
                                params.add(new BasicNameValuePair("TeamAddress_do", TeamAddress_do));
                                params.add(new BasicNameValuePair("TeamAddress_se", TeamAddress_se));
                                params.add(new BasicNameValuePair("HomeCourt", HomeCourt));
                                params.add(new BasicNameValuePair("Time", Time));
                                params.add(new BasicNameValuePair("TeamIntro", TeamIntro));
                                params.add(new BasicNameValuePair("UniformTop", UniformTop));
                                params.add(new BasicNameValuePair("UniformBottom", "no"));

                                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                post.setEntity(ent);

                                HttpResponse response = client.execute(post);
                                BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));

                                String line = null;
                                String result1 = "";
                                while ((line = bufreader.readLine()) != null) {
                                    result1 += line;
                                }
                                String[][] parsedData = jsonParserList(result1);
                                if (parsedData[0][0].equals("succed")) {
                                    Intent intent2 = new Intent(getApplicationContext(),Navigation_TeamManager_TeamMake3.class);
                                    startActivity(intent2);
                                    finish();
                                } else {

                                    Snackbar.make(view,"팀명을 입력해주세요.",Snackbar.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Snackbar.make(view,"잠시 후 다시 시도해주세요.",Snackbar.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Snackbar.make(view,"중복된 팀 이름입니다.",Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
    public String[][] jsonParserList(String pRecvServerPage){
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