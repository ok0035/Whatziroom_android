package graduation.whatziroom.activity.room;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.TextView;

import graduation.whatziroom.R;
import graduation.whatziroom.activity.base.BaseActivity;

/**
 * Created by mapl0 on 2017-09-25.
 */

public class CopyFieldVariable extends BaseActivity {






    private android.widget.TextView tvProfileIntro;
    private android.widget.TextView tvProfileID;
    private android.widget.TextView tvProfileName;
    private android.widget.TextView tvProfileEmail;
    private android.widget.EditText edProfileIntro;
    private android.widget.TextView updateUserInfoBtn;
    private android.widget.TextView signoutBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_list);
        this.signoutBtn = (TextView) findViewById(R.id.signoutBtn);
        this.updateUserInfoBtn = (TextView) findViewById(R.id.updateUserInfoBtn);
        this.edProfileIntro = (EditText) findViewById(R.id.edProfileIntro);
        this.tvProfileEmail = (TextView) findViewById(R.id.tvProfileEmail);
        this.tvProfileName = (TextView) findViewById(R.id.tvProfileName);
        this.tvProfileID = (TextView) findViewById(R.id.tvProfileID);
        this.tvProfileIntro = (TextView) findViewById(R.id.tvProfileIntro);




    }
}
