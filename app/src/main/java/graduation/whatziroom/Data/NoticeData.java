package graduation.whatziroom.Data;

/**
 * Created by user on 2017-07-16.
 */

public class NoticeData {

    public String getSeperator() {
        return seperator;
    }

    public void setSeperator(String seperator) {
        this.seperator = seperator;
    }

    private String seperator;
    //친구 알림 용 변수//
    private String friendTablePKey;
    private String userName;
    private String srFlag;
    private String friendStatus;

    public String getRoomListPKey() {
        return RoomListPKey;
    }

    public void setRoomListPKey(String roomListPKey) {
        RoomListPKey = roomListPKey;
    }

    public String getUserName_Room() {
        return userName_Room;
    }

    public void setUserName_Room(String userName_Room) {
        this.userName_Room = userName_Room;
    }

    public String getRoomNAme() {
        return roomNAme;
    }

    public void setRoomNAme(String roomNAme) {
        this.roomNAme = roomNAme;
    }

    public String getRaFlag() {
        return raFlag;
    }

    public void setRaFlag(String raFlag) {
        this.raFlag = raFlag;
    }

    //방 신청 알림용 변수 //
    private String RoomListPKey;
    private String userName_Room;
    private String roomNAme;

    public String getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(String roomStatus) {
        this.roomStatus = roomStatus;
    }

    private String roomStatus;
    private String raFlag;


    public String getFriendPKey() {
        return friendTablePKey;
    }

    public void setFriendPKey(String friendPKey) {
        this.friendTablePKey = friendPKey;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSrFlag() {
        return srFlag;
    }

    public void setSrFlag(String srFlag) {
        this.srFlag = srFlag;
    }

    public String getFriendStatus() {
        return friendStatus;
    }

    public void setFriendStatus(String friendStatus) {
        this.friendStatus = friendStatus;
    }



}
