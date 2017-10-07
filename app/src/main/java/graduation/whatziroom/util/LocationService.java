package graduation.whatziroom.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

import graduation.whatziroom.network.HttpNetwork;
import graduation.whatziroom.network.Params;

import static graduation.whatziroom.activity.base.BaseActivity.mContext;

/**
 * Created by mapl0 on 2017-10-03.
 */

public class LocationService extends Service {

    private IBinder mIBinder = new LocationBinder();
    private LocationListener locationListener;
    private android.location.LocationManager locationManager;

    private static boolean flag = false;
    private int interval = 5000;

    public double longitude;
    public double latitude;


    public class LocationBinder extends Binder {
        public LocationService getService(){
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

        unregisterRestartAlarm();

        //간격이 0이면 사용하지 않는 것으로 간주
        if(interval != 0) {

            getLocation();
        }

        return START_STICKY;
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

    public String GetDevicesUUID(){
        final TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = deviceUuid.toString();
        return deviceId;
    }

    public void getLocation() {

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
                    params.add("UUID", GetDevicesUUID());
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

                    flag = true;

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

            for (String name : m_lstProviders) {

                System.out.println("Location In Manager..." + interval);
                locationManager.requestLocationUpdates(name, interval, 5, locationListener);

            }

            new Runnable() {
                @Override
                public void run() {

                    Log.d("test", "location");

                }
            };

        } else {
            Log.e("GPS Enable", "false");

            Toast.makeText(mContext, "GPS가 꺼져있습니다. GPS를 켜주시기 바랍니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mContext.startActivity(intent);

        }
    }

//    private void function() {
//
//        Log.d(TAG, "========================");
//        Log.d(TAG, "function()");
//        Log.d(TAG, "========================");
//
//    }

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