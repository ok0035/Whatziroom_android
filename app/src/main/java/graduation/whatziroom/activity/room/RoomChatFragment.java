package graduation.whatziroom.activity.room;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import graduation.whatziroom.Data.ChatData;
import graduation.whatziroom.R;
import graduation.whatziroom.activity.base.BasicMethod;
import graduation.whatziroom.activity.main.MainViewPager;
import graduation.whatziroom.network.HttpNetwork;
import graduation.whatziroom.network.Params;
import graduation.whatziroom.util.ParseData;

import static graduation.whatziroom.activity.room.RoomViewPager.getRoomPKey;
import static graduation.whatziroom.activity.room.RoomViewPager.mActivity;
import static graduation.whatziroom.activity.room.RoomViewPager.roomInfoFragment;

/**
 * Created by ATIV on 2017-06-25.
 */

public class RoomChatFragment extends Fragment implements BasicMethod {

    private LinearLayout layout;
    public ArrayList<ChatData> chatList = new ArrayList<ChatData>();
    public static ListView lvChat;
    private ChatData chatData;

    private EditText edChat;
    private TextView tvSendChat;
    private LinearLayout llChat;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    public boolean isTracing = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layout = (LinearLayout) inflater.inflate(R.layout.room_chat, container, false);

        bindView();
        setValues();
        setUpEvents();

        return layout;
    }

    @Override
    public void setUpEvents() {

        // ListView에 어댑터 연결

        for(int i=0; i<chatList.size(); i++) {
            if(chatList.get(i).getRoomPKey().equals(getRoomPKey() + "")) {
                lvChat.setAdapter(chatList.get(i).getAdapter());
                chatList.get(i).getAdapter().notifyDataSetChanged();

                Params params = new Params();
                params.add("UserPKey", MainViewPager.getUserPKey() + "");
                params.add("RoomPKey", getRoomPKey() + "");
                params.add("ChatCount", chatList.get(i).getChatCount() + "");

                new HttpNetwork("UpdateChatCount.php", params.getParams(), new HttpNetwork.AsyncResponse() {
                    @Override
                    public void onSuccess(String response) {
                        Log.d("UpdateChatCount", response);
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

//        lvChat.setAdapter(chatData.getAdapter());

//        PC에서 안드로이드 가상머신으로 테스트할때 편함 ㅎ
//        엔터키로 채팅 할 수 있음

        llChat.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch(keyCode) {

                        case KeyEvent.KEYCODE_ENTER:

                            Log.d("레이아웃 엔터", "들어옴!");

                            if(edChat.length() != 0) {

                                chatData.addItem(getRoomPKey() + "", MainViewPager.getUserPKey() + "", MainViewPager.getUserName(), edChat.getText().toString());
                                chatData.getAdapter().notifyDataSetChanged();
                                edChat.setText("");

                            } else edChat.setText("");

                            break;
                    }
                }

                return false;
            }
        });

        edChat.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch(keyCode) {

                        case KeyEvent.KEYCODE_ENTER:

                            if(edChat.length() != 0) {

                                ChatData data = new ChatData(getRoomPKey() + "", MainViewPager.getUserPKey() + "", MainViewPager.getUserName(), edChat.getText().toString());
                                databaseReference.child("Chat").child(getRoomPKey() + "").push().setValue(data);

                                edChat.setText("");

                            } else edChat.setText("");

                            break;
                    }
                }
                return false;
            }
        });

        tvSendChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edChat.getText().length() != 0) {
                    final String edchat = edChat.getText().toString();
                    ChatData data = new ChatData(getRoomPKey() + "", MainViewPager.getUserPKey() + "", MainViewPager.getUserName(), edChat.getText().toString());

                    databaseReference.child("Chat").child(getRoomPKey() + "").push().setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            sendPostToFCM(edchat);
                        }
                    });

                    Log.d("채팅 보내기...", "...");

                    edChat.setText("");
                }
            }
        });

//        databaseReference.child("Chat").child(RoomViewPager.getRoomPKey() + "").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                ChatData data = dataSnapshot.getValue(ChatData.class);
//                chatData.addItem(data);
//                chatData.getAdapter().notifyDataSetChanged();
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

    }

    public void updateChatMapInfo() {

        Params scheduleParams = new Params();
        scheduleParams.add("RoomPKey", getRoomPKey() + "");
        scheduleParams.add("Limit", 1 + "");

        new HttpNetwork("GetScheduleData.php", scheduleParams.getParams(), new HttpNetwork.AsyncResponse() {
            @Override
            public void onSuccess(String response) {
                Log.d("LIMIT", response);

                final TextView tvRoomChatTime = mActivity.findViewById(R.id.tvRoomChatTime);
//                final TextView tvRoomChatPlace = mActivity.findViewById(R.id.tvRoomChatPlace);
                final TextView tvRoomChatDDay = mActivity.findViewById(R.id.tvRoomChatDDay);
                final TextView tvRoomChatLocation = mActivity.findViewById(R.id.tvRoomChatLocation);
                final FrameLayout flChatSchedule = mActivity.findViewById(R.id.flChatSchedule);
                final Calendar CalculateDate = Calendar.getInstance();
                final LinearLayout llChatMapView = mActivity.findViewById(R.id.llChatMapView);
                final LinearLayout llRoomChatLayout = mActivity.findViewById(R.id.llRoomChatLayout);
                final LinearLayout llRoomChatNoSchedule = mActivity.findViewById(R.id.llRoomChatNoSchedule);

                if (!response.equals("[]")) {

                    roomInfoFragment.haveSchedule = true;

                    llRoomChatNoSchedule.setVisibility(View.GONE);
                    llRoomChatLayout.setVisibility(View.VISIBLE);

                    ParseData parse = new ParseData();

                    SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    long dDay;

                    try {

                        JSONArray roomInfoArray = parse.parseJsonArray(response);
                        JSONObject roomInfo = new JSONObject(roomInfoArray.get(0).toString());
                        Date date = transFormat.parse(roomInfo.getString("Time"));

                        roomInfoFragment.SoonSchedulePKey = roomInfo.getString("SchedulePKey");
                        roomInfoFragment.ScheduleTime = (date.getYear() + 1900) + "년 " + (date.getMonth() + 1) + "월 " + date.getDate() + "일 " + date.getHours() + "시 " + date.getMinutes() + "분";
                        roomInfoFragment.SchedulePlace = roomInfo.getString("Place");

//                        dDay = dDay / 1000 / 60 / 60 / 24;

                        roomInfoFragment.ScheduleLongitude = Double.parseDouble(roomInfo.getString("Longitude"));
                        roomInfoFragment.ScheduleLatitude = Double.parseDouble(roomInfo.getString("Latitude"));

//                        Log.d("Longitude", ScheduleLongitude + "");
//                        Log.d("Latitude", ScheduleLatitude + "");

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

                        tvRoomChatTime.setText(roomInfoFragment.ScheduleTime);
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
                            tvRoomChatDDay.setText("D - "+ hour + "H");

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
                                                tvRoomChatDDay.setText("D - "+ min + "M");
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
                    roomInfoFragment.haveSchedule = false;
                    roomInfoFragment.updateRoomInfo();

                    llChatMapView.setVisibility(View.GONE);
                    llRoomChatLayout.setVisibility(View.GONE);
                    llRoomChatNoSchedule.setVisibility(View.VISIBLE);


                    Log.d("Longitude", roomInfoFragment.ScheduleLongitude + "");
                    Log.d("Latitude", roomInfoFragment.ScheduleLatitude + "");
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

    private void sendPostToFCM(String msg){

        Params params = new Params();
        params.add("UserPKey", String.valueOf(MainViewPager.getUserPKey()));
        params.add("UserName", MainViewPager.getUserName());
        params.add("RoomPKey", String.valueOf(getRoomPKey()));
        params.add("RoomName", RoomViewPager.getRoomName());
        Log.d("CHATMSG+",msg+"0");
        params.add("ChatMsg", msg);

        new HttpNetwork("SendPostToFCM.php", params.getParams(), new HttpNetwork.AsyncResponse() {
            @Override
            public void onSuccess(String response) {
                Log.d("FCM", "Success"+response);
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
    public void setValues() {

        chatData = new ChatData();

    }

    @Override
    public void bindView() {
        // Xml에서 추가한 ListView 연결
        lvChat = layout.findViewById(R.id.lvChat);
        edChat = layout.findViewById(R.id.edChat);
        tvSendChat = layout.findViewById(R.id.tvSendChat);
        llChat = layout.findViewById(R.id.llChat);

    }
}
