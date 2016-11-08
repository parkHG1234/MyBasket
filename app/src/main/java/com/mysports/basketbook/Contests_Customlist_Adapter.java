package com.mysports.basketbook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by KimIkJoong on 2016-11-08.
 */

public class Contests_Customlist_Adapter extends BaseAdapter{
    private Context context;
    private ArrayList<Contests_Customlist_MyData> arrData;
    private LayoutInflater inflater;
    public Contests_Customlist_Adapter(Context c, ArrayList<Contests_Customlist_MyData> arr) {
        this.context = c;
        this.arrData = arr;
        this.inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrData.size();
    }

    @Override
    public Object getItem(int position) {
        return arrData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.layout_customlist_contests, parent, false);


            String Contest_Title = arrData.get(position).getContest_Title();
            String Contest_Image = arrData.get(position).getContest_Image();
            String Contest_Date = arrData.get(position).getContest_Date();
            String Contest_currentNum = arrData.get(position).getContest_currentNum();
            String Contest_maxNum = arrData.get(position).getContest_maxNum();
            String Contest_Point = arrData.get(position).getContest_Point();
            String Contest_Pk = arrData.get(position).getContest_Pk();

            ImageView viewContest_Image = (ImageView)convertView.findViewById(R.id.Contest_logoImage);
            TextView viewContest_Title = (TextView)convertView.findViewById(R.id.contest_title);
            TextView viewContest_Date = (TextView)convertView.findViewById(R.id.contest_date);
            TextView viewContest_Num = (TextView)convertView.findViewById(R.id.contest_num);
            TextView viewContest_Point = (TextView)convertView.findViewById(R.id.contest_point);

            viewContest_Title.setText(Contest_Title);
            viewContest_Date.setText(Contest_Date);
            viewContest_Num.setText(Contest_currentNum + " / " + Contest_maxNum);
            viewContest_Point.setText(Contest_Point);

        }

        return null;
    }
}
