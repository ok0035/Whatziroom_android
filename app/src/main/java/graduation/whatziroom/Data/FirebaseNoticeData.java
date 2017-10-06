package graduation.whatziroom.Data;

/**
 * Created by mapl0 on 2017-10-06.
 */

public class FirebaseNoticeData {

    //0 친구 1 방 2 스케줄 3 알림 4 프로필
    //5 방정보 6 채팅 7 방 친구/요청자
    private int Status;
    private String RoomKey;

    public FirebaseNoticeData() {

    }

    public FirebaseNoticeData(int status, String roomKey) {
        Status = status;
        RoomKey = roomKey;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getRoomKey() {
        return RoomKey;
    }

    public void setRoomKey(String roomKey) {
        RoomKey = roomKey;
    }
}
