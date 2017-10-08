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

//        String body = remoteMessage.getData().get("body");
//        Log.d("remoteMessageBody..",body );
//
        Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        Log.d(TAG, "Message data payload2: " + remoteMessage.getData().get("body"));
//        remoteMessage.getData();


        sendNotification(remoteMessage);


    }

    private void sendNotification(RemoteMessage remoteMessage) {

        String msg = (remoteMessage.getData().get("txtMsg") == null )? "" :remoteMessage.getData().get("txtMsg");
        Log.d(TAG,"txtMSG.."+msg );

//        Intent intent = new Intent(this, MainViewPager.class);
//
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);

        Intent dummyIntent = new Intent();
        PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, dummyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String alramTitleTxt = "와찌룸";
        String messageBody = remoteMessage.getData().get("body");
        String alramContentTxt = remoteMessage.getData().get("body");

        switch (messageBody){
            case "RequestFriend" :
                alramContentTxt = "친구 요청이 들어왔어요!";
                break;
            case "RequestEnterRoom" : alramContentTxt = "방 입장 요청이 들어왔어요!";
                break;
            case "AcceptEnterRoom" : alramContentTxt = remoteMessage.getData().get("roomName") + "에 입장했습니다";
                break;
            case  "ChatMsg" :
                alramTitleTxt = remoteMessage.getData().get("sender") + " / " + remoteMessage.getData().get("roomName") ;
                alramContentTxt = remoteMessage.getData().get("txtMsg");
            default:
                break;
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(alramTitleTxt)
                .setContentText(alramContentTxt )
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }




}
