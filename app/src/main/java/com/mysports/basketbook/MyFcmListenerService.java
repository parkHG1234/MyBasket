package com.mysports.basketbook;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mysports.basketbook.R;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by 박효근 on 2016-09-12.
 */
public class MyFcmListenerService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMsgService";
    String approach;
    String sendTeam;
    String body;
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        try {
            String message = URLDecoder.decode(remoteMessage.getData().get("message"), "utf-8");
            //추가한것
            String str = message;
            String[] data = str.split(" / ");
            approach = data[0];
            sendTeam = data[1];
            Log.i("approach",approach);
            body = data[2];
            sendNotification(body);
        } catch (UnsupportedEncodingException e) {
        }
    }
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("approach",approach);
        intent.putExtra("sendTeam",sendTeam);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("바스켓북")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}
