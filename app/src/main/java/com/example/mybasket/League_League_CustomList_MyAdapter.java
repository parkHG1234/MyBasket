package com.example.mybasket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 박효근 on 2016-07-11.
 */
public class League_League_CustomList_MyAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<League_League_CustomList_MyData> arrData;
    private LayoutInflater inflater;
    public League_League_CustomList_MyAdapter(Context c, ArrayList<League_League_CustomList_MyData> arr) {
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
            convertView = inflater.inflate(R.layout.layout_customlist_league_league, parent, false);
        }
        int Rate = arrData.get(position).getRate();
        String TeamName = arrData.get(position).getTeamName();
        String Point = arrData.get(position).getPoint();
        TextView League_League_CustomList_Rate = (TextView)convertView.findViewById(R.id.League_League_CustomList_Rate);
        TextView League_League_CustomList_TeamName = (TextView)convertView.findViewById(R.id.League_League_CustomList_TeamName);
        TextView League_League_CustomList_Point = (TextView)convertView.findViewById(R.id.League_League_CustomList_Point);

        League_League_CustomList_Rate.setText(Integer.toString(Rate));
        League_League_CustomList_TeamName.setText(TeamName);
        League_League_CustomList_Point.setText(Point);
        return convertView;
    }
}
