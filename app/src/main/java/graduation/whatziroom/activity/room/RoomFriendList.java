package graduation.whatziroom.activity.room;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import graduation.whatziroom.R;
import graduation.whatziroom.activity.base.BaseActivity;
import graduation.whatziroom.activity.base.BasicMethod;
import graduation.whatziroom.dialog.ExitRoomDialog;
import graduation.whatziroom.network.HttpNetwork;
import graduation.whatziroom.network.Params;


/**
 * Created by ATIV on 2017-06-25.
 */

public class RoomFriendList extends Fragment implements BasicMethod{

    private ScrollView parent;
    private android.widget.ListView lvApplyUserList;
    private android.widget.ListView lvRoomUserList;
    private android.widget.TextView tvRoomExit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        parent = (ScrollView) inflater.inflate(R.layout.room_friend_list, container, false);

        bindView();
        setValues();
        setUpEvents();

        return parent;
    }

    @Override
    public void setUpEvents() {

//        updateApplyList();
//        updateRoomUserList();

        tvRoomExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new ExitRoomDialog(BaseActivity.mContext).show();

            }
        });

    }

    public void updateApplyList() {

        Params params = new Params();

        new HttpNetwork("GetRoomApplyUser.php", params.getParams(), new HttpNetwork.AsyncResponse() {
            @Override
            public void onSuccess(String response) {



            }

            @Override
            public void onFailure(String response) {

            }

            @Override
            public void onPreExcute() {

            }
        });

    }

    public void updateRoomUserList() {

        Params params = new Params();

        new HttpNetwork("GetRoomFriend.php", params.getParams(), new HttpNetwork.AsyncResponse() {
            @Override
            public void onSuccess(String response) {



            }

            @Override
            public void onFailure(String response) {

            }

            @Override
            public void onPreExcute() {

            }
        });

    }

    @Override
    public void setValues() {

    }

    @Override
    public void bindView() {

        this.lvRoomUserList = (ListView) parent.findViewById(R.id.lvRoomUserList);
        this.lvApplyUserList = (ListView) parent.findViewById(R.id.lvApplyUserList);
        this.tvRoomExit = (TextView) parent.findViewById(R.id.tvRoomExit);

    }
}
