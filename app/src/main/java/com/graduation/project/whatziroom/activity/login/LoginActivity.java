package com.graduation.project.whatziroom.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.graduation.project.whatziroom.R;
import com.graduation.project.whatziroom.activity.base.BaseActivity;
import com.graduation.project.whatziroom.activity.main.MainViewPager;
import com.graduation.project.whatziroom.network.HttpNetwork;
import com.graduation.project.whatziroom.network.Params;

/**
 * Created by heronation on 2017-05-22.
 */

public class LoginActivity extends BaseActivity {

    private TextView login_btn;
    private android.widget.ImageView imgmypage;
    private TextView tvloginid;
    private android.widget.EditText edloginid;
    private android.widget.LinearLayout layoutid;
    private android.widget.EditText edloginpw;
    private TextView btnlogin;
    private TextView btnguest;
    private TextView btnsignup;
    private TextView tvforget;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

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

                Params params = new Params();

                params.add("ID", edloginid.getText().toString());
                params.add("PW", edloginpw.getText().toString());

                new HttpNetwork("Login.php", params.getParams(), new HttpNetwork.AsyncResponse() {
                    @Override
                    public void onSuccess(String response) {
                        switch (response) {
                            case "success":
                                Toast.makeText(LoginActivity.this, "로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent successIntent = new Intent(getApplicationContext(), MainViewPager.class);
                                startActivity(successIntent);
                                break;

                            case "fail":

                                Toast.makeText(LoginActivity.this, "아이디 또는 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
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

            }
        });

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
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
        this.tvforget = (TextView) findViewById(R.id.tv_forget);
        this.btnsignup = (TextView) findViewById(R.id.btn_signup);
//        this.btnguest = (TextView) findViewById(R.id.btn_guest);
//        this.btnlogin = (TextView) findViewById(R.id.btn_login);
        this.edloginpw = (EditText) findViewById(R.id.ed_login_pw);
        this.layoutid = (LinearLayout) findViewById(R.id.layout_id);
        this.edloginid = (EditText) findViewById(R.id.ed_login_id);
        this.tvloginid = (TextView) findViewById(R.id.tv_login_id);
        this.imgmypage = (ImageView) findViewById(R.id.img_mypage);


    }


}
