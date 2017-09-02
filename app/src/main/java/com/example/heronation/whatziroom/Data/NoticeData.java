package com.example.heronation.whatziroom.Data;

/**
 * Created by user on 2017-07-16.
 */

public class NoticeData {


    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
        System.out.println("데이터클래스 초기화");
    }

    public String getUserThumbnail() {
        return UserThumbnail;
    }

    public void setUserThumbnail(String userThumbnail) {
        UserThumbnail = userThumbnail;
    }

    public String getUserText() {
        return UserText;
    }

    public void setUserText(String userText) {
        UserText = userText;
    }

    private String UserName;
    private String UserThumbnail;
    private String UserText;

    public int getCheckFlag() {
        return checkFlag;
    }

    public void setCheckFlag(int checkFlag) {
        this.checkFlag = checkFlag;
    }

    private int checkFlag = 0; // 0대기, 1수락, 2거절


}
