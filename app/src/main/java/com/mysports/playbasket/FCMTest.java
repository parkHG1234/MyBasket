package com.mysports.playbasket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by 박효근 on 2016-09-12.
 */
public class FCMTest extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fcmtest);
        String token = FirebaseInstanceId.getInstance().getToken();
        // 이 token을 서버에 전달 한다.

        Log.i("token",token);
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        FirebaseInstanceId.getInstance().getToken();

    }

}
