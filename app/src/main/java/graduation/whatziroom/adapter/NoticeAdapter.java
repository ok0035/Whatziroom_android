package graduation.whatziroom.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import graduation.whatziroom.Data.FirebaseNoticeData;
import graduation.whatziroom.Data.NoticeData;
import graduation.whatziroom.Data.RoomData;
import graduation.whatziroom.R;
import graduation.whatziroom.activity.main.MainViewPager;
import graduation.whatziroom.activity.room.RoomViewPager;
import graduation.whatziroom.network.HttpNetwork;
import graduation.whatziroom.network.Params;

import static graduation.whatziroom.activity.main.MainViewPager.roomListFragment;
import static graduation.whatziroom.activity.main.MainViewPager.scheduleListFragment;
import static graduation.whatziroom.activity.room.RoomViewPager.roomFriendList;

/**
 * Created by user on 2017-07-16.
 */

public class NoticeAdapter extends ArrayAdapter {


    Context mContext = null;
    ArrayList<NoticeData> mList = null;
    LayoutInflater inf = null;

    public NoticeAdapter(Context context, ArrayList<NoticeData> list) {
        super(context, R.layout.notice_list_item, list);
        mContext = context;
        mList = list;
        inf = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull ViewGroup parent) {


        View row = convertView;

        if (row == null) {
            row = inf.inflate(R.layout.notice_list_item, null);
        }

        final LinearLayout llParent = (LinearLayout) row.findViewById(R.id.llParent);
        final LinearLayout ll = (LinearLayout) row.findViewById(R.id.noticeSelectBeforeLL);
        final TextView noticeResultTxt = (TextView) row.findViewById(R.id.noticeResultTxt);
        final TextView readBtn = (TextView) row.findViewById(R.id.noticeReadBtn);
        TextView noticeTxt1 = (TextView) row.findViewById(R.id.noticeTxt1);
        TextView okBtn = (TextView) row.findViewById(R.id.noticeOKBtn);
        TextView cancelBtn = (TextView) row.findViewById(R.id.noticeCancelBtn);

//        noticeTxt1.setText(mList.get(position).getUserName()+"님의 친구 신청");
//        noticeTxt2.setText(mList.get(position).getUserText());
        if (mList.get(position).getSeperator().equals("Friend")) { //친구 알림
            noticeResultTxt.setText("저와 친구가 되어주실래요?");

            if (mList.get(position).getSrFlag().equals("receive")) {
                // 내가 받은 친구 신청 목록
                okBtn.setVisibility(View.VISIBLE);
                cancelBtn.setText("X");
                if (mList.get(position).getFriendStatus().equals("1")) {
                    noticeResultTxt.setText(mList.get(position).getUserName() + " 님과 친구가 되었습니다.");
                    readBtn.setVisibility(View.VISIBLE);
                    ll.setVisibility(View.GONE);
                } else if (mList.get(position).getFriendStatus().equals("2")) {
                    noticeResultTxt.setText(mList.get(position).getUserName() + " 님의 친구신청을 거절했습니다.");
                    readBtn.setVisibility(View.VISIBLE);
                    ll.setVisibility(View.GONE);
                } else {
                    noticeTxt1.setText(mList.get(position).getUserName() + " 님이 친구 신청 메시지를 보내셨어요!");
                    ll.setVisibility(View.VISIBLE);
                }
            } else {
                // 내가 보낸 친구 신청 목록
                okBtn.setVisibility(View.GONE);
                cancelBtn.setText("취소");
                if (mList.get(position).getFriendStatus().equals("1")) {
                    noticeResultTxt.setText(mList.get(position).getUserName() + " 님과 친구가 되었습니다.");
                    readBtn.setVisibility(View.VISIBLE);
                    ll.setVisibility(View.GONE);
                } else if (mList.get(position).getFriendStatus().equals("2")) {
                    noticeResultTxt.setText(mList.get(position).getUserName() + " 님이 친구신청을 거절하셨어요.");
                    readBtn.setVisibility(View.VISIBLE);
                    ll.setVisibility(View.GONE);
                } else {
                    noticeTxt1.setText(mList.get(position).getUserName() + " 님에게 친구 신청하셨어요.\n조금만 기다려주세요.");
                    ll.setVisibility(View.VISIBLE);
                }
            }

            okBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Params params = new Params();
                    params.add("FriendPKey", mList.get(position).getFriendPKey());
                    params.add("Status", "1");
                    new HttpNetwork("SetFriend.php", params.getParams(), new HttpNetwork.AsyncResponse() {
                        @Override
                        public void onSuccess(String response) {
                            if (response.contains("SUCCESS")) {
                                noticeResultTxt.setText(mList.get(position).getUserName() + "님과 친구가 되었습니다.");
                                mList.get(position).setFriendStatus("1");
                                notifyDataSetChanged();
                                readBtn.setVisibility(View.VISIBLE);
                                ll.setVisibility(View.GONE);

                            } else {
                                Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
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

            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mList.get(position).getSrFlag().equals("receive")) {
                        Params params = new Params();
                        params.add("FriendPKey", mList.get(position).getFriendPKey());
                        params.add("Status", "2");
                        new HttpNetwork("SetFriend.php", params.getParams(), new HttpNetwork.AsyncResponse() {
                            @Override
                            public void onSuccess(String response) {
                                if (response.contains("SUCCESS")) {
                                    noticeResultTxt.setText(mList.get(position).getUserName() + "님의 친구신청을 거절했습니다.");
                                    readBtn.setVisibility(View.VISIBLE);
                                    mList.get(position).setFriendStatus("2");
                                    notifyDataSetChanged();
                                    ll.setVisibility(View.GONE);
                                } else {
                                    Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(String response) {

                            }

                            @Override
                            public void onPreExcute() {

                            }
                        });


//                mList.get(position).setCheckFlag(2);
                        ll.setVisibility(View.GONE);
                    } else {

                        Params params = new Params();
                        params.add("FriendPKey", mList.get(position).getFriendPKey());
                        params.add("Status", "-1");
                        new HttpNetwork("SetFriend.php", params.getParams(), new HttpNetwork.AsyncResponse() {
                            @Override
                            public void onSuccess(String response) {
                                if (response.contains("SUCCESS")) {
                                    mList.remove(position);
                                    notifyDataSetChanged();
                                } else {
                                    Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
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

                }
            });


        } else { // 방 신청 알림
            if (mList.get(position).getRaFlag().equals("Accepter")) { // 방입장 허가
                // 내가 받은 친구 신청 목록
                okBtn.setVisibility(View.VISIBLE);
                cancelBtn.setText("X");
                noticeResultTxt.setText("들어가도 될까요?");
                if (mList.get(position).getRoomStatus().equals("1")) {
                    noticeTxt1.setText(mList.get(position).getUserName_Room() + " 님이 " + mList.get(position).getRoomNAme() + "방 입장 신청을 했습니다.");
                    readBtn.setVisibility(View.GONE);
                    okBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Params params = new Params();
                            params.add("UserPKey", mList.get(position).getUserPKey_Room());
                            params.add("RoomPKey", mList.get(position).getRoomPKey());
                            params.add("RoomName", mList.get(position).getRoomNAme());
                            new HttpNetwork("AcceptUser.php", params.getParams(), new HttpNetwork.AsyncResponse() {
                                @Override
                                public void onSuccess(String response) {
                                    switch (response) {

                                        case "success":
                                            noticeResultTxt.setText(mList.get(position).getUserName_Room() + " 님의 " + mList.get(position).getRoomNAme() + "방 입장을 수락했습니다.");
                                            readBtn.setVisibility(View.VISIBLE);
                                            mList.get(position).setRoomStatus("2");
                                            notifyDataSetChanged();
                                            ll.setVisibility(View.GONE);
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
                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Params params = new Params();
                            params.add("UserPKey", mList.get(position).getUserPKey_Room());
                            params.add("RoomPKey", mList.get(position).getRoomPKey());

                            new HttpNetwork("RefuseUser.php", params.getParams(), new HttpNetwork.AsyncResponse() {
                                @Override
                                public void onSuccess(String response) {
                                    switch(response) {

                                        case "success" :
                                            noticeResultTxt.setText(mList.get(position).getUserName_Room() + "님의 방신청을 거절했습니다.");
                                            readBtn.setVisibility(View.VISIBLE);
                                            mList.get(position).setRoomStatus("4");
                                            notifyDataSetChanged();
                                            ll.setVisibility(View.GONE);
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
                    ll.setVisibility(View.VISIBLE);
                } else if (mList.get(position).getRoomStatus().equals("4")) {
                    noticeResultTxt.setText(mList.get(position).getUserName_Room() + " 님의 방 입장신청을 거절했습니다.");
                    readBtn.setVisibility(View.VISIBLE);
                    ll.setVisibility(View.GONE);
                } else if (mList.get(position).getRoomStatus().equals("2")) {
                    noticeResultTxt.setText(mList.get(position).getUserName_Room() + " 님의 " + mList.get(position).getRoomNAme() + "방 입장을 수락했습니다.");
                    readBtn.setVisibility(View.VISIBLE);
                    ll.setVisibility(View.GONE);
                }
            } else {
                // 내가 보낸 방 신청 목록
                okBtn.setVisibility(View.GONE);
                cancelBtn.setText("취소");
                noticeResultTxt.setText("들어가도 될까요?");
                if (mList.get(position).getRoomStatus().equals("1")) {
                    noticeTxt1.setText(mList.get(position).getRoomNAme() + "방 입장을 신청하셨어요.\n조금만 기다려주세요.");
                    readBtn.setVisibility(View.GONE);
                    ll.setVisibility(View.VISIBLE);

                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Params params = new Params();
                            params.add("RoomListPKey", mList.get(position).getRoomListPKey());

                            new HttpNetwork("DeleteRoomRequest.php", params.getParams(), new HttpNetwork.AsyncResponse() {
                                @Override
                                public void onSuccess(String response) {
                                    mList.remove(position);
                                    notifyDataSetChanged();
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
                } else if (mList.get(position).getRoomStatus().equals("2")) {
                    noticeResultTxt.setText(mList.get(position).getRoomNAme() + "방 입장이 수락됐습니다.");
                    readBtn.setVisibility(View.VISIBLE);
                    ll.setVisibility(View.GONE);
                }
            }


        }

        readBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Params params = new Params();
                if (mList.get(position).getSeperator().equals("Friend")) {
                    params.add("PKey", mList.get(position).getFriendPKey());
                    params.add("Flag", mList.get(position).getSrFlag());
                } else {
                    params.add("PKey", mList.get(position).getRoomListPKey());
                    params.add("Flag", mList.get(position).getRaFlag());
                }


                new HttpNetwork("ReadNotice.php", params.getParams(), new HttpNetwork.AsyncResponse() {
                    @Override
                    public void onSuccess(String response) {
                        if (response.contains("SUCCESS")) {
                            mList.remove(position);
                            notifyDataSetChanged();
                        } else {
                            Log.d("response,,,", response);
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

        return row;
    }
}
