package com.mysports.basketbook;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ldong on 2016-11-12.
 */

public class Contest_Detail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_contest_detail);
        GlobalApplication.setCurrentActivity(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Intent intent = getIntent();
        String Pk = intent.getStringExtra("position");

        TextView title = (TextView) findViewById(R.id.layout_contest_detail_title);
        TextView price = (TextView) findViewById(R.id.layout_contest_register_price);
        TextView remainder = (TextView) findViewById(R.id.layout_contest_remainder);
        TextView host = (TextView) findViewById(R.id.layout_contest_host);
        TextView management= (TextView) findViewById(R.id.layout_contest_management);
        TextView support = (TextView) findViewById(R.id.layout_contest_support);
        TextView recruitPeriod = (TextView) findViewById(R.id.layout_contest_recruit_period);
        TextView date = (TextView) findViewById(R.id.layout_contest_date);
        TextView place = (TextView) findViewById(R.id.layout_contest_place);
        TextView DetailInfo = (TextView) findViewById(R.id.layout_contest_detailInfo);

        String result = "";
        try {
            HttpClient client = new DefaultHttpClient();
            String postURL = "http://210.122.7.193:8080/pp/Contest_Customlist_detail.jsp";
            HttpPost post = new HttpPost(postURL);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Pk", Pk));

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
        String[][] ContestsDetailParsedData = jsonParserList_getContestDetail(result);

        title.setText(ContestsDetailParsedData[0][1]);
        date.setText(ContestsDetailParsedData[0][2]);
        host.setText(ContestsDetailParsedData[0][7]);
        management.setText(ContestsDetailParsedData[0][8]);
        support.setText(ContestsDetailParsedData[0][9]);
        recruitPeriod.setText(ContestsDetailParsedData[0][10] + " ~ " + ContestsDetailParsedData[0][11]);
        DetailInfo.setText(ContestsDetailParsedData[0][12]);

        String a = ContestsDetailParsedData[0][10].trim();
        String b = ContestsDetailParsedData[0][11].trim();
        String aa = a.replace("/","-");
        String bb = b.replace("/","-");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");
    }

    public String[][] jsonParserList_getContestDetail(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용", pRecvServerPage);
        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");

            String[] jsonName = {"Pk", "Title", "Date", "Image", "currentNum", "maxNum", "Point", "Host", "Management", "Support", "RecruitmentStart", "RecruitmentFinish", "DetailInfo"};
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
