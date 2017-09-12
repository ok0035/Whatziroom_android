package com.graduation.project.whatziroom.Data;

import java.util.ArrayList;

/**
 * Created by Heronation on 2017-07-10.
 */

public class RoomData {

    private String roomName;
    private String roomMakerName;
    private String roomDate;
    private ArrayList<RoomData> roomArrayList;

    public RoomData() {
        super();
        roomArrayList = new ArrayList<>();
    }

    public RoomData(String name, String makerName, String date) {
        roomName = name;
        roomMakerName = makerName;
        roomDate = date;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomMakerName() {
        return roomMakerName;
    }

    public void setRoomMakerName(String roomMakerName) {
        this.roomMakerName = roomMakerName;
    }

    public String getRoomDate() {
        return roomDate;
    }

    public void setRoomDate(String roomDate) {
        this.roomDate = roomDate;
    }

    public void addItem(String name, String makerName, String date) {

        roomName = name;
        roomMakerName = makerName;
        roomDate = date;

        roomArrayList.add(new RoomData(name, makerName, date));

    }

    public ArrayList<RoomData> getList() {
        return roomArrayList;
    }



}
