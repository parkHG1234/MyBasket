package com.mysports.playbasket;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by park on 2016-06-13.
 */
public class Navigation_TeamIntro_CustomList_MyAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Navigation_TeamIntro_CustomList_MyData> arrData;
    private LayoutInflater inflater;
    TextView Navi_TeamIntro_CustomList_TeamName;
    ImageView Navi_TeamIntro_CustomList_Emblem;
    Bitmap bmImg;
    String TeamName;
    public Navigation_TeamIntro_CustomList_MyAdapter(Context c, ArrayList<Navigation_TeamIntro_CustomList_MyData> arr) {
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
            convertView = inflater.inflate(R.layout.layout_customlist_teamintro, parent, false);
        }
        Navi_TeamIntro_CustomList_TeamName = (TextView)convertView.findViewById(R.id.Navi_TeamIntro_CustomList_TeamName);
        Navi_TeamIntro_CustomList_Emblem = (ImageView) convertView.findViewById(R.id.Navi_TeamIntro_CustomList_Emblem);
        Navi_TeamIntro_CustomList_TeamName.setText(arrData.get(position).getTeamName());
        try{
            String En_Emblem = URLEncoder.encode(arrData.get(position).getEmblem(), "utf-8");
            if(arrData.get(position).getEmblem().equals("."))
            {
                Glide.with(context).load(R.drawable.emblem).into(Navi_TeamIntro_CustomList_Emblem);
            }
            else
            {
                    Glide.with(context).load("http://210.122.7.195:8080/Web_basket/imgs/Emblem/" + En_Emblem + ".jpg").bitmapTransform(new CropCircleTransformation(Glide.get(convertView.getContext()).getBitmapPool()))
                            .into(Navi_TeamIntro_CustomList_Emblem);
            }
        }catch (UnsupportedEncodingException e){

        }
        Navi_TeamIntro_CustomList_TeamName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Navigation_TeamIntro_Focus.class);
                intent.putExtra("TeamName",  arrData.get(position).getTeamName());
                intent.putExtra("Id", arrData.get(position).getId());
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
