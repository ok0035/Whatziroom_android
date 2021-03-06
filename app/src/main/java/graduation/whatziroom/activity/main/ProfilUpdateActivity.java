package graduation.whatziroom.activity.main;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import graduation.whatziroom.R;
import graduation.whatziroom.activity.base.BaseActivity;
import graduation.whatziroom.activity.login.LoginActivity;


public class ProfilUpdateActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_update);
        setMainActionBar();

        bindView();
        setValues();
        setUpEvents();
    }



    @Override
    public void setUpEvents() {
        super.setUpEvents();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
//                overridePendingTransition(0, 0);
            }
        });
    }

    @Override
    public void setValues() {
        super.setValues();
    }


    @Override
    public void setMainActionBar() {
        super.setMainActionBar();
        titleTxt.setText("비밀번호 변경");
        backBtn.setVisibility(View.VISIBLE);
        configTxt1.setVisibility(View.INVISIBLE);
        configTxt2.setVisibility(View.VISIBLE);
        configTxt2.setImageResource(R.mipmap.btn_ok);
        configTxt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void bindView() {
        super.bindView();
    }


}
