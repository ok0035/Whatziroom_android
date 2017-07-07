package com.example.heronation.whatziroom.activity;

import android.os.Bundle;
import android.view.View;

import com.example.heronation.whatziroom.R;
import com.example.heronation.whatziroom.activity.base.BaseActivity;

public class ProfilUpdateActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_update);
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
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
//                overridePendingTransition(0, 0);
            }
        });
    }

    @Override
    public void setValues() {
        super.setValues();
    }


    @Override
    public void setMainActionBar() {
        super.setMainActionBar();
        titleTxt.setText("내 정보 수정");
        backBtn.setVisibility(View.VISIBLE);
        configTxt1.setVisibility(View.INVISIBLE);
        configTxt2.setVisibility(View.VISIBLE);
        configTxt2.setImageResource(R.mipmap.btn_ok);

    }

    @Override
    public void bindView() {
        super.bindView();
    }


}
