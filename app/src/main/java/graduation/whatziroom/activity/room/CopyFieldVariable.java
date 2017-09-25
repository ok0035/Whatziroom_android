package graduation.whatziroom.activity.room;

import android.os.Bundle;
import android.support.annotation.Nullable;

import graduation.whatziroom.R;
import graduation.whatziroom.activity.base.BaseActivity;

/**
 * Created by mapl0 on 2017-09-25.
 */

public class CopyFieldVariable extends BaseActivity {
    private android.widget.ImageView ivInformation;
    private android.widget.TextView textTitle;
    private android.widget.TextView tvInfoGoing;
    private android.widget.TextView tvInfoNotGoing;
    private android.widget.TextView tvInfoTime;
    private android.widget.TextView tvInfoPlace;
    private android.widget.TextView tvInfoMaker;
    private android.widget.TextView tvInfoDesc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_information);

    }
}
