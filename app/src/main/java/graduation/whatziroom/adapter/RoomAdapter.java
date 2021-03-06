package graduation.whatziroom.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import graduation.whatziroom.Data.RoomData;
import graduation.whatziroom.R;

import static graduation.whatziroom.activity.room.RoomViewPager.roomChatFragment;

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
        TextView roomTime = row.findViewById(R.id.roomTimeTxt);
        TextView tvChatCount = row.findViewById(R.id.tvChatCount);
        TextView getOutBtn = row.findViewById(R.id.roomOutTxt);
        TextView roomFounder = row.findViewById(R.id.roomFounder);

        RoomData roomData = mList.get(position);
        int chatCount = 0;

        if(roomData.getChatCount() == null) tvChatCount.setVisibility(View.GONE);
        else {

            for(int i =0; i< roomChatFragment.chatList.size(); i++) {
                if(roomData.getRoomPKey().equals(roomChatFragment.chatList.get(i).getRoomPKey())) {
                    chatCount = roomChatFragment.chatList.get(i).getChatCount() - Integer.parseInt(roomData.getChatCount());

                    Log.d("chatList.getChatCount()", "...." + roomChatFragment.chatList.get(i).getChatCount() + "");
                    Log.d("roomData.getChatCount", "...." + roomData.getChatCount() + "");
                    Log.d("chatCount", "...." + chatCount + "");

                    if(chatCount < 0) {
                        Log.d("chatList.getChatCount()", roomChatFragment.chatList.get(i).getChatCount() + "");
                        Log.d("roomData.getChatCount()", roomData.getChatCount());
                    }
                    break;
                }
            }

            if(chatCount <= 0) {
                tvChatCount.setText("");
                tvChatCount.setVisibility(View.GONE);
            } else {
                tvChatCount.setText(chatCount + "");
                tvChatCount.setVisibility(View.VISIBLE);
            }
        }


        roomName.setText(roomData.getRoomName());
        roomTime.setText(roomData.getRoomDate());
        roomFounder.setText(roomData.getRoomFounderName());

        return row;
    }

}
