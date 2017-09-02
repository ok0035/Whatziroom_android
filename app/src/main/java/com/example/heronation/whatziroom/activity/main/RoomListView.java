package com.example.heronation.whatziroom.activity.main;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heronation.whatziroom.Data.RoomData;
import com.example.heronation.whatziroom.R;
import com.example.heronation.whatziroom.activity.room.RoomViewPager;
import com.example.heronation.whatziroom.adapter.RoomAdapter;

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

        layout = (LinearLayout) inflater.inflate(R.layout.layout_room_list, container, false);
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

    public void createNewRoomFunc(){
        LayoutInflater dialog = LayoutInflater.from(getActivity());
        final View dialogLayout = dialog.inflate(R.layout.create_room_dialog, null);
        final Dialog myDialog = new Dialog(getActivity());
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(dialogLayout);
//        myDialog.setTitle("대화상자 제목이다");

        DisplayMetrics dm = getActivity().getResources().getDisplayMetrics();

        int deviceWidth = dm.widthPixels;
        int deviceHeight = dm.heightPixels;

        ViewGroup.LayoutParams params = myDialog.getWindow().getAttributes();
        params.width = deviceWidth;
        params.height = deviceHeight/2;

        myDialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        myDialog.show();

//        Button btn_ok = (Button) dialogLayout.findViewById(R.id.btn_ok);
//        Button btn_cancel = (Button) dialogLayout.findViewById(R.id.btn_cancel);

        TextView btn_ok = (TextView)dialogLayout.findViewById(R.id.newRoomConfirmBtn);
        TextView btn_cancel = (TextView)dialogLayout.findViewById(R.id.newRoomCancelBtn);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"방 개설 완료",Toast.LENGTH_SHORT).show();
                myDialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "전송완료", Toast.LENGTH_SHORT).show();
                myDialog.cancel();
            }
        });
    }

}
