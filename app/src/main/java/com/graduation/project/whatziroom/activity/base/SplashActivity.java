package com.graduation.project.whatziroom.activity.base;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.graduation.project.whatziroom.R;
import com.graduation.project.whatziroom.activity.login.LoginActivity;

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

                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);

                    finish();

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
