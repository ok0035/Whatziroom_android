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
import android.widget.TextView;
import android.widget.Toast;

import graduation.whatziroom.R;
import graduation.whatziroom.activity.base.BasicMethod;
import graduation.whatziroom.activity.main.MainViewPager;
import graduation.whatziroom.activity.main.RoomListFragment;
import graduation.whatziroom.activity.main.ScheduleListFragment;
import graduation.whatziroom.activity.room.RoomInfoFragment;
import graduation.whatziroom.activity.room.RoomViewPager;
import graduation.whatziroom.network.HttpNetwork;
import graduation.whatziroom.network.Params;

/**
 * Created by mapl0 on 2017-09-30.
 */

public class ExitRoomDialog extends Dialog implements BasicMethod {

    private android.widget.TextView tvRoomExitYes;
    private android.widget.TextView tvRoomExitNo;

    public ExitRoomDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.exit_room_dialog);

        bindView();
        setValues();
        setUpEvents();

    }

    @Override
    public void setUpEvents() {

        tvRoomExitYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Params params = new Params();
                params.add("UserPKey", MainViewPager.getUserPKey() + "");
                params.add("RoomPKey", RoomInfoFragment.getRoomPKey() + "");

                new HttpNetwork("ExitRoom.php", params.getParams(), new HttpNetwork.AsyncResponse() {
                    @Override
                    public void onSuccess(String response) {
                        Log.d("ExitResponse", response);

                        switch(response) {
                            case "success":
                                RoomViewPager.mActivity.finish();
                                RoomListFragment.updateRoom();
                                ScheduleListFragment.updateSchedule();
                                Toast.makeText(getContext(), "방이 목록에서 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                break;

                            default:
                                Toast.makeText(getContext(), "오류가 있습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
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

                dismiss();

            }
        });

        tvRoomExitNo.setOnClickListener(new View.OnClickListener() {
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
        this.tvRoomExitNo = (TextView) findViewById(R.id.tvRoomExitNo);
        this.tvRoomExitYes = (TextView) findViewById(R.id.tvRoomExitYes);
    }
}
