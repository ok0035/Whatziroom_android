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

import graduation.whatziroom.Data.UserData;
import graduation.whatziroom.R;

/**
 * Created by mapl0 on 2017-10-09.
 */

public class AttendUserAdapter extends ArrayAdapter {

    LayoutInflater inf = null;
    Context mContext = null;
    ArrayList<UserData> attendUserList = null;

    public AttendUserAdapter(@NonNull Context context, ArrayList<UserData> list) {
        super(context, R.layout.room_info_going_dialog_item, list);

        mContext = context;
        attendUserList = list;
        inf = LayoutInflater.from(mContext);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row = convertView;

        if (row == null) {
            row = inf.inflate(R.layout.room_info_going_dialog_item, null);
        }

        UserData userData = attendUserList.get(position);

        TextView tvRoomGoingName = (TextView) row.findViewById(R.id.tvRoomGoingName);
        tvRoomGoingName.setText(userData.getName());

        return row;

    }

}
