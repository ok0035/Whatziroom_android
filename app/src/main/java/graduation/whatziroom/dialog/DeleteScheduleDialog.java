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
import static graduation.whatziroom.activity.room.RoomViewPager.mContext;
import static graduation.whatziroom.activity.room.RoomViewPager.roomChatFragment;
import static graduation.whatziroom.activity.room.RoomViewPager.roomInfoFragment;

/**
 * Created by mapl0 on 2017-10-12.
 */

public class DeleteScheduleDialog extends Dialog implements BasicMethod {

    private android.widget.TextView tvDeleteScheduleNo;
    private android.widget.TextView tvDeleteScheduleYes;
    private String SchedulePKey;

    public DeleteScheduleDialog(@NonNull Context context, String schedulePKey) {
        super(context);
        SchedulePKey = schedulePKey;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.delete_schedule_dialog);

        bindView();
        setValues();
        setUpEvents();

    }


    @Override
    public void setUpEvents() {

        tvDeleteScheduleNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        tvDeleteScheduleYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Params params = new Params();
                params.add("SchedulePKey", SchedulePKey);

                new HttpNetwork("DeleteSchedule.php", params.getParams(), new HttpNetwork.AsyncResponse() {
                    @Override
                    public void onSuccess(String response) {
                        switch (response) {
                            case "success":
                                roomInfoFragment.updateRoomInfo();
                                roomInfoFragment.updateNotEmptyRoom();
                                roomChatFragment.updateChatMapInfo();
                                scheduleListFragment.updateSchedule();

                                Toast.makeText(mContext, "스케줄이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                dismiss();
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
        });

    }

    @Override
    public void setValues() {

    }

    @Override
    public void bindView() {
        this.tvDeleteScheduleYes = (TextView) findViewById(R.id.tvDeleteScheduleYes);
        this.tvDeleteScheduleNo = (TextView) findViewById(R.id.tvDeleteScheduleNo);
    }
}
