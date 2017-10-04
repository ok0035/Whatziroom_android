package graduation.whatziroom.Data;

import java.util.ArrayList;

/**
 * Created by mapl0 on 2017-09-30.
 */

public class RoomUserData {


    private int RoomPKey;
    private int UserPKey;
    private String Name;
    private String Longitude, Latitude;
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

    public RoomUserData(int userPKey, int roomPKey, String name) {


        UserPKey = userPKey;
        RoomPKey = roomPKey;
        Name = name;
    }

    public RoomUserData(int userPKey, double longitude, double latitude) {


    }

    public int getRoomPKey() {
        return RoomPKey;
    }

    public void setRoomPKey(int roomPKey) {
        RoomPKey = roomPKey;
    }

    public int getUserPKey() {
        return UserPKey;
    }

    public void setUserPKey(int userPKey) {
        UserPKey = userPKey;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public void addItem(int userPKey, int roomPKey, String name) {


        UserList.add(new RoomUserData(userPKey, roomPKey, name));

    }

}
