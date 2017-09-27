package graduation.whatziroom.activity.room;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import graduation.whatziroom.R;
import graduation.whatziroom.activity.base.BaseActivity;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by mapl0 on 2017-09-25.
 */

public class CopyFieldVariable extends BaseActivity {

    private android.support.v4.view.ViewPager vp;
    private android.widget.LinearLayout llChatSchedule;
    private android.widget.FrameLayout flChatMap;
    private android.widget.TextView tvChatCloseMap;
    private android.widget.LinearLayout llChatMapView;
    private android.widget.LinearLayout llChatInfoParent;
    private me.relex.circleindicator.CircleIndicator indicator;
    private android.widget.FrameLayout flRoom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room);
        this.flRoom = (FrameLayout) findViewById(R.id.flRoom);
        this.indicator = (CircleIndicator) findViewById(R.id.indicator);
        this.llChatInfoParent = (LinearLayout) findViewById(R.id.scChatInfoParent);
        this.llChatMapView = (LinearLayout) findViewById(R.id.llChatMapView);
        this.tvChatCloseMap = (TextView) findViewById(R.id.tvChatCloseMap);
        this.flChatMap = (FrameLayout) findViewById(R.id.flChatMap);
        this.llChatSchedule = (LinearLayout) findViewById(R.id.llChatSchedule);
        this.vp = (ViewPager) findViewById(R.id.vp);


    }
}
