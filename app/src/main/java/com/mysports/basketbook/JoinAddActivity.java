package com.mysports.basketbook;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

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
 * Created by ldong on 2016-07-05.
 */
public class JoinAddActivity extends AppCompatActivity{

    static String id, pw, user_type;
    LinearLayout join_add_layout_root;
    RadioGroup join_sex_Radio, join_posi_Radio;
    EditText join_name_EditText,join_layout_weight_editText,join_layout_height_editText;
    EditText year_EditText, month_EditText, day_EditText;
    String[][] parsedData;
    ArrayAdapter<CharSequence> adspin1,adspin2;
    Spinner s_Sex;
    Spinner s_Position;
    DatePickerDialog DatePickerDialog;
    String year, month, day, hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_add_layout);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Intent intentGet = getIntent();
        id = intentGet.getStringExtra("id");
        pw = intentGet.getStringExtra("pw");
        user_type = intentGet.getStringExtra("user_type");

        final String birthTemp = "";
        String posiTemp = "";

        join_add_layout_root = (LinearLayout)findViewById(R.id.join_add_layout_root);
        year_EditText = (EditText) findViewById(R.id.Year_EditText);
        month_EditText = (EditText) findViewById(R.id.Month_EditText);
        day_EditText = (EditText) findViewById(R.id.Day_EditText);


        s_Sex = (Spinner)findViewById(R.id.spinner_sex);
        s_Position = (Spinner)findViewById(R.id.spinner_position);

        join_add_layout_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(year_EditText.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(month_EditText.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(day_EditText.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(join_name_EditText.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(join_layout_weight_editText.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(join_layout_height_editText.getWindowToken(), 0);

            }
        });

        s_Sex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if(position==0) {

                }else if(position==1) {

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        s_Position.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if(position==0) {

                }else if(position==1) {

                }else if(position==2) {

                }else if(position==3) {

                }else if(position==4) {

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        join_name_EditText = (EditText) findViewById(R.id.join_layout_name_editText);
        join_sex_Radio = (RadioGroup) this.findViewById(R.id.sex_radio);
        join_posi_Radio = (RadioGroup) this.findViewById(R.id.position_radio);
        join_layout_weight_editText= (EditText)findViewById(R.id.join_layout_weight_editText);
        join_layout_height_editText =(EditText)findViewById(R.id.join_layout_height_editText);
        join_layout_weight_editText.setFocusable(false);
        join_layout_height_editText.setFocusable(false);
///키 커스텀 다이얼로그
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.layout_customdialog_join_height, (ViewGroup) findViewById(R.id.Layout_CustomDialog_Join_Height_Root));
        final Spinner Layout_CustomDialog_Joing_Heigh_Height = (Spinner) layout.findViewById(R.id.Layout_CustomDialog_Joing_Heigh_Height);

        final MaterialDialog HeightDialog = new MaterialDialog(JoinAddActivity.this);
        join_layout_height_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adspin1 = ArrayAdapter.createFromResource(JoinAddActivity.this, R.array.height, R.layout.zfile_spinner_test);
                adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Layout_CustomDialog_Joing_Heigh_Height.setAdapter(adspin1);
                HeightDialog
                        .setView(layout)
                        .setPositiveButton("선택완료", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                join_layout_height_editText.setText(Layout_CustomDialog_Joing_Heigh_Height.getSelectedItem().toString());
                                HeightDialog.dismiss();
                            }
                        });
                HeightDialog.show();
            }
        });


        LayoutInflater inflater1 = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout1 = inflater1.inflate(R.layout.layout_customdialog_join_weight, (ViewGroup) findViewById(R.id.Layout_CustomDialog_Join_Weight_Root));
        final Spinner Layout_CustomDialog_Joing_Weight = (Spinner) layout1.findViewById(R.id.Layout_CustomDialog_Join_Weight);
        final MaterialDialog WeightDialog = new MaterialDialog(JoinAddActivity.this);

        join_layout_weight_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adspin2 = ArrayAdapter.createFromResource(JoinAddActivity.this, R.array.weight, R.layout.zfile_spinner_test);
                adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Layout_CustomDialog_Joing_Weight.setAdapter(adspin2);
                WeightDialog
                        .setView(layout1)
                        .setPositiveButton("선택완료", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                join_layout_weight_editText.setText(Layout_CustomDialog_Joing_Weight.getSelectedItem().toString());
                                WeightDialog.dismiss();
                            }
                        });
                WeightDialog.show();
            }
        });
    }




    public void onClickCommit(View view) {
        String name = join_name_EditText.getText().toString();
        String _year = year_EditText.getText().toString();

        // 월 입력시 앞에 0이 붙어있으면 0을 제거
        String _month = month_EditText.getText().toString();
        if(_month.length()>1) {
            char _monthChk = _month.charAt(0);
            if(String.valueOf(_monthChk).equals("0")) {
                _monthChk = _month.charAt(1);
                _month = String.valueOf(_monthChk);
            }
        }


        // 일 입력시 앞에 0이 붙어있으면 0을 제거
        String _day = day_EditText.getText().toString();
        if(_day.length()>1) {
            char _dayChk = _day.charAt(0);
            Log.i("일자 첫글짜", String.valueOf(_dayChk));
            if(String.valueOf(_dayChk).equals("0")) {
                _dayChk = _day.charAt(1);
                _day = String.valueOf(_dayChk);
            }

        }

        String birth = "";
        Calendar c = Calendar.getInstance();
        String currentYear = String.valueOf(c.get(Calendar.YEAR));

        if(_year.equals("") || _month.equals("") || _day.equals("")) {

        }else {
            if(Integer.parseInt(_year) > 1900 && Integer.parseInt(_year) <= Integer.parseInt(currentYear) && Integer.parseInt(_month) >= 1 && Integer.parseInt(_month) <= 12 && Integer.parseInt(_day) >= 1 && Integer.parseInt(_day) <= 31 && _month.length()<=2) {
                year = _year;
                month = _month;
                day = _day;
                birth = year + " / " + month + " / " + day ;
            }
        }
        String height = join_layout_height_editText.getText().toString();
        String weight = join_layout_weight_editText.getText().toString();
        String sex = s_Sex.getSelectedItem().toString();
        String posi = s_Position.getSelectedItem().toString();

        if (name.equals(null)||name.equals("")) {
            Snackbar.make(view, "이름이 입력되지 않았습니다.", Snackbar.LENGTH_LONG)
                    .show();
        } else if (sex.equals(null)||sex.equals("")) {
            Snackbar.make(view, "성별이 입력되지 않았습니다.", Snackbar.LENGTH_LONG)
                    .show();
        } else if (birth.equals(null)||birth.equals("")) {
            Snackbar.make(view, "생일을 정확히 입력해 주세요.", Snackbar.LENGTH_LONG)
                    .show();
        } else if (posi.equals(null)||posi.equals("")) {
            Snackbar.make(view, "포지션이 입력되지 않았습니다.", Snackbar.LENGTH_LONG)
                    .show();
        } else if (weight.equals(null)||weight.equals("")) {
            Snackbar.make(view, "몸무게가 입력되지 않았습니다.", Snackbar.LENGTH_LONG)
                    .show();
        } else if (height.equals(null)||height.equals("")) {
            Snackbar.make(view, "키가 입력되지 않았습니다.", Snackbar.LENGTH_LONG)
                    .show();
        } else {
            String result = JoinByHttp(id,pw,user_type,name,sex,birth,posi,weight,height);
            parsedData = jsonParserList(result);
            if(parsedData[0][0] != null && parsedData[0][0].equals("succed")) {
                AlertDialog dlg = new AlertDialog.Builder(this).setTitle("회원가입 완료")
                        .setMessage("회원가입이 완료되었습니다. 로그인 화면으로 이동합니다.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        }).show();
            }else {
                AlertDialog dlg = new AlertDialog.Builder(this).setTitle("오류")
                        .setMessage("에러가 발생하였습니다. 다시한번 시도해 주세요.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {}
                        }).show();
            }
        }


        //joinThread jt = new joinThread();
        //jt.run();

    }


    private String JoinByHttp(String _id, String _pw, String _user_type, String _name, String _sex,String _birth, String _posi, String _weight, String _height) {
        try {
            HttpClient client = new DefaultHttpClient();
            String url = "http://210.122.7.195:8080/pp/Join.jsp";
            HttpPost post = new HttpPost(url);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("_id", _id));
            params.add(new BasicNameValuePair("_pw", _pw));
            params.add(new BasicNameValuePair("user_type", _user_type));
            params.add(new BasicNameValuePair("_name", _name));
            params.add(new BasicNameValuePair("_sex", _sex));
            params.add(new BasicNameValuePair("_birth", _birth));
            params.add(new BasicNameValuePair("_posi", _posi));
            params.add(new BasicNameValuePair("_weight", _weight));
            params.add(new BasicNameValuePair("_height", _height));

            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);

            HttpResponse response = client.execute(post);
            BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));

            String line = null;
            String result = "";

            while ((line = bufreader.readLine()) != null) {
                result += line;
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private String[][] jsonParserList(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jarr = json.getJSONArray("List");

            //String[] jsonName = {"login_check","usercode","password","name","sex","email","univ","club" };

            String[] jsonName = {"msg1"};
            String[][] parseredData = new String[jarr.length()][jsonName.length];
            for(int i = 0; i<jarr.length();i++){
                json = jarr.getJSONObject(i);
                for (int j=0;j<jsonName.length; j++){
                    parseredData[i][j] = json.getString(jsonName[j]);
                }
            }
            for(int i=0;i<parseredData.length;i++) {
                Log.i("JSON을 분석한 데이터" + i + ":", parseredData[i][0]);
            }
            return parseredData;
        }catch(JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}