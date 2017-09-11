package com.graduation.project.whatziroom.activity.main;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.graduation.project.whatziroom.R;
import com.graduation.project.whatziroom.activity.base.BaseActivity;
import com.graduation.project.whatziroom.activity.base.BasicMethod;
import com.graduation.project.whatziroom.network.HttpNetwork;
import com.graduation.project.whatziroom.network.Params;

/**
 * Created by mapl0 on 2017-09-10.
 */

public class CreateRoomDialog extends Dialog implements BasicMethod {

    private android.widget.TextView newRoomCancelBtn;
    private android.widget.TextView newRoomConfirmBtn;
    private android.widget.EditText edCreateRoomNmae;
    private android.widget.EditText edCreateRoomMaxUser;
    private android.widget.EditText edCreateRoomDesc;

    public CreateRoomDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
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

                new HttpNetwork("CreateRoom.php", params.getParams(), new HttpNetwork.AsyncResponse() {
                    @Override
                    public void onSuccess(String response) {

                        Toast.makeText(BaseActivity.mContext, "방 개설 완료", Toast.LENGTH_SHORT).show();
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

        this.newRoomConfirmBtn = (TextView) findViewById(R.id.newRoomConfirmBtn);
        this.newRoomCancelBtn = (TextView) findViewById(R.id.newRoomCancelBtn);
        this.edCreateRoomDesc = (EditText) findViewById(R.id.edCreateRoomDesc);
        this.edCreateRoomMaxUser = (EditText) findViewById(R.id.edCreateRoomMaxUser);
        this.edCreateRoomNmae = (EditText) findViewById(R.id.edCreateRoomNmae);

    }
}
