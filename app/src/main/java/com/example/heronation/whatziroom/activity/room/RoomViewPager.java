package com.example.heronation.whatziroom.activity.room;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.example.heronation.whatziroom.R;
import com.example.heronation.whatziroom.activity.base.BaseActivity;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by heronation on 2017-05-22.
 */

public class RoomViewPager extends BaseActivity {

    private pagerAdapter vpAdapter;
    private ViewPager vp;
    private LinearLayout ll;
    private me.relex.circleindicator.CircleIndicator indicator;
    private LinearLayout linIndicator;
    private LinearLayout linRoom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);


        setRoomActionBar();

        setValues();
        bindView();
        setUpEvents();

    }

    @Override
    public void setUpEvents() {
        super.setUpEvents();

        /*
        *   Viewpase Adapter 설정
        */

        vpAdapter = new pagerAdapter(getSupportFragmentManager());

        vp.setAdapter(vpAdapter);
        indicator.setViewPager(vp);
        vp.setCurrentItem(0); // 앱실행시 첫번째 화면

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 1) {
                    indicator.setVisibility(View.GONE);
                } else {
                    indicator.setVisibility(View.VISIBLE);
                }

                switch(position) {
                    case 0:
                        btnRoomSchedule.setVisibility(View.VISIBLE);
                        btnRoomSetting.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                    case 2:
                        btnRoomSchedule.setVisibility(View.GONE);
                        btnRoomSetting.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    // 프래그먼트 어댑터
    private class pagerAdapter extends FragmentStatePagerAdapter {
        public pagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:

                    return new RoomFragment1();
                case 1:

                    return new RoomFragment2();
                case 2:

                    return new RoomFragment3();
                default:
                    return null;
            }
        }

        // 총 프래그먼트 수
        @Override
        public int getCount() {
            return 3;
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
    public void bindView() {
        super.bindView();

        this.vp = (ViewPager) findViewById(R.id.vp);
        this.indicator = (CircleIndicator) findViewById(R.id.indicator);
        this.linRoom = (LinearLayout) findViewById(R.id.linRoom);
        this.vp = (ViewPager) findViewById(R.id.vp);

    }
}
