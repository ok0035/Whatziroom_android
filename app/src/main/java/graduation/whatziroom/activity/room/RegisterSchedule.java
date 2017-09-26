package graduation.whatziroom.activity.room;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import graduation.whatziroom.R;
import graduation.whatziroom.activity.base.BaseActivity;


/**
 * Created by mapl0 on 2017-09-05.
 */

public class RegisterSchedule extends BaseActivity {

    private TextView tvNoSelectDate;
    private TextView tvBtnSelectPlace;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();
        setContentView(R.layout.register_schedule);

        bindView();
        setValues();
        setUpEvents();

    }

    @Override
    public void setUpEvents() {
        super.setUpEvents();

        tvBtnSelectPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), SearchPlaceActivity.class);
                startActivity(intent);

            }
        });

    }

    @Override
    public void setValues() {
        super.setValues();
    }

    @Override
    public void bindView() {
        super.bindView();

        this.tvBtnSelectPlace = (TextView) findViewById(R.id.tvBtnSelectPlace);
        this.tvNoSelectDate = (TextView) findViewById(R.id.tvNoSelectDate);

    }
}
