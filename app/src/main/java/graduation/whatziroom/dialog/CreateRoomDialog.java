package graduation.whatziroom.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import graduation.whatziroom.R;
import graduation.whatziroom.activity.base.BaseActivity;
import graduation.whatziroom.activity.base.BasicMethod;
import graduation.whatziroom.activity.main.RoomListFragment;
import graduation.whatziroom.activity.room.RoomViewPager;
import graduation.whatziroom.network.DBSI;
import graduation.whatziroom.network.HttpNetwork;
import graduation.whatziroom.network.Params;


/**
 * Created by mapl0 on 2017-09-10.
 */

public class CreateRoomDialog extends Dialog implements BasicMethod {

    private TextView newRoomCancelBtn;
    private TextView newRoomConfirmBtn;
    private EditText edCreateRoomNmae;
    private EditText edCreateRoomMaxUser;
    private EditText edCreateRoomDesc;

    public CreateRoomDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.create_room_dialog);

        bindView();
        setValues();
        setUpEvents();
    }

    @Override
    public void setUpEvents() {
        newRoomConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Params params = new Params();
                DBSI db = new DBSI();
                params.add("PKey", db.selectQuery("select PKey from User")[0][0]);
                params.add("Name", edCreateRoomNmae.getText().toString());
                params.add("Description", edCreateRoomDesc.getText().toString());

                new HttpNetwork("CreateRoom.php", params.getParams(), new HttpNetwork.AsyncResponse() {
                    @Override
                    public void onSuccess(String response) {

                        Toast.makeText(BaseActivity.mContext, "방 개설 완료", Toast.LENGTH_SHORT).show();
                        RoomViewPager.setRoomPKey(response);
                        RoomListFragment.updateRoom();
                        Intent intent = new Intent(BaseActivity.mContext, RoomViewPager.class);
                        BaseActivity.mContext.startActivity(intent);
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

        newRoomCancelBtn.setOnClickListener(new View.OnClickListener() {
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

        this.newRoomConfirmBtn = findViewById(R.id.newRoomConfirmBtn);
        this.newRoomCancelBtn = findViewById(R.id.newRoomCancelBtn);
        this.edCreateRoomDesc = findViewById(R.id.edCreateRoomDesc);
        this.edCreateRoomNmae = findViewById(R.id.edCreateRoomNmae);

    }
}
