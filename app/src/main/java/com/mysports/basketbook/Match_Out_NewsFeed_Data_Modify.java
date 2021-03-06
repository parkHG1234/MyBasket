package com.mysports.basketbook;

/**
 * Created by 박효근 on 2016-07-22.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by 박지훈 on 2016-07-06.
 */
public class Match_Out_NewsFeed_Data_Modify extends Activity {

    View view;

    LinearLayout Layout_Match_Out_NewsFeed_Writing_root;
    EditText NewsFeed_Writing_TextEditText;
    ImageView NewsFeed_Camera_Image;
    Button NewsFeed_Writing_Button;
    Button NewsFeed_Writing_cancelButton;
    Button NewsFeed_Writing_Button_CourtModify;
    Button NewsFeed_Writing_CameraButton;
    TextView NewsFeed_Writing_TextVIew_Court;
    TextView NewsFeed_Writing_TextView_Name;


    Intent dataIntent;
    ArrayAdapter<CharSequence> adspin1, adspin2, adspin3;
    ArrayList arr;
    String[][] parsedData;
    static int spinnum1, spinnum2;
    static String Do, Si, Court, ImageURL = null, ImageFile = null;

    private static boolean flag = false;
    Intent CameraIntent = null;
    String str;
    Boolean enabled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_match_out_newsfeed_writing);

        if (android.os.Build.VERSION.SDK_INT > 9) { //oncreate 에서 바로 쓰레드돌릴려고 임시방편으로 넣어둔소스
            policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Layout_Match_Out_NewsFeed_Writing_root = (LinearLayout)findViewById(R.id.Layout_Match_Out_NewsFeed_Writing_root);
        NewsFeed_Writing_TextView_Name = (TextView) findViewById(R.id.NewsFeed_Writing_TextView_Name);
        NewsFeed_Writing_TextVIew_Court = (TextView) findViewById(R.id.NewsFeed_Writing_TextVIew_Court);
        NewsFeed_Camera_Image = (ImageView) findViewById(R.id.NewsFeed_Camera_Image);
        NewsFeed_Writing_CameraButton = (Button) findViewById(R.id.NewsFeed_Writing_CameraButton);
        NewsFeed_Writing_TextEditText = (EditText) findViewById(R.id.NewsFeed_Writing_TextEditText);
        NewsFeed_Writing_Button = (Button) findViewById(R.id.NewsFeed_Writing_Button);
        NewsFeed_Writing_cancelButton = (Button) findViewById(R.id.NewsFeed_Writing_cancelButton);
        NewsFeed_Writing_Button_CourtModify = (Button) findViewById(R.id.NewsFeed_Writing_Button_CourtModify);


        final Intent DataIntent = getIntent();
        NewsFeed_Writing_TextView_Name.setText(DataIntent.getExtras().getString("User"));
        NewsFeed_Writing_TextEditText.setText(DataIntent.getExtras().getString("data"));
        NewsFeed_Writing_TextVIew_Court.setText(DataIntent.getExtras().getString("court"));
        Do = DataIntent.getExtras().getString("Do");
        Si = DataIntent.getExtras().getString("Si");
        Court = DataIntent.getExtras().getString("court");


        NewsFeed_Writing_Button.setTextColor(Color.BLACK);
        NewsFeed_Writing_Button.setEnabled(true);


        Layout_Match_Out_NewsFeed_Writing_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(NewsFeed_Writing_TextEditText.getWindowToken(), 0);
            }
        });

        try {
            if (DataIntent.getExtras().getString("Image").equals("")) {
                NewsFeed_Camera_Image.setVisibility(View.GONE);
            } else {
                NewsFeed_Camera_Image.setVisibility(View.VISIBLE);
                flag = true;
                ImageFile = DataIntent.getExtras().getString("Image");
                String En_Profile = URLEncoder.encode(DataIntent.getExtras().getString("Image"), "utf-8");
                Glide.with(getApplicationContext()).load("http://210.122.7.193:8080/gg/imgs1/" + DataIntent.getExtras().getString("Image") + ".jpg").into(NewsFeed_Camera_Image);
                ImageURL = String.valueOf("http://210.122.7.193:8080/gg/imgs1/" + DataIntent.getExtras().getString("Image") + ".jpg");
            }
        } catch (UnsupportedEncodingException e) {
        }
        adspin1 = ArrayAdapter.createFromResource(Match_Out_NewsFeed_Data_Modify.this, R.array.spinner_do, R.layout.zfile_spinner_test);
        adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        NewsFeed_Writing_TextEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view = v;
            }
        });

        NewsFeed_Writing_TextEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (NewsFeed_Writing_TextEditText.getText().length() >= 1) {
                    NewsFeed_Writing_Button.setTextColor(Color.BLACK);
                    enabled = true;
                } else if (NewsFeed_Writing_TextEditText.getText().length() <= 0) {
                    NewsFeed_Writing_Button.setTextColor(Color.GRAY);
                    enabled = false;
                }
                if(NewsFeed_Writing_TextEditText.getText().toString().length()>200){
                    str = NewsFeed_Writing_TextEditText.getText().toString();
                    NewsFeed_Writing_TextEditText.setText(str.substring(0,199));
                    Snackbar.make(view, "200자안으로 작성해주세요.", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        final View layout = getLayoutInflater().inflate(R.layout.layout_customdialog_courtchoice, (ViewGroup) findViewById(R.id.Layout_CustomDialog_CourtChoice_Root));
        final Spinner Layout_CustomDialog_CourtChoice_Do = (Spinner) layout.findViewById(R.id.Layout_CustomDialog_CourtChoice_Do);
        final Spinner Layout_CustomDialog_CourtChoice_Se = (Spinner) layout.findViewById(R.id.Layout_CustomDialog_CourtChoice_Se);
        final Spinner Layout_CustomDialog_CourtChoice_Court = (Spinner) layout.findViewById(R.id.Layout_CustomDialog_CourtChoice_Court);

        final MaterialDialog TeamPlayerDialog = new MaterialDialog(Match_Out_NewsFeed_Data_Modify.this);
        TeamPlayerDialog
                .setTitle("코트 선택")
                .setView(layout)
                .setPositiveButton("선택 완료", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        view =v;
                        if (Court.equals("코트 선택")) {
                            Snackbar.make(v, "코트를 선택해주세요", Snackbar.LENGTH_SHORT).show();
                        } else {
                            TeamPlayerDialog.dismiss();
                        }
                    }
                });
        NewsFeed_Writing_Button_CourtModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view =v;
                TeamPlayerDialog
                        .setTitle("코트 선택")
                        .setView(layout)
                        .setPositiveButton("선택 완료", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (Court.equals("코트 선택")) {
                                    Snackbar.make(view, "코트를 선택해주세요", Snackbar.LENGTH_SHORT).show();
                                } else {
                                    TeamPlayerDialog.dismiss();
                                }
                            }
                        });
                TeamPlayerDialog.show();
            }
        });

        Layout_CustomDialog_CourtChoice_Do.setAdapter(adspin1);
        Layout_CustomDialog_CourtChoice_Do.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        spinnum1 = i;
                        Do = adspin1.getItem(i).toString();
                        if (adspin1.getItem(i).equals("서울")) {
                            adspin2 = ArrayAdapter.createFromResource(Match_Out_NewsFeed_Data_Modify.this, R.array.spinner_do_seoul, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Layout_CustomDialog_CourtChoice_Se.setAdapter(adspin2);
                            Layout_CustomDialog_CourtChoice_Se.setOnItemSelectedListener(
                                    new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            spinnum2 = i;
                                            Si = adspin2.getItem(i).toString();
                                            String result = "";
                                            try {
                                                HttpClient client = new DefaultHttpClient();
                                                String postURL = "http://210.122.7.193:8080/gg/CourtInformation.jsp";
                                                HttpPost post = new HttpPost(postURL);
                                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                                params.add(new BasicNameValuePair("NewsFeed_Do", (String) adspin1.getItem(spinnum1)));
                                                params.add(new BasicNameValuePair("NewsFeed_Si", (String) adspin2.getItem(spinnum2)));
                                                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                                post.setEntity(ent);
                                                HttpResponse response = client.execute(post);
                                                BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                                String line = null;
                                                while ((line = bufreader.readLine()) != null) {
                                                    result += line;
                                                }
                                                parsedData = jsonParserList(result);
                                                arr = new ArrayList();
                                                arr.add("코트 선택");
                                                for (int a = 0; a < parsedData.length; a++) {
                                                    arr.add(parsedData[a][0]);
                                                }
                                                adspin3 = new ArrayAdapter<CharSequence>(Match_Out_NewsFeed_Data_Modify.this, android.R.layout.simple_spinner_item, arr);
                                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                Layout_CustomDialog_CourtChoice_Court.setAdapter(adspin3);
                                                Layout_CustomDialog_CourtChoice_Court.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                        Court = adspin3.getItem(i).toString();
                                                        NewsFeed_Writing_TextVIew_Court.setText(Court);
                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                                    }
                                                });
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {
                                        }
                                    }
                            );
                        } else if (adspin1.getItem(i).equals("인천")) {
                            adspin2 = ArrayAdapter.createFromResource(Match_Out_NewsFeed_Data_Modify.this, R.array.spinner_do_incheon, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Layout_CustomDialog_CourtChoice_Se.setAdapter(adspin2);
                            Layout_CustomDialog_CourtChoice_Se.setOnItemSelectedListener(
                                    new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            spinnum2 = i;
                                            Si = adspin2.getItem(i).toString();
                                            String result = "";
                                            try {
                                                HttpClient client = new DefaultHttpClient();
                                                String postURL = "http://210.122.7.193:8080/gg/CourtInformation.jsp";
                                                HttpPost post = new HttpPost(postURL);
                                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                                params.add(new BasicNameValuePair("NewsFeed_Do", (String) adspin1.getItem(spinnum1)));
                                                params.add(new BasicNameValuePair("NewsFeed_Si", (String) adspin2.getItem(spinnum2)));
                                                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                                post.setEntity(ent);
                                                HttpResponse response = client.execute(post);
                                                BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                                String line = null;
                                                while ((line = bufreader.readLine()) != null) {
                                                    result += line;
                                                }
                                                parsedData = jsonParserList(result);
                                                arr = new ArrayList();
                                                arr.add("코트 선택");
                                                for (int a = 0; a < parsedData.length; a++) {
                                                    arr.add(parsedData[a][0]);
                                                }
                                                adspin3 = new ArrayAdapter<CharSequence>(Match_Out_NewsFeed_Data_Modify.this, android.R.layout.simple_spinner_item, arr);
                                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                Layout_CustomDialog_CourtChoice_Court.setAdapter(adspin3);
                                                Layout_CustomDialog_CourtChoice_Court.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                        Court = adspin3.getItem(i).toString();
                                                        NewsFeed_Writing_TextVIew_Court.setText(Court);
                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                                    }
                                                });
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {
                                        }
                                    }
                            );
                        } else if (adspin1.getItem(i).equals("광주")) {
                            adspin2 = ArrayAdapter.createFromResource(Match_Out_NewsFeed_Data_Modify.this, R.array.spinner_do_gwangju, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Layout_CustomDialog_CourtChoice_Se.setAdapter(adspin2);
                            Layout_CustomDialog_CourtChoice_Se.setOnItemSelectedListener(
                                    new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            spinnum2 = i;
                                            Si = adspin2.getItem(i).toString();
                                            String result = "";
                                            try {
                                                HttpClient client = new DefaultHttpClient();
                                                String postURL = "http://210.122.7.193:8080/gg/CourtInformation.jsp";
                                                HttpPost post = new HttpPost(postURL);
                                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                                params.add(new BasicNameValuePair("NewsFeed_Do", (String) adspin1.getItem(spinnum1)));
                                                params.add(new BasicNameValuePair("NewsFeed_Si", (String) adspin2.getItem(spinnum2)));
                                                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                                post.setEntity(ent);
                                                HttpResponse response = client.execute(post);
                                                BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                                String line = null;
                                                while ((line = bufreader.readLine()) != null) {
                                                    result += line;
                                                }
                                                parsedData = jsonParserList(result);
                                                arr = new ArrayList();
                                                arr.add("코트 선택");
                                                for (int a = 0; a < parsedData.length; a++) {
                                                    arr.add(parsedData[a][0]);
                                                }
                                                adspin3 = new ArrayAdapter<CharSequence>(Match_Out_NewsFeed_Data_Modify.this, android.R.layout.simple_spinner_item, arr);
                                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                Layout_CustomDialog_CourtChoice_Court.setAdapter(adspin3);
                                                Layout_CustomDialog_CourtChoice_Court.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                        Court = adspin3.getItem(i).toString();
                                                        NewsFeed_Writing_TextVIew_Court.setText(Court);
                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                                    }
                                                });
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {
                                        }
                                    }
                            );
                        }else if (adspin1.getItem(i).equals("대구")) {
                            adspin2 = ArrayAdapter.createFromResource(Match_Out_NewsFeed_Data_Modify.this, R.array.spinner_do_DaeGu, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Layout_CustomDialog_CourtChoice_Se.setAdapter(adspin2);
                            Layout_CustomDialog_CourtChoice_Se.setOnItemSelectedListener(
                                    new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            spinnum2 = i;
                                            Si = adspin2.getItem(i).toString();
                                            String result = "";
                                            try {
                                                HttpClient client = new DefaultHttpClient();
                                                String postURL = "http://210.122.7.193:8080/gg/CourtInformation.jsp";
                                                HttpPost post = new HttpPost(postURL);
                                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                                params.add(new BasicNameValuePair("NewsFeed_Do", (String) adspin1.getItem(spinnum1)));
                                                params.add(new BasicNameValuePair("NewsFeed_Si", (String) adspin2.getItem(spinnum2)));
                                                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                                post.setEntity(ent);
                                                HttpResponse response = client.execute(post);
                                                BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                                String line = null;
                                                while ((line = bufreader.readLine()) != null) {
                                                    result += line;
                                                }
                                                parsedData = jsonParserList(result);
                                                arr = new ArrayList();
                                                arr.add("코트 선택");
                                                for (int a = 0; a < parsedData.length; a++) {
                                                    arr.add(parsedData[a][0]);
                                                }
                                                adspin3 = new ArrayAdapter<CharSequence>(Match_Out_NewsFeed_Data_Modify.this, android.R.layout.simple_spinner_item, arr);
                                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                Layout_CustomDialog_CourtChoice_Court.setAdapter(adspin3);
                                                Layout_CustomDialog_CourtChoice_Court.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                        Court = adspin3.getItem(i).toString();
                                                        NewsFeed_Writing_TextVIew_Court.setText(Court);
                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                                    }
                                                });
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {
                                        }
                                    }
                            );
                        }else if (adspin1.getItem(i).equals("울산")) {
                            adspin2 = ArrayAdapter.createFromResource(Match_Out_NewsFeed_Data_Modify.this, R.array.spinner_do_Ulsan, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Layout_CustomDialog_CourtChoice_Se.setAdapter(adspin2);
                            Layout_CustomDialog_CourtChoice_Se.setOnItemSelectedListener(
                                    new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            spinnum2 = i;
                                            Si = adspin2.getItem(i).toString();
                                            String result = "";
                                            try {
                                                HttpClient client = new DefaultHttpClient();
                                                String postURL = "http://210.122.7.193:8080/gg/CourtInformation.jsp";
                                                HttpPost post = new HttpPost(postURL);
                                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                                params.add(new BasicNameValuePair("NewsFeed_Do", (String) adspin1.getItem(spinnum1)));
                                                params.add(new BasicNameValuePair("NewsFeed_Si", (String) adspin2.getItem(spinnum2)));
                                                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                                post.setEntity(ent);
                                                HttpResponse response = client.execute(post);
                                                BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                                String line = null;
                                                while ((line = bufreader.readLine()) != null) {
                                                    result += line;
                                                }
                                                parsedData = jsonParserList(result);
                                                arr = new ArrayList();
                                                arr.add("코트 선택");
                                                for (int a = 0; a < parsedData.length; a++) {
                                                    arr.add(parsedData[a][0]);
                                                }
                                                adspin3 = new ArrayAdapter<CharSequence>(Match_Out_NewsFeed_Data_Modify.this, android.R.layout.simple_spinner_item, arr);
                                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                Layout_CustomDialog_CourtChoice_Court.setAdapter(adspin3);
                                                Layout_CustomDialog_CourtChoice_Court.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                        Court = adspin3.getItem(i).toString();
                                                        NewsFeed_Writing_TextVIew_Court.setText(Court);
                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                                    }
                                                });
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {
                                        }
                                    }
                            );
                        }else if (adspin1.getItem(i).equals("대전")) {
                            adspin2 = ArrayAdapter.createFromResource(Match_Out_NewsFeed_Data_Modify.this, R.array.spinner_do_DaeJeon, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Layout_CustomDialog_CourtChoice_Se.setAdapter(adspin2);
                            Layout_CustomDialog_CourtChoice_Se.setOnItemSelectedListener(
                                    new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            spinnum2 = i;
                                            Si = adspin2.getItem(i).toString();
                                            String result = "";
                                            try {
                                                HttpClient client = new DefaultHttpClient();
                                                String postURL = "http://210.122.7.193:8080/gg/CourtInformation.jsp";
                                                HttpPost post = new HttpPost(postURL);
                                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                                params.add(new BasicNameValuePair("NewsFeed_Do", (String) adspin1.getItem(spinnum1)));
                                                params.add(new BasicNameValuePair("NewsFeed_Si", (String) adspin2.getItem(spinnum2)));
                                                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                                post.setEntity(ent);
                                                HttpResponse response = client.execute(post);
                                                BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                                String line = null;
                                                while ((line = bufreader.readLine()) != null) {
                                                    result += line;
                                                }
                                                parsedData = jsonParserList(result);
                                                arr = new ArrayList();
                                                arr.add("코트 선택");
                                                for (int a = 0; a < parsedData.length; a++) {
                                                    arr.add(parsedData[a][0]);
                                                }
                                                adspin3 = new ArrayAdapter<CharSequence>(Match_Out_NewsFeed_Data_Modify.this, android.R.layout.simple_spinner_item, arr);
                                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                Layout_CustomDialog_CourtChoice_Court.setAdapter(adspin3);
                                                Layout_CustomDialog_CourtChoice_Court.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                        Court = adspin3.getItem(i).toString();
                                                        NewsFeed_Writing_TextVIew_Court.setText(Court);
                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                                    }
                                                });
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {
                                        }
                                    }
                            );
                        }else if (adspin1.getItem(i).equals("부산")) {
                            adspin2 = ArrayAdapter.createFromResource(Match_Out_NewsFeed_Data_Modify.this, R.array.spinner_do_Busan, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Layout_CustomDialog_CourtChoice_Se.setAdapter(adspin2);
                            Layout_CustomDialog_CourtChoice_Se.setOnItemSelectedListener(
                                    new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            spinnum2 = i;
                                            Si = adspin2.getItem(i).toString();
                                            String result = "";
                                            try {
                                                HttpClient client = new DefaultHttpClient();
                                                String postURL = "http://210.122.7.193:8080/gg/CourtInformation.jsp";
                                                HttpPost post = new HttpPost(postURL);
                                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                                params.add(new BasicNameValuePair("NewsFeed_Do", (String) adspin1.getItem(spinnum1)));
                                                params.add(new BasicNameValuePair("NewsFeed_Si", (String) adspin2.getItem(spinnum2)));
                                                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                                post.setEntity(ent);
                                                HttpResponse response = client.execute(post);
                                                BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                                String line = null;
                                                while ((line = bufreader.readLine()) != null) {
                                                    result += line;
                                                }
                                                parsedData = jsonParserList(result);
                                                arr = new ArrayList();
                                                arr.add("코트 선택");
                                                for (int a = 0; a < parsedData.length; a++) {
                                                    arr.add(parsedData[a][0]);
                                                }
                                                adspin3 = new ArrayAdapter<CharSequence>(Match_Out_NewsFeed_Data_Modify.this, android.R.layout.simple_spinner_item, arr);
                                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                Layout_CustomDialog_CourtChoice_Court.setAdapter(adspin3);
                                                Layout_CustomDialog_CourtChoice_Court.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                        Court = adspin3.getItem(i).toString();
                                                        NewsFeed_Writing_TextVIew_Court.setText(Court);
                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                                    }
                                                });
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {
                                        }
                                    }
                            );
                        }else if (adspin1.getItem(i).equals("강원도")) {
                            adspin2 = ArrayAdapter.createFromResource(Match_Out_NewsFeed_Data_Modify.this, R.array.spinner_do_Gangwondo, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Layout_CustomDialog_CourtChoice_Se.setAdapter(adspin2);
                            Layout_CustomDialog_CourtChoice_Se.setOnItemSelectedListener(
                                    new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            spinnum2 = i;
                                            Si = adspin2.getItem(i).toString();
                                            String result = "";
                                            try {
                                                HttpClient client = new DefaultHttpClient();
                                                String postURL = "http://210.122.7.193:8080/gg/CourtInformation.jsp";
                                                HttpPost post = new HttpPost(postURL);
                                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                                params.add(new BasicNameValuePair("NewsFeed_Do", (String) adspin1.getItem(spinnum1)));
                                                params.add(new BasicNameValuePair("NewsFeed_Si", (String) adspin2.getItem(spinnum2)));
                                                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                                post.setEntity(ent);
                                                HttpResponse response = client.execute(post);
                                                BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                                String line = null;
                                                while ((line = bufreader.readLine()) != null) {
                                                    result += line;
                                                }
                                                parsedData = jsonParserList(result);
                                                arr = new ArrayList();
                                                arr.add("코트 선택");
                                                for (int a = 0; a < parsedData.length; a++) {
                                                    arr.add(parsedData[a][0]);
                                                }
                                                adspin3 = new ArrayAdapter<CharSequence>(Match_Out_NewsFeed_Data_Modify.this, android.R.layout.simple_spinner_item, arr);
                                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                Layout_CustomDialog_CourtChoice_Court.setAdapter(adspin3);
                                                Layout_CustomDialog_CourtChoice_Court.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                        Court = adspin3.getItem(i).toString();
                                                        NewsFeed_Writing_TextVIew_Court.setText(Court);
                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                                    }
                                                });
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {
                                        }
                                    }
                            );
                        }else if (adspin1.getItem(i).equals("경기도")) {
                            adspin2 = ArrayAdapter.createFromResource(Match_Out_NewsFeed_Data_Modify.this, R.array.spinner_do_Gyeonggido, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Layout_CustomDialog_CourtChoice_Se.setAdapter(adspin2);
                            Layout_CustomDialog_CourtChoice_Se.setOnItemSelectedListener(
                                    new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            spinnum2 = i;
                                            Si = adspin2.getItem(i).toString();
                                            String result = "";
                                            try {
                                                HttpClient client = new DefaultHttpClient();
                                                String postURL = "http://210.122.7.193:8080/gg/CourtInformation.jsp";
                                                HttpPost post = new HttpPost(postURL);
                                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                                params.add(new BasicNameValuePair("NewsFeed_Do", (String) adspin1.getItem(spinnum1)));
                                                params.add(new BasicNameValuePair("NewsFeed_Si", (String) adspin2.getItem(spinnum2)));
                                                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                                post.setEntity(ent);
                                                HttpResponse response = client.execute(post);
                                                BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                                String line = null;
                                                while ((line = bufreader.readLine()) != null) {
                                                    result += line;
                                                }
                                                parsedData = jsonParserList(result);
                                                arr = new ArrayList();
                                                arr.add("코트 선택");
                                                for (int a = 0; a < parsedData.length; a++) {
                                                    arr.add(parsedData[a][0]);
                                                }
                                                adspin3 = new ArrayAdapter<CharSequence>(Match_Out_NewsFeed_Data_Modify.this, android.R.layout.simple_spinner_item, arr);
                                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                Layout_CustomDialog_CourtChoice_Court.setAdapter(adspin3);
                                                Layout_CustomDialog_CourtChoice_Court.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                        Court = adspin3.getItem(i).toString();
                                                        NewsFeed_Writing_TextVIew_Court.setText(Court);
                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                                    }
                                                });
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {
                                        }
                                    }
                            );
                        }else if (adspin1.getItem(i).equals("충청북도")) {
                            adspin2 = ArrayAdapter.createFromResource(Match_Out_NewsFeed_Data_Modify.this, R.array.spinner_do_Chungcheongbukdo, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Layout_CustomDialog_CourtChoice_Se.setAdapter(adspin2);
                            Layout_CustomDialog_CourtChoice_Se.setOnItemSelectedListener(
                                    new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            spinnum2 = i;
                                            Si = adspin2.getItem(i).toString();
                                            String result = "";
                                            try {
                                                HttpClient client = new DefaultHttpClient();
                                                String postURL = "http://210.122.7.193:8080/gg/CourtInformation.jsp";
                                                HttpPost post = new HttpPost(postURL);
                                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                                params.add(new BasicNameValuePair("NewsFeed_Do", (String) adspin1.getItem(spinnum1)));
                                                params.add(new BasicNameValuePair("NewsFeed_Si", (String) adspin2.getItem(spinnum2)));
                                                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                                post.setEntity(ent);
                                                HttpResponse response = client.execute(post);
                                                BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                                String line = null;
                                                while ((line = bufreader.readLine()) != null) {
                                                    result += line;
                                                }
                                                parsedData = jsonParserList(result);
                                                arr = new ArrayList();
                                                arr.add("코트 선택");
                                                for (int a = 0; a < parsedData.length; a++) {
                                                    arr.add(parsedData[a][0]);
                                                }
                                                adspin3 = new ArrayAdapter<CharSequence>(Match_Out_NewsFeed_Data_Modify.this, android.R.layout.simple_spinner_item, arr);
                                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                Layout_CustomDialog_CourtChoice_Court.setAdapter(adspin3);
                                                Layout_CustomDialog_CourtChoice_Court.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                        Court = adspin3.getItem(i).toString();
                                                        NewsFeed_Writing_TextVIew_Court.setText(Court);
                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                                    }
                                                });
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {
                                        }
                                    }
                            );
                        }else if (adspin1.getItem(i).equals("충청남도")) {
                            adspin2 = ArrayAdapter.createFromResource(Match_Out_NewsFeed_Data_Modify.this, R.array.spinner_do_Chungcheongnamdo, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Layout_CustomDialog_CourtChoice_Se.setAdapter(adspin2);
                            Layout_CustomDialog_CourtChoice_Se.setOnItemSelectedListener(
                                    new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            spinnum2 = i;
                                            Si = adspin2.getItem(i).toString();
                                            String result = "";
                                            try {
                                                HttpClient client = new DefaultHttpClient();
                                                String postURL = "http://210.122.7.193:8080/gg/CourtInformation.jsp";
                                                HttpPost post = new HttpPost(postURL);
                                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                                params.add(new BasicNameValuePair("NewsFeed_Do", (String) adspin1.getItem(spinnum1)));
                                                params.add(new BasicNameValuePair("NewsFeed_Si", (String) adspin2.getItem(spinnum2)));
                                                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                                post.setEntity(ent);
                                                HttpResponse response = client.execute(post);
                                                BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                                String line = null;
                                                while ((line = bufreader.readLine()) != null) {
                                                    result += line;
                                                }
                                                parsedData = jsonParserList(result);
                                                arr = new ArrayList();
                                                arr.add("코트 선택");
                                                for (int a = 0; a < parsedData.length; a++) {
                                                    arr.add(parsedData[a][0]);
                                                }
                                                adspin3 = new ArrayAdapter<CharSequence>(Match_Out_NewsFeed_Data_Modify.this, android.R.layout.simple_spinner_item, arr);
                                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                Layout_CustomDialog_CourtChoice_Court.setAdapter(adspin3);
                                                Layout_CustomDialog_CourtChoice_Court.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                        Court = adspin3.getItem(i).toString();
                                                        NewsFeed_Writing_TextVIew_Court.setText(Court);
                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                                    }
                                                });
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {
                                        }
                                    }
                            );
                        }else if (adspin1.getItem(i).equals("전라북도")) {
                            adspin2 = ArrayAdapter.createFromResource(Match_Out_NewsFeed_Data_Modify.this, R.array.spinner_do_Jeolabukdo, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Layout_CustomDialog_CourtChoice_Se.setAdapter(adspin2);
                            Layout_CustomDialog_CourtChoice_Se.setOnItemSelectedListener(
                                    new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            spinnum2 = i;
                                            Si = adspin2.getItem(i).toString();
                                            String result = "";
                                            try {
                                                HttpClient client = new DefaultHttpClient();
                                                String postURL = "http://210.122.7.193:8080/gg/CourtInformation.jsp";
                                                HttpPost post = new HttpPost(postURL);
                                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                                params.add(new BasicNameValuePair("NewsFeed_Do", (String) adspin1.getItem(spinnum1)));
                                                params.add(new BasicNameValuePair("NewsFeed_Si", (String) adspin2.getItem(spinnum2)));
                                                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                                post.setEntity(ent);
                                                HttpResponse response = client.execute(post);
                                                BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                                String line = null;
                                                while ((line = bufreader.readLine()) != null) {
                                                    result += line;
                                                }
                                                parsedData = jsonParserList(result);
                                                arr = new ArrayList();
                                                arr.add("코트 선택");
                                                for (int a = 0; a < parsedData.length; a++) {
                                                    arr.add(parsedData[a][0]);
                                                }
                                                adspin3 = new ArrayAdapter<CharSequence>(Match_Out_NewsFeed_Data_Modify.this, android.R.layout.simple_spinner_item, arr);
                                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                Layout_CustomDialog_CourtChoice_Court.setAdapter(adspin3);
                                                Layout_CustomDialog_CourtChoice_Court.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                        Court = adspin3.getItem(i).toString();
                                                        NewsFeed_Writing_TextVIew_Court.setText(Court);
                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                                    }
                                                });
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {
                                        }
                                    }
                            );
                        }else if (adspin1.getItem(i).equals("전라남도")) {
                            adspin2 = ArrayAdapter.createFromResource(Match_Out_NewsFeed_Data_Modify.this, R.array.spinner_do_Jeolanamdo, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Layout_CustomDialog_CourtChoice_Se.setAdapter(adspin2);
                            Layout_CustomDialog_CourtChoice_Se.setOnItemSelectedListener(
                                    new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            spinnum2 = i;
                                            Si = adspin2.getItem(i).toString();
                                            String result = "";
                                            try {
                                                HttpClient client = new DefaultHttpClient();
                                                String postURL = "http://210.122.7.193:8080/gg/CourtInformation.jsp";
                                                HttpPost post = new HttpPost(postURL);
                                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                                params.add(new BasicNameValuePair("NewsFeed_Do", (String) adspin1.getItem(spinnum1)));
                                                params.add(new BasicNameValuePair("NewsFeed_Si", (String) adspin2.getItem(spinnum2)));
                                                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                                post.setEntity(ent);
                                                HttpResponse response = client.execute(post);
                                                BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                                String line = null;
                                                while ((line = bufreader.readLine()) != null) {
                                                    result += line;
                                                }
                                                parsedData = jsonParserList(result);
                                                arr = new ArrayList();
                                                arr.add("코트 선택");
                                                for (int a = 0; a < parsedData.length; a++) {
                                                    arr.add(parsedData[a][0]);
                                                }
                                                adspin3 = new ArrayAdapter<CharSequence>(Match_Out_NewsFeed_Data_Modify.this, android.R.layout.simple_spinner_item, arr);
                                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                Layout_CustomDialog_CourtChoice_Court.setAdapter(adspin3);
                                                Layout_CustomDialog_CourtChoice_Court.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                        Court = adspin3.getItem(i).toString();
                                                        NewsFeed_Writing_TextVIew_Court.setText(Court);
                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                                    }
                                                });
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {
                                        }
                                    }
                            );
                        }else if (adspin1.getItem(i).equals("경상북도")) {
                            adspin2 = ArrayAdapter.createFromResource(Match_Out_NewsFeed_Data_Modify.this, R.array.spinner_do_Gyeongsangbukdo, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Layout_CustomDialog_CourtChoice_Se.setAdapter(adspin2);
                            Layout_CustomDialog_CourtChoice_Se.setOnItemSelectedListener(
                                    new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            spinnum2 = i;
                                            Si = adspin2.getItem(i).toString();
                                            String result = "";
                                            try {
                                                HttpClient client = new DefaultHttpClient();
                                                String postURL = "http://210.122.7.193:8080/gg/CourtInformation.jsp";
                                                HttpPost post = new HttpPost(postURL);
                                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                                params.add(new BasicNameValuePair("NewsFeed_Do", (String) adspin1.getItem(spinnum1)));
                                                params.add(new BasicNameValuePair("NewsFeed_Si", (String) adspin2.getItem(spinnum2)));
                                                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                                post.setEntity(ent);
                                                HttpResponse response = client.execute(post);
                                                BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                                String line = null;
                                                while ((line = bufreader.readLine()) != null) {
                                                    result += line;
                                                }
                                                parsedData = jsonParserList(result);
                                                arr = new ArrayList();
                                                arr.add("코트 선택");
                                                for (int a = 0; a < parsedData.length; a++) {
                                                    arr.add(parsedData[a][0]);
                                                }
                                                adspin3 = new ArrayAdapter<CharSequence>(Match_Out_NewsFeed_Data_Modify.this, android.R.layout.simple_spinner_item, arr);
                                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                Layout_CustomDialog_CourtChoice_Court.setAdapter(adspin3);
                                                Layout_CustomDialog_CourtChoice_Court.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                        Court = adspin3.getItem(i).toString();
                                                        NewsFeed_Writing_TextVIew_Court.setText(Court);
                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                                    }
                                                });
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {
                                        }
                                    }
                            );
                        }else if (adspin1.getItem(i).equals("경상남도")) {
                            adspin2 = ArrayAdapter.createFromResource(Match_Out_NewsFeed_Data_Modify.this, R.array.spinner_do_Gyeongsangnamdo, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Layout_CustomDialog_CourtChoice_Se.setAdapter(adspin2);
                            Layout_CustomDialog_CourtChoice_Se.setOnItemSelectedListener(
                                    new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            spinnum2 = i;
                                            Si = adspin2.getItem(i).toString();
                                            String result = "";
                                            try {
                                                HttpClient client = new DefaultHttpClient();
                                                String postURL = "http://210.122.7.193:8080/gg/CourtInformation.jsp";
                                                HttpPost post = new HttpPost(postURL);
                                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                                params.add(new BasicNameValuePair("NewsFeed_Do", (String) adspin1.getItem(spinnum1)));
                                                params.add(new BasicNameValuePair("NewsFeed_Si", (String) adspin2.getItem(spinnum2)));
                                                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                                post.setEntity(ent);
                                                HttpResponse response = client.execute(post);
                                                BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                                String line = null;
                                                while ((line = bufreader.readLine()) != null) {
                                                    result += line;
                                                }
                                                parsedData = jsonParserList(result);
                                                arr = new ArrayList();
                                                arr.add("코트 선택");
                                                for (int a = 0; a < parsedData.length; a++) {
                                                    arr.add(parsedData[a][0]);
                                                }
                                                adspin3 = new ArrayAdapter<CharSequence>(Match_Out_NewsFeed_Data_Modify.this, android.R.layout.simple_spinner_item, arr);
                                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                Layout_CustomDialog_CourtChoice_Court.setAdapter(adspin3);
                                                Layout_CustomDialog_CourtChoice_Court.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                        Court = adspin3.getItem(i).toString();
                                                        NewsFeed_Writing_TextVIew_Court.setText(Court);
                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                                    }
                                                });
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {
                                        }
                                    }
                            );
                        }else if (adspin1.getItem(i).equals("제주도")) {
                            adspin2 = ArrayAdapter.createFromResource(Match_Out_NewsFeed_Data_Modify.this, R.array.spinner_do_Jejudo, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Layout_CustomDialog_CourtChoice_Se.setAdapter(adspin2);
                            Layout_CustomDialog_CourtChoice_Se.setOnItemSelectedListener(
                                    new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            spinnum2 = i;
                                            Si = adspin2.getItem(i).toString();
                                            String result = "";
                                            try {
                                                HttpClient client = new DefaultHttpClient();
                                                String postURL = "http://210.122.7.193:8080/gg/CourtInformation.jsp";
                                                HttpPost post = new HttpPost(postURL);
                                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                                params.add(new BasicNameValuePair("NewsFeed_Do", (String) adspin1.getItem(spinnum1)));
                                                params.add(new BasicNameValuePair("NewsFeed_Si", (String) adspin2.getItem(spinnum2)));
                                                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                                post.setEntity(ent);
                                                HttpResponse response = client.execute(post);
                                                BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                                String line = null;
                                                while ((line = bufreader.readLine()) != null) {
                                                    result += line;
                                                }
                                                parsedData = jsonParserList(result);
                                                arr = new ArrayList();
                                                arr.add("코트 선택");
                                                for (int a = 0; a < parsedData.length; a++) {
                                                    arr.add(parsedData[a][0]);
                                                }
                                                adspin3 = new ArrayAdapter<CharSequence>(Match_Out_NewsFeed_Data_Modify.this, android.R.layout.simple_spinner_item, arr);
                                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                Layout_CustomDialog_CourtChoice_Court.setAdapter(adspin3);
                                                Layout_CustomDialog_CourtChoice_Court.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                        Court = adspin3.getItem(i).toString();
                                                        NewsFeed_Writing_TextVIew_Court.setText(Court);
                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                                    }
                                                });
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {
                                        }
                                    }
                            );
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                }
        );


        NewsFeed_Camera_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MaterialDialog Dialog = new MaterialDialog(Match_Out_NewsFeed_Data_Modify.this);
                Dialog
                        .setMessage("삭제하시겠습니까?")
                        .setPositiveButton("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                NewsFeed_Camera_Image.setVisibility(View.GONE);
                                flag = false;
                                Dialog.dismiss();
                            }
                        })
                        .setNegativeButton("NO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Dialog.dismiss();
                            }
                        });
                Dialog.show();
            }
        });

        NewsFeed_Writing_cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        NewsFeed_Writing_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (enabled) {
                    try {
                        HttpClient client = new DefaultHttpClient();
                        String postURL = "http://210.122.7.193:8080/gg/newsfeed_data_modify.jsp";
                        HttpPost post = new HttpPost(postURL);
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("NewsFeed_Num", (DataIntent.getExtras().getString("Num"))));
                        params.add(new BasicNameValuePair("NewsFeed_Do", Do));
                        params.add(new BasicNameValuePair("NewsFeed_Si", Si));
                        params.add(new BasicNameValuePair("NewsFeed_Name", DataIntent.getExtras().getString("Name")));
                        params.add(new BasicNameValuePair("NewsFeed_Court", Court));
                        params.add(new BasicNameValuePair("NewsFeed_Data", NewsFeed_Writing_TextEditText.getText().toString()));
                        params.add(new BasicNameValuePair("NewsFeed_Month", new SimpleDateFormat("MM").format(new java.sql.Date(System.currentTimeMillis()))));
                        params.add(new BasicNameValuePair("NewsFeed_Day", new SimpleDateFormat("dd").format(new java.sql.Date(System.currentTimeMillis()))));
                        params.add(new BasicNameValuePair("NewsFeed_Hour", new SimpleDateFormat("kk").format(new java.sql.Date(System.currentTimeMillis()))));
                        params.add(new BasicNameValuePair("NewsFeed_Minute", new SimpleDateFormat("mm").format(new java.sql.Date(System.currentTimeMillis()))));

                        int MaxNum = Integer.parseInt(DataIntent.getExtras().getString("MaxNum")) + 1;
                        params.add(new BasicNameValuePair("NewsFeed_MaxNum", String.valueOf(MaxNum)));

                        if (flag) {
                            params.add(new BasicNameValuePair("NewsFeed_Image", ImageFile));
                            String urlString = "http://210.122.7.193:8080/gg/newsfeed_Image_upload.jsp";
                            //파일 업로드 시작!
                            HttpFileUpload(urlString, "", ImageURL);
                        } else {
                            params.add(new BasicNameValuePair("NewsFeed_Image", ""));
                        }
                        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                        post.setEntity(ent);
                        HttpResponse response = client.execute(post);

                        flag = false;
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                    Snackbar.make(view, "내용 입력후 게시하여주세요", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        NewsFeed_Writing_CameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Match_Out_NewsFeed_Camera camera = new Match_Out_NewsFeed_Camera();
                camera.camera_ImageView(NewsFeed_Camera_Image);
                CameraIntent = new Intent(getApplicationContext(), Match_Out_NewsFeed_Camera.class);

                startActivityForResult(CameraIntent, 1);
            }
        });
    }

    public String[][] jsonParserList(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");
            String[] jsonName = {"NewsFeed_Court"};
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


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.dataIntent = data;
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                ImageURL = data.getStringExtra("ImageURL");
                ImageFile = data.getStringExtra("ImageFile");
                ImageDownload();
                flag = true;
            }
        }
    }


    public void ImageDownload() {
        try {
            FileInputStream in;
            BufferedInputStream buf;
            in = new FileInputStream(String.valueOf(ImageURL));
            buf = new BufferedInputStream(in);
            Bitmap orgImage = BitmapFactory.decodeFile(String.valueOf(ImageURL));
            Bitmap resize = Bitmap.createScaledBitmap(orgImage, 1080, 1080, true);


            NewsFeed_Camera_Image.setVisibility(View.VISIBLE);
            NewsFeed_Camera_Image.setImageBitmap(resize);
//            NewsFeed_Camera_Image.setRotation(90);

        } catch (Exception e) {
        }
    }


    public void HttpFileUpload(String urlString, String params, String fileName) {


        Log.i("HttpFileUpload", fileName);
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        // fileName=TeamName;
        try {
            //선택한 파일의 절대 경로를 이용해서 파일 입력 스트림 객체를 얻어온다.
            FileInputStream mFileInputStream = new FileInputStream(fileName);
            //파일을 업로드할 서버의 url 주소를이용해서 URL 객체 생성하기.
            URL connectUrl = new URL(urlString);
            //Connection 객체 얻어오기.
            HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
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
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + URLEncoder.encode(ImageFile, "utf-8") + ".jpg" + "\"" + lineEnd);
            dos.writeBytes(lineEnd);
            //한번에 읽어들일수있는 스트림의 크기를 얻어온다.
            int bytesAvailable = mFileInputStream.available();
            //byte단위로 읽어오기 위하여 byte 배열 객체를 준비한다.
            byte[] buffer = new byte[bytesAvailable];
            int bytesRead = 0;
            // read image
            while (bytesRead != -1) {
                //파일에서 바이트단위로 읽어온다.
                bytesRead = mFileInputStream.read(buffer);
                if (bytesRead == -1) break; //더이상 읽을 데이터가 없다면 빠저나온다.
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
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            String s = b.toString();
            Log.e("Test", "result = " + s);

        } catch (Exception e) {
            Log.d("Test", "exception " + e.getMessage());
            Snackbar.make(view, "업로드중 에러발생!", Snackbar.LENGTH_SHORT).show();
        }
    }
}