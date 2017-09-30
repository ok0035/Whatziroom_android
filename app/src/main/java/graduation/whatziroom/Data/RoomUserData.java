package graduation.whatziroom.Data;

import java.util.ArrayList;

/**
 * Created by mapl0 on 2017-09-30.
 */

public class RoomUserData {

    private String RoomPKey;
    private String UserPKey;
    private String Name;
    private ArrayList<RoomUserData> UserList;

    public ArrayList<RoomUserData> getUserList() {
        return UserList;
    }

    public void setUserList(ArrayList<RoomUserData> userList) {
        UserList = userList;
    }

    public RoomUserData() {

        UserList = new ArrayList<RoomUserData>();
    }

    public String getRoomPKey() {
        return RoomPKey;
    }

    public void setRoomPKey(String roomPKey) {
        RoomPKey = roomPKey;
    }

    public String getUserPKey() {
        return UserPKey;
    }

    public void setUserPKey(String userPKey) {
        UserPKey = userPKey;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public void addItem(String userPKey, String roomPKey, String name) {

        UserPKey = userPKey;
        RoomPKey = roomPKey;
        Name = name;

    }

}
