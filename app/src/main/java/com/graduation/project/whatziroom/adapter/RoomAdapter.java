package com.graduation.project.whatziroom.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.graduation.project.whatziroom.Data.RoomData;
import com.graduation.project.whatziroom.R;

import java.util.ArrayList;

/**
 * Created by Heronation on 2017-07-10.
 */

public class RoomAdapter extends ArrayAdapter {

    Context mContext = null;
    ArrayList<RoomData> mList = null;
    LayoutInflater inf = null;
    int flag;

    public RoomAdapter(Context context, ArrayList<RoomData> list, int flag){
        super(context, R.layout.room_list_item, list);
        mContext = context;
        mList = list;
        inf = LayoutInflater.from(mContext);
        this.flag = flag;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row = convertView;

        if(row == null){
            row =inf.inflate(R.layout.room_list_item,null);
        }

        ImageView roomThumbNail = (ImageView)row.findViewById(R.id.roomListThumbImg);
        TextView roomName = (TextView)row.findViewById(R.id.roomNameTxt);
        TextView roomMakerName = (TextView)row.findViewById(R.id.roomMakerNameTxt);
        TextView roomTime = (TextView)row.findViewById(R.id.roomTimeTxt);
        TextView getOutBtn = (TextView)row.findViewById(R.id.roomOutTxt);

        RoomData data = mList.get(position);

        roomName.setText(data.getRoomName());
        roomMakerName.setText(data.getRoomMakerName());
        roomTime.setText(data.getRoomDate());

        return row;
    }

//    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
//    public void addItem(String roomNumber, String roomName, String roomDescription, String chatRoomPKey) {
//        GameListViewItem item = new GameListViewItem();
//
//        item.setRoomNumber(roomNumber);
//        item.setRoomName(roomName);
//        item.setDescription(roomDescription);
//        item.setChatRoomPKey(chatRoomPKey);
//
//        listViewItemList.add(item);
//    }
}
