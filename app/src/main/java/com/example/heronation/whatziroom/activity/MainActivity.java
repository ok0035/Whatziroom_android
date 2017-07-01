package com.example.heronation.whatziroom.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
                switch (position){
                    case 0:
                        titleTxt.setText("친구목록");
                        backBtn.setVisibility(View.INVISIBLE);
                        configTxt1.setVisibility(View.VISIBLE);
                        configTxt2.setVisibility(View.VISIBLE);
                        configTxt1.setText("추가");
                        configTxt2.setText("편집");
                        break;
                    case 1:
                        titleTxt.setText("방 목록");
                        backBtn.setVisibility(View.INVISIBLE);
                        configTxt1.setVisibility(View.VISIBLE);
                        configTxt2.setVisibility(View.VISIBLE);
                        configTxt1.setText("방개설");
                        configTxt2.setText("편집");
                        break;
                    case 2:
                        titleTxt.setText("내 스케줄");
                        backBtn.setVisibility(View.INVISIBLE);
                        configTxt1.setVisibility(View.INVISIBLE);
                        configTxt2.setVisibility(View.INVISIBLE);
                        configTxt1.setText("추가");
                        configTxt2.setText("편집");
                        break;
                    case 3:
                        titleTxt.setText("알림");
                        backBtn.setVisibility(View.INVISIBLE);
                        configTxt1.setVisibility(View.INVISIBLE);
                        configTxt2.setVisibility(View.INVISIBLE);
                        configTxt1.setText("추가");
                        configTxt2.setText("편집");
                        break;
                    case 4:
                        titleTxt.setText("프로필");
                        backBtn.setVisibility(View.INVISIBLE);
                        configTxt1.setVisibility(View.INVISIBLE);
                        configTxt2.setVisibility(View.VISIBLE);
                        configTxt2.setText("설정");
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
    public void setCustomActionBar() {
        super.setCustomActionBar();
        titleTxt.setText("친구목록");
        backBtn.setVisibility(View.INVISIBLE);
        configTxt1.setText("추가");
        configTxt2.setText("편집");
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
