package graduation.whatziroom.activity.main;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import graduation.whatziroom.R;
import graduation.whatziroom.activity.base.BaseActivity;
import graduation.whatziroom.network.DBSI;


public class
ProfilUserSettingActivity extends BaseActivity {

    private RadioButton noticeOnRadioBtn;
    private RadioButton noticeOffRadioBtn;
    private Spinner noticeWaySpinner;
    private DBSI dbsi;

    private int pushFlag;
    private int alarmFlag;

    final String[] noticeWay = new String[]{"소리+진동", "진동만", "소리만"};
    private ArrayAdapter<String> spinnerAdpater;
    private android.widget.RadioGroup noticeOnRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_user_setting);

        bindView();
        setValues();
        setUpEvents();
        setMainActionBar();

    }

    @Override
    public void setUpEvents() {
        super.setUpEvents();
        noticeOnRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.noticeOnRadioBtn : pushFlag = 0;
                        break;
                    case R.id.noticeOffRadioBtn : pushFlag = 1;
                        break;
                }

                Log.d("Push값", pushFlag+"");

            }
        });

        noticeWaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long ld) {
                alarmFlag = position;
                Log.d("alarm값",alarmFlag+"");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void setValues() {
        super.setValues();

        dbsi = new DBSI();

        pushFlag = Integer.parseInt(dbsi.selectQuery("Select Push from User Where PKey = " + MainViewPager.getUserPKey())[0][0]);
        alarmFlag = Integer.parseInt(dbsi.selectQuery("Select Alarm from User Where PKey = " + MainViewPager.getUserPKey())[0][0]);

        Log.d("Flag 값들 : ", pushFlag + "/" + alarmFlag);



        switch (pushFlag) {
            case 0:
                noticeOnRadioGroup.check(noticeOnRadioBtn.getId());
                break;
            default:
                noticeOnRadioGroup.check(noticeOffRadioBtn.getId());
                break;
        }


        spinnerAdpater = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, noticeWay);
        noticeWaySpinner.setAdapter(spinnerAdpater);
        spinnerAdpater.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        noticeWaySpinner.setSelection(alarmFlag);

    }


    @Override
    public void setMainActionBar() {
        super.setMainActionBar();
        titleTxt.setText("와찌룸 설정");
        backBtn.setVisibility(View.INVISIBLE);
        configTxt1.setVisibility(View.INVISIBLE);
        configTxt2.setVisibility(View.VISIBLE);
        configTxt2.setImageResource(R.mipmap.btn_ok);
        configTxt2.setPadding(0, 0, 10, 0);
        configTxt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        configTxt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbsi.query("Update User Set Push = "+ pushFlag +", Alarm = "+alarmFlag+" WHERE PKey = "+ MainViewPager.getUserPKey());

//                dbsi.selectQuery("Select * from User Where PKey = "+MainViewPager.getUserPKey());
                Toast.makeText(ProfilUserSettingActivity.this, "변경하신 내용이 저장되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }


    @Override
    public void bindView() {
        super.bindView();
        this.noticeWaySpinner = (Spinner) findViewById(R.id.noticeWaySpinner);
        this.noticeOnRadioGroup = (RadioGroup) findViewById(R.id.noticeOnRadioGroup);
        this.noticeOffRadioBtn = (RadioButton) findViewById(R.id.noticeOffRadioBtn);
        this.noticeOnRadioBtn = (RadioButton) findViewById(R.id.noticeOnRadioBtn);
    }

}
