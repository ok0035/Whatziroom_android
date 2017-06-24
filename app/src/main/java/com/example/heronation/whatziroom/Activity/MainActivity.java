package com.example.heronation.whatziroom.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.example.heronation.whatziroom.R;

/**
 * Created by ATIV on 2017-06-24.
 */

public class MainActivity extends BaseActivity {
    private pagerAdapter vpAdapter;
    private ViewPager vp;
    private LinearLayout ll;

    private LinearLayout tab_first;
    private LinearLayout tab_second;
    private LinearLayout tab_third;
    private LinearLayout tab_fourth;
    private LinearLayout tab_fifth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setCustomActionBar();

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
        vp.setCurrentItem(0); // 앱실행시 첫번째 화면

        /*
        *   버튼 클릭 시 페이지 이동
        *   movePageListener 에서 페이지 이동 시 이벤트 구현
        */



        tab_first.setOnClickListener(movePageListener);
        tab_first.setTag(0);
        tab_second.setOnClickListener(movePageListener);
        tab_second.setTag(1);
        tab_third.setOnClickListener(movePageListener);
        tab_third.setTag(2);
        tab_fourth.setOnClickListener(movePageListener);
        tab_fourth.setTag(3);
        tab_fifth.setOnClickListener(movePageListener);
        tab_fifth.setTag(4);

        tab_first.setSelected(true);
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
                    ll.findViewWithTag(i).setSelected(true);
                }
                else
                {
                    ll.findViewWithTag(i).setSelected(false);
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
                    return new SubMainFirstActivity();
                case 1:
                    return new SubMainSecondActivity();
                case 2:
                    return new SubMainThirdActivity();
                case 3:
                    return new SubMainFourthActivity();
                case 4:
                    return new SubMainFifthActivity();
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
    public void bindView() {
        super.bindView();

        vp = (ViewPager)findViewById(R.id.vp);
        ll = (LinearLayout)findViewById(R.id.ll);

        tab_first = (LinearLayout) findViewById(R.id.page1Btn);
        tab_second = (LinearLayout) findViewById(R.id.page2Btn);
        tab_third = (LinearLayout) findViewById(R.id.page3Btn);
        tab_fourth = (LinearLayout) findViewById(R.id.page4Btn);
        tab_fifth = (LinearLayout) findViewById(R.id.page5Btn);
    }




}
