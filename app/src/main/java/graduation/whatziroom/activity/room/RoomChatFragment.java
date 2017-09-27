package graduation.whatziroom.activity.room;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import net.daum.mf.map.api.MapView;

import java.util.Timer;
import java.util.TimerTask;

import graduation.whatziroom.R;
import graduation.whatziroom.activity.base.BasicMethod;
import graduation.whatziroom.adapter.ChatAdapter;


/**
 * Created by ATIV on 2017-06-25.
 */

public class RoomChatFragment extends Fragment implements BasicMethod {
    LinearLayout layout;

    ListView m_ListVieww;
    ChatAdapter m_Adapter;
    EditText edChat;
    TextView sendButton;
    LinearLayout linChatList;
    FrameLayout flChatMapView;
    public static SlidingUpPanelLayout slidingChat;
    ProgressDialog  mProgressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layout = (LinearLayout) inflater.inflate(R.layout.chat, container, false);

        bindView();
        setValues();
        setUpEvents();

        return layout;
    }

    @Override
    public void setUpEvents() {


        // ListView에 어댑터 연결
        m_ListVieww.setAdapter(m_Adapter);


//        PC에서 안드로이드 가상머신으로 테스트할때 편함 ㅎ
//        엔터키로 채팅 할 수 있음

        final MapView chatMap = new MapView(RoomViewPager.mContext);
        chatMap.setDaumMapApiKey(getResources().getString(R.string.APIKEY));
//        chatMap.setMapViewEventListener();
//        chatMap.setPOIItemEventListener(this);

        Handler handler = new Handler(Looper.getMainLooper());

        slidingChat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

//                if(motionEvent.getAction() == MotionEvent.ACTION_MOVE) slidingChat.onInterceptTouchEvent(motionEvent);

//                slidingChat

                return false;
            }
        });



        final Timer timer = new Timer();
        mProgressDialog = ProgressDialog.show(RoomViewPager.mContext, "",
                "잠시만 기다려 주세요.", true);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {

                    flChatMapView.addView(chatMap);

                    mProgressDialog.dismiss();
                    timer.cancel();
                }
            }
        };
        timer.schedule(task, 0, 2000);

//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//
//                flChatMapView.addView(chatMap);
//
//            }
//        });

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                flChatMapView.addView(chatMap);
//
//            }
//        }).start();

        linChatList.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch(keyCode) {

                        case KeyEvent.KEYCODE_ENTER:


                            Log.d("레이아웃 엔터", "들어옴!");

                            if(edChat.length() != 0) {

                                m_Adapter.add(edChat.getText().toString().replaceAll("\n", ""), 1);
                                m_Adapter.notifyDataSetChanged();
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

                                m_Adapter.add(edChat.getText().toString(), 1);
                                m_Adapter.notifyDataSetChanged();
                                edChat.setText("");

                            } else edChat.setText("");

                            break;
                    }
                }
                return false;
            }
        });



        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edChat.getText().length() != 0) {

                    m_Adapter.add(edChat.getText().toString(), 1);
                    m_Adapter.notifyDataSetChanged();
                    edChat.setText("");
                }
            }
        });

    }

    @Override
    public void setValues() {

        // 커스텀 어댑터 생성
        m_Adapter = new ChatAdapter();

    }

    @Override
    public void bindView() {
        // Xml에서 추가한 ListView 연결
        m_ListVieww = layout.findViewById(R.id.lvChat);
        edChat = layout.findViewById(R.id.edChatText);
        sendButton = layout.findViewById(R.id.sendChat);
        linChatList = layout.findViewById(R.id.linChatList);
        flChatMapView = layout.findViewById(R.id.flChatMapView);
        slidingChat = layout.findViewById(R.id.slidingChat);
    }
}
