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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import graduation.whatziroom.R;
import graduation.whatziroom.activity.base.BasicMethod;
import graduation.whatziroom.activity.main.RoomListFragment;
import graduation.whatziroom.network.HttpNetwork;
import graduation.whatziroom.network.Params;
import graduation.whatziroom.util.ParseData;


/**
 * Created by ATIV on 2017-06-25.
 */

public class RoomInfoFragment extends Fragment implements BasicMethod{

    View layout;
    private ImageView[] ivAttendee;
    private LinearLayout linAttendee;
    private String isEmpty;
    private String result = "notEmpty";

    private static android.widget.ImageView ivInformation;
    private static android.widget.TextView textTitle;
    private static android.widget.TextView tvInfoGoing;
    private static android.widget.TextView tvInfoNotGoing;
    private static android.widget.TextView tvInfoTime;
    private static android.widget.TextView tvInfoPlace;
    private static android.widget.TextView tvInfoMaker;
    private static android.widget.TextView tvInfoDesc;

//    private ProgressDialog mProgressDialog;

    public String getIsEmpty() {
        return isEmpty;
    }

    public void setIsEmpty(String isEmpty) {
        this.isEmpty = isEmpty;
    }

    public RoomInfoFragment() {
        super();


    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        switch (getIsEmpty()) {
            case "empty":
                layout = (LinearLayout) inflater.inflate(R.layout.no_schedule, container, false);
                break;

            case "notEmpty":

                layout = (ScrollView) inflater.inflate(R.layout.room_information, container, false);

                setValues();
                bindView();
                setUpEvents();

                break;
        }

        return layout;
    }

    //dp값을 입력하여 px로 변환하여 반환해줌
    public int convertDPtoPX(int size) {

        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, r.getDisplayMetrics());

        return px;

    }

    @Override
    public void setUpEvents() {

        updateRoomInfo();

    }

    public static void updateRoomInfo() {

        Params params = new Params();
        params.add("RoomPKey", String.valueOf(RoomListFragment.getRoomPKey()));

        new HttpNetwork("GetScheduleData.php", params.getParams(), new HttpNetwork.AsyncResponse() {
            @Override
            public void onSuccess(String response) {
                ParseData parse = new ParseData();

                try {

                    Log.d("asdf", parse.parseJsonArray(response).get(0) + "");
                    JSONObject roomInfo = new JSONObject(parse.parseJsonArray(response).get(0).toString());

//                    Glide.with(SearchPlaceActivity.searchActivity).clear(ivInformation);
                    Glide.with(RoomViewPager.roomInfoView.getContext()).load(roomInfo.getString("URL")).into(ivInformation);
                    textTitle.setText(roomInfo.getString("Name"));
                    tvInfoPlace.setText(roomInfo.getString("Place"));
                    tvInfoTime.setText(roomInfo.getString("time"));
                    tvInfoDesc.setText(roomInfo.getString("Description"));

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

        this.tvInfoDesc = (TextView) layout.findViewById(R.id.tvInfoDesc);
        this.tvInfoMaker = (TextView) layout.findViewById(R.id.tvInfoMaker);
        this.tvInfoPlace = (TextView) layout.findViewById(R.id.tvInfoPlace);
        this.tvInfoTime = (TextView) layout.findViewById(R.id.tvInfoTime);
        this.tvInfoNotGoing = (TextView) layout.findViewById(R.id.tvInfoNotGoing);
        this.tvInfoGoing = (TextView) layout.findViewById(R.id.tvInfoGoing);
        this.textTitle = (TextView) layout.findViewById(R.id.textTitle);
        this.ivInformation = (ImageView) layout.findViewById(R.id.ivInformation);

    }
}
