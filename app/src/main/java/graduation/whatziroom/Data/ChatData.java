package graduation.whatziroom.Data;

import java.util.ArrayList;

import graduation.whatziroom.activity.base.BaseActivity;
import graduation.whatziroom.adapter.ChatAdapter;

/**
 * Created by mapl0 on 2017-10-01.
 */

public class ChatData {

    private String UserPKey;
    private String RoomPKey;
    private String Name;
    private String message;
    private ArrayList<ChatData> chatDataList;
    private ChatAdapter adapter;
    private int chatCount = 0;

    public String getFBToken() {
        return FBToken;
    }

    public void setFBToken(String FBToken) {
        this.FBToken = FBToken;
    }

    private String FBToken;
    public ChatData() {

        chatDataList = new ArrayList<ChatData>();
        adapter = new ChatAdapter(BaseActivity.mContext, chatDataList);

    }

    public ChatData(String roomPKey, String userPKey, String name, String message) {

        RoomPKey = roomPKey;
        UserPKey = userPKey;
        Name = name;
        this.message = message;

    }

    public int getChatCount() {
        return chatCount;
    }

    public void setChatCount(int chatCount) {
        this.chatCount = chatCount;
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
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public ArrayList<ChatData> getChatDataList() {
        return chatDataList;
    }

    public void setChatDataList(ArrayList<ChatData> chatDataList) {
        this.chatDataList = chatDataList;
    }

    public ChatAdapter getAdapter() {
//        adapter.notifyDataSetChanged();
        return adapter;
    }

    public void setAdapter(ChatAdapter adapter) {
        this.adapter = adapter;
    }


    public void addItem(String roomPKey, String userPKey, String name, String message) {

        chatDataList.add(new ChatData(roomPKey, userPKey, name, message));

    }

    public void addItem(ChatData data) {

        chatDataList.add(data);

    }
}
