package graduation.whatziroom.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import graduation.whatziroom.Data.RoomUserData;
import graduation.whatziroom.R;
import graduation.whatziroom.activity.main.RoomListFragment;
import graduation.whatziroom.activity.main.ScheduleListFragment;
import graduation.whatziroom.activity.room.RoomUserList;
import graduation.whatziroom.network.HttpNetwork;
import graduation.whatziroom.network.Params;

/**
 * Created by mapl0 on 2017-09-30.
 */

public class RequestUserAdapter extends ArrayAdapter {

    private Context mContext = null;
    private ArrayList<RoomUserData> requestRoomUserList = null;
    private LayoutInflater inflater = null;

    private android.widget.TextView tvRoomRequestName;
    private android.widget.TextView tvRoomRequestYes;
    private android.widget.TextView tvRoomRequestNo;

    private RoomUserData data;

    public RequestUserAdapter(@NonNull Context context, ArrayList<RoomUserData> list) {
        super(context, R.layout.request_room_user_item, list);
        mContext = context;
        requestRoomUserList = list;
        inflater = inflater.from(mContext);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View parentLayout = convertView;

        if(parentLayout == null) {
            parentLayout = inflater.inflate(R.layout.request_room_user_item, null);
        }

        data = requestRoomUserList.get(position);

        this.tvRoomRequestNo = (TextView) parentLayout.findViewById(R.id.tvRoomRequestNo);
        this.tvRoomRequestYes = (TextView) parentLayout.findViewById(R.id.tvRoomRequestYes);
        this.tvRoomRequestName = (TextView) parentLayout.findViewById(R.id.tvRoomRequestName);

        tvRoomRequestName.setText(data.getName() + " 님이 입장을 신청하였습니다.");
//        Log.d("requestAdapterName", data.getName());

        setUpEvents();

        return parentLayout;
    }

    public void setUpEvents() {

        tvRoomRequestYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Params params = new Params();
                params.add("UserPKey", data.getUserPKey() + "");
                params.add("RoomPKey", data.getRoomPKey() + "");

                new HttpNetwork("AcceptUser.php", params.getParams(), new HttpNetwork.AsyncResponse() {
                    @Override
                    public void onSuccess(String response) {
                        switch(response) {

                            case "success" :

                                RoomUserList.updateRequestList();
                                RoomUserList.updateRoomUserList();
                                RoomListFragment.updateRoom();
                                ScheduleListFragment.updateSchedule();

                                Toast.makeText(mContext, "수락하였습니다.", Toast.LENGTH_SHORT).show();
                                break;

                            default:

                                Toast.makeText(mContext, "오류가 생겼습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                                break;

                        }
                    }

                    @Override
                    public void onFailure(String response) {

                    }

                    @Override
                    public void onPreExcute() {

                    }
                });

            }
        });

        tvRoomRequestNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Params params = new Params();
                params.add("UserPKey", data.getUserPKey() + "");
                params.add("RoomPKey", data.getRoomPKey() + "");

                new HttpNetwork("RefuseUser.php", params.getParams(), new HttpNetwork.AsyncResponse() {
                    @Override
                    public void onSuccess(String response) {
                        switch(response) {

                            case "success" :
                                RoomUserList.updateRequestList();
                                Toast.makeText(mContext, "거절하였습니다.", Toast.LENGTH_SHORT).show();
                                break;

                            default:
                                Toast.makeText(mContext, "오류가 생겼습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                                break;

                        }
                    }

                    @Override
                    public void onFailure(String response) {

                    }

                    @Override
                    public void onPreExcute() {

                    }
                });
            }
        });

    }
}
