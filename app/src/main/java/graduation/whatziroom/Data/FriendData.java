package graduation.whatziroom.Data;

/**
 * Created by Heronation on 2017-07-10.
 */

public class FriendData {

    private String UserName;
    private int UserPKey;
    private String ThumbNailImagePath;
    private String freindStatus;
    private int FirendTablePKey;

    public int getFirendTablePKey() {
        return FirendTablePKey;
    }

    public void setFirendTablePKey(int firendTablePKey) {
        FirendTablePKey = firendTablePKey;
    }


    public String getFreindStatus() {
        return freindStatus;
    }

    public void setFreindStatus(String freindStatus) {
        this.freindStatus = freindStatus;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public int getUserPKey() {
        return UserPKey;
    }

    public void setUserPKey(int userPKey) {
        UserPKey = userPKey;
    }

    public String getThumbNailImagePath() {
        return ThumbNailImagePath;
    }

    public void setThumbNailImagePath(String thumbNailImagePath) {
        ThumbNailImagePath = thumbNailImagePath;
    }

}
