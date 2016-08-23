package com.example.mybasket;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by park on 2016-04-16.
 */
public class Match_In_CustomList_MyAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Match_In_CustomList_MyData> arrData;
    private LayoutInflater inflater;
    private int MonthGap[] = {-30, -30, -27, -30, -29, -30, -29, -30, -30, -29, -30, -29};
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
        String Name = arrData.get(position).getName();
        String Profile = arrData.get(position).getProfile();
        String MyId = arrData.get(position).getMyId();
        String Id = arrData.get(position).getId();
        final TextView Match_In_CustomList_Title = (TextView)convertView.findViewById(R.id.Match_In_CustomList_Title);
        final LinearLayout Match_In_CustomList_Layout = (LinearLayout)convertView.findViewById(R.id.Match_In_CustomList_Layout);
        final TextView Match_In_CustomList_TeamName = (TextView)convertView.findViewById(R.id.Match_In_CustomList_TeamName);
        final TextView Match_In_CustomList_Address = (TextView)convertView.findViewById(R.id.Match_In_CustomList_Address);
        final TextView Match_In_CustomList_Time = (TextView)convertView.findViewById(R.id.Match_In_CustomList_Time);
        final ImageView Match_In_CustomList_Profile = (ImageView) convertView.findViewById(R.id.Match_In_CustomList_Profile);
        final TextView Match_In_CustomList_Name = (TextView)convertView.findViewById(R.id.Match_In_CustomList_Name);
        final TextView Match_In_CustomList_RealTime = (TextView)convertView.findViewById(R.id.Match_In_CustomList_WritingTime);
        final ImageButton Mathc_In_CustomList_Setting = (ImageButton)convertView.findViewById(R.id.Mathc_In_CustomList_Setting);

        //유저 개인 이미지를 서버에서 받아옵니다.
        try {
            String En_Profile = URLEncoder.encode(Profile, "utf-8");
            if (Profile.equals(".")) {
                Glide.with(context).load(R.drawable.profile_basic_image).into(Match_In_CustomList_Profile);
            } else {
                Glide.with(context).load("http://210.122.7.195:8080/Web_basket/imgs/Profile/" + En_Profile + ".jpg").bitmapTransform(new CropCircleTransformation(Glide.get(context).getBitmapPool()))
                        .into(Match_In_CustomList_Profile);
            }
        } catch (UnsupportedEncodingException e) {

        }
        if(MyId.equals(Id)){
            Mathc_In_CustomList_Setting.setVisibility(View.VISIBLE);
        }
        Match_In_CustomList_Name.setText(Name);
        Match_In_CustomList_RealTime.setText(GetTime(position));
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
                } else if (Hour == 1 && Minute > 0) {
                    return Hour + "시간전";
                } else if (Hour == 1 && Minute < 0) {
                    return 60 + Minute + "분전";
                } else if (Hour == 0 && Minute > 0) {
                    return Minute + "분전";
                } else if (Hour == 0 && Minute < 0) {
                    return 60 + Minute + "분전";
                } else {
                    return "방금전";
                }
            }
        }
        return "Time Error";
    }

}
