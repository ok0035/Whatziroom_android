package com.graduation.project.whatziroom.activity.room;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.graduation.project.whatziroom.R;
import com.graduation.project.whatziroom.activity.base.BaseActivity;

/**
 * Created by mapl0 on 2017-09-16.
 */

public class RoomNoSchedule extends BaseActivity {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();
        setContentView(R.layout.no_schedule);
    }

    @Override
    public void setUpEvents() {
        super.setUpEvents();
    }

    @Override
    public void setValues() {
        super.setValues();
    }

    @Override
    public void bindView() {
        super.bindView();
    }
}
