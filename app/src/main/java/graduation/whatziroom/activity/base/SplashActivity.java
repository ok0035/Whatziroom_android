package graduation.whatziroom.activity.base;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import graduation.whatziroom.R;
import graduation.whatziroom.activity.login.LoginActivity;
import graduation.whatziroom.activity.main.MainViewPager;
import graduation.whatziroom.network.DBSI;
import graduation.whatziroom.network.HttpNetwork;
import graduation.whatziroom.network.Params;


public class SplashActivity extends BaseActivity {

    private ImageView LogoImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        bindView();
        setValues();
        setUpEvents();
    }

    @Override
    public void setUpEvents() {
        super.setUpEvents();

        hideActionBar();



        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                final Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                        }

                        if(ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                            timer.cancel();
                        }
                    }
                };

                timer.schedule(task, 0, 100);

                DBSI db = new DBSI();
                String[][] user = db.selectQuery("select ID, PW, AutoLogin from User");

                if(user != null) {
                    Params params = new Params();
                    params.add("ID", user[0][0]);
                    params.add("PW", user[0][1]);
                    params.add("UUID", GetDevicesUUID(mContext));

                    if(user[0][2].equals("1")) {

                        new HttpNetwork("AutoLogin.php", params.getParams(), new HttpNetwork.AsyncResponse() {
                            @Override
                            public void onSuccess(String response) {
                                switch (response) {

                                    case "success":
                                        Toast.makeText(SplashActivity.this, "로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                                        Intent successIntent = new Intent(getApplicationContext(), MainViewPager.class);
                                        startActivity(successIntent);
                                        finish();
                                        break;

                                    case "fail":
                                        Intent failIntent = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(failIntent);
                                        finish();
                                        break;

                                }
                            }

                            @Override
                            public void onFailure(String response) {

                            }

                            @Override
                            public void onPreExcute() {

                            }

                        });
                    } else {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }



                } else {
                    Intent failIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(failIntent);
                    finish();

                }
            }
        }, 2000);

//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(2000);
//
//
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

//        thread.start();

    }

    @Override
    public void setValues() {
        super.setValues();
    }

    @Override
    public void bindView() {
        super.bindView();
        this.LogoImage = findViewById(R.id.LogoImage);
    }
}
