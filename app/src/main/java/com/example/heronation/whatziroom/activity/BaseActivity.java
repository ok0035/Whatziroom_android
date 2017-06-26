package com.example.heronation.whatziroom.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import com.example.heronation.whatziroom.R;

/**
 * Created by heronation on 2017-05-22.
 */

public class BaseActivity extends AppCompatActivity {

    public static Context mContext = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
    }

    public void setUpEvents() {
        //TODO - 이벤트 처리
    }

    public void setValues() {
        //TODO  - Set 설정

    }

    public void bindView() {
        //TODO - 레이아웃 ID초기화
    }

    // 액션바 숨김
    public void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.hide();
        }
    }

    public void setCustomActionBar() {

        ActionBar myActionBar = getSupportActionBar();
        myActionBar.setDisplayShowHomeEnabled(false);
        myActionBar.setDisplayHomeAsUpEnabled(false);
        myActionBar.setDisplayShowTitleEnabled(false);
        myActionBar.setDisplayShowCustomEnabled(true);
        myActionBar.setHomeButtonEnabled(true);

//        myActionBar.setHomeAsUpIndicator(R.mipmap.hambutton);

        LayoutInflater inf = LayoutInflater.from(mContext);
        View customBarView = inf.inflate(R.layout.main_action_bar, null);

        myActionBar.setCustomView(customBarView);
        myActionBar.setDisplayShowCustomEnabled(true);

        Toolbar parent = (Toolbar) customBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        getSupportActionBar().setElevation(0);

    }

}