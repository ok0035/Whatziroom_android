package graduation.whatziroom.activity.room;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import graduation.whatziroom.R;
import graduation.whatziroom.adapter.ChatAdapter;


/**
 * Created by ATIV on 2017-06-25.
 */

public class RoomChatFragment extends Fragment {
    LinearLayout layout;

    ListView m_ListVieww;
    ChatAdapter m_Adapter;
    EditText edChat;
    TextView sendButton;
    LinearLayout linChatList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layout = (LinearLayout) inflater.inflate(R.layout.chat, container, false);
        // 커스텀 어댑터 생성
        m_Adapter = new ChatAdapter();

        // Xml에서 추가한 ListView 연결
        m_ListVieww = (ListView) layout.findViewById(R.id.lvChat);
        edChat = (EditText) layout.findViewById(R.id.edChatText);
        sendButton = (TextView) layout.findViewById(R.id.sendChat);
        linChatList = (LinearLayout) layout.findViewById(R.id.linChatList);

        // ListView에 어댑터 연결
        m_ListVieww.setAdapter(m_Adapter);

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