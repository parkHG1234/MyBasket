package com.example.mybasket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by park on 2016-04-17.
 */
public class League_Contest_CustomList_MyAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<League_Contest_CustomList_MyData> arrData;
    private LayoutInflater inflater;
    public League_Contest_CustomList_MyAdapter(Context c, ArrayList<League_Contest_CustomList_MyData> arr) {
        this.context = c;
        this.arrData = arr;
        inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public int getCount() {
        return arrData.size();
    }
    public Object getItem(int position) {
        return arrData.get(position).getTitle();
    }
    public long getItemId(int position) {
        return position;
    }
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.layout_customlist_league_contest, parent, false);
        }
        String Title = arrData.get(position).getTitle();
        String Date = arrData.get(position).getDate();
        String Address = arrData.get(position).getAddress();
        String image = arrData.get(position).getImage();

        TextView League_Contest_CustomList_Title = (TextView)convertView.findViewById(R.id.League_Contest_CustomList_Title);
        TextView League_Contest_CustomList_Date = (TextView)convertView.findViewById(R.id.League_Contest_CustomList_Date);
        TextView League_Contest_CustomList_Address = (TextView)convertView.findViewById(R.id.League_Contest_CustomList_Address);
        ImageView League_Contest_CustomList_Image = (ImageView)convertView.findViewById(R.id.League_Contest_CustomList_Image);

        Glide.with(context).load("http://210.122.7.195:8080/Web_basket/imgs/Contest/" + image + ".jpg").into(League_Contest_CustomList_Image);

        League_Contest_CustomList_Title.setText(Title);
        League_Contest_CustomList_Date.setText(Date);
        League_Contest_CustomList_Address.setText(Address);
        return convertView;
    }
}
