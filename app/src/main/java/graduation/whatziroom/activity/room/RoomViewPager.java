package graduation.whatziroom.activity.room;

import android.app.ProgressDialog;
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;

import net.daum.mf.map.api.MapView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import graduation.whatziroom.R;
import graduation.whatziroom.activity.base.BaseActivity;
import graduation.whatziroom.activity.main.MainViewPager;
import graduation.whatziroom.activity.main.RoomListFragment;
import graduation.whatziroom.network.HttpNetwork;
import graduation.whatziroom.network.Params;
import me.relex.circleindicator.CircleIndicator;


/**
 * Created by heronation on 2017-05-22.
 */

public class RoomViewPager extends BaseActivity {

    //    private pagerAdapter vpAdapter;
    private LinearLayout ll;
    private LinearLayout linIndicator;

    public static RoomInfoFragment roomInfoView;
    public static RoomChatFragment roomChatView;
    public static RoomFriendList roomFriendList;

    public static android.widget.LinearLayout llChatSchedule;
    public static android.widget.LinearLayout llChatMapView;
    public static android.widget.ScrollView scChatInfoParent;
    public static android.widget.TextView tvChatCloseMap;

    private android.support.v4.view.ViewPager vp;
    private android.widget.FrameLayout flChatMap;
    private me.relex.circleindicator.CircleIndicator indicator;
    private android.widget.FrameLayout flRoom;

    ProgressDialog  mProgressDialog;

    private int roomPKey;
    private int userPKey;
    private String result = "notEmpty";
    private boolean shield = false;

    // TimePicker, DatePicker를 위한 변수 선언

    private static final String TAG = "Sample";
    private static final String TAG_DATETIME_FRAGMENT = "TAG_DATETIME_FRAGMENT";
    private static final String STATE_TEXTVIEW = "STATE_TEXTVIEW";

    private SwitchDateTimeDialogFragment dateTimeFragment;

    public RoomViewPager() {

        roomInfoView = new RoomInfoFragment();
        roomChatView = new RoomChatFragment();
        roomFriendList = new RoomFriendList();

    }

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

        final MapView chatMap = new MapView(RoomViewPager.mContext);
        chatMap.setDaumMapApiKey(getResources().getString(R.string.APIKEY));
//        chatMap.setMapViewEventListener();
//        chatMap.setPOIItemEventListener(this);


        final Timer timer = new Timer();
        mProgressDialog = ProgressDialog.show(RoomViewPager.mContext, "",
                "잠시만 기다려 주세요.", true);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {

                    flChatMap.addView(chatMap);

                    mProgressDialog.dismiss();
                    timer.cancel();
                }
            }
        };
        timer.schedule(task, 0, 2000);

        roomPKey = RoomListFragment.getRoomPKey();
        userPKey = MainViewPager.getUserPKey();

        Log.d("RoomPKey", roomPKey + "");

        Params params = new Params();
        params.add("PKey", roomPKey + "");

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
//                Log.d("indicator", indicator.getHeight() + "");
//                indicator.configureIndicator(0,0,0);
                indicator.setViewPager(vp);
                vp.setCurrentItem(0); // 앱실행시 첫번째 화면

                vp.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {

//                        if(vp.getCurrentItem() == 1 && RoomChatFragment.llChatInfoParent.getVisibility() == View.VISIBLE) {
//
//                            Log.d("ererer","ERERERER");
////                            vp.setEnabled(false);
//
//                            shield = vp.onInterceptTouchEvent(motionEvent);
//
//                            return vp.onInterceptTouchEvent(motionEvent);
//
//                        }

                        return false;
                    }
                });



//                vp.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if(shield) vp.setFocusableInTouchMode(false);
//                    }
//                });
//
//                vp.setOnDragListener(new View.OnDragListener() {
//                    @Override
//                    public boolean onDrag(View view, DragEvent dragEvent) {
//
//                        if(shield) return true;
//
//                        return false;
//                    }
//                });

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
                                btnRoomSetting.setImageResource(R.mipmap.btn_setting);

                                scChatInfoParent.setVisibility(View.GONE);
                                llChatSchedule.setVisibility(View.GONE);
                                llChatMapView.setVisibility(View.GONE);

                                break;

                            case 1:

                                scChatInfoParent.setVisibility(View.VISIBLE);
                                llChatSchedule.setVisibility(View.VISIBLE);
                                llChatMapView.setVisibility(View.GONE);

                                break;

                            case 2:
                                btnRoomSchedule.setVisibility(View.GONE);
                                btnRoomSetting.setVisibility(View.GONE);

                                scChatInfoParent.setVisibility(View.GONE);
                                llChatSchedule.setVisibility(View.GONE);
                                llChatMapView.setVisibility(View.GONE);

                                break;
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

//                mProgressDialog.dismiss();
//                Log.d("onPost", mProgressDialog.isShowing() + "");

                llChatSchedule.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RoomViewPager.llChatMapView.setVisibility(View.VISIBLE);
                    }
                });

                tvChatCloseMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        llChatMapView.setVisibility(View.GONE);
                    }
                });

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

        // Construct SwitchDateTimePicker
        dateTimeFragment = (SwitchDateTimeDialogFragment) getSupportFragmentManager().findFragmentByTag(TAG_DATETIME_FRAGMENT);
        if(dateTimeFragment == null) {
            dateTimeFragment = SwitchDateTimeDialogFragment.newInstance(
                    getString(R.string.label_datetime_dialog),
                    getString(android.R.string.ok),
                    getString(android.R.string.cancel)
//                    getString(R.string.clean) // Optional
            );
        }

        // Init format
        final SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd a h:mm:ss", java.util.Locale.getDefault());
        // Assign unmodifiable values
        dateTimeFragment.set24HoursMode(false);
        dateTimeFragment.setMinimumDateTime(new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.JANUARY, 1).getTime());
        dateTimeFragment.setMaximumDateTime(new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR) + 30, Calendar.DECEMBER, 31).getTime());

        // Define new day and month format
        try {
            dateTimeFragment.setSimpleDateMonthAndDayFormat(new SimpleDateFormat("MMMM dd", Locale.getDefault()));
        } catch (SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException e) {
            Log.e(TAG, e.getMessage());
        }



        // Set listener for date
        // Or use dateTimeFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
        dateTimeFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonWithNeutralClickListener() {
            @Override
            public void onPositiveButtonClick(Date date) {
                Log.d("Time", date.getYear() + "");
                Log.d("Time", (date.getMonth() + 1) + "");
                Log.d("Time", date.getDay() + "");
                Log.d("Time", date.getDate() + "");
                Log.d("Time", date.getHours() +"");
                Log.d("Time", date.getMinutes() + "");

                Intent intent = new Intent(getApplicationContext(), SearchPlaceActivity.class);

                Log.d("dialogRoomPKey", roomPKey + "");

                intent.putExtra("roomPKey", Integer.parseInt(roomPKey + ""));
                intent.putExtra("date", date.toString());

                startActivity(intent);
                dateTimeFragment.dismiss();
            }

            @Override
            public void onNegativeButtonClick(Date date) {
                // Do nothing
            }

            @Override
            public void onNeutralButtonClick(Date date) {
                // Optional if neutral button does'nt exists

            }
        });

        btnRoomSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dateTimeFragment.startAtCalendarView();

                dateTimeFragment.setDefaultDateTime(new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                        Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE)).getTime());
                dateTimeFragment.show(getSupportFragmentManager(), TAG_DATETIME_FRAGMENT);

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

        this.btnRoomSetting = customBarView.findViewById(R.id.btnRoomSetting);
        this.btnRoomSchedule = customBarView.findViewById(R.id.btnRoomSchedule);
        this.btnRoomExit = customBarView.findViewById(R.id.btnRoomExit);

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

        this.flRoom = (FrameLayout) findViewById(R.id.flRoom);
        this.indicator = (CircleIndicator) findViewById(R.id.indicator);
        this.scChatInfoParent = (ScrollView) findViewById(R.id.scChatInfoParent);
        this.llChatMapView = (LinearLayout) findViewById(R.id.llChatMapView);
        this.flChatMap = (FrameLayout) findViewById(R.id.flChatMap);
        this.llChatSchedule = (LinearLayout) findViewById(R.id.llChatSchedule);
        this.vp = (ViewPager) findViewById(R.id.vp);
        this.tvChatCloseMap = (TextView) findViewById(R.id.tvChatCloseMap);

    }
}
