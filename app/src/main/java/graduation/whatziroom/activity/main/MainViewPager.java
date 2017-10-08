package graduation.whatziroom.activity.main;


import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;

import graduation.whatziroom.Data.ChatData;
import graduation.whatziroom.Data.FirebaseNoticeData;
import graduation.whatziroom.Data.RoomData;
import graduation.whatziroom.R;
import graduation.whatziroom.activity.base.BaseActivity;
import graduation.whatziroom.dialog.CreateRoomDialog;
import graduation.whatziroom.dialog.ExitMainDialog;
import graduation.whatziroom.network.DBSI;
import graduation.whatziroom.network.HttpNetwork;
import graduation.whatziroom.network.Params;
import graduation.whatziroom.util.LocationService;
import graduation.whatziroom.util.ParseData;


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

    // 각 프래그먼트 별로 태그 값 설정하기 위해 선언
    private FriendListFragment friendListView;
    private RoomListFragment roomListView;
    private ScheduleListFragment scheduleListView;
    private NotificationListFragment notificationListView;
    private ProfileFragment profileView;

    //채팅데이터를 받는 리스트
    public static ArrayList<ChatData> chatList = new ArrayList<ChatData>();

    //방정보
    public static RoomData roomData;
    private static int UserPKey;
    private static String UserName;
    ProgressDialog mProgressDialog;

    private static FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private static DatabaseReference databaseReference = firebaseDatabase.getReference();

    public static LocationService locationService;
    private boolean isBind = false;
    public static Timer CheckLocationTimer;

    private TextView tvMainExitYes;
    private TextView tvMainExitNo;

    public interface AfterUpdate {
        void onPost(RoomData data);
    }

    ServiceConnection sconn = new ServiceConnection() {
        @Override //서비스가 실행될 때 호출
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationService.LocationBinder locationBinder = (LocationService.LocationBinder) service;
            locationService = locationBinder.getService();
            Log.d("ServiceLongitude", locationService.longitude + "");
            isBind = true;
            Log.e("LOG", "onServiceConnected()");
        }

        @Override //서비스가 종료될 때 호출 그러나 강제 종료가 된경우 혹은 충돌이 일어날때만 간헐적으로 실행된다고 함. 이부분은 직접 체크해주어야 한다.
        public void onServiceDisconnected(ComponentName name) {
            locationService = null;
            isBind = false;
            Log.e("LOG", "onServiceDisconnected()");
        }
    };

    public MainViewPager() {

        friendListView = new FriendListFragment();
        roomListView = new RoomListFragment();
        scheduleListView = new ScheduleListFragment();
        notificationListView = new NotificationListFragment();
        profileView = new ProfileFragment();
        MainViewPager.roomData = new RoomData();


    }

//    public boolean isBind = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setMainActionBar();

        setValues();
        bindView();
        setUpEvents();

//        LinearLayout layout = (LinearLayout)ll.findViewWithTag(0);
//        ll.findViewWithTag(0).setBackgroundColor(getResources().getColor(R.color.colorAccent));
//        TextView textView = (TextView) (layout.getChildAt(0));
//        textView.setTextColor(Color.WHITE);
        linFriend.performClick();
        configTxt1.setOnClickListener(addFriendClickListner);
        configTxt2.setOnClickListener(editFriendClickListener);
    }


    @Override
    public void setUpEvents() {
        super.setUpEvents();

//        GPSTracer.getInstance().getLocation();
        DBSI db = new DBSI();
        String UserInfo[][] = db.selectQuery("select PKey, Name from User");
        UserPKey = Integer.parseInt(UserInfo[0][0]);
        UserName = UserInfo[0][1];

        Log.d("UserPKeyMain", UserPKey + "");

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

                if (tag == position) changeTabColor(position);
                Log.i("Position", " " + position);
                Log.i("Fragment : ", getSupportFragmentManager().findFragmentById(R.id.vp) + "");
                switch (position) {


                    case 0:
                        titleTxt.setText("친구목록");
                        backBtn.setVisibility(View.INVISIBLE);
                        configTxt1.setVisibility(View.VISIBLE);
                        configTxt2.setVisibility(View.VISIBLE);
                        configTxt1.setImageResource(R.mipmap.btn_add_friend); // 친구 추가
                        configTxt1.setOnClickListener(addFriendClickListner);
                        configTxt2.setImageResource(R.mipmap.btn_edit);
                        configTxt2.setOnClickListener(editFriendClickListener);
                        friendListView.reloadFunc();
//                        friendListView.testFunc();
//                        configTxt2.setText("편집");
                        break;
                    case 1:
                        MainViewPager.updateRoom(new AfterUpdate() {
                            @Override
                            public void onPost(RoomData data) {

                            }
                        });
                        titleTxt.setText("방 목록");
                        backBtn.setVisibility(View.INVISIBLE);
                        configTxt1.setVisibility(View.VISIBLE);
                        configTxt2.setVisibility(View.VISIBLE);
                        configTxt1.setImageResource(R.mipmap.btn_add);
                        configTxt1.setOnClickListener(createNewRoomListener);
                        configTxt2.setImageResource(R.mipmap.btn_edit);
                        configTxt2.setOnClickListener(null);
//                        configTxt2.setText("편집");
                        break;
                    case 2:
                        titleTxt.setText("내 일정");
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
                        configTxt2.setOnClickListener(configProfilListener);

                        break;

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    int position = vp.getCurrentItem();

                    switch (position) {
                        case 1:
                            MainViewPager.updateRoom(new AfterUpdate() {
                                @Override
                                public void onPost(RoomData data) {

                                }
                            });
                            break;
                        case 2:
                            ScheduleListFragment.updateSchedule();
                            break;
                    }

                    // 원하는 페이지에 맞게 조건
                    InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                    im.hideSoftInputFromWindow(vp.getWindowToken(), 0);
                }
            }
        });


        databaseReference.child("Notice").child(MainViewPager.getUserPKey() + "").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final FirebaseNoticeData data = dataSnapshot.getValue(FirebaseNoticeData.class);

                boolean flag = false;
                for(int i = 0; i< roomData.getRoomArrayList().size(); i++) {
                    if(roomData.getRoomArrayList().get(i).getRoomPKey().equals(data.getRoomKey())) {
                        flag = true;
                    }
                }

                if(!flag) {

                    switch (data.getStatus()) {

                        case 1:

                            updateRoom(new AfterUpdate() {
                                @Override
                                public void onPost(RoomData roomData) {
                                    MainViewPager.childAddListener(data.getRoomKey());
                                }
                            });

                            Log.d("NoticeTest", "Notice");
                            break;
                    }

                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        updateRoom(new AfterUpdate() {
            @Override
            public void onPost(RoomData data) {

                for(int i=0; i<data.getRoomArrayList().size(); i++) {
                    ChatData initData = new ChatData();
                    initData.setRoomPKey(data.getRoomArrayList().get(i).getRoomPKey());
                    chatList.add(initData);
                }

                initFirebase();
            }
        });

        //현재 위치 추적이 필요한 상황인지를 체크해서 시작해주면 될 듯 하다.
        //이 서비스가 실행되면 앱이 종료되지 않는다.
        startLocationService();

    }

    public void initFirebase() {

        for (int i = 0; i < roomData.getRoomArrayList().size(); i++) {
            final int finalI = i;

            databaseReference.child("Chat").child(roomData.getRoomArrayList().get(i).getRoomPKey()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("value.getChildrenCount", dataSnapshot.getValue() + "");
                    Log.d("value.getChildrenCount", dataSnapshot.getChildren() + "");
                    Log.d("value.getChildrenCount", dataSnapshot.getChildrenCount() + "");
                    chatList.get(finalI).setChatCount(Integer.parseInt(dataSnapshot.getChildrenCount() + ""));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            databaseReference.child("Chat").child(roomData.getRoomArrayList().get(i).getRoomPKey()).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    ChatData data = dataSnapshot.getValue(ChatData.class);
//                    Log.d("data.getChildrenCount", dataSnapshot.getChildrenCount() + "");

                    if (chatList.get(finalI).getRoomPKey().equals(data.getRoomPKey())) {

                        chatList.get(finalI).addItem(data);
                        chatList.get(finalI).getAdapter().notifyDataSetChanged();

                        RoomListFragment.roomListView.setAdapter(roomData.getAdapter());
                        roomData.getAdapter().notifyDataSetChanged();

//                        Log.d("message", data.getMessage());
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Log.d("onChildChanged", "onChildChanged");

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    public static void childAddListener(String roomPKey) {
        ChatData chat = new ChatData();
        chat.setRoomPKey(roomPKey);
        chatList.add(chat);

        databaseReference.child("Chat").child(roomPKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("value.getChildrenCount", dataSnapshot.getValue() + "");
                Log.d("value.getChildrenCount", dataSnapshot.getChildren() + "");
                Log.d("value.getChildrenCount", dataSnapshot.getChildrenCount() + "");
                chatList.get(chatList.size()-1).setChatCount(Integer.parseInt(dataSnapshot.getChildrenCount() + ""));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.child("Chat").child(roomPKey).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("add.getChildrenCount", dataSnapshot.getChildrenCount() + "");

                ChatData data = dataSnapshot.getValue(ChatData.class);
                chatList.get(chatList.size()-1).addItem(data);
                chatList.get(chatList.size()-1).getAdapter().notifyDataSetChanged();

                RoomListFragment.roomListView.setAdapter(roomData.getAdapter());
                roomData.getAdapter().notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public static void updateRoom(final AfterUpdate delegate) {

        Params params = new Params();
        params.add("UserPKey", MainViewPager.getUserPKey() + "");

        new HttpNetwork("GetRoomList.php", params.getParams(), new HttpNetwork.AsyncResponse() {
            @Override
            public void onSuccess(String response) {

                try {
                    ParseData parse = new ParseData();
                    final JSONArray roomList = parse.parseJsonArray(response);
                    MainViewPager.roomData = new RoomData();

                    for (int i = 0; i < roomList.length(); i++) {
                        JSONObject jsonRoomData = new JSONObject(roomList.get(i).toString());
                        MainViewPager.roomData.addItem(jsonRoomData.getString("PKey"), jsonRoomData.getString("Name"), jsonRoomData.getString("Description"), jsonRoomData.getString("ChatCount"));

                        Log.d("roomPKey", jsonRoomData.getString("PKey"));
                        Log.d("Name", jsonRoomData.getString("Name"));
                        Log.d("DESC", jsonRoomData.getString("Description"));
                        Log.d("ChatCount", jsonRoomData.getString("ChatCount"));

                    }

                    RoomListFragment.roomListView.setAdapter(MainViewPager.roomData.getAdapter());
                    MainViewPager.roomData.getAdapter().notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                delegate.onPost(roomData);

            }

            @Override
            public void onFailure(String response) {

            }

            @Override
            public void onPreExcute() {

            }
        });
    }

    @Override
    public void onBackPressed() {

        if (CheckLocationTimer != null) {
            CheckLocationTimer.cancel();
            CheckLocationTimer = null;
        }

        ExitMainDialog emd = new ExitMainDialog(this);
        if(!isFinishing())
            emd.show();
        //super.onBackPressed();

    }

    public void startLocationService() {

        //서비스가 시작되있지 않다면 서비스를 시작하고 바인딩 해준다.
        if (!isBind) {

//            Intent locationIntent = new Intent(MainViewPager.this, LocationService.class);

            startService(new Intent(MainViewPager.this, LocationService.class).putExtra("UserPKey", getUserPKey() + ""));
            bindService(new Intent(MainViewPager.this, LocationService.class), sconn, BIND_AUTO_CREATE);
            isBind = true;
        }

    }

    private void changeTabColor(int position) {

        ll.findViewWithTag(position).setSelected(true);
        ll.findViewWithTag(position).setBackgroundResource(R.drawable.round_background_main);

        for (int i = 0; i < 5; i++) {

            LinearLayout layout = ll.findViewWithTag(i);
            TextView textView = (TextView) layout.getChildAt(0);

            if (i != position) {

                ll.findViewWithTag(i).setSelected(false);
                ll.findViewWithTag(i).setBackgroundResource(R.drawable.round_background);
                textView.setTextColor(getResources().getColor(R.color.colorAccent));

            } else
                textView.setTextColor(Color.WHITE);

        }

    }


    @Override
    public void setValues() {

        super.setValues();

        // 태그값을 먹여야 밑의 프래그먼트 내의 함수를 MainViewPager에서 실행시킬수 있다.
//        getSupportFragmentManager().beginTransaction().add(friendListView, "1").commit();
//        getSupportFragmentManager().beginTransaction().add(roomListView, "2").commit();
//        getSupportFragmentManager().beginTransaction().add(scheduleListView, "3").commit();
//        getSupportFragmentManager().beginTransaction().add(notificationListView, "4").commit();
//        getSupportFragmentManager().beginTransaction().add(profileView, "5").commit();
    }

    @Override
    public void setMainActionBar() {
        super.setMainActionBar();
        titleTxt.setText("친구목록");
        backBtn.setVisibility(View.INVISIBLE);
        configTxt1.setImageResource(R.mipmap.btn_add_friend);
        configTxt2.setImageResource(R.mipmap.btn_edit);
    }

    // 프래그먼트간 페이지 이동
    View.OnClickListener movePageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int tag = (int) v.getTag();
            vp.setCurrentItem(tag);

            int i = 0;

            while (i < 5) {
                if (tag == i) {

                    LinearLayout layout = ll.findViewWithTag(i);
                    ll.findViewWithTag(i).setSelected(true);
                    ll.findViewWithTag(i).setBackgroundResource(R.drawable.round_background_main);
                    TextView textView = (TextView) (layout.getChildAt(0));
                    textView.setTextColor(Color.WHITE);
                } else {

                    LinearLayout layout = ll.findViewWithTag(i);
                    ll.findViewWithTag(i).setSelected(false);
                    ll.findViewWithTag(i).setBackgroundResource(R.drawable.round_background);
                    TextView textView = (TextView) (layout.getChildAt(0));
                    textView.setTextColor(getResources().getColor(R.color.mainTxtColor));
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
                    return friendListView;
                case 1:
                    return roomListView;
                case 2:
                    return scheduleListView;
                case 3:
                    return notificationListView;
                case 4:
                    return profileView;
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

    // 상황에 따른 customActionbar 클릭 이벤트 리스너 클래스 모음//

    View.OnClickListener addFriendClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            titleTxt.setText("친구추가");
            configTxt1.setVisibility(View.INVISIBLE);
            configTxt2.setImageResource(R.mipmap.btn_ok);
//            final FriendListFragment friendListView = (FriendListFragment)getSupportFragmentManager().findFragmentByTag("1");

            friendListView.findFriendFunc();

            configTxt2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    titleTxt.setText("친구목록");
                    backBtn.setVisibility(View.INVISIBLE);
                    configTxt1.setVisibility(View.VISIBLE);
                    configTxt2.setVisibility(View.VISIBLE);
                    configTxt1.setImageResource(R.mipmap.btn_add_friend); // 친구 추가
                    configTxt1.setOnClickListener(addFriendClickListner);
                    configTxt2.setImageResource(R.mipmap.btn_edit);
                    configTxt2.setOnClickListener(editFriendClickListener);
                    friendListView.reloadFunc();
                }
            });
        }
    };

    View.OnClickListener editFriendClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i("버튼 클릭", "ㅇㅇㅇ");
//            FriendListFragment friendListView = (FriendListFragment) getSupportFragmentManager().findFragmentByTag("1");

            friendListView.showBlockBtn();
        }
    };

    View.OnClickListener createNewRoomListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CreateRoomDialog dialog = new CreateRoomDialog(MainViewPager.this);
            dialog.show();
        }
    };

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

        vp = findViewById(R.id.vp);
        ll = findViewById(R.id.ll);

        this.vp = findViewById(R.id.vp);
        this.ll = findViewById(R.id.ll);
        this.linProfile = findViewById(R.id.linProfile);
        this.tvProfile = findViewById(R.id.tvProfile);
        this.linArlert = findViewById(R.id.linArlert);
        this.tvAlert = findViewById(R.id.tvAlert);
        this.linSchedule = findViewById(R.id.linSchedule);
        this.tvSchedule = findViewById(R.id.tvSchedule);
        this.linRoomBtn = findViewById(R.id.linRoomBtn);
        this.tvRoom = findViewById(R.id.tvRoom);
        this.linFriend = findViewById(R.id.linFriend);
        this.tvFriend = findViewById(R.id.tvFriend);

        this.tvMainExitNo = (TextView) findViewById(R.id.tvMainExitNo);
        this.tvMainExitYes = (TextView) findViewById(R.id.tvMainExitYes);

    }

    public static int getUserPKey() {
        return UserPKey;
    }

    public static String getUserName() {
        return UserName;
    }

    @Override
    protected void onDestroy() {
        if (CheckLocationTimer != null) {
            CheckLocationTimer.cancel();
            CheckLocationTimer = null;
        }
        unbindService(sconn);
        isBind = false;
        locationService = null;
        super.onDestroy();
    }

    @Override
    protected void onResume() {

        Log.d("onResume", "Here");
        super.onResume();
    }

}
