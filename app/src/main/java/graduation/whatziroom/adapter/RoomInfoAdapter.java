package graduation.whatziroom.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import graduation.whatziroom.Data.RoomInfoData;
import graduation.whatziroom.R;
import graduation.whatziroom.activity.main.MainViewPager;
import graduation.whatziroom.activity.room.RoomViewPager;
import graduation.whatziroom.dialog.AttendScheduleDialog;

/**
 * Created by mapl0 on 2017-09-28.
 */

public class RoomInfoAdapter extends ArrayAdapter {

    private Context mContext = null;
    private ArrayList<RoomInfoData> roomInfoList = null;
    private LayoutInflater inflater = null;

    private ImageView ivInformation;
    private TextView textTitle;
    private TextView tvInfoGoing;
    private TextView tvInfoMaker;
    private TextView tvInfoTime;
    private TextView tvInfoPlace;
    private TextView tvInfoNewAdd;
    private TextView tvInfoOldAdd;
    private TextView tvInfoTel;
    private TextView tvInfoSite;
    private TextView tvInfoDesc;

    private RoomInfoData data;

    public RoomInfoAdapter(@NonNull Context context, ArrayList<RoomInfoData> list) {
        super(context, R.layout.room_info_item, list);

        mContext = context;
        roomInfoList = list;
        inflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View roomInfoLayout = convertView;

        if (roomInfoLayout == null)
            roomInfoLayout = inflater.inflate(R.layout.room_info_item, null);

        this.tvInfoDesc = (TextView) roomInfoLayout.findViewById(R.id.tvInfoDesc);
        this.tvInfoSite = (TextView) roomInfoLayout.findViewById(R.id.tvInfoSite);
        this.tvInfoTel = (TextView) roomInfoLayout.findViewById(R.id.tvInfoTel);
        this.tvInfoOldAdd = (TextView) roomInfoLayout.findViewById(R.id.tvInfoOldAdd);
        this.tvInfoNewAdd = (TextView) roomInfoLayout.findViewById(R.id.tvInfoNewAdd);
        this.tvInfoPlace = (TextView) roomInfoLayout.findViewById(R.id.tvInfoPlace);
        this.tvInfoTime = (TextView) roomInfoLayout.findViewById(R.id.tvInfoTime);
        this.tvInfoMaker = (TextView) roomInfoLayout.findViewById(R.id.tvInfoMaker);
        this.tvInfoGoing = (TextView) roomInfoLayout.findViewById(R.id.tvInfoGoing);
        this.textTitle = (TextView) roomInfoLayout.findViewById(R.id.textTitle);
        this.ivInformation = (ImageView) roomInfoLayout.findViewById(R.id.ivInformation);

        data = roomInfoList.get(position);

        if (data.getStatus().equals("1"))
            tvInfoGoing.setText("참석 확정");

        Glide.with(RoomViewPager.mContext).load(data.getImageURL()).into(ivInformation);
        textTitle.setText(data.getTitle().equals("null") ? "" : data.getTitle());
        tvInfoPlace.setText(data.getPlace().equals("null") ? "" : data.getPlace());
        tvInfoTime.setText(data.getTime().equals("null") ? "" : data.getTime());
        tvInfoDesc.setText(data.getDescription().equals("null") ? "" : data.getDescription());
        tvInfoMaker.setText(data.getName().equals("null") ? "" : data.getName());
        tvInfoOldAdd.setText(data.getOldAddress().equals("null") ? "" : data.getOldAddress());
        tvInfoNewAdd.setText(data.getNewAddress().equals("null") ? "" : data.getNewAddress());
        tvInfoTel.setText(data.getTEL().equals("null") ? "" : data.getTEL());
        tvInfoSite.setText(data.getWebURL().equals("null") ? "" : data.getWebURL());

        setUpEvents();

        return roomInfoLayout;
    }

    public void setUpEvents() {

        tvInfoGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvInfoGoing.getText().toString().equals("참석 확정"))
                    Toast.makeText(mContext, "이미 참석을 확정하였습니다.", Toast.LENGTH_SHORT).show();
                else
                    new AttendScheduleDialog(getContext(), MainViewPager.getUserPKey() + "", data.getSchedulePKey()).show();

            }
        });

    }

}
