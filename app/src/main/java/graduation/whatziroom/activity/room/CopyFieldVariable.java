package graduation.whatziroom.activity.room;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import graduation.whatziroom.R;
import graduation.whatziroom.activity.base.BaseActivity;

/**
 * Created by mapl0 on 2017-09-25.
 */

public class CopyFieldVariable extends BaseActivity {


    private android.widget.ImageView roomListThumbImg;
    private android.widget.TextView roomNameTxt;
    private android.widget.TextView roomFounderLabel;
    private android.widget.TextView roomFounder;
    private android.widget.TextView roomTimeTxt;
    private android.widget.TextView roomOutTxt;
    private android.widget.TextView tvChatCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_list_item);
        this.tvChatCount = (TextView) findViewById(R.id.tvChatCount);
        this.roomOutTxt = (TextView) findViewById(R.id.roomOutTxt);
        this.roomTimeTxt = (TextView) findViewById(R.id.roomTimeTxt);
        this.roomFounder = (TextView) findViewById(R.id.roomFounder);
        this.roomFounderLabel = (TextView) findViewById(R.id.roomFounderLabel);
        this.roomNameTxt = (TextView) findViewById(R.id.roomNameTxt);
        this.roomListThumbImg = (ImageView) findViewById(R.id.roomListThumbImg);


    }
}
