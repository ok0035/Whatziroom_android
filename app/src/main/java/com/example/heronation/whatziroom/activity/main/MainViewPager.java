package com.example.heronation.whatziroom.activity.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.heronation.whatziroom.R;
import com.example.heronation.whatziroom.activity.ProfilUserSettingActivity;
import com.example.heronation.whatziroom.activity.base.BaseActivity;

/**
 * Created by ATIV on 2017-06-24.
 */

public class MainViewPager extends BaseActivity {
    private pagerAdapter vpAdapter;
    private ViewPager vp;
    private LinearLayout ll;
    private TextView tvFriend;
    private TextView tvRoom;
    private TextView tvSchedule;
    private TextView tvAlert;
    private TextView tvProfile;
    private LinearLayout linFriend;
    private LinearLayout linRoomBtn;
    private LinearLayout linSchedule;
    private LinearLayout linArlert;
    private LinearLayout linProfile;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setMainActionBar();

        setValues();
        bindView();
        setUpEvents();

        LinearLayout layout = (LinearLayout)ll.findViewWithTag(0);
        ll.findViewWithTag(0).setBackgroundColor(getResources().getColor(R.color.colorAccent));
        TextView textView = (TextView) (layout.getChildAt(0));
        textView.setTextColor(Color.WHITE);

    }

    @Override
    public void setUpEvents() {
        super.setUpEvents();

                /*
        *   버튼 클릭 시 페이지 이동
        *   movePageListener 에서 페이지 이동 시 이벤트 구현
        */

        linFriend.setOnClickListener(movePageListener);
        linFriend.setTag(0);
        linRoomBtn.setOnClickListener(movePageListener);
        linRoomBtn.setTag(1);
        linSchedule.setOnClickListener(movePageListener);
        linSchedule.setTag(2);
        linArlert.setOnClickListener(movePageListener);
        linArlert.setTag(3);
        linProfile.setOnClickListener(movePageListener);
        linProfile.setTag(4);


        /*
        *   Viewpase Adapter 설정
        */

        vpAdapter = new pagerAdapter(getSupportFragmentManager());
        vp.setAdapter(vpAdapter);
        vp.setCurrentItem(0); // 앱실행시 첫번째 화면

        /*
        * 뷰페이저 이동시 이벤트
        * customActionbar의 버튼 변경
        * edittext 키보드 자판 강제 내리기도 적용
        */

        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                int tag = vp.getCurrentItem();

                if(tag==position) changeTabColor(position);

                switch (position){
                    case 0:
                        titleTxt.setText("친구목록");
                        backBtn.setVisibility(View.INVISIBLE);
                        configTxt1.setVisibility(View.VISIBLE);
                        configTxt2.setVisibility(View.VISIBLE);
                        configTxt1.setImageResource(R.mipmap.btn_add_friend); // 친구 추가
                        configTxt2.setImageResource(R.mipmap.btn_edit);
//                        configTxt2.setText("편집");
                        break;
                    case 1:
                        titleTxt.setText("방 목록");
                        backBtn.setVisibility(View.INVISIBLE);
                        configTxt1.setVisibility(View.VISIBLE);
                        configTxt2.setVisibility(View.VISIBLE);
                        configTxt1.setImageResource(R.mipmap.btn_add);
                        configTxt2.setImageResource(R.mipmap.btn_edit);
//                        configTxt2.setText("편집");
                        break;
                    case 2:
                        titleTxt.setText("내 스케줄");
                        backBtn.setVisibility(View.INVISIBLE);
                        configTxt1.setVisibility(View.INVISIBLE);
                        configTxt2.setVisibility(View.INVISIBLE);
                        configTxt1.setImageResource(R.mipmap.btn_add_friend);
                        configTxt2.setImageResource(R.mipmap.btn_edit);
//                        configTxt2.setText("편집");
                        break;
                    case 3:
                        titleTxt.setText("알림");
                        backBtn.setVisibility(View.INVISIBLE);
                        configTxt1.setVisibility(View.INVISIBLE);
                        configTxt2.setVisibility(View.INVISIBLE);
                        configTxt1.setImageResource(R.mipmap.btn_add_friend);
                        configTxt2.setImageResource(R.mipmap.btn_edit);
//                        configTxt2.setText("편집");
                        break;
                    case 4:
                        titleTxt.setText("프로필");
                        backBtn.setVisibility(View.INVISIBLE);
                        configTxt1.setVisibility(View.INVISIBLE);
                        configTxt2.setVisibility(View.VISIBLE);
//                        configTxt2.setText("설정");
//                        configTxt2.setText("");
                        configTxt2.setImageResource(R.mipmap.btn_setting);
                        configTxt2.setOnClickListener(configProfilListener );
                        break;

                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    int position = vp.getCurrentItem();


                    // 원하는 페이지에 맞게 조건
                    InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(vp.getWindowToken(), 0);
                }
            }
        });

    }
    private void changeTabColor(int position){

        ll.findViewWithTag(position).setSelected(true);
        ll.findViewWithTag(position).setBackgroundColor(getResources().getColor(R.color.colorAccent));

        for(int i=0; i<5; i++) {

            LinearLayout layout = (LinearLayout)ll.findViewWithTag(i);
            TextView textView = (TextView) layout.getChildAt(0);

            if(i != position) {

                ll.findViewWithTag(i).setSelected(false);
                ll.findViewWithTag(i).setBackgroundColor(Color.WHITE);
                textView.setTextColor(getResources().getColor(R.color.colorAccent));

            } else
                textView.setTextColor(Color.WHITE);

        }

    }



    // 프래그먼트간 페이지 이동
    View.OnClickListener movePageListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            int tag = (int) v.getTag();
            vp.setCurrentItem(tag);

            int i = 0;
            while(i<5)
            {
                if(tag==i)
                {
                    LinearLayout layout = (LinearLayout)ll.findViewWithTag(i);
                    ll.findViewWithTag(i).setSelected(true);
                    ll.findViewWithTag(i).setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    TextView textView = (TextView) (layout.getChildAt(0));
                    textView.setTextColor(Color.WHITE);
                }
                else
                {
                    LinearLayout layout = (LinearLayout)ll.findViewWithTag(i);
                    ll.findViewWithTag(i).setSelected(false);
                    ll.findViewWithTag(i).setBackgroundColor(Color.WHITE);
                    TextView textView = (TextView) (layout.getChildAt(0));
                    textView.setTextColor(getResources().getColor(R.color.colorAccent));
                }
                i++;

            }
        }
    };

    // 프래그먼트 어댑터
    private class pagerAdapter extends FragmentStatePagerAdapter {
        public pagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new MainFragment1();
                case 1:
                    return new MainFragment2();
                case 2:
                    return new MainFragment3();
                case 3:
                    return new MainFragment4();
                case 4:
                    return new MainFragment5();
                default:
                    return null;
            }
        }

        // 총 프래그먼트 수
        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }

    @Override
    public void setValues() {
        super.setValues();
    }

    @Override
    public void setMainActionBar() {
        super.setMainActionBar();
        titleTxt.setText("친구목록");
        backBtn.setVisibility(View.INVISIBLE);
        configTxt1.setImageResource(R.mipmap.btn_add_friend);
        configTxt2.setImageResource(R.mipmap.btn_edit);
//        configTxt2.setText("편집");
    }

    // 상황에 따른 customActionbar 클릭 이벤트 리스너 클래스 모음//

    View.OnClickListener configProfilListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, ProfilUserSettingActivity.class);
            startActivity(intent);
        }
    };


    @Override
    public void bindView() {
        super.bindView();

        vp = (ViewPager)findViewById(R.id.vp);
        ll = (LinearLayout)findViewById(R.id.ll);

        this.vp = (ViewPager) findViewById(R.id.vp);
        this.ll = (LinearLayout) findViewById(R.id.ll);
        this.linProfile = (LinearLayout) findViewById(R.id.linProfile);
        this.tvProfile = (TextView) findViewById(R.id.tvProfile);
        this.linArlert = (LinearLayout) findViewById(R.id.linArlert);
        this.tvAlert = (TextView) findViewById(R.id.tvAlert);
        this.linSchedule = (LinearLayout) findViewById(R.id.linSchedule);
        this.tvSchedule = (TextView) findViewById(R.id.tvSchedule);
        this.linRoomBtn = (LinearLayout) findViewById(R.id.linRoomBtn);
        this.tvRoom = (TextView) findViewById(R.id.tvRoom);
        this.linFriend = (LinearLayout) findViewById(R.id.linFriend);
        this.tvFriend = (TextView) findViewById(R.id.tvFriend);

    }
}
