package graduation.whatziroom.dialog;

import android.app.Dialog;
import android.content.Context;
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

/**
 * Created by mapl0 on 2017-09-30.
 */

public class ApplyRoomDialog extends Dialog implements BasicMethod {

    private android.widget.TextView tvApplyYes;
    private android.widget.TextView tvApplyNo;
    private int UserPKey, RoomPKey;

    public ApplyRoomDialog(@NonNull Context context, int userPKey, int roomPKey) {
        super(context);

        UserPKey = userPKey;
        RoomPKey = roomPKey;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.request_room_dialog);

        bindView();
        setValues();
        setUpEvents();

    }

    @Override
    public void setUpEvents() {

        tvApplyYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Params params = new Params();
                params.add("UserPKey", UserPKey + "");
                params.add("RoomPKey", RoomPKey + "");

                new HttpNetwork("RequestRoom.php", params.getParams(), new HttpNetwork.AsyncResponse() {
                    @Override
                    public void onSuccess(String response) {
                        switch(response) {

                            case "fail":
                                Toast.makeText(getContext(), "모임 신청에 실패하였습니다. \n잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                                break;
                            case "success":
                                Toast.makeText(getContext(), "모임 신청이 완료되었습니다.", Toast.LENGTH_SHORT).show();
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

        tvApplyNo.setOnClickListener(new View.OnClickListener() {
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
        this.tvApplyNo = (TextView) findViewById(R.id.tvDialogApplyNo);
        this.tvApplyYes = (TextView) findViewById(R.id.tvDialogApplyYes);
    }
}
