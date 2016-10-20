package com.mysports.basketbook;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.mysports.basketbook.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by 박효근 on 2016-07-08.
 */
public class Match_In_Focus_Player_MyAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<Match_In_Focus_Player_MyData> arrData;
    private LayoutInflater inflater;
    private BitmapPool mBitmapPool;
    ImageView Match_In_Focus_Player_CustomList_ProfileImage;
    public Match_In_Focus_Player_MyAdapter(Context c, ArrayList<Match_In_Focus_Player_MyData> arr) {
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
            convertView = inflater.inflate(R.layout.layout_customlist_match_in_focus_player, parent, false);
        }
        TextView Name = (TextView) convertView.findViewById(R.id.Match_In_Focus_Player_CustomList_Name);
        TextView Duty = (TextView) convertView.findViewById(R.id.Match_In_Focus_Player_CustomList_Duty);
        Match_In_Focus_Player_CustomList_ProfileImage = (ImageView)convertView.findViewById(R.id.Match_In_Focus_Player_CustomList_ProfileImage);;

        Name.setText(arrData.get(position).getName());
        Duty.setText(arrData.get(position).getDuty());
        Log.i("tttt",arrData.get(position).getProfile());
        try{
            String En_Profile = URLEncoder.encode(arrData.get(position).getProfile(), "utf-8");
            if(arrData.get(position).getProfile().equals("")) {
                Glide.with(context).load(R.drawable.profile_basic_image).bitmapTransform(new CropCircleTransformation(Glide.get(context).getBitmapPool()))
                        .into(Match_In_Focus_Player_CustomList_ProfileImage);
            }
            else{
                Glide.with(context).load("http://210.122.7.195:8080/Web_basket/imgs/Profile/" + En_Profile + ".jpg").bitmapTransform(new CropCircleTransformation(Glide.get(context).getBitmapPool()))
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(Match_In_Focus_Player_CustomList_ProfileImage);
            }
        }
        catch (UnsupportedEncodingException e){

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
            if(arrData.get(position).getProfile().equals(""))
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
