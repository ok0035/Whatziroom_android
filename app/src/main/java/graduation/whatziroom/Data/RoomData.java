package graduation.whatziroom.Data;


import java.util.ArrayList;

import graduation.whatziroom.activity.base.BaseActivity;
import graduation.whatziroom.adapter.RoomAdapter;

/**
 * Created by Heronation on 2017-07-10.
 */

public class RoomData {

    private String roomName;
    private String roomFounderName;
    private String roomDate;
    private String roomPKey;
    private String chatCount;

    private RoomAdapter adapter;

    private ArrayList<RoomData> roomArrayList;

    public RoomData() {
        super();
        roomArrayList = new ArrayList<>();
    }

    public RoomData(String PKey, String name, String date, String count, String founder) {

        setRoomPKey(PKey);
        setRoomName(name);
        setRoomDate(date);
        setChatCount(count);
        setRoomFounderName(founder);

    }

    public RoomData(String PKey, String name, String date, String founder) {

        setRoomPKey(PKey);
        setRoomName(name);
        setRoomDate(date);
        setRoomFounderName(founder);

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

    public String getRoomFounderName() {
        return roomFounderName;
    }

    public void setRoomFounderName(String roomFounderName) {
        this.roomFounderName = roomFounderName;
    }

    public String getRoomDate() {
        return roomDate;
    }

    public void setRoomDate(String roomDate) {
        this.roomDate = roomDate;
    }

    public String getChatCount() {
        return chatCount;
    }

    public void setChatCount(String chatCount) {
        this.chatCount = chatCount;
    }



    public void addItem(String roomPKey, String name, String date, String count, String founder) {

        roomArrayList.add(new RoomData(roomPKey ,name, date, count, founder));

    }

    public void addItem(String roomPKey, String name, String date, String founder) {

        roomArrayList.add(new RoomData(roomPKey ,name, date, founder));

    }

    public RoomAdapter getAdapter() {
        adapter = new RoomAdapter(BaseActivity.mContext, roomArrayList);
        return adapter;
    }

}
