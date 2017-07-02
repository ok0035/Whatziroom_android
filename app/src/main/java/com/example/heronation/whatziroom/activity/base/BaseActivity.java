package com.example.heronation.whatziroom.activity.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.heronation.whatziroom.R;

/**
 * Created by heronation on 2017-05-22.
 */

public class BaseActivity extends AppCompatActivity {

    public static Context mContext = null;

    //메인 액션바
    public ImageView backBtn;
    public TextView titleTxt;
    public TextView configTxt1;
    public TextView configTxt2;

    //룸 액션바
    public TextView btnRoomExit;
    public TextView btnRoomSchedule;
    public TextView btnRoomSetting;

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

    public void setMainActionBar() {

        ActionBar myActionBar = getSupportActionBar();
        myActionBar.setDisplayShowHomeEnabled(false);
        myActionBar.setDisplayHomeAsUpEnabled(false);
        myActionBar.setDisplayShowTitleEnabled(false);
        myActionBar.setDisplayShowCustomEnabled(true);
        myActionBar.setHomeButtonEnabled(true);

//        myActionBar.setHomeAsUpIndicator(R.mipmap.hambutton);

        LayoutInflater inf = LayoutInflater.from(mContext);
        View customBarView = inf.inflate(R.layout.actionbar_main, null);

        myActionBar.setCustomView(customBarView);
        myActionBar.setDisplayShowCustomEnabled(true);

        Toolbar parent = (Toolbar) customBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        getSupportActionBar().setElevation(0);


        backBtn = (ImageView) customBarView.findViewById(R.id.backBtn);
        titleTxt = (TextView) customBarView.findViewById(R.id.titleTxt);
        configTxt1 = (TextView) customBarView.findViewById(R.id.configTxt1);
        configTxt2 = (TextView) customBarView.findViewById(R.id.configTxt2);

    }

    public void setRoomActionBar() {

        ActionBar myActionBar = getSupportActionBar();
        myActionBar.setDisplayShowHomeEnabled(false);
        myActionBar.setDisplayHomeAsUpEnabled(false);
        myActionBar.setDisplayShowTitleEnabled(false);
        myActionBar.setDisplayShowCustomEnabled(true);
        myActionBar.setHomeButtonEnabled(true);

//        myActionBar.setHomeAsUpIndicator(R.mipmap.hambutton);

        LayoutInflater inf = LayoutInflater.from(mContext);
        View customBarView = inf.inflate(R.layout.actionbar_room, null);

        this.btnRoomSetting = (TextView) customBarView.findViewById(R.id.btnRoomSetting);
        this.btnRoomSchedule = (TextView) customBarView.findViewById(R.id.btnRoomSchedule);
        this.btnRoomExit = (TextView) customBarView.findViewById(R.id.btnRoomExit);

        myActionBar.setCustomView(customBarView);
        myActionBar.setDisplayShowCustomEnabled(true);

        Toolbar parent = (Toolbar) customBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        getSupportActionBar().setElevation(0);


    }

}
