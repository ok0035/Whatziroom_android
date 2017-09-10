package com.graduation.project.whatziroom.activity.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.graduation.project.whatziroom.Data.FriendData;
import com.graduation.project.whatziroom.R;
import com.graduation.project.whatziroom.adapter.FriendAdapter;

import java.util.ArrayList;

/**
 * Created by ATIV on 2017-06-25.
 */

public class FriendListView extends Fragment {
    LinearLayout layout;
    boolean blockFlag = false; // true면 차단 버튼 보이게, false면 안보임
    FriendAdapter mFriendAdapter;
    ListView freindListView;
    ArrayList<FriendData> friendListItem;
    TextView searchFreindBtn;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layout = (LinearLayout) inflater.inflate(R.layout.friend_list, container, false);
        searchFreindBtn = (TextView)layout.findViewById(R.id.searchFreindBtn);
        freindListView = (ListView) layout.findViewById(R.id.friendList);
        friendListItem = new ArrayList<>();

        searchFreindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("toucg","asdf");
                FriendData friendData = new FriendData();
                friendData.setUserName("동적추가");
                mFriendAdapter.notifyDataSetChanged();
            }
        });

        final FriendData f1 = new FriendData();
        f1.setUserName("최현승");
        friendListItem.add(f1);
        friendListItem.add(f1);

        FriendData f2 = new FriendData();
        f2.setUserName("장영도");
        friendListItem.add(f2);
        friendListItem.add(f2);

        FriendData f3 = new FriendData();
        f3.setUserName("이연희");
        friendListItem.add(f3);
        friendListItem.add(f3);

        FriendData f4 = new FriendData();
        f4.setUserName("이우석");
        friendListItem.add(f4);
        friendListItem.add(f4);

        FriendData f5 = new FriendData();
        f5.setUserName("유석현");
        friendListItem.add(f5);
        friendListItem.add(f5);

        mFriendAdapter = new FriendAdapter(getActivity(), friendListItem, 0);
        freindListView.setAdapter(mFriendAdapter);

        return layout;
    }


    // 친구 목록에서 친구 추가 눌렀을 경우 실행되는 함수
    public void findFriendFunc(){

        EditText edittext = (EditText)getActivity().findViewById(R.id.findFreindEdt);
        edittext.setHint("친구 찾기(이메일, 닉네임)");

        final ListView listview = (ListView) getActivity().findViewById(R.id.friendList);
        TextView textview = (TextView)getActivity().findViewById(R.id.searchFreindBtn);

        listview.setAdapter(null);
        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listview.setAdapter(null);
//                friendListItem.clear();
                // 서버 연결되면 검색하는 함수로 변경 지금은 임시
                FriendData fd = new FriendData();
                fd.setUserName("김종록");
                ArrayList<FriendData> list = new ArrayList<FriendData>();
                list.add(fd);
                listview.setAdapter(new FriendAdapter(getActivity(), list, 2));
            }
        });

    }
    // 친구 추가 끝나고 완료 버튼 클릭시 실행되는 함수
    public void reloadFunc(){
        EditText edittext = (EditText)getActivity().findViewById(R.id.findFreindEdt);
        edittext.setHint("친구 찾기(이름)");

        TextView textview = (TextView)getActivity().findViewById(R.id.searchFreindBtn);
        textview.setOnClickListener(null);

        ListView listview = (ListView) getActivity().findViewById(R.id.friendList);
        listview.setAdapter(null);
        listview.setAdapter(new FriendAdapter(getActivity(), friendListItem, 0));

    }


    // 친구 목록에서 편집 눌렀을 경우 실행되는 함수
    public void showBlockBtn() {
//        Toast.makeText(getActivity(), "버튼 클릭!!", Toast.LENGTH_SHORT).show();

        if (!blockFlag) {
            blockFlag = true;
            ListView listview = (ListView) getActivity().findViewById(R.id.friendList);
            listview.setAdapter(null);
            listview.setAdapter(new FriendAdapter(getActivity(), friendListItem, 1));
        } else {
            blockFlag = false;
            ListView listview = (ListView) getActivity().findViewById(R.id.friendList);
            listview.setAdapter(null);
            listview.setAdapter(new FriendAdapter(getActivity(), friendListItem, 0));
        }

    }

}
