package com.example.heronation.whatziroom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.example.heronation.whatziroom.R;

/**
 * Created by heronation on 2017-05-22.
 */

public class LoginActivity extends BaseActivity {

    private TextView login_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bindView();
        setValues();
        setUpEvents();

    }

    @Override
    public void setUpEvents() {
        super.setUpEvents();
        hideActionBar();

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void setValues() {
        super.setValues();
    }

    @Override
    public void bindView() {
        super.bindView();

        login_btn = (TextView) findViewById(R.id.btn_login);
    }

}
