package graduation.whatziroom.Data;

import java.util.ArrayList;

import graduation.whatziroom.activity.room.RoomViewPager;
import graduation.whatziroom.adapter.RoomInfoAdapter;

/**
 * Created by mapl0 on 2017-09-28.
 */

public class RoomInfoData {

    private String ImageURL;
    private String Title;
    private String Place;
    private String Time;
    private String Description;
    private String Name;
    private String OldAddress;
    private String NewAddress;
    private String TEL;
    private String WebURL;
    private String SchedulePKey;

    private String Status;
    private ArrayList<RoomInfoData> RoomInfoList;

    public RoomInfoAdapter getAdapter() {
        adapter = new RoomInfoAdapter(RoomViewPager.mContext, RoomInfoList);
        return adapter;
    }

    public void setAdapter(RoomInfoAdapter adapter) {
        this.adapter = adapter;
    }

    private RoomInfoAdapter adapter;

    public RoomInfoData(String schedulePKey, String status, String imageURL, String title, String place, String time, String description, String name, String oldAddress, String newAddress, String tel, String webUrl) {
        super();

        SchedulePKey = schedulePKey;
        Status = status;
        ImageURL = imageURL;
        Title = title;
        Place = place;
        Time = time;
        Description = description;
        Name = name;
        OldAddress = oldAddress;
        NewAddress = newAddress;
        TEL = tel;
        WebURL = webUrl;

    }

    public RoomInfoData() {
        super();
        RoomInfoList = new ArrayList<RoomInfoData>();

    }

    public void addItem(String schedulePKey, String status, String imageURL, String title, String place, String time, String description, String name, String oldAddress, String newAddress, String tel, String webUrl) {

        RoomInfoList.add(new RoomInfoData(schedulePKey, status, imageURL, title, place, time, description, name, oldAddress, newAddress, tel, webUrl));
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getSchedulePKey() {
        return SchedulePKey;
    }

    public void setSchedulePKey(String schedulePKey) {
        SchedulePKey = schedulePKey;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getOldAddress() {
        return OldAddress;
    }

    public void setOldAddress(String oldAddress) {
        OldAddress = oldAddress;
    }

    public String getNewAddress() {
        return NewAddress;
    }

    public void setNewAddress(String newAddress) {
        NewAddress = newAddress;
    }

    public String getTEL() {
        return TEL;
    }

    public void setTEL(String TEL) {
        this.TEL = TEL;
    }

    public String getWebURL() {
        return WebURL;
    }

    public void setWebURL(String webURL) {
        this.WebURL = webURL;
    }

    public ArrayList<RoomInfoData> getRoomInfoList() {
        return RoomInfoList;
    }

    public void setRoomInfoList(ArrayList<RoomInfoData> roomInfoList) {
        RoomInfoList = roomInfoList;
    }




}
