package graduation.whatziroom.Data;


import java.util.ArrayList;

import graduation.whatziroom.activity.base.BaseActivity;
import graduation.whatziroom.adapter.RoomAdapter;

/**
 * Created by Heronation on 2017-07-10.
 */

public class RoomData {

    private String roomName;
    private String roomUserNumber;
    private String roomDate;
    private String roomPKey;

    private RoomAdapter adapter;

    private ArrayList<RoomData> roomArrayList;

    public RoomData() {
        super();
        roomArrayList = new ArrayList<>();
        adapter = new RoomAdapter(BaseActivity.mContext, roomArrayList);
    }

    public RoomData(String PKey, String name, String date) {

        setRoomPKey(PKey);
        setRoomName(name);
        setRoomDate(date);
    }

    public ArrayList<RoomData> getRoomArrayList() {
        return roomArrayList;
    }

    public String getRoomPKey() {
        return roomPKey;
    }

    public void setRoomPKey(String roomPKey) {
        this.roomPKey = roomPKey;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomUserNumber() {
        return roomUserNumber;
    }

    public void setRoomUserNumber(String roomUserNumber) {
        this.roomUserNumber = roomUserNumber;
    }

    public String getRoomDate() {
        return roomDate;
    }

    public void setRoomDate(String roomDate) {
        this.roomDate = roomDate;
    }



    public void addItem(String roomPKey, String name, String date) {

        roomArrayList.add(new RoomData(roomPKey ,name, date));

    }

    public RoomAdapter getAdapter() {

        return adapter;
    }



}
