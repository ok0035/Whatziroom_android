package graduation.whatziroom.activity.room;

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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import graduation.whatziroom.Data.ChatData;
import graduation.whatziroom.R;
import graduation.whatziroom.activity.base.BasicMethod;
import graduation.whatziroom.activity.main.MainViewPager;


/**
 * Created by ATIV on 2017-06-25.
 */

public class RoomChatFragment extends Fragment implements BasicMethod {

    private LinearLayout layout;
    private ListView lvChat;
    private ChatData chatData;
    private EditText edChat;
    private TextView tvSendChat;
    private LinearLayout llChat;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

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

//        채팅 구현 예상
//        파이어베이스에 유저키, 룸키, 메시지를 저장한다.
//        저장 후 보낼때는 무조건 오른쪽에(flag를 ChatData에 추가한다.) 받을 때는 UserPKey를 비교해서 자신이면 오른쪽에 뿌려주도록 한다.



        // ListView에 어댑터 연결
        lvChat.setAdapter(chatData.getAdapter());


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

                                chatData.addItem(RoomViewPager.getRoomPKey() + "", MainViewPager.getUserPKey() + "", MainViewPager.getUserName(), edChat.getText().toString(), 0);
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

                                chatData.addItem(RoomViewPager.getRoomPKey() + "", MainViewPager.getUserPKey() + "", MainViewPager.getUserName(), edChat.getText().toString(), 0);
                                chatData.getAdapter().notifyDataSetChanged();
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

                    chatData.addItem(RoomViewPager.getRoomPKey() + "", MainViewPager.getUserPKey() + "", MainViewPager.getUserName(), edChat.getText().toString(), 1);
                    chatData.getAdapter().notifyDataSetChanged();
                    edChat.setText("");
                }
            }
        });

    }

    @Override
    public void setValues() {
        chatData = new ChatData();
        // 커스텀 어댑터 생성
//        m_Adapter = new ChatAdapter();

    }

    public void updateInputList() {

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
