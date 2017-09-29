package graduation.whatziroom.activity.room;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import graduation.whatziroom.R;
import graduation.whatziroom.activity.base.BaseActivity;

/**
 * Created by mapl0 on 2017-09-25.
 */

public class CopyFieldVariable extends BaseActivity {


    private android.widget.EditText findFreindEdt;
    private android.widget.ImageView searchFreindBtn;
    private android.widget.TextView tvFriendSearchBack;
    private android.widget.ListView friendList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_list);
        this.friendList = (ListView) findViewById(R.id.friendList);
        this.tvFriendSearchBack = (TextView) findViewById(R.id.tvFriendSearchBack);
        this.searchFreindBtn = (ImageView) findViewById(R.id.searchFreindBtn);
        this.findFreindEdt = (EditText) findViewById(R.id.findFreindEdt);


    }
}
