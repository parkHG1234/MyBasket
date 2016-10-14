package com.mysports.basketbook;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ldong on 2016-10-12.
 */

public class Leauge_MyRecord_MyAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Leauge_MyRecord_MyData> arrData;
    private LayoutInflater inflater;
    public Leauge_MyRecord_MyAdapter(Context c, ArrayList<Leauge_MyRecord_MyData> arr) {
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
        return arrData.get(position).getIsWin();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.layout_customlist_leauge_myrecord, parent, false);

            String HomeTeam_name = arrData.get(position).getHomeTeam_name();
            String AwayTeam_name = arrData.get(position).getAwayTeam_name();
            String HomeTeam_score = arrData.get(position).getHomeTeam_score();
            String AwayTeam_score = arrData.get(position).getAwayTeam_score();
            String Match_date = arrData.get(position).getMatch_date();
            String Match_time = arrData.get(position).getMatch_time();
            String isWin = arrData.get(position).getIsWin();

            Button btn_HomeTeam_name = (Button)convertView.findViewById(R.id.Leauge_MyRecord_Button_HomeTeam);
            Button btn_AwayTeam_name = (Button)convertView.findViewById(R.id.Leauge_MyRecord_Button_AwayTeam);
            TextView tv_HomeTeam_score = (TextView)convertView.findViewById(R.id.Leauge_MyRecord_TextView_HomeTeam_score);
            TextView tv_AwayTeam_score = (TextView)convertView.findViewById(R.id.Leauge_MyRecord_TextView_AwayTeam_score);
            TextView tv_Match_date = (TextView)convertView.findViewById(R.id.Leauge_MyRecord_TextView_Match_date);

            btn_HomeTeam_name.setText(HomeTeam_name);
            btn_AwayTeam_name.setText(AwayTeam_name);
            tv_HomeTeam_score.setText(HomeTeam_score);
            tv_AwayTeam_score.setText(AwayTeam_score);
            tv_Match_date.setText(Match_date);

            if(isWin.equals("win")) {
                convertView.setBackgroundColor(Color.BLUE);
            }else if(isWin.equals("lose")){
                convertView.setBackgroundColor(Color.RED);
            }
        }
        return convertView;
    }
}
