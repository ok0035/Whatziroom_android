package graduation.whatziroom.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import graduation.whatziroom.Data.RoomUserData;
import graduation.whatziroom.R;

/**
 * Created by mapl0 on 2017-09-30.
 */

public class RoomApplyAdapter extends ArrayAdapter {

    private Context mContext = null;
    private ArrayList<RoomUserData> roomApplyList = null;
    private LayoutInflater inflater = null;

    public RoomApplyAdapter(@NonNull Context context, ArrayList<RoomUserData> list) {
        super(context, R.layout.room_apply_user_item, list);
        mContext = context;
        roomApplyList = list;
        inflater = inflater.from(mContext);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View parentLayout = convertView;

        if(parentLayout == null) {
            parentLayout = inflater.inflate(R.layout.room_apply_user_item, null);
        }

        TextView tvRoomApplyName = parentLayout.findViewById(R.id.tvRoomApplyName);
        tvRoomApplyName.setText(roomApplyList.get(position).getName() + " 님이 입장을 신청하였습니다.");

        setUpEvents();

        return parentLayout;
    }

    public void setUpEvents() {


    }
}
