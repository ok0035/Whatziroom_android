package com.graduation.project.whatziroom.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.graduation.project.whatziroom.Data.RoomData;
import com.graduation.project.whatziroom.R;
import com.graduation.project.whatziroom.activity.room.RoomViewPager;
import com.graduation.project.whatziroom.adapter.RoomAdapter;

import java.util.ArrayList;

/**
 * Created by ATIV on 2017-06-25.
 */

public class RoomListView extends Fragment {
    LinearLayout layout;
    ImageView searchBtn;
    ArrayList<RoomData> roomListItem;
    ListView roomListView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layout = (LinearLayout) inflater.inflate(R.layout.room_list, container, false);
        roomListView = (ListView) layout.findViewById(R.id.roomListView);

        roomListItem= new ArrayList<>();

        RoomData r1 = new RoomData();
        r1.setRoomName("히어로네이션");
        r1.setRoomMakerName("Choi");
        r1.setRoomDate("17.05.06");
        roomListItem.add(r1);

        RoomData r2 = new RoomData();
        r2.setRoomName("UOS");
        r2.setRoomMakerName("Hwang");
        r2.setRoomDate("16.07.04");
        roomListItem.add(r2);

        RoomData r3 = new RoomData();
        r3.setRoomName("Dream");
        r3.setRoomMakerName("Park");
        r3.setRoomDate("15.08.20");
        roomListItem.add(r3);

        RoomAdapter roomAdapter = new RoomAdapter(getActivity(), roomListItem, 0);
        roomListView.setAdapter(roomAdapter);
        roomListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), RoomViewPager.class);
                startActivity(intent);
            }
        });

        searchBtn = (ImageView) layout.findViewById(R.id.searchRoom);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return layout;
    }

}
