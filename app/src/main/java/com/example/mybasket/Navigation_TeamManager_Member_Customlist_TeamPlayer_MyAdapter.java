package com.example.mybasket;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 박효근 on 2016-07-18.
 */
public class Navigation_TeamManager_Member_Customlist_TeamPlayer_MyAdapter {
    private Context context;
    private ArrayList<Navigation_TeamManager_Member_Customlist_TeamPlayer_MyData> arrData;
    private LayoutInflater inflater;
    Bitmap bmImg;
    String[][] parsedData;
    public Navigation_TeamManager_Member_Customlist_TeamPlayer_MyAdapter(Context c, ArrayList<Navigation_TeamManager_Member_Customlist_TeamPlayer_MyData> arr) {
        this.context = c;
        this.arrData = arr;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return arrData.size();
    }

    public Object getItem(int position) {
        return arrData.get(position).getNumber();
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_customlist_teamplayer, parent, false);
        }
        ImageView TeamPlayer_CustomList_Profile = (ImageView)convertView.findViewById(R.id.TeamPlayer_CustomList_Profile);
        TextView TeamPlayer_CustomList_TeamNumber = (TextView)convertView.findViewById(R.id.TeamPlayer_CustomList_TeamNumber);
        TextView TeamPlayer_CustomList_Name = (TextView)convertView.findViewById(R.id.TeamPlayer_CustomList_Name);

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
