package com.mysports.basketbook;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
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
    RadioGroup join_sex_Radio, join_posi_Radio;
    EditText join_name_EditText,join_layout_weight_editText,join_layout_height_editText;
    String[][] parsedData;
    ArrayAdapter<CharSequence> adspin1,adspin2;
    TextView tvBirth;
    Spinner s_Sex;
    Spinner s_Position;
    DatePickerDialog DatePickerDialog;
    int year, month, day, hour, minute;

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


        GregorianCalendar calendar = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        tvBirth = (TextView) findViewById(R.id.join_layout_birth_tv);

        s_Sex = (Spinner)findViewById(R.id.spinner_sex);
        s_Position = (Spinner)findViewById(R.id.spinner_position);

        tvBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog = new DatePickerDialog(JoinAddActivity.this, dateSetListener, year, month, day);
                DatePickerDialog.getDatePicker().setCalendarViewShown(false);
                DatePickerDialog.show();
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


    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub

            String msg = String.format("%d / %d / %d", year,monthOfYear+1, dayOfMonth);
            tvBirth.setText(msg);

        }
    };



    public void onClickCommit(View view) {
        String name = join_name_EditText.getText().toString();
        String birth = tvBirth.getText().toString();
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
            Snackbar.make(view, "생일이 입력되지 않았습니다.", Snackbar.LENGTH_LONG)
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

                Snackbar.make(view, "회원가입이 완료되었습니다.", Snackbar.LENGTH_LONG)
                        .show();

                Intent intent = new Intent(JoinAddActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }else {

            }
        }


        //joinThread jt = new joinThread();
        //jt.run();

    }

    public void onClickBack(View view) {

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
            params.add(new BasicNameValuePair("_height", _height));;

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
