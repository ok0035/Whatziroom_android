package com.example.heronation.whatziroom.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.heronation.whatziroom.R;
import com.example.heronation.whatziroom.activity.base.BaseActivity;

public class ProfilUpdateActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_update);
        setCustomActionBar();
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
    public void setCustomActionBar() {
        super.setCustomActionBar();
        titleTxt.setText("내 정보 수정");
        backBtn.setVisibility(View.VISIBLE);
        configTxt1.setVisibility(View.INVISIBLE);
        configTxt2.setVisibility(View.VISIBLE);
        configTxt2.setText("저장");

    }

    @Override
    public void bindView() {
        super.bindView();
    }


}
