package com.example.heronation.whatziroom.activity.room;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.heronation.whatziroom.R;
import com.example.heronation.whatziroom.adapter.ChatAdapter;
import com.example.heronation.whatziroom.adapter.ChatNoticeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ATIV on 2017-06-25.
 */

public class RoomFragment2 extends Fragment {
    private LinearLayout layout;
    private ListView m_ListView;
    private ChatAdapter m_Adapter;
    private EditText edChat;
    private TextView sendButton;
    private LinearLayout linChatList;
    private RecyclerView recyclerview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layout = (LinearLayout) inflater.inflate(R.layout.activity_room_sub_2, container, false);
        // 커스텀 어댑터 생성
        m_Adapter = new ChatAdapter();

        // Xml에서 추가한 ListView 연결
        m_ListView = (ListView) layout.findViewById(R.id.lvChat);
        edChat = (EditText) layout.findViewById(R.id.edChatText);
        sendButton = (TextView) layout.findViewById(R.id.sendChat);
        linChatList = (LinearLayout) layout.findViewById(R.id.linChatList);

        //공지사항에 사용될 리싸이클뷰
        recyclerview = (RecyclerView) layout.findViewById(R.id.noticeChat);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        List<ChatNoticeAdapter.Item> data = new ArrayList<>();

        data.add(new ChatNoticeAdapter.Item(ChatNoticeAdapter.HEADER, "스케줄을 입력해주세요."));
        data.add(new ChatNoticeAdapter.Item(ChatNoticeAdapter.CHILD, "밥먹고"));
        data.add(new ChatNoticeAdapter.Item(ChatNoticeAdapter.CHILD, "술먹고"));
        data.add(new ChatNoticeAdapter.Item(ChatNoticeAdapter.CHILD, "집고고"));
//        data.add(new ChatNoticeAdapter.Item(ChatNoticeAdapter.HEADER, "Cars"));
//        data.add(new ChatNoticeAdapter.Item(ChatNoticeAdapter.CHILD, "Audi"));
//        data.add(new ChatNoticeAdapter.Item(ChatNoticeAdapter.CHILD, "Aston Martin"));
//        data.add(new ChatNoticeAdapter.Item(ChatNoticeAdapter.CHILD, "BMW"));
//        data.add(new ChatNoticeAdapter.Item(ChatNoticeAdapter.CHILD, "Cadillac"));

        // 동적으로 리스트 추가하는 법
//        ChatNoticeAdapter.Item places = new ChatNoticeAdapter.Item(ChatNoticeAdapter.HEADER, "Places");
//        places.invisibleChildren = new ArrayList<>();
//        places.invisibleChildren.add(new ChatNoticeAdapter.Item(ChatNoticeAdapter.CHILD, "Kerala"));
//        places.invisibleChildren.add(new ChatNoticeAdapter.Item(ChatNoticeAdapter.CHILD, "Tamil Nadu"));
//        places.invisibleChildren.add(new ChatNoticeAdapter.Item(ChatNoticeAdapter.CHILD, "Karnataka"));
//        places.invisibleChildren.add(new ChatNoticeAdapter.Item(ChatNoticeAdapter.CHILD, "Maharashtra"));

//        data.add(places);
        recyclerview.setAdapter(new ChatNoticeAdapter(data));

        // ListView에 어댑터 연결
        m_ListView.setAdapter(m_Adapter);

        //pull test!!!

//        m_Adapter.add("이건 뭐지",1);
//        m_Adapter.add("쿨쿨",1);
//        m_Adapter.add("쿨쿨쿨쿨",0);
//        m_Adapter.add("재미있게",1);
//        m_Adapter.add("놀자라구나힐힐 감사합니다. 동해물과 백두산이 마르고 닳도록 놀자 놀자 우리 놀자",1);
//        m_Adapter.add("재미있게",1);
//        m_Adapter.add("재미있게",0);
//        m_Adapter.add("2015/11/20",2);
//        m_Adapter.add("재미있게",1);
//        m_Adapter.add("재미있게",1);

        //PC에서 안드로이드 가상머신으로 테스트할때 편함 ㅎ
        //엔터키로 채팅 할 수 있음

//        linChatList.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//
//                if(event.getAction() == KeyEvent.ACTION_DOWN) {
//                    switch(keyCode) {
//
//                        case KeyEvent.KEYCODE_ENTER:
//
//                            Log.d("레이아웃 엔터", "들어옴!");
//
//                            if(edChat.length() != 0) {
//
//                                m_Adapter.add(edChat.getText().toString(), 1);
//                                m_Adapter.notifyDataSetChanged();
//                                edChat.setText("");
//
//                            } else edChat.setText("");
//
//                            break;
//                    }
//                }
//
//                return false;
//            }
//        });

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

        return layout;
    }
    
}
