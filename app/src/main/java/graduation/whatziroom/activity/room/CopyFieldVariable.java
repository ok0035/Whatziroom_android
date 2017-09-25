package graduation.whatziroom.activity.room;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    private android.widget.LinearLayout tvNeedCrateSchedule;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_information);
        this.tvNeedCrateSchedule = (LinearLayout) findViewById(R.id.tvNeedCreateSchedule);
        this.tvInfoDesc = (TextView) findViewById(R.id.tvInfoDesc);
        this.tvInfoMaker = (TextView) findViewById(R.id.tvInfoMaker);
        this.tvInfoPlace = (TextView) findViewById(R.id.tvInfoPlace);
        this.tvInfoTime = (TextView) findViewById(R.id.tvInfoTime);
        this.tvInfoNotGoing = (TextView) findViewById(R.id.tvInfoNotGoing);
        this.tvInfoGoing = (TextView) findViewById(R.id.tvInfoGoing);
        this.textTitle = (TextView) findViewById(R.id.textTitle);
        this.ivInformation = (ImageView) findViewById(R.id.ivInformation);

    }
}
