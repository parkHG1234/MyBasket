package com.example.mybasket;

/**
 * Created by 박효근 on 2016-07-22.
 */

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by 박지훈 on 2016-06-24.
 */
public class Match_Out_NewsFeed_Data_Adapter extends BaseAdapter {
    private Context context;
    private ArrayList<Match_Out_NewsFeed_Data_Setting> arrData;
    private LayoutInflater inflater;

    private int MonthGap[] = {-30, -30, -27, -30, -29, -30, -29, -30, -30, -29, -30, -29};

    ListView NewsFeed_List;
    String[][] parsedData;
    String UserID;
    private int MaxNum = 0;

    public Match_Out_NewsFeed_Data_Adapter(Context c, ArrayList<Match_Out_NewsFeed_Data_Setting> arr, String ID, int MaxNum) {
        this.context = c;
        this.arrData = arr;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.MaxNum = MaxNum;
        this.UserID=ID;
    }

    public void listview(ListView listView) {
        NewsFeed_List = listView;
        return;
    }

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
            convertView = inflater.inflate(R.layout.layout_match_out_newsfeed_customlist_data, parent, false);
        }


        String En_Profile = null;


        final MaterialDialog Dialog = new MaterialDialog(context);
        final View layout = inflater.inflate(R.layout.layout_customdialog_teamplayer, (ViewGroup) convertView.findViewById(R.id.Layout_CustomDialog_TeamPlayer_Root));


        ImageView NewsFeed_CustomList_Emblem = (ImageView) convertView.findViewById(R.id.NewsFeed_CustomList_Emblem);
        try {
            En_Profile = URLEncoder.encode(arrData.get(position).getInformation_Profile(), "utf-8");
            if (arrData.get(position).getInformation_Profile().equals(".")) {
                Glide.with(context).load(R.drawable.ball).into(NewsFeed_CustomList_Emblem);
            } else {
                Glide.with(context).load("http://210.122.7.195:8080/gg/imgs1/" + En_Profile + ".jpg").bitmapTransform(new CropCircleTransformation(Glide.get(context).getBitmapPool()))
                        .into(NewsFeed_CustomList_Emblem);
            }
        } catch (UnsupportedEncodingException e) {
        }

        NewsFeed_CustomList_Emblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ImageView Layout_CustomDialog_TeamPlayer_Profile = (ImageView) layout.findViewById(R.id.Layout_CustomDialog_TeamPlayer_Profile);
                final Button Layout_CustomDialog_TeamPlayer_TeamNameAndDuty = (Button) layout.findViewById(R.id.Layout_CustomDialog_TeamPlayer_TeamNameAndDuty);
                final Button Layout_CustomDialog_TeamPlayer_Name = (Button) layout.findViewById(R.id.Layout_CustomDialog_TeamPlayer_Name);
                final Button Layout_CustomDialog_TeamPlayer_Position = (Button) layout.findViewById(R.id.Layout_CustomDialog_TeamPlayer_Position);
                final Button Layout_CustomDialog_TeamPlayer_Age = (Button) layout.findViewById(R.id.Layout_CustomDialog_TeamPlayer_Age);
                final Button Layout_CustomDialog_TeamPlayer_Sex = (Button) layout.findViewById(R.id.Layout_CustomDialog_TeamPlayer_Sex);
                try {
                    String En_Profile = URLEncoder.encode(arrData.get(position).getInformation_Profile(), "utf-8");
                    if (arrData.get(position).getInformation_Profile().equals(".")) {
                        Glide.with(context).load(R.drawable.ball).into(Layout_CustomDialog_TeamPlayer_Profile);
                    } else {
                        Glide.with(context).load("http://210.122.7.195:8080/gg/imgs1/" + En_Profile + ".jpg").bitmapTransform(new CropCircleTransformation(Glide.get(context).getBitmapPool()))
                                .into(Layout_CustomDialog_TeamPlayer_Profile);
                    }
                    Layout_CustomDialog_TeamPlayer_TeamNameAndDuty.setText(arrData.get(position).getInformation_Team());
                    Layout_CustomDialog_TeamPlayer_Name.setText(arrData.get(position).getInformation_Name());
                    Layout_CustomDialog_TeamPlayer_Position.setText(arrData.get(position).getInformation_Position());
                    Layout_CustomDialog_TeamPlayer_Age.setText(arrData.get(position).getInformation_Phone());
                    Layout_CustomDialog_TeamPlayer_Sex.setText(arrData.get(position).getInformation_Sex());
                } catch (UnsupportedEncodingException e) {

                }
                Dialog
                        .setView(layout)
                        .setPositiveButton("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Dialog.dismiss();
                            }
                        });
                Dialog.show();

            }
        });

        TextView textView30 = (TextView) convertView.findViewById(R.id.textView30);
        textView30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent CommentIntent = new Intent(context, Match_Out_NewsFeed_Comment.class);
                CommentIntent.putExtra("Num", arrData.get(position).getnum());
                CommentIntent.putExtra("Court", arrData.get(position).getcourt());
                CommentIntent.putExtra("Data", arrData.get(position).getdata());
                CommentIntent.putExtra("Time", GetTime(position));
                CommentIntent.putExtra("Id", arrData.get(position).getuser());
                context.startActivity(CommentIntent);
            }
        });

        TextView Court = (TextView) convertView.findViewById(R.id.NewsFeed_CustomList_Court);
        Court.setText(arrData.get(position).getcourt());

        Court.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent CommentIntent = new Intent(context, Match_Out_NewsFeed_Comment.class);
                CommentIntent.putExtra("Num", arrData.get(position).getnum());
                CommentIntent.putExtra("Court", arrData.get(position).getcourt());
                CommentIntent.putExtra("Data", arrData.get(position).getdata());
                CommentIntent.putExtra("Time", GetTime(position));
                CommentIntent.putExtra("Id", arrData.get(position).getuser());
                context.startActivity(CommentIntent);
            }
        });

        TextView Time = (TextView) convertView.findViewById(R.id.NewsFeed_CustomList_Time);
        Time.setText(GetTime(position));

        Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent CommentIntent = new Intent(context, Match_Out_NewsFeed_Comment.class);
                CommentIntent.putExtra("Num", arrData.get(position).getnum());
                CommentIntent.putExtra("Court", arrData.get(position).getcourt());
                CommentIntent.putExtra("Data", arrData.get(position).getdata());
                CommentIntent.putExtra("Time", GetTime(position));
                CommentIntent.putExtra("Id", arrData.get(position).getuser());
                context.startActivity(CommentIntent);
            }
        });
        TextView Data = (TextView) convertView.findViewById(R.id.NewsFeed_CustomList_Data);
        Data.setText(arrData.get(position).getdata());

        Data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent CommentIntent = new Intent(context, Match_Out_NewsFeed_Comment.class);
                CommentIntent.putExtra("Num", arrData.get(position).getnum());
                CommentIntent.putExtra("Court", arrData.get(position).getcourt());
                CommentIntent.putExtra("Data", arrData.get(position).getdata());
                CommentIntent.putExtra("Time", GetTime(position));
                CommentIntent.putExtra("Id", arrData.get(position).getuser());
                context.startActivity(CommentIntent);
            }
        });

        ImageButton NewsFeed_Modify_Button = (ImageButton) convertView.findViewById(R.id.NewsFeed_Modify_Button);
        if (arrData.get(position).getuser().equals(UserID)) {
            NewsFeed_Modify_Button.setVisibility(View.VISIBLE);
        } else {
            NewsFeed_Modify_Button.setVisibility(View.GONE);
        }

        NewsFeed_Modify_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserID.equals(arrData.get(position).getuser())) {
                    Intent DataIntent = new Intent(context, Match_Out_NewsFeed_Data_Modify.class);

                    DataIntent.putExtra("Num", arrData.get(position).getnum());
                    DataIntent.putExtra("Do", arrData.get(position).getDo());
                    DataIntent.putExtra("Si", arrData.get(position).getSi());
                    DataIntent.putExtra("court", arrData.get(position).getcourt());
                    DataIntent.putExtra("data", arrData.get(position).getdata());
                    DataIntent.putExtra("month", arrData.get(position).getMonth());
                    DataIntent.putExtra("day", arrData.get(position).getDay());
                    DataIntent.putExtra("hour", arrData.get(position).getHour());
                    DataIntent.putExtra("minute", arrData.get(position).getMinute());
                    DataIntent.putExtra("Image", arrData.get(position).getImage());
                    DataIntent.putExtra("MaxNum", String.valueOf(MaxNum));

                    context.startActivity(DataIntent);

                } else {
                    Toast.makeText(context, "사용자를확인해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });


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
                } else if (Hour > 0 && Minute <= 0) {
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
                if (Hour > 1 && Minute > 0) {
                    return Hour + "시간전";
                } else if (Hour > 1 && Minute < 0) {
                    return Hour - 1 + "시간전";
                } else if (Hour == 1 && Minute > 0) {
                    return Hour + "시간전";
                } else if (Hour == 1 && Minute < 0) {
                    return 60 + Minute + "분전";
                } else if (Hour == 0 && Minute > 0) {
                    return Minute + "분전";
                } else if (Hour == 0 && Minute < 0) {
                    return 60 + Minute + "분전";
                } else {
                    return "방금전";
                }
            }
        }
        return "Time Error";
    }


}
