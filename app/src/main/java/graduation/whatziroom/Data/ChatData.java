package graduation.whatziroom.Data;

import java.util.ArrayList;

import graduation.whatziroom.activity.room.RoomViewPager;
import graduation.whatziroom.adapter.ChatAdapter;

/**
 * Created by mapl0 on 2017-10-01.
 */

public class ChatData {

    private String UserPKey;
    private String RoomPKey;
    private String Name;
    private String Message;
    private ArrayList<ChatData> ChatDataList;
    private ChatAdapter adapter;

    public String getFBToken() {
        return FBToken;
    }

    public void setFBToken(String FBToken) {
        this.FBToken = FBToken;
    }

    private String FBToken;
    public ChatData() {

        ChatDataList = new ArrayList<ChatData>();
        adapter = new ChatAdapter(RoomViewPager.mContext, ChatDataList);

    }

    public ChatData(String roomPKey, String userPKey, String name, String message) {

        RoomPKey = roomPKey;
        UserPKey = userPKey;
        Name = name;
        Message = message;

    }

    public String getUserPKey() {
        return UserPKey;
    }

    public void setUserPKey(String userPKey) {
        UserPKey = userPKey;
    }

    public String getRoomPKey() {
        return RoomPKey;
    }

    public void setRoomPKey(String roomPKey) {
        RoomPKey = roomPKey;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public ArrayList<ChatData> getChatDataList() {
        return ChatDataList;
    }

    public void setChatDataList(ArrayList<ChatData> chatDataList) {
        ChatDataList = chatDataList;
    }

    public ChatAdapter getAdapter() {
//        adapter.notifyDataSetChanged();
        return adapter;
    }

    public void setAdapter(ChatAdapter adapter) {
        this.adapter = adapter;
    }


    public void addItem(String roomPKey, String userPKey, String name, String message) {

        ChatDataList.add(new ChatData(roomPKey, userPKey, name, message));

    }

    public void addItem(ChatData data) {

        ChatDataList.add(data);

    }
}
