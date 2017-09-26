package graduation.whatziroom.Data;


import java.util.ArrayList;

import graduation.whatziroom.activity.main.MainViewPager;
import graduation.whatziroom.adapter.ScheduleAdapter;

/**
 * Created by mapl0 on 2017-09-16.
 */

public class ScheduleData {

    public String getScheduleRoomPKey() {
        return scheduleRoomPKey;
    }

    public void setScheduleRoomPKey(String scheduleRoomPKey) {
        this.scheduleRoomPKey = scheduleRoomPKey;
    }

    private String scheduleRoomPKey;
    private String scheduleName;
    private String scheduleDate;
    private String scheduleDday;

    public String getSchedulePlace() {
        return schedulePlace;
    }

    public void setSchedulePlace(String schedulePlace) {
        this.schedulePlace = schedulePlace;
    }

    private String schedulePlace;

    public ArrayList<ScheduleData> getScheduleList() {
        return scheduleList;
    }

    private ArrayList<ScheduleData> scheduleList;

    public ScheduleData() {
        super();
        scheduleList = new ArrayList<>();
    }

    public ScheduleData(String roomPKey, String sName, String sPlace, String sDate, String sDday) {

        scheduleRoomPKey = roomPKey;
        scheduleName = sName;
        schedulePlace = sPlace;
        scheduleDate = sDate;
        scheduleDday = sDday;

    }

    public ScheduleAdapter getAdapter() {

        ScheduleAdapter adapter = new ScheduleAdapter(MainViewPager.mContext, scheduleList);

        return adapter;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getScheduleDday() {
        return scheduleDday;
    }

    public void setScheduleDday(String scheduleDday) {
        this.scheduleDday = scheduleDday;
    }

    public void addItem(String roomPKey, String scheduleName, String schedulePlace, String scheduleDate, String scheduleDday) {

        scheduleList.add(new ScheduleData(roomPKey, scheduleName, schedulePlace, scheduleDate, scheduleDday));

    }
}
