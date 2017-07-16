package com.example.heronation.whatziroom.activity.room;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.example.heronation.whatziroom.R;
import com.example.heronation.whatziroom.activity.base.BaseActivity;

/**
 * Created by ATIV on 2017-07-16.
 */

public class RoomAddSchedule extends BaseActivity {
    // 날짜 선택 시 바뀌여야 하는 뷰,,
    private android.widget.LinearLayout isDateCheckView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_addschedule);
        setMainActionBar();

        bindView();
        setValues();
        setUpEvents();
    }

    @Override
    public void setUpEvents() {
        super.setUpEvents();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void setMainActionBar() {
        super.setMainActionBar();
        titleTxt.setText("스케쥴 입력");
        backBtn.setVisibility(View.VISIBLE);
        configTxt1.setVisibility(View.INVISIBLE);
        configTxt2.setVisibility(View.INVISIBLE);
    }


    @Override
    public void setValues() {
        super.setValues();
    }

    @Override
    public void bindView() {
        super.bindView();

        this.isDateCheckView = (LinearLayout) findViewById(R.id.isDateCheckView);
    }
}
