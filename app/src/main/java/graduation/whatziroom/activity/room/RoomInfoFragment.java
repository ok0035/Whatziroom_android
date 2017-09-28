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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private static String isEmpty;
    private String result = "notEmpty";

    public static LinearLayout tvNeedCreateSchedule;

    private static int roomPKey = 0;
    private static int schedulePKey = 0;

    private static ListView roomInfoList;

    public static int getRoomPKey() {
        return roomPKey;
    }

    public static void setRoomPKey(String PKey) {
        if(PKey == null || PKey.equals("null")) roomPKey = 0;
        else roomPKey = Integer.parseInt(PKey);
    }

    public static int getSchedulePKey() {
        return schedulePKey;
    }

    public static void setSchedulePKey(int schedulePKey) {
        RoomInfoFragment.schedulePKey = schedulePKey;
    }

//    private ProgressDialog mProgressDialog;

    public static String getIsEmpty() {
        return isEmpty;
    }
    public void setIsEmpty(String isEmpty) {
        RoomInfoFragment.isEmpty = isEmpty;
    }

    public RoomInfoFragment() {
        super();


    }

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

        if(RoomInfoFragment.getRoomPKey() == 0) {
            tvNeedCreateSchedule.setVisibility(View.VISIBLE);
        } else {

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


    }

    public static void updateRoomInfo() {

        Params params = new Params();
        params.add("RoomPKey", String.valueOf(RoomInfoFragment.getRoomPKey()));

        new HttpNetwork("GetScheduleData.php", params.getParams(), new HttpNetwork.AsyncResponse() {
            @Override
            public void onSuccess(String response) {

                ParseData parse = new ParseData();
                try {

                    Log.d("asdf", parse.parseJsonArray(response).get(0) + "");

                    JSONArray roomInfoArray = parse.parseJsonArray(response);
                    RoomInfoData roomInfoData = new RoomInfoData();

                    for (int i = 0; i < roomInfoArray.length(); i++) {
                        JSONObject roomInfo = new JSONObject(roomInfoArray.get(i).toString());

                        Log.d("Title", roomInfo.getString("Title"));

                        roomInfoData.addItem(roomInfo.getString("ImageURL"), roomInfo.getString("Title"), roomInfo.getString("Place"),
                                roomInfo.getString("Time"), roomInfo.getString("Description"), roomInfo.getString("Name"), roomInfo.getString("OldAddress"),
                                roomInfo.getString("NewAddress"), roomInfo.getString("TEL"), roomInfo.getString("WURL"));

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

        this.tvNeedCreateSchedule = (LinearLayout) infoLayout.findViewById(R.id.tvNeedCreateSchedule);
        this.roomInfoList = (ListView) infoLayout.findViewById(R.id.roomInfoList);

    }
}
