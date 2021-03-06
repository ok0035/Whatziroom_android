package graduation.whatziroom.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import graduation.whatziroom.Data.FriendData;
import graduation.whatziroom.R;
import graduation.whatziroom.network.DBSI;
import graduation.whatziroom.network.HttpNetwork;
import graduation.whatziroom.network.Params;

/**
 * Created by Heronation on 2017-07-10.
 */

public class FriendAdapter extends ArrayAdapter {

    Context mContext = null;
    ArrayList<FriendData> mList = null;
    LayoutInflater inf = null;
    int blockFlag;
    DBSI dbsi;

    ImageView thumNail;
    TextView friendName;
    ImageView blockBtn;

    // 친구 신청 다이얼로그
    TextView tvFriendRequestTxt;
    TextView btnRequestFriendOk;
    TextView btnRequestFriendCancel;

    public FriendAdapter(Context context, ArrayList<FriendData> list, int blockFlag) {
        super(context, R.layout.friend_list_tiem, list);
        mContext = context;
        mList = list;
        inf = LayoutInflater.from(mContext);
        this.blockFlag = blockFlag;
        dbsi = new DBSI();

        Log.d("생성자 block flag : ", this.blockFlag + "");
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row = convertView;

        if (row == null) {
            row = inf.inflate(R.layout.find_friend_list_item, null);
        }

        thumNail = row.findViewById(R.id.friendListThumbImg);
        friendName = row.findViewById(R.id.friendListNameTxt);
        blockBtn = row.findViewById(R.id.friendListBlockTxt);

        final FriendData data = mList.get(position);

        friendName.setText(mList.get(position).getUserName());

        if (this.blockFlag == 0) {
            System.out.println("어댑터 이벤트 0 작동" + blockFlag);
            blockBtn.setVisibility(View.INVISIBLE);
        } else if (this.blockFlag == 1) {
            System.out.println("어댑터 이벤트 1 작동" + blockFlag);
            blockBtn.setVisibility(View.VISIBLE);
        } else if (this.blockFlag == 2) {
            blockBtn.setVisibility(View.INVISIBLE);
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowRequestDialog(position);
                }
            });
        }

        blockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowBlockDialog(position, data);
            }
        });

        return row;
    }

    // 친구 차단 다이얼로그
    private void ShowBlockDialog(final int position, final FriendData data) {
        //Toast.makeText(mContext, position + "번째 친구", Toast.LENGTH_SHORT).show();
        View view = LayoutInflater.from(mContext).inflate(R.layout.block_friend_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setView(view);
        builder.setPositiveButton("네.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(mContext, "차단되었습니다.", Toast.LENGTH_SHORT).show();
                Params params = new Params();
                params.add("UserPKey", dbsi.selectQuery("Select PKey From User")[0][0]);
                Log.d("FriendKey",String.valueOf(data.getUserPKey()));
                params.add("FriendKey", String.valueOf(data.getUserPKey()));

                new HttpNetwork("DeleteFriend.php", params.getParams(), new HttpNetwork.AsyncResponse() {
                    @Override
                    public void onSuccess(String response) {
                        Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
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
        builder.setNegativeButton("아니요. 실수로 눌렀어요!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.create().show();

    }

    // 친구 신청 다이얼로그
    private void ShowRequestDialog(final int position) {

        LayoutInflater dialog = LayoutInflater.from(mContext);
        final View dialogLayout = dialog.inflate(R.layout.request_friend_dialog, null);
        final Dialog myDialog = new Dialog(mContext);

        myDialog.setTitle(null);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(dialogLayout);

        final ViewGroup.LayoutParams params = myDialog.getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        myDialog.getWindow().setAttributes((WindowManager.LayoutParams) params);

        myDialog.show();

        tvFriendRequestTxt = dialogLayout.findViewById(R.id.tvFriendRequestTxt);
        btnRequestFriendCancel = dialogLayout.findViewById(R.id.btnRequestFriendCancel);
        btnRequestFriendOk = dialogLayout.findViewById(R.id.btnRequestFriendOk);

        tvFriendRequestTxt.setText(mList.get(position).getUserName()+"님에게\n친구신청 메시지를 보내드릴께요!^.^");

        btnRequestFriendCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel();
            }
        });

        btnRequestFriendOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(mContext, "전송완료", Toast.LENGTH_SHORT).show();
                if(mList.get(position).getFreindStatus().equals("send_wating")){
                    Toast.makeText(mContext, "이미 친구 신청이 완료되었어요.", Toast.LENGTH_SHORT).show();
                }else if(mList.get(position).getFreindStatus().equals("receive_wating")){
                    Toast.makeText(mContext, "상대방이 먼저 메시지를 보내셨네요. \n 알림창을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{

                    Params params1 = new Params();

                    Log.d("UserPKey",dbsi.selectQuery("Select PKey From User")[0][0]);
                    Log.d("FriendKey", String.valueOf(mList.get(position).getUserPKey()));
                    Log.d("Status", "0");

                    params1.add("UserPKey",dbsi.selectQuery("Select PKey From User")[0][0]);
                    params1.add("FriendKey", String.valueOf(mList.get(position).getUserPKey()));
                    params1.add("Status","0");

                    new HttpNetwork("AddFriend.php", params1.getParams(), new HttpNetwork.AsyncResponse() {
                        @Override
                        public void onSuccess(String response) {
                            //Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
                            mList.get(position).setFreindStatus("send_wating");
                        }

                        @Override
                        public void onFailure(String response) {
                            //Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onPreExcute() {

                        }
                    });

                }

                myDialog.dismiss();
            }
        });
    }

    public void refreshAdapter(ArrayList<FriendData> items) {
        this.mList.clear();
        this.mList.addAll(items);
        notifyDataSetChanged();
    }
}
