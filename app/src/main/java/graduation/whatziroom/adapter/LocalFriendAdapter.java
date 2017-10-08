package graduation.whatziroom.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
 * Created by yrs00 on 2017-10-08.
 */

public class LocalFriendAdapter extends BaseAdapter implements Filterable {

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList. (원본 데이터 리스트)
    private ArrayList<FriendData> listViewItemList = new ArrayList<>();
    // 필터링된 결과 데이터를 저장하기 위한 ArrayList. 최초에는 전체 리스트 보유.
    private ArrayList<FriendData> filteredItemList = listViewItemList;

    Filter listFilter;
    private int blockFlag;
    private DBSI dbsi;

    public void setBlockFlag(int blockFlag){
        this.blockFlag = blockFlag;
    }

    public LocalFriendAdapter() {
        this.blockFlag = 0;
        dbsi = new DBSI();
    }

    @Override
    public int getCount() {
        return filteredItemList.size();

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.friend_list_tiem, parent, false);
        }

        ImageView thumNail = (ImageView) convertView.findViewById(R.id.friendListThumbImg);
        TextView friendName = (TextView) convertView.findViewById(R.id.friendListNameTxt);
        ;
        ImageView blockBtn = (ImageView) convertView.findViewById(R.id.friendListBlockTxt);
        ;

        final FriendData friendData = filteredItemList.get(position);

        friendName.setText(friendData.getUserName());

        if (this.blockFlag == 0) {
            System.out.println("어댑터 이벤트 0 작동" + blockFlag);
            blockBtn.setVisibility(View.INVISIBLE);
        } else {
            System.out.println("어댑터 이벤트 1 작동" + blockFlag);
            blockBtn.setVisibility(View.VISIBLE);
        }


        blockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowBlockDialog(context, position, friendData);
            }
        });

        return convertView;
    }


    // 친구 차단 다이얼로그
    private void ShowBlockDialog(final Context context, final int position, final FriendData data) {
        //Toast.makeText(mContext, position + "번째 친구", Toast.LENGTH_SHORT).show();
        View view = LayoutInflater.from(context).inflate(R.layout.block_friend_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(view);
        builder.setPositiveButton("네, 괜찮아요.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "차단되었습니다.", Toast.LENGTH_SHORT).show();
                Params params = new Params();
                params.add("UserPKey", dbsi.selectQuery("Select PKey From User")[0][0]);
                Log.d("FriendKey", String.valueOf(data.getUserPKey()));
                params.add("FriendKey", String.valueOf(data.getUserPKey()));

                new HttpNetwork("DeleteFriend.php", params.getParams(), new HttpNetwork.AsyncResponse() {
                    @Override
                    public void onSuccess(String response) {
                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                        filteredItemList.remove(position);
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

    @Override
    public Object getItem(int i) {
        return filteredItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    public void addItem(String name, int pkey) {
        FriendData friendData = new FriendData();
        friendData.setUserName(name);
        friendData.setUserPKey(pkey);
        listViewItemList.add(friendData);
    }


    @Override
    public Filter getFilter() {
        if (listFilter == null) {
            listFilter = new ListFilter();
        }
        return listFilter;
    }


    private class ListFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();


            if (constraint == null || constraint.length() == 0) {
                results.values = listViewItemList;
                results.count = listViewItemList.size();
            } else {
                ArrayList<FriendData> itemList = new ArrayList<FriendData>();
                for (FriendData item : listViewItemList) {
                    if (item.getUserName().toUpperCase().contains(constraint.toString().toUpperCase())) {
                        itemList.add(item);
                    }
                }
                results.values = itemList;
                results.count = itemList.size();
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredItemList = (ArrayList<FriendData>) results.values;

            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }

        }
    }

}
