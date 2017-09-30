package graduation.whatziroom.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import graduation.whatziroom.R;
import graduation.whatziroom.activity.base.BasicMethod;
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
        setContentView(R.layout.exit_room_dialog);
    }

    @Override
    public void setUpEvents() {

        tvRoomExitYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Params params = new Params();

                new HttpNetwork("ExitRoom.php", params.getParams(), new HttpNetwork.AsyncResponse() {
                    @Override
                    public void onSuccess(String response) {

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
