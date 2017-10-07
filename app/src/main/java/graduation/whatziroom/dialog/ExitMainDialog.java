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

import graduation.whatziroom.Data.RoomData;
import graduation.whatziroom.R;
import graduation.whatziroom.activity.base.BasicMethod;
import graduation.whatziroom.activity.main.MainViewPager;
import graduation.whatziroom.activity.main.ScheduleListFragment;
import graduation.whatziroom.activity.room.RoomViewPager;
import graduation.whatziroom.network.HttpNetwork;
import graduation.whatziroom.network.Params;

/**
 * Created by mapl0 on 2017-09-30.
 */

public class ExitMainDialog extends Dialog implements BasicMethod {

    private android.widget.TextView tvMainExitYes;
    private android.widget.TextView tvMainExitNo;

    public ExitMainDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.exit_main_dialog);

        bindView();
        setValues();
        setUpEvents();

    }

    @Override
    public void setUpEvents() {

        tvMainExitYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });

        tvMainExitNo.setOnClickListener(new View.OnClickListener() {
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
        this.tvMainExitNo = (TextView) findViewById(R.id.tvMainExitNo);
        this.tvMainExitYes = (TextView) findViewById(R.id.tvMainExitYes);
    }

}
