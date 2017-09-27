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

import graduation.whatziroom.Data.RoomData;
import graduation.whatziroom.R;

/**
 * Created by Heronation on 2017-07-10.
 */

public class RoomAdapter extends ArrayAdapter {


    private Context mContext = null;
    private ArrayList<RoomData> mList = null;
    private LayoutInflater inf = null;

    public RoomAdapter(Context context, ArrayList<RoomData> list){
        super(context, R.layout.room_list_item, list);
        mContext = context;
        mList = list;
        inf = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row = convertView;

        if(row == null){
            row =inf.inflate(R.layout.room_list_item,null);
        }

//        ImageView roomThumbNail = (ImageView)row.findViewById(R.id.roomListThumbImg);
        TextView roomName = row.findViewById(R.id.roomNameTxt);
        TextView roomMakerName = row.findViewById(R.id.tvUserNumber);
        TextView roomTime = row.findViewById(R.id.roomTimeTxt);
        TextView getOutBtn = row.findViewById(R.id.roomOutTxt);

        RoomData data = mList.get(position);

        roomName.setText(data.getRoomName());
        roomMakerName.setText(data.getRoomUserNumber());
        roomTime.setText(data.getRoomDate());

        return row;
    }



}
