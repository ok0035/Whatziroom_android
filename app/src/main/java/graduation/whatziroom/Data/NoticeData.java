package graduation.whatziroom.Data;

/**
 * Created by user on 2017-07-16.
 */

public class NoticeData {

    private String friendTablePKey;
    private String userName;
    private String srFlag;
    private String friendStatus;


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
