//package com.example.mybasket;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bumptech.glide.Glide;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.protocol.HTTP;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.List;
//
//import jp.wasabeef.glide.transformations.CropCircleTransformation;
//
///**
// * Created by 박지훈 on 2016-08-09.
// */
//
//public class User_Information_Adapter extends Activity {
//
//
//    private Context context;
//    private ArrayList<User_Information_Setting> InformationComment;
//    private LayoutInflater inflater;
//    private User_Information_Setting Information_Setting;
//
//
//    ImageView Layout_CustomDialog_TeamPlayer_Profile;
//    Button Layout_CustomDialog_TeamPlayer_TeamNameAndDuty;
//    Button Layout_CustomDialog_TeamPlayer_Name;
//    Button Layout_CustomDialog_TeamPlayer_Position;
//    Button Layout_CustomDialog_TeamPlayer_Age;
//    Button Layout_CustomDialog_TeamPlayer_Sex;
//
//    Match_Out_NewsFeed_Comment_Adapter CommentAdapter;
//    String[][] parsedData;
//    public User_Information_Adapter(Context c, ArrayList<User_Information_Setting> arr) {
//        this.context = c;
//        this.InformationComment = arr;
//        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//    }
//
//    String[] jsonName = {"Name", "Birth", "Sex", "Position", "Team", "Profile", "Height", "Weight", "Phone"};
//
//
//
//    public long getItemId(int position) {
//        return position;
//    }
//
//    public View getView(final int position, View convertView, ViewGroup parent) {
//        if (convertView == null) {
//            convertView = inflater.inflate(R.layout.layout_match_out_newsfeed_comment_data, parent, false);
//        }
//
//
//        Layout_CustomDialog_TeamPlayer_Profile = (ImageView) findViewById(R.id.Layout_CustomDialog_TeamPlayer_Profile);
//        try{
//            String En_Profile = URLEncoder.encode(InformationComment.get(position).getInformation_Profile(), "utf-8");
////            if (InformationComment.get(position).getInformation_Profile().equals(".")) {
//                Glide.with(context).load(R.drawable.profile_basic_image).bitmapTransform(new CropCircleTransformation(Glide.get(context).getBitmapPool()))
//                        .into(Layout_CustomDialog_TeamPlayer_Profile);
////            } else {
//                Glide.with(context).load("http://210.122.7.195:8080/Web_basket/imgs/Profile/" + En_Profile + ".jpg").bitmapTransform(new CropCircleTransformation(Glide.get(context).getBitmapPool()))
//                        .into(Layout_CustomDialog_TeamPlayer_Profile);
////            }
//        }
//        catch (UnsupportedEncodingException e){
//
//        }
//        Layout_CustomDialog_TeamPlayer_TeamNameAndDuty = (Button) findViewById(R.id.Layout_CustomDialog_TeamPlayer_TeamNameAndDuty);
////        Layout_CustomDialog_TeamPlayer_TeamNameAndDuty.setText(Information_Setting.);
//        Layout_CustomDialog_TeamPlayer_Name = (Button) findViewById(R.id.Layout_CustomDialog_TeamPlayer_Name);
//        Layout_CustomDialog_TeamPlayer_Name.setText(Information_Setting.getInformation_Name());
//
//        Layout_CustomDialog_TeamPlayer_Position = (Button) findViewById(R.id.Layout_CustomDialog_TeamPlayer_Position);
//        Layout_CustomDialog_TeamPlayer_Position.setText(Information_Setting.getInformation_Position());
//
//        Layout_CustomDialog_TeamPlayer_Age = (Button) findViewById(R.id.Layout_CustomDialog_TeamPlayer_Age);
//
//        Layout_CustomDialog_TeamPlayer_Sex = (Button) findViewById(R.id.Layout_CustomDialog_TeamPlayer_Sex);
//        Layout_CustomDialog_TeamPlayer_Sex.setText(Information_Setting.getInformation_Sex());
//
//
//        return convertView;
//    }
//}