package graduation.whatziroom.util;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import graduation.whatziroom.R;
import graduation.whatziroom.activity.base.SplashActivity;
import graduation.whatziroom.network.HttpNetwork;
import graduation.whatziroom.network.Params;

/**
 * Created by mapl0 on 2017-10-03.
 */

public class LocationService extends Service {

    //Service
    private IBinder mIBinder = new LocationBinder();

    //Location
    private LocationListener locationListener;
    private android.location.LocationManager locationManager;
    public double longitude;
    public double latitude;
    private int locationInterval = 3000;
    private int locationDistance = 10;

    //Notification
    Intent mMainIntent;
    PendingIntent mPendingIntent;
    Context mContext;

    //UserPKey
    private String UserPKey;

    //Schedule
    private String place;
    private long dSec;
    private long dMin;
    private long dHour;
    private long dDay;

    //Timer
    private Timer timer;
    private TimerTask task;
    private int timerInterval = 3000;
    private boolean locationFlag = false;

    public class LocationBinder extends Binder {
        public LocationService getService() {
            return LocationService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("LOG", "onBind()");
        //앱에서 서비스를 사용할때 호출된다.
        //이곳이 호출되어야 액티비티에 값을 전달 할 수 있다.

        return mIBinder;
    }

    @Override
    public void onCreate() {
        Log.e("LOG", "onCreate()");

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("LOG", "onStartCommand()");
        //서비스가 시작되면 이곳에 도착한다. 즉 여기서 원하는 이벤트를 주면 된다. OnCreate에서 해주려고 했으나 intent 값을 제대로 받아오지 못한다.
        UserPKey = intent.getStringExtra("UserPKey");
        if(UserPKey != null)
            Log.d("UserPKeyInService", UserPKey);

        mContext = this;

        mMainIntent = new Intent(this, SplashActivity.class);
        mPendingIntent = PendingIntent.getActivity(this, 1, mMainIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //간격이 0이면 사용하지 않는 것으로 간주

        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {

                update();

                if(dMin <= 60 && dMin > 0 && locationInterval != 0) {
                    Log.d("StartLocationService", "Here");
                    getLocation(locationInterval, locationDistance);

                } else if(dSec <= 0) {
//                    registerRestartAlarm();
                    stopSelf();
                }

            }
        };
        timer.schedule(task, 0, timerInterval);
        unregisterRestartAlarm();

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        Log.e("LOG", "onDestroy()");
        //서비스가 종료되면 다시 서비스를 재요청해서 백그라운드에 계속 살아남게 한다.
        //찾아본 결과 앱 정보에서 강제 정지하면 이 부분이 안먹힌다고 한다. 완전한 좀비는 아니라는 것...
//        sendBroadcast(new Intent("LocationReceiver"));

        registerRestartAlarm();
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        //언바운드되면 다시 서비스를 재요청해서 백그라운드에 계속 살아남게 한다.
        Log.e("LOG", "onUnbind()");
        registerRestartAlarm();
        return super.onUnbind(intent);
    }

    public String GetDevicesUUID() {
        final TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = deviceUuid.toString();
        return deviceId;
    }

    public void update() {

        Params params = new Params();
        params.add("UserPKey", UserPKey);
        params.add("Limit", 1 + "");

        new HttpNetwork("GetScheduleList.php", params.getParams(), new HttpNetwork.AsyncResponse() {
            @Override
            public void onSuccess(String response) {
                if (!response.equals("[]")) {

                    try {
                        ParseData parse = new ParseData();
                        JSONArray roomList = parse.parseJsonArray(response);

                        for (int i = 0; i < roomList.length(); i++) {

                            JSONObject jsonScheduleData = new JSONObject(roomList.get(i).toString());

                            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = transFormat.parse(jsonScheduleData.getString("Time"));

                            Calendar CalculateDate= Calendar.getInstance();

                            CalculateDate.setTime(date);
                            long goalTime = CalculateDate.getTimeInMillis();

                            CalculateDate.setTime(Calendar.getInstance().getTime());
                            long nowTime = CalculateDate.getTimeInMillis();

                            long dTime = (goalTime - nowTime);

                            //밀리세컨드를 밀리초, 초, 분, 시간 으로 나눔
                            dSec = dTime / 1000;
                            dMin = dSec / 60;
                            dHour = dMin / 60;
                            dDay = dHour / 24;
                            place = jsonScheduleData.getString("Place");

                            String message  = "";

                            if(dDay > 0) message = dDay + "일 뒤에 " + place + "에서 일정이 있습니다.";
                            else if(dHour > 0) message = dHour + "시간 " + dMin + "분 뒤에 " + place + "에서 일정이 있습니다.";
                            else message = dMin + "분 뒤에 " + place + "에서 일정이 있습니다.";

                            NotificationCompat.Builder mBuilder =
                                    new NotificationCompat.Builder(mContext)
                                            .setSmallIcon(R.drawable.logo)
                                            .setContentTitle(message)
                                            .setContentIntent(mPendingIntent)
                                            .setContentText("와찌룸으로 이동하려면 여기를 누르세요.")
                                            .setOngoing(true);

                            NotificationManager mNotifyMgr =
                                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                            mNotifyMgr.notify(001, mBuilder.build());
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();

                    } catch (ParseException e) {
                        e.printStackTrace();

                    }
                } else {

                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(mContext)
                                    .setSmallIcon(R.drawable.logo)
                                    .setContentTitle("잊어버린 약속은 없으신가요?")
                                    .setContentIntent(mPendingIntent)
                                    .setContentText("와찌룸으로 이동하려면 여기를 누르세요.")
                                    .setOngoing(true);

                    NotificationManager mNotifyMgr =
                            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                    mNotifyMgr.notify(001, mBuilder.build());

                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//        long[] vibratePattern = {300,100,100};
//        vibrator.vibrate(vibratePattern, -1);

                    vibrator.vibrate(500);
                }
            }

            @Override
            public void onFailure(String response) {

            }

            @Override
            public void onPreExcute() {

            }
        });

    }

    public void getLocation(final int interval, final int distance) {

        String message = "D - " + dMin + "min / " + place;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(message)
                        .setContentIntent(mPendingIntent)
                        .setContentText("위치공유가 시작되었습니다. 친구들의 위치를 확인하세요!")
                        .setOngoing(true);

        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mNotifyMgr.notify(001, mBuilder.build());

        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);

        locationManager = (android.location.LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        boolean isGPSEnabled = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER);

        if (isGPSEnabled || isNetworkEnabled) {
            Log.e("GPS Enable", "true");

            final List<String> m_lstProviders = locationManager.getProviders(false);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Log.e("onLocationChanged", "onLocationChanged ----- Here is Service");
                    Log.e("location", "[" + location.getProvider() + "] (" + location.getLatitude() + "," + location.getLongitude() + ")");
//                    locationManager.removeUpdates(locationListener);

                    longitude = location.getLongitude();
                    latitude = location.getLatitude();

                    Params params = new Params();
                    params.add("UserPKey", UserPKey);
                    params.add("Longitude", longitude + "");
                    params.add("Latitude", latitude + "");

                    new HttpNetwork("UpdateUserLocation.php", params.getParams(), new HttpNetwork.AsyncResponse() {
                        @Override
                        public void onSuccess(String response) {
                            Log.d("LocationNetwork", response);
                        }

                        @Override
                        public void onFailure(String response) {

                        }

                        @Override
                        public void onPreExcute() {

                        }
                    });

                    Log.d("경도 : ", location.getLongitude() + "\n 위도 : " + location.getLatitude());
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    Log.e("onStatusChanged", "onStatusChanged");
                }

                @Override
                public void onProviderEnabled(String provider) {
                    Log.e("onProviderEnabled", "onProviderEnabled");

                }

                @Override
                public void onProviderDisabled(String provider) {
                    Log.e("onProviderDisabled", "onProviderDisabled");
                }
            };

            // QQQ: 시간, 거리를 0 으로 설정하면 가급적 자주 위치 정보가 갱신되지만 베터리 소모가 많을 수 있다.

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for (String name : m_lstProviders) {

                        System.out.println("Location In Manager..." + interval);
                        locationManager.requestLocationUpdates(name, interval, distance, locationListener);

                    }
                }
            });

            new Runnable() {
                @Override
                public void run() {

                    Log.d("test", "location");

                }
            };

        } else if(!(isGPSEnabled || isNetworkEnabled)) {

            Log.e("GPS Enable", "false");

            Toast.makeText(mContext, "GPS가 꺼져있습니다. GPS를 켜주시기 바랍니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mContext.startActivity(intent);

        }

    }

    /**
     * 서비스가 시스템에 의해서 또는 강제적으로 종료되었을 때 호출되어
     * 알람을 등록해서 10초 후에 서비스가 실행되도록 한다.
     */
    private void registerRestartAlarm() {

        Log.d("LocationService", "registerRestartAlarm()");

        Intent intent = new Intent(LocationService.this, LocationReceiver.class);
        intent.setAction("LocationReceiver");
        PendingIntent sender = PendingIntent.getBroadcast(LocationService.this, 0, intent, 0);

        long firstTime = SystemClock.elapsedRealtime();
        firstTime += 10000; // 10초 후에 알람이벤트 발생

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, 6000, sender);

//        //앱이 종료되면 바로 다시 앱 실행~!
//        Intent dialogIntent = new Intent(this, SplashActivity.class);
//        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(dialogIntent);
    }

    /**
     * 기존 등록되어있는 알람을 해제한다.
     */
    private void unregisterRestartAlarm() {

        Log.d("PersistentService", "unregisterRestartAlarm()");
        Intent intent = new Intent(LocationService.this, LocationReceiver.class);
        intent.setAction("LocationReceiver");
        PendingIntent sender = PendingIntent.getBroadcast(LocationService.this, 0, intent, 0);

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(sender);
    }

}
