package com.example.mybasket;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by park on 2016-04-16.
 */
public class Match_In_CustomList_MyAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Match_In_CustomList_MyData> arrData;
    private LayoutInflater inflater;
    public Match_In_CustomList_MyAdapter(Context c, ArrayList<Match_In_CustomList_MyData> arr) {
        this.context = c;
        this.arrData = arr;
        inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public int getCount() {
        return arrData.size();
    }
    public Object getItem(int position) {
        return arrData.get(position).getTeamName();
    }
    public long getItemId(int position) {
        return position;
    }
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.layout_customlist_in, parent, false);
        }
        String TeamName = arrData.get(position).getTeamName();
        String Address = arrData.get(position).getAddress();
        String Date = arrData.get(position).getDate();
        String Time = arrData.get(position).getTime();
        String Title = arrData.get(position).getTitle();

        final TextView Match_In_CustomList_Title = (TextView)convertView.findViewById(R.id.Match_In_CustomList_Title);
        final LinearLayout Match_In_CustomList_Layout = (LinearLayout)convertView.findViewById(R.id.Match_In_CustomList_Layout);
        final TextView Match_In_CustomList_TeamName = (TextView)convertView.findViewById(R.id.Match_In_CustomList_TeamName);
        final TextView Match_In_CustomList_Address = (TextView)convertView.findViewById(R.id.Match_In_CustomList_Address);
        final TextView Match_In_CustomList_Time = (TextView)convertView.findViewById(R.id.Match_In_CustomList_Time);

        Match_In_CustomList_Title.setText(Title);
        Match_In_CustomList_TeamName.setText(TeamName);
        Match_In_CustomList_Address.setText(Address);
        String[] str_Date = new String(Date).split(" - ");
        Time = ChageTime(Time);
        //Match_In_CustomList_Time.setText(str_Date[1]+ "월 " + str_Date[2]+"일 | "+ Time);
        Match_In_CustomList_Time.setText(str_Date[1]+"월"+str_Date[2]+"일 | "+ Time);
       // Match_In_CustomList_Time.setText(Date +" | "+ Time);
        Match_In_CustomList_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, Match_In_Focus.class);
                intent.putExtra("ScheduleId",arrData.get(position).getScheduleId());
                intent.putExtra("TeamName",arrData.get(position).getTeamName());
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    public String ChageTime(String time){
        int int_Time;
        String Time_hour, Time_min;
        String[] str_Time = new String(time).split(" : ");
        Time_hour = str_Time[0];
        Time_min =  str_Time[1]+"분";
        int_Time = Integer.parseInt(Time_hour);
        if(int_Time > 12){
            Time_hour = "오후 "+Integer.toString(int_Time - 12)+"시";
        }
        else{
            Time_hour = "오전 "+int_Time+"시";
        }


        return Time_hour+""+Time_min;
    }
}
