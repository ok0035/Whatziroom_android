package graduation.whatziroom.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import graduation.whatziroom.R;
import graduation.whatziroom.activity.base.BasicMethod;
import graduation.whatziroom.network.HttpNetwork;
import graduation.whatziroom.network.Params;

import static graduation.whatziroom.activity.main.MainViewPager.scheduleListFragment;
import static graduation.whatziroom.activity.room.RoomViewPager.roomInfoFragment;

/**
 * Created by mapl0 on 2017-10-01.
 */

public class AttendScheduleDialog extends Dialog implements BasicMethod {

    private String UserPKey, SchedulePKey;
    private Context mContext = null;
    private android.widget.TextView tvRoomAttendYes;
    private android.widget.TextView tvRoomAttendNo;

    public AttendScheduleDialog(@NonNull Context context, String userPKey, String schedulePKey) {
        super(context);

        mContext = context;
        UserPKey = userPKey;
        SchedulePKey = schedulePKey;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.attend_schedule_dialog);

        bindView();
        setValues();
        setUpEvents();

    }

    @Override
    public void setUpEvents() {

        tvRoomAttendYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Params params = new Params();
                params.add("UserPKey", UserPKey);
                params.add("SchedulePKey", SchedulePKey);

                new HttpNetwork("CheckAttendSchedule.php", params.getParams(), new HttpNetwork.AsyncResponse() {
                    @Override
                    public void onSuccess(String response) {
                        switch (response) {

                            case "success":

                                roomInfoFragment.updateRoomInfo();
                                scheduleListFragment.updateSchedule();
                                Toast.makeText(mContext, "참석이 확정되었습니다.", Toast.LENGTH_SHORT).show();
                                break;

                            default:
                                Toast.makeText(mContext, "오류가 발생했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
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

        tvRoomAttendNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    @Override
    public void setValues() {

    }

    @Override
    public void bindView() {
        this.tvRoomAttendNo = (TextView) findViewById(R.id.tvRoomAttendNo);
        this.tvRoomAttendYes = (TextView) findViewById(R.id.tvRoomAttendYes);
    }
}
