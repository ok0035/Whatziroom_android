package com.example.heronation.whatziroom.activity.room;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.heronation.whatziroom.R;
import com.example.heronation.whatziroom.activity.base.BaseActivity;

public class RoomSettingActivity extends BaseActivity {

    private RadioButton noticeOnRadioBtn;
    private RadioButton noticeOffRadioBtn;
    private Spinner noticeWaySpinner;

    final String[] noticeWay = new String[]{"소리+진동" , "소리만", "진동만"};
    private ArrayAdapter<String> spinnerAdpater ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_setting);
        bindView();
        setValues();
        setUpEvents();
        setMainActionBar();

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
    public void setMainActionBar() {
        super.setMainActionBar();
        titleTxt.setText("와찌룸 설정");
        backBtn.setVisibility(View.INVISIBLE);
        configTxt1.setVisibility(View.INVISIBLE);
        configTxt2.setVisibility(View.VISIBLE);
        configTxt2.setImageResource(R.mipmap.btn_ok);
        configTxt2.setPadding(0,0,10,0);
//        configTxt2.setText("완료");

    }



    @Override
    public void bindView() {
        super.bindView();
        this.noticeWaySpinner = (Spinner) findViewById(R.id.noticeWaySpinner);
        this.noticeOffRadioBtn = (RadioButton) findViewById(R.id.noticeOffRadioBtn);
        this.noticeOnRadioBtn = (RadioButton) findViewById(R.id.noticeOnRadioBtn);


    }

}