package com.mysports.basketbook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.mysports.basketbook.R;

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
 * Created by park on 2016-06-13.
 */
public class Navigation_TeamIntro extends Activity{
    Spinner TeamIntro_Spinner_Do,TeamIntro_Spinner_Se;
    Button TeamIntro_Spinner_Search;
    ListView TeamIntro_ListView;
    ArrayAdapter<CharSequence> adspin1, adspin2;
    String choice_do, choice_se;
    String[][] parsedData;
    Navigation_TeamIntro_CustomList_MyAdapter navi_TeamIntro_CustomList_MyAdapter;
    ArrayList<Navigation_TeamIntro_CustomList_MyData> navi_TeamIntro_CustomList_MyData;
    String Id;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_navigation_teamintro);

        TeamIntro_Spinner_Do = (Spinner)findViewById(R.id.TeamIntro_Spinner_Do);
        TeamIntro_Spinner_Se = (Spinner)findViewById(R.id.TeamIntro_Spinner_Se);
        TeamIntro_Spinner_Search = (Button)findViewById(R.id.TeamIntro_Spinner_Search);
        TeamIntro_ListView = (ListView)findViewById(R.id.TeamIntro_ListView);
        Intent intent1 = getIntent();
        Id = intent1.getStringExtra("Id");
        adspin1 = ArrayAdapter.createFromResource(Navigation_TeamIntro.this, R.array.spinner_do, R.layout.zfile_spinner_test);
        adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TeamIntro_Spinner_Do.setAdapter(adspin1);
        TeamIntro_Spinner_Do.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (adspin1.getItem(i).equals("서울")) {
                    choice_do = "서울";
                    //두번째 스피너 이벤트
                    adspin2 = ArrayAdapter.createFromResource(Navigation_TeamIntro.this, R.array.spinner_do_seoul, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    TeamIntro_Spinner_Se.setAdapter(adspin2);
                    TeamIntro_Spinner_Se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                    adspin2 = ArrayAdapter.createFromResource(Navigation_TeamIntro.this
                            , R.array.spinner_do_incheon, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    TeamIntro_Spinner_Se.setAdapter(adspin2);
                    TeamIntro_Spinner_Se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            choice_se = adspin2.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }else if (adspin1.getItem(i).equals("광주")) {
                    choice_do = "광주";
                    adspin2 = ArrayAdapter.createFromResource(Navigation_TeamIntro.this
                            , R.array.spinner_do_gwangju, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    TeamIntro_Spinner_Se.setAdapter(adspin2);
                    TeamIntro_Spinner_Se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            choice_se = adspin2.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }else if (adspin1.getItem(i).equals("대구")) {
                    choice_do = "대구";
                    adspin2 = ArrayAdapter.createFromResource(Navigation_TeamIntro.this
                            , R.array.spinner_do_DaeGu, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    TeamIntro_Spinner_Se.setAdapter(adspin2);
                    TeamIntro_Spinner_Se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            choice_se = adspin2.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }else if (adspin1.getItem(i).equals("울산")) {
                    choice_do = "울산";
                    adspin2 = ArrayAdapter.createFromResource(Navigation_TeamIntro.this
                            , R.array.spinner_do_Ulsan, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    TeamIntro_Spinner_Se.setAdapter(adspin2);
                    TeamIntro_Spinner_Se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            choice_se = adspin2.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }else if (adspin1.getItem(i).equals("대전")) {
                    choice_do = "대전";
                    adspin2 = ArrayAdapter.createFromResource(Navigation_TeamIntro.this
                            , R.array.spinner_do_DaeJeon, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    TeamIntro_Spinner_Se.setAdapter(adspin2);
                    TeamIntro_Spinner_Se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            choice_se = adspin2.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }else if (adspin1.getItem(i).equals("부산")) {
                    choice_do = "부산";
                    adspin2 = ArrayAdapter.createFromResource(Navigation_TeamIntro.this
                            , R.array.spinner_do_Busan, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    TeamIntro_Spinner_Se.setAdapter(adspin2);
                    TeamIntro_Spinner_Se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            choice_se = adspin2.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }else if (adspin1.getItem(i).equals("강원도")) {
                    choice_do = "강원도";
                    adspin2 = ArrayAdapter.createFromResource(Navigation_TeamIntro.this
                            , R.array.spinner_do_Gangwondo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    TeamIntro_Spinner_Se.setAdapter(adspin2);
                    TeamIntro_Spinner_Se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            choice_se = adspin2.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }else if (adspin1.getItem(i).equals("경기도")) {
                    choice_do = "경기도";
                    adspin2 = ArrayAdapter.createFromResource(Navigation_TeamIntro.this
                            , R.array.spinner_do_Gyeonggido, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    TeamIntro_Spinner_Se.setAdapter(adspin2);
                    TeamIntro_Spinner_Se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            choice_se = adspin2.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }else if (adspin1.getItem(i).equals("충청북도")) {
                    choice_do = "충청북도";
                    adspin2 = ArrayAdapter.createFromResource(Navigation_TeamIntro.this
                            , R.array.spinner_do_Chungcheongbukdo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    TeamIntro_Spinner_Se.setAdapter(adspin2);
                    TeamIntro_Spinner_Se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            choice_se = adspin2.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }else if (adspin1.getItem(i).equals("충청남도")) {
                    choice_do = "충청남도";
                    adspin2 = ArrayAdapter.createFromResource(Navigation_TeamIntro.this
                            , R.array.spinner_do_Chungcheongnamdo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    TeamIntro_Spinner_Se.setAdapter(adspin2);
                    TeamIntro_Spinner_Se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            choice_se = adspin2.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }else if (adspin1.getItem(i).equals("전라북도")) {
                    choice_do = "전라북도";
                    adspin2 = ArrayAdapter.createFromResource(Navigation_TeamIntro.this
                            , R.array.spinner_do_Jeolabukdo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    TeamIntro_Spinner_Se.setAdapter(adspin2);
                    TeamIntro_Spinner_Se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            choice_se = adspin2.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }else if (adspin1.getItem(i).equals("전라남도")) {
                    choice_do = "전라남도";
                    adspin2 = ArrayAdapter.createFromResource(Navigation_TeamIntro.this
                            , R.array.spinner_do_Jeolanamdo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    TeamIntro_Spinner_Se.setAdapter(adspin2);
                    TeamIntro_Spinner_Se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            choice_se = adspin2.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }else if (adspin1.getItem(i).equals("경상북도")) {
                    choice_do = "경상북도";
                    adspin2 = ArrayAdapter.createFromResource(Navigation_TeamIntro.this
                            , R.array.spinner_do_Gyeongsangbukdo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    TeamIntro_Spinner_Se.setAdapter(adspin2);
                    TeamIntro_Spinner_Se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            choice_se = adspin2.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }else if (adspin1.getItem(i).equals("경상남도")) {
                    choice_do = "경상남도";
                    adspin2 = ArrayAdapter.createFromResource(Navigation_TeamIntro.this
                            , R.array.spinner_do_Gyeongsangnamdo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    TeamIntro_Spinner_Se.setAdapter(adspin2);
                    TeamIntro_Spinner_Se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            choice_se = adspin2.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }else if (adspin1.getItem(i).equals("제주도")) {
                    choice_do = "제주도";
                    adspin2 = ArrayAdapter.createFromResource(Navigation_TeamIntro.this
                            , R.array.spinner_do_Jejudo, R.layout.zfile_spinner_test);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    TeamIntro_Spinner_Se.setAdapter(adspin2);
                    TeamIntro_Spinner_Se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        TeamIntro_Spinner_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    HttpClient client = new DefaultHttpClient();
                    String postURL = "http://210.122.7.193:8080/Web_basket/Navi_TeamIntro.jsp";
                    HttpPost post = new HttpPost(postURL);

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("do", choice_do));
                    params.add(new BasicNameValuePair("se", choice_se));

                    UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                    post.setEntity(ent);

                    HttpResponse response = client.execute(post);
                    BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));

                    String line = null;
                    String result = "";
                    while ((line = bufreader.readLine()) != null) {
                        result += line;
                    }
                    parsedData = jsonParserList(result);
                    setData();
                    navi_TeamIntro_CustomList_MyAdapter = new Navigation_TeamIntro_CustomList_MyAdapter(Navigation_TeamIntro.this, navi_TeamIntro_CustomList_MyData);
                    TeamIntro_ListView.setAdapter(navi_TeamIntro_CustomList_MyAdapter);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
    private void setData()
    {
        navi_TeamIntro_CustomList_MyData = new ArrayList<Navigation_TeamIntro_CustomList_MyData>();
        for(int i =0; i<parsedData.length; i++) {
            navi_TeamIntro_CustomList_MyData.add(new Navigation_TeamIntro_CustomList_MyData(parsedData[i][0],parsedData[i][1],Id));
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    /////매칭 탭 - out : 받아온 json 파싱합니다.//////////////////////////////////////////////////////////
    public String[][] jsonParserList(String pRecvServerPage){
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
