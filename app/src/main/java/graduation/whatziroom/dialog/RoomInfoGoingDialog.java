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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import graduation.whatziroom.Data.UserData;
import graduation.whatziroom.R;
import graduation.whatziroom.activity.base.BasicMethod;
import graduation.whatziroom.activity.main.MainViewPager;
import graduation.whatziroom.activity.room.RoomViewPager;
import graduation.whatziroom.adapter.AttendUserAdapter;
import graduation.whatziroom.network.HttpNetwork;
import graduation.whatziroom.network.Params;
import graduation.whatziroom.util.ParseData;

import static graduation.whatziroom.activity.room.RoomViewPager.roomInfoFragment;

/**
 * Created by mapl0 on 2017-09-30.
 */

public class RoomInfoGoingDialog extends Dialog implements BasicMethod {

    private ListView lvRoomGoingUserList;
    private String SchedulePKey;
    private TextView tvUnCheckAttend;

    public RoomInfoGoingDialog(@NonNull Context context, String SchedulePKey) {
        super(context);
        this.SchedulePKey = SchedulePKey;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.room_info_going_dialog);

        bindView();
        setValues();
        setUpEvents();

    }

    @Override
    public void setUpEvents() {

        Params params = new Params();
        params.add("SchedulePKey", SchedulePKey);

        new HttpNetwork("GetAttendUserList.php", params.getParams(), new HttpNetwork.AsyncResponse() {
            @Override
            public void onSuccess(String response) {

                if (!response.equals("[]")) {

                    try {
                        ParseData parse = new ParseData();
                        JSONArray jsonAttendUserList = parse.parseJsonArray(response);
                        ArrayList<UserData> attendUserList = new ArrayList<UserData>();

                        for (int i = 0; i < jsonAttendUserList.length(); i++) {
                            JSONObject jsonUserData = new JSONObject(jsonAttendUserList.get(i).toString());
                            attendUserList.add(new UserData(jsonUserData.getString("PKey"), jsonUserData.getString("Name")));
                            Log.d("AttendName", attendUserList.get(i).getName());
                        }

                        AttendUserAdapter adapter = new AttendUserAdapter(RoomViewPager.mContext, attendUserList);
                        lvRoomGoingUserList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }


            @Override
            public void onFailure(String response) {

            }

            @Override
            public void onPreExcute() {

            }
        });

        tvUnCheckAttend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Params notAttendParams = new Params();
                notAttendParams.add("UserPKey", MainViewPager.getUserPKey() + "");
                notAttendParams.add("SchedulePKey", SchedulePKey);

                new HttpNetwork("UnCheckAttendSchedule.php", notAttendParams.getParams(), new HttpNetwork.AsyncResponse() {
                    @Override
                    public void onSuccess(String response) {
                        switch (response) {
                            case "success":
                                Toast.makeText(RoomViewPager.mContext, "불참으로 변경되었습니다.", Toast.LENGTH_SHORT).show();
                                roomInfoFragment.updateRoomInfo();
                                break;
                        }

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
        this.lvRoomGoingUserList = (ListView) findViewById(R.id.lvRoomGoingUserList);
        this.tvUnCheckAttend = (TextView) findViewById(R.id.tvUnCheckAttend);
    }
}