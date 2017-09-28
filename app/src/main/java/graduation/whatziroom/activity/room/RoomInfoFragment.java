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
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

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

    private static android.widget.ImageView ivInformation;
    private static android.widget.TextView textTitle;
    private static android.widget.TextView tvInfoGoing;
    private static android.widget.TextView tvInfoNotGoing;
    private static android.widget.TextView tvInfoTime;
    private static android.widget.TextView tvInfoPlace;
    private static android.widget.TextView tvInfoMaker;
    private static android.widget.TextView tvInfoDesc;
    private static android.widget.TextView tvInfoNewAdd;
    private static android.widget.TextView tvInfoOldAdd;
    private static android.widget.TextView tvInfoTel;
    private static android.widget.TextView tvInfoSite;
    public static android.widget.LinearLayout tvNeedCreateSchedule;

    private static int roomPKey = 0;
    private static int schedulePKey = 0;

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

    public static void setSchedulePKey(String schedulePKey) {
        RoomInfoFragment.schedulePKey = Integer.parseInt(schedulePKey);
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
//        params.add("SchedulePKey", String.valueOf(RoomInfoFragment.getSchedulePKey()));

        new HttpNetwork("GetScheduleData.php", params.getParams(), new HttpNetwork.AsyncResponse() {
            @Override
            public void onSuccess(String response) {

                ParseData parse = new ParseData();
                try {

                    Log.d("asdf", parse.parseJsonArray(response).get(0) + "");
                    JSONObject roomInfo = new JSONObject(parse.parseJsonArray(response).get(0).toString());

//                    Glide.with(SearchPlaceActivity.searchActivity).clear(ivInformation);

                    Log.d("Neme", roomInfo.getString("Name"));
                    Log.d("Place", roomInfo.getString("Place"));
                    Log.d("Time", roomInfo.getString("Time"));
                    Log.d("Description", roomInfo.getString("Description"));

                    Glide.with(RoomViewPager.roomInfoView.getContext()).load(roomInfo.getString("ImageURL")).into(ivInformation);
                    textTitle.setText(roomInfo.getString("Title").equals("null") ? "" : roomInfo.getString("Title"));
                    tvInfoPlace.setText(roomInfo.getString("Place").equals("null") ? "" : roomInfo.getString("Place"));
                    tvInfoTime.setText(roomInfo.getString("Time").equals("null") ? "" : roomInfo.getString("Time"));
                    tvInfoDesc.setText(roomInfo.getString("Description").equals("null") ? "" : roomInfo.getString("Description"));
                    tvInfoMaker.setText(roomInfo.getString("Name").equals("null") ? "" : roomInfo.getString("Name"));
                    tvInfoOldAdd.setText(roomInfo.getString("OldAddress").equals("null") ? "" : roomInfo.getString("OldAddress"));
                    tvInfoNewAdd.setText(roomInfo.getString("NewAddress").equals("null") ? "" : roomInfo.getString("NewAddress"));
                    tvInfoTel.setText(roomInfo.getString("TEL").equals("null") ? "" : roomInfo.getString("TEL"));
                    tvInfoSite.setText(roomInfo.getString("WURL").equals("null") ? "" : roomInfo.getString("WURL"));

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

        this.tvInfoDesc = (TextView) infoLayout.findViewById(R.id.tvInfoDesc);
        this.tvInfoMaker = (TextView) infoLayout.findViewById(R.id.tvInfoMaker);
        this.tvInfoPlace = (TextView) infoLayout.findViewById(R.id.tvInfoPlace);
        this.tvInfoTime = (TextView) infoLayout.findViewById(R.id.tvInfoTime);
        this.tvInfoNotGoing = (TextView) infoLayout.findViewById(R.id.tvInfoNotGoing);
        this.tvInfoGoing = (TextView) infoLayout.findViewById(R.id.tvInfoGoing);
        this.textTitle = (TextView) infoLayout.findViewById(R.id.textTitle);
        this.ivInformation = (ImageView) infoLayout.findViewById(R.id.ivInformation);
        this.tvNeedCreateSchedule = (LinearLayout) infoLayout.findViewById(R.id.tvNeedCreateSchedule);
        this.tvInfoSite = (TextView) infoLayout.findViewById(R.id.tvInfoSite);
        this.tvInfoTel = (TextView) infoLayout.findViewById(R.id.tvInfoTel);
        this.tvInfoOldAdd = (TextView) infoLayout.findViewById(R.id.tvInfoOldAdd);
        this.tvInfoNewAdd = (TextView) infoLayout.findViewById(R.id.tvInfoNewAdd);

    }
}
