package graduation.whatziroom.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import graduation.whatziroom.Data.FirebaseNoticeData;
import graduation.whatziroom.Data.RoomData;
import graduation.whatziroom.Data.RoomUserData;
import graduation.whatziroom.R;
import graduation.whatziroom.activity.main.MainViewPager;
import graduation.whatziroom.activity.room.RoomUserList;
import graduation.whatziroom.activity.room.RoomViewPager;
import graduation.whatziroom.network.HttpNetwork;
import graduation.whatziroom.network.Params;

import static graduation.whatziroom.activity.main.MainViewPager.roomListFragment;
import static graduation.whatziroom.activity.main.MainViewPager.scheduleListFragment;

/**
 * Created by mapl0 on 2017-09-30.
 */

public class RequestUserAdapter extends ArrayAdapter {

    private Context mContext = null;
    private ArrayList<RoomUserData> requestRoomUserList = null;
    private LayoutInflater inflater = null;

    private android.widget.LinearLayout llRequestRoomUser;
    private android.widget.LinearLayout llRoomRequestResult;
    private android.widget.ImageView ivRoomRequestCheck;
    private android.widget.TextView tvRoomRequestName;
    private android.widget.TextView tvRoomRequestYes;
    private android.widget.TextView tvRoomRequestNo;

    private View parentLayout;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private RoomUserData requestData;

    public RequestUserAdapter(@NonNull Context context, ArrayList<RoomUserData> list) {
        super(context, R.layout.request_room_user_item, list);
        mContext = context;
        requestRoomUserList = list;
        inflater = inflater.from(mContext);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        parentLayout = convertView;

        if(parentLayout == null) {
            parentLayout = inflater.inflate(R.layout.request_room_user_item, null);
        }

        requestData = requestRoomUserList.get(position);

        this.llRequestRoomUser = (LinearLayout) parentLayout.findViewById(R.id.llRequestRoomUser);
        this.llRoomRequestResult = (LinearLayout) parentLayout.findViewById(R.id.llRoomRequestResult);
        this.ivRoomRequestCheck = (ImageView) parentLayout.findViewById(R.id.ivRoomRequestCheck);
        this.tvRoomRequestNo = (TextView) parentLayout.findViewById(R.id.tvRoomRequestNo);
        this.tvRoomRequestYes = (TextView) parentLayout.findViewById(R.id.tvRoomRequestYes);
        this.tvRoomRequestName = (TextView) parentLayout.findViewById(R.id.tvRoomRequestName);

        tvRoomRequestName.setText(requestData.getName() + " 님이 입장을 신청하였습니다.");
//        Log.d("requestAdapterName", requestData.getName());

        setUpEvents();

        return parentLayout;
    }

    public void setUpEvents() {

        llRequestRoomUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(llRoomRequestResult.getVisibility() == View.GONE) {
                    llRoomRequestResult.setVisibility(View.VISIBLE);

                    Bitmap bitmap = BitmapFactory.decodeResource(parentLayout.getResources(), R.mipmap.arrow_icon);

                    Matrix sideInversion = new Matrix();
                    sideInversion.setScale(1, -1);

                    ivRoomRequestCheck.setImageBitmap(Bitmap.createBitmap(bitmap, 0, 0,
                            bitmap.getWidth(), bitmap.getHeight(), sideInversion, false));
                } else {
                    llRoomRequestResult.setVisibility(View.GONE);
                    ivRoomRequestCheck.setImageResource(R.mipmap.arrow_icon);
                }
            }
        });

        tvRoomRequestYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Params params = new Params();

                params.add("UserPKey", requestData.getUserPKey() + "");
                params.add("RoomPKey", requestData.getRoomPKey() + "");
                params.add("RoomName", RoomViewPager.getRoomName());


                new HttpNetwork("AcceptUser.php", params.getParams(), new HttpNetwork.AsyncResponse() {
                    @Override
                    public void onSuccess(String response) {
                        switch(response) {

                            case "success" :

                                RoomUserList.updateRequestList();
                                RoomUserList.updateRoomUserList();
                                roomListFragment.updateRoom(new MainViewPager.AfterUpdate() {
                                    @Override
                                    public void onPost(RoomData data) {
                                        FirebaseNoticeData notice = new FirebaseNoticeData(1, RoomViewPager.getRoomPKey() + "");
                                        databaseReference.child("Notice").child(requestData.getUserPKey() + "").push().setValue(notice);
                                        scheduleListFragment.updateSchedule();
                                    }
                                });

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
                params.add("UserPKey", requestData.getUserPKey() + "");
                params.add("RoomPKey", requestData.getRoomPKey() + "");

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
