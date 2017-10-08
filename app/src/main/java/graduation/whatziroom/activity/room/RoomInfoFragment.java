package graduation.whatziroom.activity.room;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import graduation.whatziroom.Data.RoomInfoData;
import graduation.whatziroom.R;
import graduation.whatziroom.activity.base.BasicMethod;
import graduation.whatziroom.network.HttpNetwork;
import graduation.whatziroom.network.Params;
import graduation.whatziroom.util.ParseData;


/**
 * Created by ATIV on 2017-06-25.
 */

public class RoomInfoFragment extends Fragment implements BasicMethod{

    public static View infoLayout;
    private ImageView[] ivAttendee;
    private LinearLayout linAttendee;
    private static String isEmpty = "notEmpty";

    public static TextView tvNeedCreateSchedule;

    private static ListView roomInfoList;

//    private ProgressDialog mProgressDialog;

    public String getIsEmpty() {
        return isEmpty;
    }
    public void setIsEmpty(String isEmpty) {
        RoomInfoFragment.isEmpty = isEmpty;
    }

    public RoomInfoFragment() {
        super();

    }

    // 우선 채팅부터 구현하고 그 뒤에 디데이도 추가하자
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        infoLayout = (FrameLayout) inflater.inflate(R.layout.room_information, container, false);

        setValues();
        bindView();
        setUpEvents();

        return infoLayout;
    }

    //dp값을 입력하여 px로 변환하여 반환해줌
    public int convertDPtoPX(int size) {

        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, r.getDisplayMetrics());

        return px;

    }

    @Override
    public void setUpEvents() {

        if(RoomViewPager.getRoomPKey() == 0) {
            tvNeedCreateSchedule.setVisibility(View.VISIBLE);
        } else {

            Params params = new Params();
            params.add("PKey", RoomViewPager.getRoomPKey() + "");

            new HttpNetwork("IsEmptySchedule.php", params.getParams(), new HttpNetwork.AsyncResponse() {

                @Override
                public void onSuccess(final String response) {
                    //Log.d("re", response);
                    setIsEmpty(response);

                    updateRoomInfo();

                    switch (getIsEmpty()) {
                        case "empty":
                            tvNeedCreateSchedule.setVisibility(View.VISIBLE);
                            break;

                        case "notEmpty":
                            tvNeedCreateSchedule.setVisibility(View.GONE);
                            break;
                    }

                }

                @Override
                public void onFailure(String response) {

                }

                @Override
                public void onPreExcute() {

                }

            });

        }

    }

    public static void updateRoomInfo() {

        Params params = new Params();
        params.add("RoomPKey", String.valueOf(RoomViewPager.getRoomPKey()));

        new HttpNetwork("GetScheduleData.php", params.getParams(), new HttpNetwork.AsyncResponse() {
            @Override
            public void onSuccess(String response) {

                ParseData parse = new ParseData();
                try {

                    //Log.d("asdf", parse.parseJsonArray(response).get(0) + "");

                    JSONArray roomInfoArray = parse.parseJsonArray(response);
                    RoomInfoData roomInfoData = new RoomInfoData();

                    String parseTime;

                    for (int i = 0; i < roomInfoArray.length(); i++) {
                        JSONObject roomInfo = new JSONObject(roomInfoArray.get(i).toString());

                        //Log.d("Title", roomInfo.getString("Title"));

                        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        long dDay;

                        try {
                            Date date = transFormat.parse(roomInfo.getString("Time"));

//                            Log.d("Title", roomInfo.getString("Title"));
//                            Log.d("Time", (date.getYear() + 1900) + "");
//                            Log.d("Time", (date.getMonth() + 1) + "");
//                            Log.d("Time", date.getDate() + "");
//                            Log.d("Time", date.getHours() + "");
//                            Log.d("Time", date.getMinutes() + "");

                            parseTime =  (date.getYear() + 1900) + "년 " + (date.getMonth() + 1) + "월 " + date.getDate() + "일 " + date.getHours() + "시 " + date.getMinutes() + "분";

                            Calendar CalculateDate= Calendar.getInstance();

                            CalculateDate.setTime(date);
                            long goalTime = CalculateDate.getTimeInMillis();

                            CalculateDate.setTime(Calendar.getInstance().getTime());
                            long nowTime = CalculateDate.getTimeInMillis();

                            dDay = (goalTime - nowTime);

                            //밀리세컨드를 밀리초, 초, 분, 시간 으로 나눔
                            dDay = dDay / 1000 / 60 / 60 / 24;

                            Log.d("SListDate", date.getDate() + "");

                            Date nowDate = new Date();
                            nowDate.getDate();

                        } catch (ParseException e) {
                            e.printStackTrace();
                            dDay = -1;
                            parseTime = roomInfo.getString("Time");
                        }

                        roomInfoData.addItem(
                                roomInfo.getString("SchedulePKey"),
                                roomInfo.getString("ImageURL"),
                                roomInfo.getString("Title"),
                                roomInfo.getString("Place"),
                                roomInfo.getString("Longitude"),
                                roomInfo.getString("Latitude"), parseTime,
                                roomInfo.getString("Description"),
                                roomInfo.getString("Name"),
                                roomInfo.getString("OldAddress"),
                                roomInfo.getString("NewAddress"),
                                roomInfo.getString("TEL"),
                                roomInfo.getString("WURL"),
                                dDay + "");

                    }

                    roomInfoList.setAdapter(roomInfoData.getAdapter());
                    roomInfoData.getAdapter().notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(String response) {

            }

            @Override
            public void onPreExcute() {

            }
        });

    }

    @Override
    public void setValues() {

    }

    @Override
    public void bindView() {

        this.tvNeedCreateSchedule = (TextView) infoLayout.findViewById(R.id.tvNeedCreateSchedule);
        this.roomInfoList = (ListView) infoLayout.findViewById(R.id.roomInfoList);

    }
}
