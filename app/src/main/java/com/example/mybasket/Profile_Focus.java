package com.example.mybasket;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by 박효근 on 2016-07-14.
 */
public class Profile_Focus extends AppCompatActivity {
    static String Profile;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_profile_focus);
        Intent intent = getIntent();
        Profile = intent.getStringExtra("Profile");
        ImageView Profile_Focus_ImageVIew = (ImageView)findViewById(R.id.Profile_Focus_ImageVIew);
        try{
            String En_Profile = URLEncoder.encode(Profile, "utf-8");
            if(Profile.equals(".")) {
                Glide.with(Profile_Focus.this).load(R.drawable.profile_basic_image).into(Profile_Focus_ImageVIew);
            }
            else{
                Glide.with(Profile_Focus.this).load("http://210.122.7.195:8080/Web_basket/imgs/Profile/" + En_Profile + ".jpg")
                        .into(Profile_Focus_ImageVIew);

            }
        }
        catch (UnsupportedEncodingException e){

        }
    }
}
