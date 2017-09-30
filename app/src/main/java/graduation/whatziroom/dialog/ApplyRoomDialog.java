package graduation.whatziroom.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import graduation.whatziroom.R;
import graduation.whatziroom.activity.base.BasicMethod;

/**
 * Created by mapl0 on 2017-09-30.
 */

public class ApplyRoomDialog extends Dialog implements BasicMethod {

    private android.widget.TextView tvApplyYes;
    private android.widget.TextView tvApplyNo;

    public ApplyRoomDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.apply_room_dialog);

        bindView();
        setValues();
        setUpEvents();

    }

    @Override
    public void setUpEvents() {

        tvApplyYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
