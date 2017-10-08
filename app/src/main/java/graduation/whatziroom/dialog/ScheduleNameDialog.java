package graduation.whatziroom.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import graduation.whatziroom.Data.MapData;
import graduation.whatziroom.R;
import graduation.whatziroom.activity.base.BasicMethod;
import graduation.whatziroom.activity.main.ScheduleListFragment;
import graduation.whatziroom.activity.room.RoomViewPager;
import graduation.whatziroom.activity.room.SearchPlaceActivity;
import graduation.whatziroom.network.HttpNetwork;
import graduation.whatziroom.network.Params;

import static graduation.whatziroom.activity.room.RoomViewPager.roomInfoFragment;

/**
 * Created by mapl0 on 2017-09-24.
 */

public class ScheduleNameDialog extends Dialog implements BasicMethod {

    private android.widget.EditText edScheduleName;
    private android.widget.TextView tvBtnResister;

    private int userPKey, roomPKey;
    private MapData data;
    private String myScheduleDate;
    private TextView textView;
    private EditText edScheduleDesc;

    public ScheduleNameDialog(@NonNull Context context, int userPKey, int roomPKey, String date, MapData item) {
        super(context);

        this.userPKey = userPKey;
        this.roomPKey = roomPKey;
        myScheduleDate = date;
        data = item;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.create_schedule_dialog);

        bindView();
        setValues();
        setUpEvents();

    }

    @Override
    public void setUpEvents() {

        tvBtnResister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault());

                Log.d("ScheduleDate", myDateFormat.format(Date.parse(myScheduleDate)));

                Params params = new Params();
                params.add("UserPKey", userPKey + "");
                params.add("RoomPKey", roomPKey + "");
                params.add("Date", myDateFormat.format(Date.parse(myScheduleDate)));
                params.add("Name", edScheduleName.getText().toString());
                params.add("Place", data.getTitle());
                params.add("Description", edScheduleDesc.getText().toString());
                params.add("Longitude", data.getLongitude() + "");
                params.add("Latitude", data.getLatitude() + "");
                params.add("ImageURL", data.getImageUrl() + "");
                params.add("NewAddress", data.getNewAddress());
                params.add("OldAddress", data.getAddress());
                params.add("TEL", data.getPhone());
                params.add("WURL", data.getPlaceUrl());

                new HttpNetwork("RegisterSchedule.php", params.getParams(), new HttpNetwork.AsyncResponse() {
                    @Override
                    public void onSuccess(String response) {
                        Log.d("RegistSchedule", response);
                        ScheduleListFragment.updateSchedule();
                        roomInfoFragment.updateRoomInfo();
                        roomInfoFragment.tvNeedCreateSchedule.setVisibility(View.GONE);
                        SearchPlaceActivity.searchActivity.finish();
                        Toast.makeText(RoomViewPager.mContext, "스케줄이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                        dismiss();

                    }

                    @Override
                    public void onFailure(String response) {

                    }

                    @Override
                    public void onPreExcute() {

                    }
                });

            }
        });

    }

    @Override
    public void setValues() {

    }

    @Override
    public void bindView() {
        this.tvBtnResister = findViewById(R.id.tvBtnResister);
        this.edScheduleDesc = findViewById(R.id.edScheduleDesc);
        this.textView = findViewById(R.id.textView);
        this.edScheduleName = findViewById(R.id.edScheduleName);
    }
}
