package graduation.whatziroom.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by mapl0 on 2017-10-03.
 */

public class LocationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

//        String name = intent.getAction();
//        Toast.makeText
//                (context, "정상적으로 값을 받았습니다.", Toast.LENGTH_SHORT).show();

        context.startService(new Intent(context, LocationService.class));
    }

}
