package com.example.heronation.whatziroom.activity.room;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.heronation.whatziroom.R;
import com.example.heronation.whatziroom.adapter.ChatAdapter;

/**
 * Created by ATIV on 2017-06-25.
 */

public class RoomFragment2 extends Fragment {
    LinearLayout layout;

    ListView m_ListView;
    ChatAdapter m_Adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layout = (LinearLayout) inflater.inflate(R.layout.activity_room_sub_2, container, false);
        // 커스텀 어댑터 생성
        m_Adapter = new ChatAdapter();

        // Xml에서 추가한 ListView 연결
        m_ListView = (ListView) layout.findViewById(R.id.lvChat);

        // ListView에 어댑터 연결
        m_ListView.setAdapter(m_Adapter);

        m_Adapter.add("이건 뭐지",1);
        m_Adapter.add("쿨쿨",1);
        m_Adapter.add("쿨쿨쿨쿨",0);
        m_Adapter.add("재미있게",1);
        m_Adapter.add("놀자라구나힐힐 감사합니다. 동해물과 백두산이 마르고 닳도록 놀자 놀자 우리 놀자",1);
        m_Adapter.add("재미있게",1);
        m_Adapter.add("재미있게",0);
        m_Adapter.add("2015/11/20",2);
        m_Adapter.add("재미있게",1);
        m_Adapter.add("재미있게",1);

        return layout;
    }
    
}
