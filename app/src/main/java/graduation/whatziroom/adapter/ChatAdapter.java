package graduation.whatziroom.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import graduation.whatziroom.Data.ChatData;
import graduation.whatziroom.R;


/**
 * Created by mapl0 on 2017-06-28.
 */

public class ChatAdapter extends ArrayAdapter {

    private Context mContext = null;
    private ArrayList<ChatData> mList = null;
    private LayoutInflater inf = null;

    private android.view.View divisionLeft;
    private de.hdodenhof.circleimageview.CircleImageView ivProfileLeft;
    private android.widget.TextView tvChatMessage, tvChatName;
    private de.hdodenhof.circleimageview.CircleImageView ivProfileRight;
    private android.view.View divisionRight;
    private android.widget.LinearLayout llParent;

    public ChatAdapter(Context context, ArrayList<ChatData> list) {
        super(context, R.layout.chat_item, list);

        mContext = context;
        mList = list;
        inf = LayoutInflater.from(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View parentLayout = convertView;

        // 리스트가 길어지면서 현재 화면에 보이지 않는 아이템은 converView가 null인 상태로 들어 옴
        if (parentLayout == null) {
            // view가 null일 경우 커스텀 레이아웃을 얻어 옴
            parentLayout = inf.inflate(R.layout.chat_item, parent, false);

        }

        this.llParent = (LinearLayout) parentLayout.findViewById(R.id.llParent);
        this.divisionRight = (View) parentLayout.findViewById(R.id.divisionRight);
        this.ivProfileRight = (CircleImageView) parentLayout.findViewById(R.id.ivProfileRight);
        this.tvChatMessage = (TextView) parentLayout.findViewById(R.id.tvChatMessage);
        this.tvChatName = (TextView) parentLayout.findViewById(R.id.tvChatName);
        this.ivProfileLeft = (CircleImageView) parentLayout.findViewById(R.id.ivProfileLeft);
        this.divisionLeft = (View) parentLayout.findViewById(R.id.divisionLeft);

        ChatData data = mList.get(position);

        tvChatMessage.setText(data.getMessage());
        tvChatName.setText(data.getName());

        switch (data.getFlag()) {

            case 0:
                tvChatMessage.setBackgroundResource(R.drawable.inbox2);
                llParent.setGravity(Gravity.LEFT);
                divisionLeft.setVisibility(View.GONE);
                divisionRight.setVisibility(View.GONE);
                break;

            case 1:
                tvChatMessage.setBackgroundResource(R.drawable.outbox2);
                llParent.setGravity(Gravity.RIGHT);
                divisionLeft.setVisibility(View.GONE);
                divisionRight.setVisibility(View.GONE);
                break;

            case 2:
                tvChatMessage.setBackgroundResource(R.drawable.outbox2);
                llParent.setGravity(Gravity.CENTER);
                divisionLeft.setVisibility(View.VISIBLE);
                divisionRight.setVisibility(View.VISIBLE);
                break;

        }

        return parentLayout;
    }

}
