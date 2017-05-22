package com.example.heronation.whatziroom.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

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

}
