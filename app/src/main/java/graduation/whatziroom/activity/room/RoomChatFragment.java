package graduation.whatziroom.activity.room;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import graduation.whatziroom.R;
import graduation.whatziroom.activity.base.BasicMethod;
import graduation.whatziroom.adapter.ChatAdapter;


/**
 * Created by ATIV on 2017-06-25.
 */

public class RoomChatFragment extends Fragment implements BasicMethod {

    private LinearLayout layout;
    private ListView m_ListView;
    private ChatAdapter m_Adapter;
    private EditText edChat;
    private TextView sendButton;
    private LinearLayout linChatList;


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
        m_ListView.setAdapter(m_Adapter);


//        PC에서 안드로이드 가상머신으로 테스트할때 편함 ㅎ
//        엔터키로 채팅 할 수 있음


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
        m_ListView = layout.findViewById(R.id.lvChat);
        edChat = layout.findViewById(R.id.edChatText);
        sendButton = layout.findViewById(R.id.sendChat);
        linChatList = layout.findViewById(R.id.linChatList);

    }
}
