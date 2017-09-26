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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import graduation.whatziroom.Data.FriendData;
import graduation.whatziroom.R;

/**
 * Created by Heronation on 2017-07-10.
 */

public class FriendAdapter extends ArrayAdapter {

    Context mContext = null;
    ArrayList<FriendData> mList = null;
    LayoutInflater inf = null;
    int blockFlag;

    public FriendAdapter(Context context, ArrayList<FriendData> list, int blockFlag) {
        super(context, R.layout.friend_list_tiem, list);
        mContext = context;
        mList = list;
        inf = LayoutInflater.from(mContext);
        this.blockFlag = blockFlag;
        Log.d("생성자 block flag : ", this.blockFlag + "");
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row = convertView;

        if (row == null) {
            row = inf.inflate(R.layout.friend_list_tiem, null);
        }

        ImageView thumNail = row.findViewById(R.id.friendListThumbImg);
        TextView firendName = row.findViewById(R.id.friendListNameTxt);
        TextView blockBtn = row.findViewById(R.id.friendListBlockTxt);

        FriendData data = mList.get(position);

        firendName.setText(data.getUserName());

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
                    Toast.makeText(mContext, position + "커스텀 다이얼로그", Toast.LENGTH_SHORT).show();
                    ShowDialog();
                }
            });
        }

        blockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, position + "번째 친구", Toast.LENGTH_SHORT).show();
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("정말로 차단하시겠습니까?");
                builder.setPositiveButton("네 괜찮아요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext, "차단 완료", Toast.LENGTH_SHORT).show();

                    }
                });
                builder.setNegativeButton("아니요. 실수로 눌렀어요!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        return row;
    }

    private void ShowDialog() {
        LayoutInflater dialog = LayoutInflater.from(mContext);
        final View dialogLayout = dialog.inflate(R.layout.request_friend_dialog, null);
        final Dialog myDialog = new Dialog(mContext);

//        myDialog.setTitle("대화상자 제목이다");
        myDialog.setContentView(dialogLayout);
        myDialog.show();

        Button btn_ok = dialogLayout.findViewById(R.id.btn_ok);
        Button btn_cancel = dialogLayout.findViewById(R.id.btn_cancel);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "전송완료", Toast.LENGTH_SHORT).show();
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
