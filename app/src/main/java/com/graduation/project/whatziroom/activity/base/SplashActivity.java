package com.graduation.project.whatziroom.activity.base;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.graduation.project.whatziroom.R;
import com.graduation.project.whatziroom.activity.login.LoginActivity;
import com.graduation.project.whatziroom.activity.main.MainViewPager;
import com.graduation.project.whatziroom.network.DBSI;
import com.graduation.project.whatziroom.network.HttpNetwork;
import com.graduation.project.whatziroom.network.Params;

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

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);

                    DBSI db = new DBSI();
                    String[][] user = db.selectQuery("select ID, PW from User");

                    if(user != null) {
                        Params params = new Params();
                        params.add("ID", user[0][0]);
                        params.add("PW", user[0][1]);

                        new HttpNetwork("AutoLogin.php", params.getParams(), new HttpNetwork.AsyncResponse() {
                            @Override
                            public void onSuccess(String response) {
                                switch (response) {

                                    case "success":
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
                        Intent failIntent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(failIntent);
                        finish();

                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }

    @Override
    public void setValues() {
        super.setValues();
    }

    @Override
    public void bindView() {
        super.bindView();
        this.LogoImage = (ImageView) findViewById(R.id.LogoImage);
    }
}
