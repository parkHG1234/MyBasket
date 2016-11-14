package com.mysports.basketbook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by 박효근 on 2016-11-13.
 */

public class Contest_Detail_Form_Customlist_Adapter extends BaseAdapter{
    private Context context;
    private ArrayList<Contest_Detail_Form_Customlist_MyData> arrData;
    private LayoutInflater inflater;
    String ProfileUrl;
    Bitmap bmImg;
    ImageView Contest_Detail_Form_Player_CustomList_ProfileImage;
    public Contest_Detail_Form_Customlist_Adapter(Context c, ArrayList<Contest_Detail_Form_Customlist_MyData> arr) {
        this.context = c;
        this.arrData = arr;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return arrData.size();
    }

    public Object getItem(int position) {
        return arrData.get(position).getName();
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_customlist_contest_detail_form_player, parent, false);
        }

        TextView Name = (TextView) convertView.findViewById(R.id.Contest_Detail_Form_Player_CustomList_Name);
        TextView Duty = (TextView) convertView.findViewById(R.id.Contest_Detail_Form_Player_CustomList_Duty);
        TextView Age = (TextView)convertView.findViewById(R.id.Contest_Detail_Form_Player_CustomList_Age) ;
        CheckBox check= (CheckBox)convertView.findViewById(R.id.Contest_Detail_Form_Player_CustomList_Check);

        Contest_Detail_Form_Player_CustomList_ProfileImage = (ImageView)convertView.findViewById(R.id.Contest_Detail_Form_Player_CustomList_ProfileImage);
        Name.setText(arrData.get(position).getName());
        Duty.setText(arrData.get(position).getDuty());
        Age.setText(ChangeAge(arrData.get(position).getBirth()));
        try {
            String En_Profile = URLEncoder.encode(arrData.get(position).getProfile(), "utf-8");
            if (arrData.get(position).getProfile().equals(".")) {
                Glide.with(context).load(R.drawable.profile_basic_image).bitmapTransform(new CropCircleTransformation(Glide.get(context).getBitmapPool()))
                        .into(Contest_Detail_Form_Player_CustomList_ProfileImage);
            } else {
                Glide.with(context).load("http://210.122.7.193:8080/Web_basket/imgs/Profile/" + En_Profile + ".jpg").bitmapTransform(new CropCircleTransformation(Glide.get(context).getBitmapPool()))
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(Contest_Detail_Form_Player_CustomList_ProfileImage);
            }
        } catch (UnsupportedEncodingException e) {

        }

        return convertView;
    }
    //date 입력받아 나이 구하는 함수
    public String ChangeAge(String Age){
        Calendar cal= Calendar.getInstance ();
        String[] str = new String(Age).split(" \\/ ");
        String[] str_day = new String(str[2]).split(" ");
        int year = Integer.parseInt(str[0]);
        int month = Integer.parseInt(str[1]);
        int day = Integer.parseInt(str_day[0]);

        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month-1);
        cal.set(Calendar.DATE, day);

        Calendar now = Calendar.getInstance ();

        int age = now.get(Calendar.YEAR) - cal.get(Calendar.YEAR);
        if (  (cal.get(Calendar.MONTH) > now.get(Calendar.MONTH))
                || (    cal.get(Calendar.MONTH) == now.get(Calendar.MONTH)
                && cal.get(Calendar.DAY_OF_MONTH) > now.get(Calendar.DAY_OF_MONTH)   )
                ){
            age--;
        }
        String Str_age = Integer.toString(age);
        return Str_age;
    }
}
