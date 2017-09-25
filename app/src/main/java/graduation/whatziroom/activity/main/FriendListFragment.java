package graduation.whatziroom.activity.main;

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

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import graduation.whatziroom.Data.FriendData;
import graduation.whatziroom.R;
import graduation.whatziroom.adapter.FriendAdapter;
import graduation.whatziroom.network.DBSI;
import graduation.whatziroom.network.HttpNetwork;
import graduation.whatziroom.network.Params;
import graduation.whatziroom.util.ParseData;

/**
 * Created by ATIV on 2017-06-25.
 */

public class FriendListFragment extends Fragment {
    LinearLayout layout;
    boolean blockFlag = false; // true면 차단 버튼 보이게, false면 안보임
    FriendAdapter mFriendAdapter;
    ListView freindListView;
    ArrayList<FriendData> friendListItem;
    TextView searchFreindBtn;
    DBSI dbsi;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layout = (LinearLayout) inflater.inflate(R.layout.friend_list, container, false);
        searchFreindBtn = (TextView)layout.findViewById(R.id.searchFreindBtn);
        freindListView = (ListView) layout.findViewById(R.id.friendList);
        friendListItem = new ArrayList<>();
        dbsi = new DBSI();

        searchFreindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("toucg","asdf");
                FriendData friendData = new FriendData();
                friendData.setUserName("동적추가");
                mFriendAdapter.notifyDataSetChanged();
            }
        });

        final Params params = new Params();
        params.add("UserPKey", dbsi.selectQuery("Select PKey From User")[0][0]);
        new HttpNetwork("GetFriendList.php", params.getParams(), new HttpNetwork.AsyncResponse() {
            @Override
            public void onSuccess(String response) {
                Log.d("response",response);
//                Toast.makeText(getActivity(),response, Toast.LENGTH_SHORT).show();
                ParseData parseData = new ParseData();
                JSONArray parseArray = new JSONArray();
                try {
                    parseArray  = parseData.parseJsonArray(response);
                    for(int i = 0 ; i < parseArray.length() ; i++){

                        FriendData friendData = new FriendData();
                        friendData.setUserName(parseArray.getJSONObject(i).getString("Name"));
                        friendData.setUserPKey(parseArray.getJSONObject(i).getInt("PKey"));
                        friendListItem.add(friendData);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mFriendAdapter = new FriendAdapter(getActivity(), friendListItem, 0);
                freindListView.setAdapter(mFriendAdapter);


            }

            @Override
            public void onFailure(String response) {

            }

            @Override
            public void onPreExcute() {

            }
        });

        return layout;
    }


    // 친구 목록에서 친구 추가 눌렀을 경우 실행되는 함수
    public void findFriendFunc(){


        final EditText edittext = (EditText)getActivity().findViewById(R.id.findFreindEdt);
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

                final Params params = new Params();
                params.add("UserPKey",dbsi.selectQuery("Select PKey From User")[0][0]);
                params.add("FindText", edittext.getText().toString());
                final ArrayList<FriendData> list = new ArrayList<FriendData>();
                new HttpNetwork("FindFriend.php", params.getParams(), new HttpNetwork.AsyncResponse() {
                    @Override
                    public void onSuccess(String response) {
                        ParseData parseData = new ParseData();
                        try {
                            JSONArray parseArray = parseData.parseJsonArray(response);

                            for(int i = 0 ; i < parseArray.length(); i++){
                                Log.d("for문"," 동작");
                                FriendData friendData = new FriendData();
                                friendData.setUserPKey(parseArray.getJSONObject(i).getInt("PKey"));
                                friendData.setUserName(parseArray.getJSONObject(i).getString("Name"));
                                friendData.setFreindStatus(parseArray.getJSONObject(i).getString("FriendStatus"));

                                list.add(friendData);
                            }
                            listview.setAdapter(new FriendAdapter(getActivity(), list, 2));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }


                    @Override
                    public void onFailure(String response) {

                    }

                    @Override
                    public void onPreExcute() {

                    }
                });

//
//                FriendData fd = new FriendData();
//                fd.setUserName("김종록");
//                ArrayList<FriendData> list = new ArrayList<FriendData>();
//                list.add(fd);
//                listview.setAdapter(new FriendAdapter(getActivity(), list, 2));

            }
        });

    }
    // 친구 추가 끝나고 완료 버튼 클릭시 실행되는 함수
    public void reloadFunc(){
        EditText edittext = (EditText)getActivity().findViewById(R.id.findFreindEdt);
        edittext.setText(null);
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
