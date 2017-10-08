package graduation.whatziroom.Firebase;

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

import graduation.whatziroom.R;
import graduation.whatziroom.activity.base.SplashActivity;
import graduation.whatziroom.activity.main.MainViewPager;

/**
 * Created by user on 2017-10-04.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FirebaseMsgService";

    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String body = remoteMessage.getData().get("body");
//        Log.d("remoteMessageBody..",body );

        Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        Log.d(TAG, "Message data payload2: " + remoteMessage.getData().get("body"));
//        remoteMessage.getData();
        String msg = (remoteMessage.getData().get("txtMsg") == null )? "" :remoteMessage.getData().get("txtMsg");
        sendNotification(body, msg);


    }

    private void sendNotification(String messageBody, String chatMsg) {
        Intent intent = new Intent(this, MainViewPager.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String alramTxt = messageBody;

        switch (messageBody){
            case "RequestFriend" : alramTxt = "친구 요청이 들어왔어요!";
                break;
            case "RequestEnterRoom" : alramTxt = "방 입장 요청이 들어왔어요!";
                break;
            case  "ChatMsg" : alramTxt = chatMsg;
            default:
                break;
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("WhatZiroom")
                .setContentText(alramTxt)
                .setAutoCancel(true)
                .setSound(defaultSoundUri);
//                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }




}
