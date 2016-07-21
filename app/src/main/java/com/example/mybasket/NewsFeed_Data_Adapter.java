package com.example.mybasket;

/**
 * Created by 박효근 on 2016-07-22.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by 박지훈 on 2016-06-24.
 */
public class NewsFeed_Data_Adapter extends BaseAdapter {
    private Context context;
    private ArrayList<NewsFeed_Data_Setting> arrData;
    private LayoutInflater inflater;

    private int MonthGap[] = {-30, -30, -27, -30, -29, -30, -29, -30, -30, -29, -30, -29};

    ListView NewsFeed_List;
    NewsFeed_Data_Adapter dataadapter;
    String[][] parsedData;

    public NewsFeed_Data_Adapter(Context c, ArrayList<NewsFeed_Data_Setting> arr) {
        this.context = c;
        this.arrData = arr;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void listview(ListView listView){
        NewsFeed_List=listView;
        return ;}
    @Override
    public int getViewTypeCount() {
        // menu type count
        return 2;
    }
    @Override
    public int getItemViewType(int position) {
        // current menu type
        return 1;
    }
    public int getCount() {
        return arrData.size();
    }

    @Override
    public Object getItem(int position) {
        return arrData.get(position);
    }


    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.newsfeed_customlist_data, parent, false);
        }
        TextView Court = (TextView) convertView.findViewById(R.id.NewsFeed_CustomList_Court);
        Court.setText(arrData.get(position).getcourt());

        TextView Person = (TextView) convertView.findViewById(R.id.NewsFeed_CustomList_Person);
        Person.setText(arrData.get(position).getperson());

        TextView Time = (TextView) convertView.findViewById(R.id.NewsFeed_CustomList_Time);
        Time.setText(GetTime(position));

        TextView Data = (TextView) convertView.findViewById(R.id.NewsFeed_CustomList_Data);
        Data.setText(arrData.get(position).getdata());

        return convertView;
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
                } else if (Hour > 0 && Minute <=0) {
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
                if (Hour > 1&& Minute >0) {
                    return Hour + "시간전";
                } else if(Hour > 1&& Minute <0){
                    return Hour - 1 + "시간전";
                } else if (Hour == 1 && Minute >= 0) {
                    return Hour + "시간전";
                } else if (Hour == 1 && Minute <0) {
                    return 60 + Minute + "분전";
                } else if (Hour == 0 && Minute >0) {
                    return  Minute + "분전";
                }else if(Hour == 0 && Minute <0){
                    return 60 + Minute + "분전";
                } else {
                    return "방금";
                }
            }
        }
        return "Time Error";
    }

}
