package com.example.mybasket;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pkmmte.view.CircularImageView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by 박효근 on 2016-06-28.
 */
public class Navigation_TeamIntro_Focus_Player_MyAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Navigation_TeamIntro_Focus_Player_MyData> arrData;
    private LayoutInflater inflater;
    String ProfileUrl;
    Bitmap bmImg;
    ImageView Teamintro_Focus_Player_CustomList_ProfileImage;
    public Navigation_TeamIntro_Focus_Player_MyAdapter(Context c, ArrayList<Navigation_TeamIntro_Focus_Player_MyData> arr) {
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
            convertView = inflater.inflate(R.layout.layout_customlist_teamintro_focus_player, parent, false);
        }

        TextView Name = (TextView) convertView.findViewById(R.id.Teamintro_Focus_Player_CustomList_Name);
        TextView Duty = (TextView) convertView.findViewById(R.id.Teamintro_Focus_Player_CustomList_Duty);
        Name.setText(arrData.get(position).getName());
        Duty.setText(arrData.get(position).getDuty());
        try {
            String En_Profile = URLEncoder.encode(arrData.get(position).getProfile(), "utf-8");
            if (arrData.get(position).getProfile().equals(".")) {
                Glide.with(context).load(R.drawable.profile_basic_image).bitmapTransform(new CropCircleTransformation(Glide.get(context).getBitmapPool()))
                        .into(Teamintro_Focus_Player_CustomList_ProfileImage);
            } else {
                Glide.with(context).load("http://210.122.7.195:8080/Web_basket/imgs/Profile/" + En_Profile + ".jpg").bitmapTransform(new CropCircleTransformation(Glide.get(context).getBitmapPool()))
                        .into(Teamintro_Focus_Player_CustomList_ProfileImage);
            }
        } catch (UnsupportedEncodingException e) {

        }
        LayoutInflater inflater = (LayoutInflater)convertView.getContext().getSystemService(context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.layout_customdialog_teamplayer, (ViewGroup) convertView.findViewById(R.id.Layout_CustomDialog_TeamPlayer_Root));
        final ImageView Layout_CustomDialog_TeamPlayer_Profile = (ImageView)layout.findViewById(R.id.Layout_CustomDialog_TeamPlayer_Profile);
        final Button Layout_CustomDialog_TeamPlayer_TeamNameAndDuty = (Button)layout.findViewById(R.id.Layout_CustomDialog_TeamPlayer_TeamNameAndDuty);
        final Button Layout_CustomDialog_TeamPlayer_Name = (Button)layout.findViewById(R.id.Layout_CustomDialog_TeamPlayer_Name);
        final Button Layout_CustomDialog_TeamPlayer_Position = (Button)layout.findViewById(R.id.Layout_CustomDialog_TeamPlayer_Position);
        final Button Layout_CustomDialog_TeamPlayer_Age = (Button)layout.findViewById(R.id.Layout_CustomDialog_TeamPlayer_Age);
        final Button Layout_CustomDialog_TeamPlayer_Sex = (Button)layout.findViewById(R.id.Layout_CustomDialog_TeamPlayer_Sex);

        try{
            String En_Profile = URLEncoder.encode(arrData.get(position).getProfile(), "utf-8");
            if(arrData.get(position).getProfile().equals("."))
            {
                Glide.with(context).load(R.drawable.profile_basic_image).into(Layout_CustomDialog_TeamPlayer_Profile);
            }
            else
            {
                Glide.with(context).load("http://210.122.7.195:8080/Web_basket/imgs/Profile/" + En_Profile + ".jpg").bitmapTransform(new CropCircleTransformation(Glide.get(context).getBitmapPool()))
                        .into(Layout_CustomDialog_TeamPlayer_Profile);
            }
        }
        catch (UnsupportedEncodingException e){

        }
        Layout_CustomDialog_TeamPlayer_TeamNameAndDuty.setText(arrData.get(position).getTeamName()+" | "+arrData.get(position).getDuty());
        Layout_CustomDialog_TeamPlayer_Name.setText(arrData.get(position).getName());
        Layout_CustomDialog_TeamPlayer_Position.setText(arrData.get(position).getPosition());
        Layout_CustomDialog_TeamPlayer_Age.setText(ChangeAge(arrData.get(position).getBirth()));
        Layout_CustomDialog_TeamPlayer_Sex.setText((arrData.get(position).getSex()));
        Layout_CustomDialog_TeamPlayer_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,Profile_Focus.class);
                intent.putExtra("Profile", arrData.get(position).getProfile());
                context.startActivity(intent);

            }
        });
        final MaterialDialog TeamPlayerDialog = new MaterialDialog(context);
        Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TeamPlayerDialog
                        .setTitle("팀원 정보")
                        .setView(layout)
                        .setPositiveButton("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TeamPlayerDialog.dismiss();
                            }
                        });
                TeamPlayerDialog.show();
            }
        });
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
