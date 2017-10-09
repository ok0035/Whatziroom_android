package graduation.whatziroom.activity.room;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import graduation.whatziroom.Data.RoomData;
import graduation.whatziroom.Data.UserData;
import graduation.whatziroom.R;
import graduation.whatziroom.activity.base.BaseActivity;
import graduation.whatziroom.activity.main.MainViewPager;
import graduation.whatziroom.network.HttpNetwork;
import graduation.whatziroom.network.Params;
import graduation.whatziroom.util.GPSTracer;
import graduation.whatziroom.util.ParseData;

import static graduation.whatziroom.activity.main.MainViewPager.roomListFragment;


/**
 * Created by heronation on 2017-05-22.
 */

public class RoomViewPager extends BaseActivity implements MapView.MapViewEventListener, MapView.POIItemEventListener {

    //    private pagerAdapter vpAdapter;
    private LinearLayout linIndicator;

    public static RoomInfoFragment roomInfoFragment = new RoomInfoFragment();
    public static RoomChatFragment roomChatFragment = new RoomChatFragment();
    public static RoomUserList roomFriendList = new RoomUserList();

    public android.widget.FrameLayout flChatSchedule;
    public android.widget.LinearLayout llChatMapView;
    private TextView tvChatMapCheck;
    private ImageView ivChatMapCheck;
    private ImageView ivRoomChatNoScheduleDelete;

    public android.widget.FrameLayout flChatMap;
    public android.widget.ScrollView scChatInfoParent;

    public android.widget.TextView tvRoomChatDDay;
    private android.widget.TextView tvRoomChatPlace;
    private android.widget.TextView tvRoomChatTime;
    private android.widget.LinearLayout llRoomChatNoSchedule;
    private android.widget.LinearLayout llRoomChatLayout;

    private HashMap<Integer, UserData> mTagItemMap = new HashMap<Integer, UserData>();

//    private static boolean haveSchedule = false;
//    private static double ScheduleLongitude;
//    private static double ScheduleLatitude;
//    private static String SoonSchedulePKey;

    private ArrayList<UserData> attendUserList;
    private Timer traceLocationTimer;
    private boolean isMove = false;

    private ProgressDialog mProgressDialog;

    private android.support.v4.view.ViewPager vp;
    private me.relex.circleindicator.CircleIndicator indicator;
    private android.widget.FrameLayout flRoom;

    public MapView chatMap;
    private String result = "notEmpty";
    private boolean shield = false;
    public static Context mContext;
    public static Activity mActivity;

    private static int roomPKey = 0;
    private static int schedulePKey = 0;
    private static String roomName = "";

    // TimePicker, DatePicker를 위한 변수 선언

    private final String TAG = "Sample";
    private final String TAG_DATETIME_FRAGMENT = "TAG_DATETIME_FRAGMENT";
    private final String STATE_TEXTVIEW = "STATE_TEXTVIEW";

    private SwitchDateTimeDialogFragment dateTimeFragment;

    public RoomViewPager() {

        mContext = this;
        mActivity = this;
        roomInfoFragment.ScheduleLongitude = 0.0;
        roomInfoFragment.ScheduleLatitude = 0.0;

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

//        updateMap();

        /*
        *   Viewpase Adapter 설정
        */

        roomPKey = getRoomPKey();

        Log.d("RoomPKey", roomPKey + "");

        // 프래그먼트 어댑터
        class pagerAdapter extends FragmentStatePagerAdapter {
            public pagerAdapter(FragmentManager fm) {
                super(fm);
            }

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return roomInfoFragment;

                    case 1:
                        return roomChatFragment;

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
                return false;
            }
        });

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
                        Log.d("getIsCreate", roomInfoFragment.getIsEmpty());
                        btnRoomSchedule.setVisibility(View.VISIBLE);
                        btnRoomSetting.setVisibility(View.VISIBLE);
                        btnRoomSetting.setImageResource(R.mipmap.btn_setting);
                        scChatInfoParent.setVisibility(View.GONE);
                        flChatSchedule.setVisibility(View.GONE);
                        llChatMapView.setVisibility(View.GONE);
                        break;

                    case 1:

                        scChatInfoParent.setVisibility(View.VISIBLE);
                        flChatSchedule.setVisibility(View.VISIBLE);
                        btnRoomSchedule.setVisibility(View.VISIBLE);
                        btnRoomSetting.setVisibility(View.VISIBLE);
                        llChatMapView.setVisibility(View.GONE);
                        tvChatMapCheck.setText("지도 보기");
                        ivChatMapCheck.setImageResource(R.mipmap.arrow_icon);
                        break;

                    case 2:
                        btnRoomSchedule.setVisibility(View.GONE);
                        btnRoomSetting.setVisibility(View.GONE);
                        scChatInfoParent.setVisibility(View.GONE);
                        flChatSchedule.setVisibility(View.GONE);
                        llChatMapView.setVisibility(View.GONE);
                        RoomUserList.updateRoomUserList();
                        RoomUserList.updateRequestList();

                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        roomChatFragment.updateChatMapInfo();

//                mProgressDialog.dismiss();
//                Log.d("onPost", mProgressDialog.isShowing() + "");

        flChatSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //지도 안열려있으면
                if (llChatMapView.getVisibility() == View.GONE && roomInfoFragment.haveSchedule) {
                    llChatMapView.setVisibility(View.VISIBLE);
                    tvChatMapCheck.setText("지도 닫기");

                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.arrow_icon);

                    Matrix sideInversion = new Matrix();
                    sideInversion.setScale(1, -1);

                    ivChatMapCheck.setImageBitmap(Bitmap.createBitmap(bitmap, 0, 0,
                            bitmap.getWidth(), bitmap.getHeight(), sideInversion, false));

                    mProgressDialog = ProgressDialog.show(BaseActivity.mContext, "",
                            "지도 활성화중...", true);

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            updateMap();
                        }
                    });

                } else {
                    llChatMapView.setVisibility(View.GONE);
                    tvChatMapCheck.setText("지도 보기");
                    ivChatMapCheck.setImageResource(R.mipmap.arrow_icon);

                    if (traceLocationTimer != null) {
                        traceLocationTimer.cancel();
                        traceLocationTimer = null;
                    }
//                            mProgressDialog = ProgressDialog.show(BaseActivity.mContext, "",
//                                    "지도 비활성화중...", true);
//
//                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
////                                    chatMap = null;
////                                    flChatMap.removeAllViews();
//                                    mProgressDialog.dismiss();
//                                }
//                            }, 500);


                }
            }
        });


//        while(mProgressDialog.isShowing())


        btnRoomExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < roomChatFragment.chatList.size(); i++) {
                    if (roomChatFragment.chatList.get(i).getRoomPKey().equals(getRoomPKey() + "")) {
                        RoomChatFragment.lvChat.setAdapter(roomChatFragment.chatList.get(i).getAdapter());
                        roomChatFragment.chatList.get(i).getAdapter().notifyDataSetChanged();

                        Params params = new Params();
                        params.add("UserPKey", MainViewPager.getUserPKey() + "");
                        params.add("RoomPKey", getRoomPKey() + "");
                        params.add("ChatCount", roomChatFragment.chatList.get(i).getChatCount() + "");

                        new HttpNetwork("UpdateChatCount.php", params.getParams(), new HttpNetwork.AsyncResponse() {
                            @Override
                            public void onSuccess(String response) {
                                Log.d("UpdateChatCount", response);

                                roomListFragment.updateRoom(new MainViewPager.AfterUpdate() {
                                    @Override
                                    public void onPost(RoomData data) {

                                    }
                                });

                                if (traceLocationTimer != null) {
                                    traceLocationTimer.cancel();
                                    traceLocationTimer = null;
                                }
                                flChatMap.removeAllViews();
                                chatMap = null;
                                finish();

                            }

                            @Override
                            public void onFailure(String response) {

                            }

                            @Override
                            public void onPreExcute() {

                            }
                        });

                        //Log.d("여기를", "들어와야되!");
                        break;

                    }
                    //Log.d("여길봐!", MainViewPager.chatList.get(i).getRoomPKey() + "..");
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!isFinishing()) finish();
                    }
                }, 1500);

            }
        });

        // Construct SwitchDateTimePicker
        dateTimeFragment = (SwitchDateTimeDialogFragment) getSupportFragmentManager().findFragmentByTag(TAG_DATETIME_FRAGMENT);
        if (dateTimeFragment == null) {
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
//                    Log.d("Time", date.getYear() + "");
//                    Log.d("Time", (date.getMonth() + 1) + "");
//                    Log.d("Time", date.getDay() + "");
//                    Log.d("Time", date.getDate() + "");
//                    Log.d("Time", date.getHours() + "");
//                    Log.d("Time", date.getMinutes() + "");

                Intent intent = new Intent(getApplicationContext(), SearchPlaceActivity.class);

//                    Log.d("dialogRoomPKey", roomPKey + "");

                intent.putExtra("roomPKey", Integer.parseInt(roomPKey + ""));
                intent.putExtra("date", date.toString());

                startActivity(intent);
                dateTimeFragment.dismiss();
            }

            @Override
            public void onNegativeButtonClick(Date date) {

                updateMap();

            }

            @Override
            public void onNeutralButtonClick(Date date) {
                // Optional if neutral button does'nt exists

            }
        });


        btnRoomSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                flChatMap.removeAllViews();
                chatMap = null;

                if (traceLocationTimer != null) {
                    traceLocationTimer.cancel();
                    traceLocationTimer = null;
                }

                flChatSchedule.setVisibility(View.VISIBLE);
                llChatMapView.setVisibility(View.GONE);
                tvChatMapCheck.setText("지도 보기");
                ivChatMapCheck.setImageResource(R.mipmap.arrow_icon);

                try {
                    if (chatMap.getPOIItems().length > 0) {
                        chatMap.removeAllPOIItems();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                mProgressDialog = ProgressDialog.show(BaseActivity.mContext, "",
                        "잠시만 기다려주세요...", true);

                GPSTracer.getInstance().startLocation(1000, 0);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dateTimeFragment.startAtCalendarView();
                        dateTimeFragment.setDefaultDateTime(new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),
                                Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                                Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE)).getTime());
                        dateTimeFragment.show(getSupportFragmentManager(), TAG_DATETIME_FRAGMENT);
                        mProgressDialog.dismiss();
                    }
                }, 1000);


            }
        });

        btnRoomSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RoomViewPager.this, "모임별 옵션 준비 중입니다.", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(getApplicationContext(), RoomSettingActivity.class);
                //startActivity(intent);
            }
        });

        ivRoomChatNoScheduleDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llRoomChatNoSchedule.setVisibility(View.GONE);
                //isDelete = true;
            }
        });

    }

<<<<<<< HEAD
    //스케줄을 생성했을때 다시 정보를 불러와야 하는데 이 작업은 RegisterScheduleDialog에서 하기 때문에 스태틱으로 할 수 밖에 없었음
    //스태틱 함수가 너무 많은게 걱정이 되긴 하지만 현재로선 마땅한 방법도 없는 듯... 우선 이렇게 쓰기로.

    public static void updateChatMapInfo() {

        Params scheduleParams = new Params();
        scheduleParams.add("RoomPKey", getRoomPKey() + "");
        scheduleParams.add("Limit", 1 + "");

        new HttpNetwork("GetScheduleData.php", scheduleParams.getParams(), new HttpNetwork.AsyncResponse() {
            @Override
            public void onSuccess(String response) {
                Log.d("LIMIT", response);

                if (!response.equals("[]")) {

                    haveSchedule = true;

                    llRoomChatNoSchedule.setVisibility(View.GONE);
                    llRoomChatLayout.setVisibility(View.VISIBLE);
                    ParseData parse = new ParseData();

                    SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    long dDay;

                    try {

                        JSONArray roomInfoArray = parse.parseJsonArray(response);
                        JSONObject roomInfo = new JSONObject(roomInfoArray.get(0).toString());
                        Date date = transFormat.parse(roomInfo.getString("Time"));
                        String Minutes = "0";
                        SoonSchedulePKey = roomInfo.getString("SchedulePKey");
                        if(date.getMinutes() < 10)
                            Minutes += date.getMinutes();
                        else
                            Minutes = date.getMinutes() + "";
                        ScheduleTime = (date.getYear() + 1900) + "." + (date.getMonth() + 1) + "." + date.getDate() + " " + date.getHours() + ":" + Minutes;
                        SchedulePlace = roomInfo.getString("Place");

//                        dDay = dDay / 1000 / 60 / 60 / 24;

                        ScheduleLongitude = Double.parseDouble(roomInfo.getString("Longitude"));
                        ScheduleLatitude = Double.parseDouble(roomInfo.getString("Latitude"));

//                        Log.d("Longitude", ScheduleLongitude + "");
//                        Log.d("Latitude", ScheduleLatitude + "");

                        final TextView tvRoomChatTime = mActivity.findViewById(R.id.tvRoomChatTime);
                        //final TextView tvRoomChatPlace = mActivity.findViewById(R.id.tvRoomChatPlace);
                        final TextView tvRoomChatDDay = mActivity.findViewById(R.id.tvRoomChatDDay);
                        final TextView tvRoomChatLocation = mActivity.findViewById(R.id.tvRoomChatLocation);
                        final Calendar CalculateDate = Calendar.getInstance();

                        CalculateDate.setTime(date);
                        final long goalTime = CalculateDate.getTimeInMillis();

                        CalculateDate.setTime(Calendar.getInstance().getTime());
                        long nowTime = CalculateDate.getTimeInMillis();

                        dDay = (goalTime - nowTime);

                        //밀리세컨드를 밀리초, 초, 분, 시간 으로 나눔
                        long sec = dDay / 1000;
                        final long min = sec / 60;
                        long hour = min / 60;
                        final long day = hour / 24;

                        if (day == 0 && min > 0) {
                            tvRoomChatDDay.setText(min + "min");
                        } else if (day > 0) {
                            tvRoomChatDDay.setText("D - " + day);
                        } else {
                            tvRoomChatDDay.setText("");
                        }

                        tvRoomChatTime.setText(ScheduleTime);
                        //tvRoomChatPlace.setText(SchedulePlace);

                        if (day > 0) {
                            isTracing = false;
                            tvRoomChatTime.setVisibility(View.VISIBLE);
                            tvRoomChatLocation.setVisibility(View.INVISIBLE);
                            tvRoomChatDDay.setText("D - " + day);
                        } else if (day == 0 && hour > 0) {
                            isTracing = false;
                            tvRoomChatTime.setVisibility(View.VISIBLE);
                            tvRoomChatLocation.setVisibility(View.INVISIBLE);
                            tvRoomChatDDay.setText(hour + "시간 전");
                        } else {
                            tvRoomChatTime.setVisibility(View.GONE);

                            Timer timer = new Timer();
                            TimerTask task = new TimerTask() {
                                @Override
                                public void run() {
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            CalculateDate.setTime(Calendar.getInstance().getTime());
                                            long nowTime = CalculateDate.getTimeInMillis();
                                            long dDay = (goalTime - nowTime);

                                            //밀리세컨드를 밀리초, 초, 분, 시간 으로 나눔
                                            long sec = dDay / 1000;
                                            final long min = sec / 60;
                                            long hour = min / 60;
                                            final long day = hour / 24;

                                            if (day == 0 && sec >= 0 && min < 60) {
                                                tvRoomChatLocation.setVisibility(View.VISIBLE);
                                                tvRoomChatDDay.setText(min+"분 전");
                                                isTracing = true;

                                            } else {
                                                tvRoomChatLocation.setVisibility(View.INVISIBLE);
                                                tvRoomChatDDay.setText("-");
                                                isTracing = false;
                                                updateChatMapInfo();
                                                roomInfoFragment.updateRoomInfo();

                                            }
                                        }
                                    });
                                }
                            };
                            timer.schedule(task, 0, 60000);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                } else {
                    /*
                    if(isDelete) {
                        llRoomChatNoSchedule.setVisibility(View.GONE);
                    } else {
                        llRoomChatNoSchedule.setVisibility(View.VISIBLE);
                    }
                    */
                    haveSchedule = false;
                    llChatMapView.setVisibility(View.GONE);
                    llRoomChatLayout.setVisibility(View.GONE);
                    llRoomChatNoSchedule.setVisibility(View.VISIBLE);
                    roomInfoFragment.updateRoomInfo();

                    Log.d("Longitude", ScheduleLongitude + "");
                    Log.d("Latitude", ScheduleLatitude + "");
                }
            }

            @Override
            public void onFailure(String response) {

            }

            @Override
            public void onPreExcute() {

            }
        });

    }

=======
>>>>>>> 5c914f66c81d86b64f9c4c54f33baafc9be6af14
    public void updateMap() {

        try {

            isMove = false;

            if (chatMap != null) {

                chatMap.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
                chatMap.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(roomInfoFragment.ScheduleLatitude, roomInfoFragment.ScheduleLongitude), 4, true);

                if(roomChatFragment.isTracing)
                    traceUserLocation();

                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }

            } else {

                if (mProgressDialog != null && mProgressDialog.isShowing())
                    mProgressDialog.setMessage("방 입장 후 처음 지도를 실행하시나요?\n지도를 실행하고 있습니다...");

                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        flChatMap.removeAllViews();
                        chatMap = null;

                        chatMap = new MapView(RoomViewPager.mContext);
                        chatMap.setDaumMapApiKey(getResources().getString(R.string.APIKEY));
                        flChatMap.addView(chatMap);

                        if (mProgressDialog != null && mProgressDialog.isShowing()) {
                            mProgressDialog.setMessage("위치 정보를 가져오고 있습니다...");

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    chatMap.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);

                                    if(roomChatFragment.isTracing)
                                        traceUserLocation();

                                    chatMap.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(roomInfoFragment.ScheduleLatitude, roomInfoFragment.ScheduleLongitude), 4, true);
                                    mProgressDialog.dismiss();

                                }
                            }, 2500);
                        }
                    }
                }, 2500);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void traceUserLocation() {

        traceLocationTimer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                showLocationMarker();
            }
        };
        traceLocationTimer.schedule(task, 0, 3000);

    }

    private void showLocationMarker() {

        Params params = new Params();
        params.add("SchedulePKey", roomInfoFragment.SoonSchedulePKey);

        new HttpNetwork("GetAttendUserList.php", params.getParams(), new HttpNetwork.AsyncResponse() {
            @Override
            public void onSuccess(String response) {

                if (!response.equals("[]")) {

                    try {
                        ParseData parse = new ParseData();
                        JSONArray jsonAttendUserList = parse.parseJsonArray(response);
                        attendUserList = new ArrayList<UserData>();

                        for (int i = 0; i < jsonAttendUserList.length(); i++) {
                            JSONObject jsonUserData = new JSONObject(jsonAttendUserList.get(i).toString());
                            attendUserList.add(new UserData(jsonUserData.getString("PKey"), jsonUserData.getString("Name"), jsonUserData.getString("Longitude"), jsonUserData.getString("Latitude"), jsonUserData.getString("FirebaseToken")));
                            Log.d("ATTEND_PKEY", jsonUserData.getString("PKey"));
                            Log.d("ATTEND_Name", jsonUserData.getString("Name"));
                            Log.d("ATTEND_Longtitude", jsonUserData.getString("Longitude"));
                            Log.d("ATTEND_Latitude", jsonUserData.getString("Latitude"));
                        }

                        try {
                            chatMap.removeAllPOIItems(); //기존 위치 마커 삭제
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        showPlaceMarker();

                        try {
                            MapPointBounds mapPointBounds = new MapPointBounds();
                            MapPOIItem poiItem = new MapPOIItem();

                            poiItem.setItemName(roomInfoFragment.SchedulePlace);
                            poiItem.setTag(0);
                            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(roomInfoFragment.ScheduleLatitude, roomInfoFragment.ScheduleLongitude);
                            mapPointBounds.add(mapPoint);
                            poiItem.setMapPoint(mapPoint);
//                            chatMap.addCircle(new MapCircle(mapPoint, 100000, Color.TRANSPARENT, Color.argb(0, 0, 255, 0)));
//            poiItem.setMarkerType(MapPOIItem.MarkerType.BluePin);
                            poiItem.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                            poiItem.setCustomImageResourceId(R.drawable.map_pin_red);
//            poiItem.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
                            poiItem.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
                            poiItem.setCustomSelectedImageResourceId(R.drawable.map_pin_red);
                            poiItem.setCustomImageAnchor(0.5f, 1.0f);
                            poiItem.setCustomImageAutoscale(true);

                            chatMap.addPOIItem(poiItem);

                            for (int i = 0; i < attendUserList.size(); i++) {
                                UserData item = attendUserList.get(i);

                                try {

                                    //위치가 0일때는 마커를 찍지 않고 제대로 위치를 불러왔을 때 마커를 찍어주자
                                    if (item.getLatitude().equals("0") || item.getLongitude().equals("0")) {
                                        Log.d("위도 경도 0", "체크");
                                        continue;
                                    }

                                    poiItem.setItemName(item.getName());
                                    poiItem.setTag(i + 1);
                                    mapPoint = MapPoint.mapPointWithGeoCoord(Double.parseDouble(item.getLatitude()), Double.parseDouble(item.getLongitude()));
                                    poiItem.setMapPoint(mapPoint);
                                    mapPointBounds.add(mapPoint);
//            poiItem.setMarkerType(MapPOIItem.MarkerType.BluePin);
                                    poiItem.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                                    poiItem.setCustomImageResourceId(R.drawable.map_pin_blue);
//            poiItem.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
                                    poiItem.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
                                    poiItem.setCustomSelectedImageResourceId(R.drawable.map_pin_blue);
                                    poiItem.setCustomImageAnchor(0.5f, 1.0f);
                                    poiItem.setCustomImageAutoscale(true);

                                    chatMap.addPOIItem(poiItem);

                                    //이것의 정확한 사용처를 아직 잘 모르겠음
                                    mTagItemMap.put(poiItem.getTag(), item);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            //최초 한번만 위치를 약속장소로 옮기고 선택해준다.
                            if (!isMove) {
                                chatMap.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds));

                                MapPOIItem[] poiItems = chatMap.getPOIItems();
                                if (poiItems.length > 0) {
                                    chatMap.selectPOIItem(poiItems[0], false);
                                }
                                isMove = true;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(String response) {

            }

            @Override
            public void onPreExcute() {

            }
        });

    }

    private void showPlaceMarker() {

        try {

            MapPOIItem poiItem = new MapPOIItem();
            poiItem.setItemName(roomInfoFragment.SchedulePlace);
            poiItem.setTag(0);
            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(roomInfoFragment.ScheduleLatitude, roomInfoFragment.ScheduleLongitude);
            poiItem.setMapPoint(mapPoint);
//            poiItem.setMarkerType(MapPOIItem.MarkerType.BluePin);
            poiItem.setMarkerType(MapPOIItem.MarkerType.CustomImage);
            poiItem.setCustomImageResourceId(R.drawable.map_pin_red);
//            poiItem.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
            poiItem.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
            poiItem.setCustomSelectedImageResourceId(R.drawable.map_pin_red);
            poiItem.setCustomImageAnchor(0.5f, 1.0f);
            poiItem.setCustomImageAutoscale(true);

            chatMap.addPOIItem(poiItem);
        } catch (Exception e) {
            e.printStackTrace();
        }


//        chatMap.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds));

//        MapPOIItem[] poiItems = chatMap.getPOIItems();
//        if (poiItems.length > 0) {
//            chatMap.selectPOIItem(poiItems[0], false);
//        }
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

        this.tvRoomName = customBarView.findViewById(R.id.tvRoomName);
        tvRoomName.setText(getRoomName());
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
    public void onBackPressed() {

        for (int i = 0; i < roomChatFragment.chatList.size(); i++) {
            if (roomChatFragment.chatList.get(i).getRoomPKey().equals(getRoomPKey() + "")) {
                roomChatFragment.lvChat.setAdapter(roomChatFragment.chatList.get(i).getAdapter());
                roomChatFragment.chatList.get(i).getAdapter().notifyDataSetChanged();

                Params params = new Params();
                params.add("UserPKey", MainViewPager.getUserPKey() + "");
                params.add("RoomPKey", getRoomPKey() + "");
                params.add("ChatCount", roomChatFragment.chatList.get(i).getChatCount() + "");

                Log.d("UserPKey", MainViewPager.getUserPKey() + "");
                Log.d("RoomPKey", getRoomPKey() + "");
                Log.d("ChatCount", roomChatFragment.chatList.get(i).getChatCount() + "");

                new HttpNetwork("UpdateChatCount.php", params.getParams(), new HttpNetwork.AsyncResponse() {
                    @Override
                    public void onSuccess(String response) {
                        Log.d("UpdateChatCount", response);

                        roomListFragment.updateRoom(new MainViewPager.AfterUpdate() {
                            @Override
                            public void onPost(RoomData data) {

                            }
                        });

                        if (traceLocationTimer != null) {
                            traceLocationTimer.cancel();
                            traceLocationTimer = null;
                        }

                        flChatMap.removeAllViews();
                        chatMap = null;
                        finish();
                    }

                    @Override
                    public void onFailure(String response) {

                    }

                    @Override
                    public void onPreExcute() {

                    }
                });

                break;
            }
        }

        //통신에 실패해서 채팅 수를 못읽었을 경우 자동 종료
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) finish();
            }
        }, 1500);

    }

    public static int getRoomPKey() {
        return roomPKey;
    }

    public static void setRoomPKey(String PKey) {
        if (PKey == null || PKey.equals("null")) roomPKey = 0;
        else roomPKey = Integer.parseInt(PKey);
    }

    public int getSchedulePKey() {
        return schedulePKey;
    }

    public static void setSchedulePKey(int schedulePKey) {
        RoomViewPager.schedulePKey = schedulePKey;
    }

    public static String getRoomName() {
        return roomName;
    }

    public static void setRoomName(String roomName) {
        RoomViewPager.roomName = roomName;
    }

    @Override
    public void bindView() {
        super.bindView();

        this.flRoom = findViewById(R.id.flRoom);
        this.indicator = findViewById(R.id.indicator);
        this.scChatInfoParent = findViewById(R.id.scChatInfoParent);
        this.llChatMapView = findViewById(R.id.llChatMapView);
        this.flChatMap = findViewById(R.id.flChatMap);
        this.flChatSchedule = findViewById(R.id.flChatSchedule);
        this.vp = findViewById(R.id.vp);
        this.tvRoomChatTime = findViewById(R.id.tvRoomChatTime);
        //this.tvRoomChatPlace = findViewById(R.id.tvRoomChatPlace);
        this.tvRoomChatDDay = findViewById(R.id.tvRoomChatDDay);
        this.llRoomChatLayout = findViewById(R.id.llRoomChatLayout);
        this.llRoomChatNoSchedule = findViewById(R.id.llRoomChatNoSchedule);
        this.ivRoomChatNoScheduleDelete = findViewById(R.id.ivRoomChatNoScheduleDelete);
        this.tvChatMapCheck = findViewById(R.id.tvChatMapCheck);
        this.ivChatMapCheck = findViewById(R.id.ivChatMapCheck);

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public void onMapViewInitialized(MapView mapView) {
        Log.d("MapINIT", "dddddddd");

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {


    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {
    }

    @Override
    protected void onDestroy() {

        if (traceLocationTimer != null) {
            traceLocationTimer.cancel();
            traceLocationTimer = null;
        }

        flChatMap.removeAllViews();
        chatMap = null;

        super.onDestroy();
    }
}
