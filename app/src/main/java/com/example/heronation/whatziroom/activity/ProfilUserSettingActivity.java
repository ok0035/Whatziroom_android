package com.example.heronation.whatziroom.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.heronation.whatziroom.R;
import com.example.heronation.whatziroom.activity.base.BaseActivity;

public class ProfilUserSettingActivity extends BaseActivity {

    private android.widget.RadioButton noticeOnRadioBtn;
    private android.widget.RadioButton noticeOffRadioBtn;
    private android.widget.Spinner noticeWaySpinner;

    final String[] noticeWay = new String[]{"소리+진동" , "소리만", "진동만"};
    private ArrayAdapter<String> spinnerAdpater ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_user_setting);
        bindView();
        setValues();
        setUpEvents();
        setCustomActionBar();

    }

    @Override
    public void setUpEvents() {
        super.setUpEvents();
    }

    @Override
    public void setValues() {
        super.setValues();
        noticeOnRadioBtn.setChecked(true);
//        noticeOffRadioBtn.setChecked(true);

        spinnerAdpater = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, noticeWay);
        noticeWaySpinner.setAdapter(spinnerAdpater);
        spinnerAdpater.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
    }


    @Override
    public void setCustomActionBar() {
        super.setCustomActionBar();
        titleTxt.setText("와찌룸 설정");
        backBtn.setVisibility(View.INVISIBLE);
        configTxt1.setVisibility(View.INVISIBLE);
        configTxt2.setVisibility(View.VISIBLE);
        configTxt2.setText("완료");

    }



    @Override
    public void bindView() {
        super.bindView();
        this.noticeWaySpinner = (Spinner) findViewById(R.id.noticeWaySpinner);
        this.noticeOffRadioBtn = (RadioButton) findViewById(R.id.noticeOffRadioBtn);
        this.noticeOnRadioBtn = (RadioButton) findViewById(R.id.noticeOnRadioBtn);
    }

}
