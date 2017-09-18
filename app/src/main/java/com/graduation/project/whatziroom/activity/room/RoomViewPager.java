package com.graduation.project.whatziroom.activity.room;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.graduation.project.whatziroom.R;
import com.graduation.project.whatziroom.activity.base.BaseActivity;
import com.graduation.project.whatziroom.network.HttpNetwork;
import com.graduation.project.whatziroom.network.Params;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by heronation on 2017-05-22.
 */

public class RoomViewPager extends BaseActivity {

    //    private pagerAdapter vpAdapter;
    private ViewPager vp;
    private LinearLayout ll;
    private me.relex.circleindicator.CircleIndicator indicator;
    private LinearLayout linIndicator;
    private LinearLayout linRoom;

    private RoomInfoFragment roomInfoView;
    private RoomChatFragment roomChatView;
    private RoomFriendList roomFriendList;

    private String roomPKey;
    private String result = "notEmpty";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room);
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

        Intent intentPKey = getIntent();

        roomPKey = intentPKey.getStringExtra("PKey");
        Log.d("RoomPKey", roomPKey);

        Params params = new Params();
        params.add("PKey", roomPKey);

        new HttpNetwork("IsEmptySchedule.php", params.getParams(), new HttpNetwork.AsyncResponse() {

            @Override
            public void onSuccess(String response) {
                Log.d("re", response);
                result = response;

                // 프래그먼트 어댑터
                class pagerAdapter extends FragmentStatePagerAdapter {
                    public pagerAdapter(FragmentManager fm) {
                        super(fm);
                    }

                    @Override
                    public Fragment getItem(int position) {
                        switch (position) {
                            case 0:
                                roomInfoView.setIsEmpty(result);
                                return roomInfoView;

                            case 1:
                                return roomChatView;

                            case 2:
                                return roomFriendList;

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

                pagerAdapter vpAdapter = new pagerAdapter(getSupportFragmentManager());

                vp.setAdapter(vpAdapter);
                indicator.setViewPager(vp);
                vp.setCurrentItem(0); // 앱실행시 첫번째 화면

                vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        if (position == 1) {
                            indicator.setVisibility(View.GONE);
                        } else {
                            indicator.setVisibility(View.VISIBLE);
                        }

                        switch (position) {
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

//                mProgressDialog.dismiss();
//                Log.d("onPost", mProgressDialog.isShowing() + "");

            }

            @Override
            public void onFailure(String response) {

            }

            @Override
            public void onPreExcute() {

//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mProgressDialog = new ProgressDialog(RoomViewPager.this);
//                        mProgressDialog.setMessage("Please wait...");
//                        mProgressDialog.setCancelable(false);
//                        mProgressDialog.show();
//                        Log.d("onPre", mProgressDialog.isShowing() + "");
//                    }
//                });
            }
        });

//        while(mProgressDialog.isShowing())


        btnRoomExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnRoomSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterSchedule.class);
                startActivity(intent);
            }
        });

        btnRoomSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RoomSettingActivity.class);
                startActivity(intent);
            }
        });

    }

    public void setRoomActionBar() {

        ActionBar myActionBar = getSupportActionBar();
        myActionBar.setDisplayShowHomeEnabled(false);
        myActionBar.setDisplayHomeAsUpEnabled(false);
        myActionBar.setDisplayShowTitleEnabled(false);
        myActionBar.setDisplayShowCustomEnabled(true);
        myActionBar.setHomeButtonEnabled(true);

//        myActionBar.setHomeAsUpIndicator(R.mipmap.hambutton);

        LayoutInflater inf = LayoutInflater.from(mContext);
        View customBarView = inf.inflate(R.layout.actionbar_room, null);

        this.btnRoomSetting = (TextView) customBarView.findViewById(R.id.btnRoomSetting);
        this.btnRoomSchedule = (TextView) customBarView.findViewById(R.id.btnRoomSchedule);
        this.btnRoomExit = (TextView) customBarView.findViewById(R.id.btnRoomExit);

        myActionBar.setCustomView(customBarView);
        myActionBar.setDisplayShowCustomEnabled(true);

        Toolbar parent = (Toolbar) customBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        getSupportActionBar().setElevation(0);

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

        roomInfoView = new RoomInfoFragment();
        roomChatView = new RoomChatFragment();
        roomFriendList = new RoomFriendList();

    }
}
