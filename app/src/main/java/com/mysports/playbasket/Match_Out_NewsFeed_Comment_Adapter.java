package com.mysports.playbasket;

/**
 * Created by 박효근 on 2016-07-22.
 */
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by 박지훈 on 2016-06-28.
 */
public class Match_Out_NewsFeed_Comment_Adapter extends BaseAdapter {

    private Context context;
    View view;
    private ArrayList<Match_Out_NewsFeed_Comment_Setting> arrComment;
    private LayoutInflater inflater;
    static ListView NewSpeed_Comment_List;
    ImageView NewSpeed_Comment_List_Emblem;
    TextView NewsFeed_CommentList_User;
    TextView NewSpeed_Comment_List_Time;
    TextView NewSpeed_Comment_List_Data;
    ImageButton NewsFeed_CommentList_Setting_Button;
    ImageView NewSpeed_Comment_ImageView;
    static String ID,ProfileImage;

    Match_Out_NewsFeed_Comment_Adapter CommentAdapter;
    String[][] parsedData;
    String[] jsonName = {"Comment_Num", "NewsFeed_Num", "Comment_User", "Comment_Data", "Comment_Month", "Comment_Day", "Comment_Hour", "Comment_Minute", "Name", "Birth", "Sex", "Position", "Team", "Profile", "Height", "Weight", "Phone"};

    public Match_Out_NewsFeed_Comment_Adapter(Context c, ArrayList<Match_Out_NewsFeed_Comment_Setting> arr,String ID) {
        this.context = c;
        this.arrComment = arr;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.ID = ID;
    }



    public void listview(ListView listView) {
        this.NewSpeed_Comment_List = listView;
        return;
    }

    public int getCount() {
        return arrComment.size();
    }

    public Object getItem(int position) {
        return arrComment.get(position).getcomment_user();
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_match_out_newsfeed_comment_data, parent, false);
        }

        ProfileImage=arrComment.get(position).getInformation_Profile();
        NewSpeed_Comment_List_Emblem = (ImageView)convertView.findViewById(R.id.NewsFeed_CommentList_Emblem);
        if (ProfileImage.equals("")) {
            Glide.with(context).load(R.drawable.basic_image).into(NewSpeed_Comment_List_Emblem);
        } else {
            Glide.with(context).load("http://210.122.7.195:8080/Web_basket/imgs/Profile/" + ProfileImage + ".jpg").bitmapTransform(new CropCircleTransformation(Glide.get(context).getBitmapPool()))
                    .into(NewSpeed_Comment_List_Emblem);
        }

        NewSpeed_Comment_List_Time = (TextView) convertView.findViewById(R.id.NewsFeed_CommentList_Time);
        NewSpeed_Comment_List_Time.setText(GetTime(position));

        NewsFeed_CommentList_User = (TextView) convertView.findViewById(R.id.NewsFeed_CommentList_User);
        NewsFeed_CommentList_User.setText(arrComment.get(position).getcomment_user());


        NewSpeed_Comment_List_Data = (TextView) convertView.findViewById(R.id.NewsFeed_CommentList_Data);
        NewSpeed_Comment_List_Data.setText(arrComment.get(position).getdata());


        NewsFeed_CommentList_Setting_Button = (ImageButton) convertView.findViewById(R.id.NewsFeed_CommentList_Setting_Button);
        if (arrComment.get(position).getcomment_user().equals(ID)) {
            NewsFeed_CommentList_Setting_Button.setVisibility(View.VISIBLE);
        } else {
            NewsFeed_CommentList_Setting_Button.setVisibility(View.GONE);
        }
        NewsFeed_CommentList_Setting_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view =v;
                final CharSequence[] items = {"삭제"};
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//                alertDialogBuilder.setTitle("선택 목록 대화 상자");
                alertDialogBuilder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        String result = "";
                        switch (i) {
                            case 0:
                                try {
                                    if(ID.equals(arrComment.get(position).getcomment_user())) {
                                        HttpClient client = new DefaultHttpClient();
                                        String postURL = "http://210.122.7.195:8080/gg/newsfeed_comment_delete.jsp";
                                        HttpPost post = new HttpPost(postURL);
                                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                                        params.add(new BasicNameValuePair("Comment_Num", arrComment.get(position).getcomment_num()));
                                        params.add(new BasicNameValuePair("NewsFeed_Num", arrComment.get(position).getnewsfeed_num()));
                                        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                        post.setEntity(ent);
                                        HttpResponse response = client.execute(post);
                                        postURL = "http://210.122.7.195:8080/gg/newsfeed_comment_download.jsp";
                                        post = new HttpPost(postURL);
                                        params = new ArrayList<NameValuePair>();
                                        params.add(new BasicNameValuePair("NewsFeed_Num", arrComment.get(position).getnewsfeed_num()));
                                        ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                        post.setEntity(ent);
                                        response = client.execute(post);

                                        BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                        String line = null;
                                        while ((line = bufreader.readLine()) != null) {
                                            result += line;
                                        }
                                        parsedData = jsonParserList(result);
                                        setData();
                                        CommentAdapter = new Match_Out_NewsFeed_Comment_Adapter(context, arrComment,ID);
                                        NewSpeed_Comment_List.setAdapter(CommentAdapter);

                                    }
                                    else{
                                        Snackbar.make(view, "사용자를확인해주세요.", Snackbar.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

//        NewSpeed_Comment_ImageView = (ImageView)convertView.findViewById(R.id.NewSpeed_Comment_ImageView);
//        try {
//            if (Image.equals(".")) {
//                NewSpeed_Comment_ImageView.setVisibility(View.GONE);
//            } else {
//                NewSpeed_Comment_ImageView.setVisibility(View.VISIBLE);
//                String En_Profile = URLEncoder.encode((Image), "utf-8");
//                Glide.with(convertView.getContext()).load("http://210.122.7.195:8080/gg/imgs1/" + Image + ".jpg").into(NewSpeed_Comment_ImageView);
//            }
//        } catch (UnsupportedEncodingException e) {
//        }





        return convertView;
    }

    public String GetTime(int position) {
        String Time;
        Integer Month, Day, Hour, Minute;

        Month = (Integer.parseInt(new SimpleDateFormat("MM").format(new java.sql.Date(System.currentTimeMillis())))) - Integer.parseInt(arrComment.get(position).getMonth());
        Day = (Integer.parseInt(new SimpleDateFormat("dd").format(new java.sql.Date(System.currentTimeMillis())))) - Integer.parseInt(arrComment.get(position).getDay());
        Hour = (Integer.parseInt(new SimpleDateFormat("kk").format(new java.sql.Date(System.currentTimeMillis())))) - Integer.parseInt(arrComment.get(position).getHour());
        Minute = (Integer.parseInt(new SimpleDateFormat("mm").format(new java.sql.Date(System.currentTimeMillis())))) - Integer.parseInt(arrComment.get(position).getMinute());
        if (Month > 0) {
            Month = Integer.parseInt(arrComment.get(position).getMonth());
            Day = Integer.parseInt(arrComment.get(position).getDay());
            Hour = Integer.parseInt(arrComment.get(position).getHour());
            Minute = Integer.parseInt(arrComment.get(position).getMinute());
            Time = Month + "월 " + Day + "일 " + Hour + "시 " + Minute + "분 ";

            return Time;
        } else {
            if (Day > 0 && Day < 7) {
                Day = (Integer.parseInt(new SimpleDateFormat("dd").format(new java.sql.Date(System.currentTimeMillis())))) - Integer.parseInt(arrComment.get(position).getDay());
                return Day + "일전";
            } else if (Day > 6) {
                Month = Integer.parseInt(arrComment.get(position).getMonth());
                Day = Integer.parseInt(arrComment.get(position).getDay());
                Hour = Integer.parseInt(arrComment.get(position).getHour());
                Minute = Integer.parseInt(arrComment.get(position).getMinute());
                Time = Month + "월 " + Day + "일 " + Hour + "시 " + Minute + "분 ";
                return Time;
            } else {
                if (Hour > 1 && Minute > 0) {
                    return Hour + "시간전";
                } else if (Hour > 1 && Minute < 0) {
                    return Hour - 1 + "시간전";
                } else if (Hour == 1 && Minute >0) {
                    return Hour + "시간전";
                } else if (Hour == 1 && Minute < 0) {
                    return 60 + Minute + "분전";
                } else if (Hour == 0 && Minute >0) {
                    return Minute + "분전";
                } else if (Hour == 0 && Minute < 0) {
                    return 60 + Minute + "분전";
                } else {
                    return "방금전";
                }
            }
        }
    }

    private void setData() {
        arrComment = new ArrayList<Match_Out_NewsFeed_Comment_Setting>();
        for (int a = 0; a < parsedData.length; a++) {
            arrComment.add(new Match_Out_NewsFeed_Comment_Setting(parsedData[a][0], parsedData[a][1], parsedData[a][2], parsedData[a][3], parsedData[a][4], parsedData[a][5], parsedData[a][6], parsedData[a][7],
                    parsedData[a][8], parsedData[a][9], parsedData[a][10], parsedData[a][11], parsedData[a][12], parsedData[a][13], parsedData[a][14], parsedData[a][15], parsedData[a][16]));
        }
    }

    public String[][] jsonParserList(String pRecvServerPage) {
        Log.i("새로운 받은 전체 내용", pRecvServerPage);
        try {

            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");

            parsedData = new String[jArr.length()][jsonName.length];
            for (int i = 0; i < jArr.length(); i++) {
                json = jArr.getJSONObject(i);
                for (int j = 0; j < jsonName.length; j++) {
                    parsedData[i][j] = json.getString(jsonName[j]);
                }
            }
            return parsedData;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
