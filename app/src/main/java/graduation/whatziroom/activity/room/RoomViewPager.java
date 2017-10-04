package graduation.whatziroom.activity.room;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import graduation.whatziroom.Data.UserData;
import graduation.whatziroom.R;
import graduation.whatziroom.activity.base.BaseActivity;
import graduation.whatziroom.network.HttpNetwork;
import graduation.whatziroom.network.Params;
import graduation.whatziroom.util.ParseData;
import me.relex.circleindicator.CircleIndicator;


/**
 * Created by heronation on 2017-05-22.
 */

public class RoomViewPager extends BaseActivity implements MapView.MapViewEventListener, MapView.POIItemEventListener {

    //    private pagerAdapter vpAdapter;
    private LinearLayout linIndicator;

    public static RoomInfoFragment roomInfoView;
    public static RoomChatFragment roomChatView;
    public static RoomUserList roomFriendList;

    public android.widget.LinearLayout llChatSchedule;
    public android.widget.LinearLayout llChatMapView;
    public android.widget.ScrollView scChatInfoParent;
    public android.widget.TextView tvChatCloseMap;
    public android.widget.FrameLayout flChatMap;
    private android.widget.TextView tvRoomChatPlace;
    private android.widget.TextView tvRoomChatTime;
    private HashMap<Integer, UserData> mTagItemMap = new HashMap<Integer, UserData>();

    private static double ScheduleLongitude;
    private static double ScheduleLatitude;
    private static String ScheduleTime;
    private static String SchedulePlace;
    private static String SoonSchedulePKey;
    private ArrayList<UserData> attendUserList;
    private Timer traceLocationTimer;
    private boolean isMove = false;

    private ProgressDialog mProgressDialog;

    private android.support.v4.view.ViewPager vp;
    private me.relex.circleindicator.CircleIndicator indicator;
    private android.widget.FrameLayout flRoom;

    public static MapView chatMap;
    private String result = "notEmpty";
    private boolean shield = false;
    public static Context mContext;
    public static Activity mActivity;

    private static int roomPKey = 0;
    private static int schedulePKey = 0;

    // TimePicker, DatePicker를 위한 변수 선언

    private static final String TAG = "Sample";
    private static final String TAG_DATETIME_FRAGMENT = "TAG_DATETIME_FRAGMENT";
    private static final String STATE_TEXTVIEW = "STATE_TEXTVIEW";

    private SwitchDateTimeDialogFragment dateTimeFragment;

    public RoomViewPager() {

        roomInfoView = new RoomInfoFragment();
        roomChatView = new RoomChatFragment();
        roomFriendList = new RoomUserList();
        mContext = this;
        mActivity = this;
        ScheduleLongitude = 0.0;
        ScheduleLatitude = 0.0;

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
    public void onBackPressed() {
        if(traceLocationTimer != null) {
            traceLocationTimer.cancel();
            traceLocationTimer = null;
        }
        flChatMap.removeAllViews();
        chatMap = null;
        finish();
    }

    @Override
    public void setUpEvents() {
        super.setUpEvents();

//        updateMap();

        /*
        *   Viewpase Adapter 설정
        */

        roomPKey = RoomViewPager.getRoomPKey();

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
                                btnRoomSchedule.setVisibility(View.VISIBLE);
                                btnRoomSetting.setVisibility(View.VISIBLE);
                                llChatMapView.setVisibility(View.GONE);

                                break;

                            case 2:
                                btnRoomSchedule.setVisibility(View.GONE);
                                btnRoomSetting.setVisibility(View.GONE);
                                scChatInfoParent.setVisibility(View.GONE);
                                llChatSchedule.setVisibility(View.GONE);
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

                updateChatMapInfo();

//                mProgressDialog.dismiss();
//                Log.d("onPost", mProgressDialog.isShowing() + "");

                llChatSchedule.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (llChatMapView.getVisibility() == View.GONE) {
                            llChatMapView.setVisibility(View.VISIBLE);
                            llChatSchedule.setVisibility(View.GONE);

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
                            llChatSchedule.setVisibility(View.VISIBLE);
                            if(traceLocationTimer != null) {
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

                tvChatCloseMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        llChatMapView.setVisibility(View.GONE);
                        llChatSchedule.setVisibility(View.VISIBLE);
                    }
                });

            }

            @Override
            public void onFailure(String response) {

            }

            @Override
            public void onPreExcute() {

            }
        });

//        while(mProgressDialog.isShowing())


        btnRoomExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(traceLocationTimer != null) {
                    traceLocationTimer.cancel();
                    traceLocationTimer = null;
                }

                flChatMap.removeAllViews();
                chatMap = null;
                finish();
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
                Log.d("Time", date.getYear() + "");
                Log.d("Time", (date.getMonth() + 1) + "");
                Log.d("Time", date.getDay() + "");
                Log.d("Time", date.getDate() + "");
                Log.d("Time", date.getHours() + "");
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

                if(traceLocationTimer != null) {
                    traceLocationTimer.cancel();
                    traceLocationTimer = null;
                }

                llChatSchedule.setVisibility(View.VISIBLE);
                llChatMapView.setVisibility(View.GONE);
                chatMap.removeAllPOIItems();
                chatMap = null;
                flChatMap.removeAllViews();

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

    //스케줄을 생성했을때 다시 정보를 불러와야 하는데 이 작업은 RegisterScheduleDialog에서 하기 때문에 스태틱으로 할 수 밖에 없었음
    //스태틱 함수가 너무 많은게 걱정이 되긴 하지만 현재로선 마땅한 방법도 없는 듯... 우선 이렇게 쓰기로.
    public static void updateChatMapInfo() {

        Params scheduleParams = new Params();
        scheduleParams.add("RoomPKey", RoomViewPager.getRoomPKey() + "");
        scheduleParams.add("Limit", 1 + "");

        new HttpNetwork("GetScheduleData.php", scheduleParams.getParams(), new HttpNetwork.AsyncResponse() {
            @Override
            public void onSuccess(String response) {
                Log.d("LIMIT", response);

                if (!response.equals("[]")) {

                    ParseData parse = new ParseData();

                    SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {

                        JSONArray roomInfoArray = parse.parseJsonArray(response);
                        JSONObject roomInfo = new JSONObject(roomInfoArray.get(0).toString());
                        Date date = transFormat.parse(roomInfo.getString("Time"));

                        SoonSchedulePKey = roomInfo.getString("SchedulePKey");
                        ScheduleTime = (date.getYear() + 1900) + "년 " + (date.getMonth() + 1) + "월 " + date.getDate() + "일 " + date.getHours() + "시 " + date.getMinutes() + "분";
                        SchedulePlace = roomInfo.getString("Place");

                        ScheduleLongitude = Double.parseDouble(roomInfo.getString("Longitude"));
                        ScheduleLatitude = Double.parseDouble(roomInfo.getString("Latitude"));

//                        Log.d("Longitude", ScheduleLongitude + "");
//                        Log.d("Latitude", ScheduleLatitude + "");

                        TextView tvRoomChatTime = mActivity.findViewById(R.id.tvRoomChatTime);
                        TextView tvRoomChatPlace = mActivity.findViewById(R.id.tvRoomChatPlace);

                        tvRoomChatTime.setText(ScheduleTime);
                        tvRoomChatPlace.setText(SchedulePlace);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
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

    public void updateMap() {

//        mProgressDialog = ProgressDialog.show(RoomViewPager.mContext, "",
//                "잠시만 기다려 주세요.", true);

        try {

            isMove = false;

            if (chatMap != null) {

//            chatMap.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(ScheduleLatitude, ScheduleLongitude), 4, true);
                chatMap.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
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
                        chatMap.setDaumMapApiKey(RoomViewPager.mContext.getResources().getString(R.string.APIKEY));
                        flChatMap.addView(chatMap);

                        if (mProgressDialog != null && mProgressDialog.isShowing()) {
                            mProgressDialog.setMessage("위치 정보를 가져오고 있습니다...");

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    chatMap.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
                                    traceUserLocation();

//                                chatMap.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(ScheduleLatitude, ScheduleLongitude), 4, true);
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
        params.add("SchedulePKey", SoonSchedulePKey);

        new HttpNetwork("GetAttendUserList.php", params.getParams(), new HttpNetwork.AsyncResponse() {
            @Override
            public void onSuccess(String response) {

                if(!response.equals("[]")) {

                    try {
                        ParseData parse = new ParseData();
                        JSONArray jsonAttendUserList = parse.parseJsonArray(response);
                        attendUserList = new ArrayList<UserData>();

                        for (int i = 0; i < jsonAttendUserList.length(); i++) {
                            JSONObject jsonUserData = new JSONObject(jsonAttendUserList.get(i).toString());
                            attendUserList.add(new UserData(jsonUserData.getString("PKey"), jsonUserData.getString("Name"), jsonUserData.getString("Longitude"), jsonUserData.getString("Latitude")));
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

                            poiItem.setItemName(SchedulePlace);
                            poiItem.setTag(0);
                            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(ScheduleLatitude, ScheduleLongitude);
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
                                    if(item.getLatitude().equals("0") || item.getLongitude().equals("0")) {
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
                            if(!isMove) {
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
            poiItem.setItemName(SchedulePlace);
            poiItem.setTag(0);
            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(ScheduleLatitude, ScheduleLongitude);
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

    public static int getRoomPKey() {
        return roomPKey;
    }

    public static void setRoomPKey(String PKey) {
        if (PKey == null || PKey.equals("null")) roomPKey = 0;
        else roomPKey = Integer.parseInt(PKey);
    }

    public static int getSchedulePKey() {
        return schedulePKey;
    }
    public static void setSchedulePKey(int schedulePKey) {
        RoomViewPager.schedulePKey = schedulePKey;
    }

    public static String getSoonSchedulePKey() {
        return SoonSchedulePKey;
    }

    public static void setSoonSchedulePKey(String soonSchedulePKey) {
        SoonSchedulePKey = soonSchedulePKey;
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
        this.tvRoomChatTime = (TextView) findViewById(R.id.tvRoomChatTime);
        this.tvRoomChatPlace = (TextView) findViewById(R.id.tvRoomChatPlace);

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

        if(traceLocationTimer != null) {
            traceLocationTimer.cancel();
            traceLocationTimer = null;
        }

        flChatMap.removeAllViews();
        chatMap = null;

        super.onDestroy();
    }
}
