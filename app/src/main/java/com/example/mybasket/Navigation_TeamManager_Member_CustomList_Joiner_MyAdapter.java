package com.example.mybasket;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import java.util.List;

/**
 * Created by 박효근 on 2016-06-27.
 */
public class Navigation_TeamManager_Member_CustomList_Joiner_MyAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<Navigation_TeamManager_Member_CustomList_Joiner_MyData> arrData;
    private LayoutInflater inflater;
    Bitmap bmImg;
    String[][] parsedData;
    public Navigation_TeamManager_Member_CustomList_Joiner_MyAdapter(Context c, ArrayList<Navigation_TeamManager_Member_CustomList_Joiner_MyData> arr) {
        this.context = c;
        this.arrData = arr;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return arrData.size();
    }

    public Object getItem(int position) {
        return arrData.get(position).getJoinerName();
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_customlist_joiner, parent, false);
        }
        TextView JoinerName = (TextView) convertView.findViewById(R.id.Joiner_CustomList_JoinerName);
        Button Allow  = (Button)convertView.findViewById(R.id.Joiner_CustomList_Allow);
        Button Refuse = (Button)convertView.findViewById(R.id.Joiner_CustomList_Refuse);

        JoinerName.setText(arrData.get(position).getJoinerName());
        Allow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result="";
                try {
                    HttpClient client = new DefaultHttpClient();
                    String postURL = "http://210.122.7.195:8080/Web_basket/NaviTeamManager_Joiner_Allow.jsp";
                    HttpPost post = new HttpPost(postURL);

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("Id", arrData.get(position).getId()));
                    params.add(new BasicNameValuePair("TeamName", arrData.get(position).getTeamName()));

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
                if(parsedData[0][0].equals("succed")){
                    arrData.remove(position);
                    notifyDataSetChanged();
                }
            }
        });
        Refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result="";
                try {
                    HttpClient client = new DefaultHttpClient();
                    String postURL = "http://210.122.7.195:8080/Web_basket/NaviTeamManager_Joiner_Refuse.jsp";
                    HttpPost post = new HttpPost(postURL);

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("Id", arrData.get(position).getId()));
                    params.add(new BasicNameValuePair("TeamName", arrData.get(position).getTeamName()));

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
                if(parsedData[0][0].equals("succed")){
                    arrData.remove(position);
                    notifyDataSetChanged();
                }
            }
        });

        return convertView;
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
