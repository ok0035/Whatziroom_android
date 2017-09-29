package graduation.whatziroom.activity.room;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import graduation.whatziroom.R;
import graduation.whatziroom.activity.base.BaseActivity;

/**
 * Created by mapl0 on 2017-09-25.
 */

public class CopyFieldVariable extends BaseActivity {


    private android.widget.EditText findRoomEdt;
    private android.widget.ListView roomListView;
    private android.widget.ListView roomSearchListView;
    private android.widget.ImageView searchRoom;
    private EditText edFindRoom;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_list);

        this.edFindRoom = (EditText) findViewById(R.id.edFindRoom);
        this.searchRoom = (ImageView) findViewById(R.id.searchRoom);
        this.roomSearchListView = (ListView) findViewById(R.id.roomSearchListView);
        this.roomListView = (ListView) findViewById(R.id.roomListView);
        this.findRoomEdt = (EditText) findViewById(R.id.edFindRoom);


    }
}
