package com.example.heronation.whatziroom.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.heronation.whatziroom.Data.FriendData;
import com.example.heronation.whatziroom.Data.NoticeData;
import com.example.heronation.whatziroom.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by user on 2017-07-16.
 */

public class NoticeAdapter extends ArrayAdapter {

    Context mContext = null;
    ArrayList<NoticeData> mList = null;
    LayoutInflater inf = null;

    public NoticeAdapter(Context context, ArrayList<NoticeData> list){
        super(context, R.layout.notice_list_item, list);
        mContext = context;
        mList = list;
        inf = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row = convertView;

        if(row == null){
            row = inf.inflate(R.layout.notice_list_item, null);
        }

        final LinearLayout ll = (LinearLayout)row.findViewById(R.id.noticeSelectBeforeLL);
        final TextView noticeResultTxt = (TextView)row.findViewById(R.id.noticeResultTxt);
        TextView noticeTxt1 = (TextView)row.findViewById(R.id.noticeTxt1);
        TextView noticeTxt2 = (TextView)row.findViewById(R.id.noticeTxt2);
        TextView okBtn = (TextView)row.findViewById(R.id.noticeOKBtn);
        TextView cancelBtn = (TextView)row.findViewById(R.id.noticeCancelBtn);

//        noticeTxt1.setText(mList.get(position).getUserName()+"님의 친구 신청");
//        noticeTxt2.setText(mList.get(position).getUserText());

        if(mList.get(position).getCheckFlag()==1){
            noticeResultTxt.setText(mList.get(position).getUserName()+"님과 친구가 되었습니다.");
            ll.setVisibility(View.GONE);
        }else if(mList.get(position).getCheckFlag()==2){
            noticeResultTxt.setText(mList.get(position).getUserName()+"님의 친구신청을 거절했습니다.");
            ll.setVisibility(View.GONE);
        }else{
            noticeTxt1.setText(mList.get(position).getUserName()+"님의 친구 신청");
            noticeTxt2.setText(mList.get(position).getUserText());
            ll.setVisibility(View.VISIBLE);
        }

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noticeResultTxt.setText(mList.get(position).getUserName()+"님과 친구가 되었습니다.");
                mList.get(position).setCheckFlag(1);
                ll.setVisibility(View.GONE);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noticeResultTxt.setText(mList.get(position).getUserName()+"님의 친구신청을 거절했습니다.");
                mList.get(position).setCheckFlag(2);
                ll.setVisibility(View.GONE);
            }
        });

        return row;
    }
}
