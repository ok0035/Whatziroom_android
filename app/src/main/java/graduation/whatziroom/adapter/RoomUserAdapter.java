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

public class RoomUserAdapter extends ArrayAdapter {

    private Context mContext = null;
    private ArrayList<RoomUserData> roomFriendList = null;
    private LayoutInflater inflater = null;

    public RoomUserAdapter(@NonNull Context context, ArrayList<RoomUserData> list) {
        super(context, R.layout.room_user_list_item, list);
        mContext = context;
        roomFriendList = list;
        inflater = inflater.from(mContext);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View parentLayout = convertView;

        if(parentLayout == null) {
            parentLayout = inflater.inflate(R.layout.room_user_list_item, null);
        }

        TextView tvRoomFriendName = parentLayout.findViewById(R.id.tvRoomFriendName);
        tvRoomFriendName.setText(roomFriendList.get(position).getName());

        setUpEvents();

        return parentLayout;
    }

    public void setUpEvents() {


    }

}
