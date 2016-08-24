package com.example.mybasket;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by 박효근 on 2016-07-18.
 */
public class Navigation_TeamManager_Member_Customlist_TeamPlayer_MyAdapter extends BaseAdapter{
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

        TeamPlayer_CustomList_Name.setText(arrData.get(position).getName());
        TeamPlayer_CustomList_TeamNumber.setText(arrData.get(position).getNumber());
        try{
            String En_Profile = URLEncoder.encode(arrData.get(position).getProfile(), "utf-8");
            if(arrData.get(position).getProfile().equals("."))
            {
                Glide.with(context).load(R.drawable.profile_basic_image).into(TeamPlayer_CustomList_Profile);
            }
            else
            {
                Glide.with(context).load("http://210.122.7.195:8080/Web_basket/imgs/Profile/" + En_Profile + ".jpg").bitmapTransform(new CropCircleTransformation(Glide.get(context).getBitmapPool()))
                        .into(TeamPlayer_CustomList_Profile);
            }
        }
        catch (UnsupportedEncodingException e){

        }
        return convertView;
    }
}
