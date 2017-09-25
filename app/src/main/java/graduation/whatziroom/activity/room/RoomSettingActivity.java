package graduation.whatziroom.activity.room;

import android.os.Bundle;
import android.support.annotation.Nullable;

import graduation.whatziroom.R;
import graduation.whatziroom.activity.base.BaseActivity;


/**
 * Created by mapl0 on 2017-09-05.
 */

public class RoomSettingActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();
        setContentView(R.layout.room_setting);

    }

    @Override
    public void setUpEvents() {
        super.setUpEvents();
    }

    @Override
    public void setValues() {
        super.setValues();
    }

    @Override
    public void bindView() {
        super.bindView();
    }
}
