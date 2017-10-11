package graduation.whatziroom.activity.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import graduation.whatziroom.Data.FriendData;
import graduation.whatziroom.R;
import graduation.whatziroom.adapter.FriendAdapter;
import graduation.whatziroom.adapter.LocalFriendAdapter;
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
    ArrayList<FriendData> friendListItem;
    ArrayList<FriendData> findfriendLitsItem;

    ImageView searchFreindBtn;
    DBSI dbsi;

    private EditText edFindFriend;
    private TextView tvFriendSearchBack;
    private ListView friendList;
    private ImageView btnResetFriend;
    private ListView findFriendList;

    private LocalFriendAdapter localFriendAdapter;
    private TextWatcher edFindFriendTextWatch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layout = (LinearLayout) inflater.inflate(R.layout.friend_list, container, false);
        this.tvFriendSearchBack = (TextView) layout.findViewById(R.id.tvFriendSearchBack);
        this.searchFreindBtn = (ImageView) layout.findViewById(R.id.searchFriendBtn);
        this.edFindFriend = (EditText) layout.findViewById(R.id.edFindFriend);
        this.btnResetFriend = (ImageView) layout.findViewById(R.id.btnResetFriend);
        this.friendList = (ListView) layout.findViewById(R.id.friendList);
        localFriendAdapter = new LocalFriendAdapter();

        this.findFriendList = (ListView) layout.findViewById(R.id.findfriendList);
        friendListItem = new ArrayList<>();

        findfriendLitsItem = new ArrayList<>();
        dbsi = new DBSI();

        setUpEvents();

        final Params params = new Params();
        params.add("UserPKey", dbsi.selectQuery("Select PKey From User")[0][0]);
        new HttpNetwork("GetFriendList.php", params.getParams(), new HttpNetwork.AsyncResponse() {
            @Override
            public void onSuccess(String response) {
                Log.d("response", response);
//                Toast.makeText(getActivity(),response, Toast.LENGTH_SHORT).show();
                ParseData parseData = new ParseData();
                JSONArray parseArray = new JSONArray();
                try {
                    parseArray = parseData.parseJsonArray(response);
                    for (int i = 0; i < parseArray.length(); i++) {

                        localFriendAdapter.addItem(parseArray.getJSONObject(i).getString("Name"), parseArray.getJSONObject(i).getInt("PKey"));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                friendList.setAdapter(localFriendAdapter);
                edFindFriend.addTextChangedListener(edFindFriendTextWatch);


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

    public void setUpEvents() {

        edFindFriendTextWatch = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                if (text.length() > 0) {
                    btnResetFriend.setVisibility(View.VISIBLE);
                } else {
                    btnResetFriend.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String filterText = editable.toString();
//                if (filterText.length() > 0) {
//                    friendList.setFilterText(filterText);
//                } else {
//                    friendList.clearTextFilter();
//                }

                ((LocalFriendAdapter)friendList.getAdapter()).getFilter().filter(filterText) ;


            }
        };



        btnResetFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edFindFriend.setText(null);
                btnResetFriend.setVisibility(View.INVISIBLE);
            }
        });

    }

    // 친구 목록에서 친구 추가 눌렀을 경우 실행되는 함수
    public void findFriendFunc() {


        edFindFriend.setText(null);
        edFindFriend.setHint("친구 찾기(이메일, 닉네임)");

        friendList.setVisibility(View.GONE);
        findFriendList.setVisibility(View.VISIBLE);

        findFriendList.setAdapter(null);

        searchFreindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvFriendSearchBack.setVisibility(View.VISIBLE);
                final Params params = new Params();
                params.add("UserPKey", dbsi.selectQuery("Select PKey From User")[0][0]);
                params.add("FindText", edFindFriend.getText().toString());
                final ArrayList<FriendData> list = new ArrayList<FriendData>();
                new HttpNetwork("FindFriend.php", params.getParams(), new HttpNetwork.AsyncResponse() {
                    @Override
                    public void onSuccess(String response) {
                        ParseData parseData = new ParseData();
                        try {
                            JSONArray parseArray = parseData.parseJsonArray(response);

                            for (int i = 0; i < parseArray.length(); i++) {
                                Log.d("for문", " 동작");
                                FriendData friendData = new FriendData();
                                friendData.setUserPKey(parseArray.getJSONObject(i).getInt("PKey"));
                                friendData.setUserName(parseArray.getJSONObject(i).getString("Name"));
                                friendData.setFreindStatus(parseArray.getJSONObject(i).getString("FriendStatus"));

                                list.add(friendData);
                            }
                            findFriendList.setAdapter(new FriendAdapter(getActivity(), list, 2));

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


            }
        });

    }

    // 친구 추가 끝나고 완료 버튼 클릭시 실행되는 함수
    public void reloadFunc() {

        edFindFriend.setText(null);
        edFindFriend.setHint("친구 찾기(이름)");


        searchFreindBtn.setOnClickListener(null);

        tvFriendSearchBack.setVisibility(View.GONE);

        findFriendList.setVisibility(View.GONE);
        friendList.setVisibility(View.VISIBLE);


    }


    // 친구 목록에서 편집 눌렀을 경우 실행되는 함수
    public void showBlockBtn() {

        if (!blockFlag) {
            blockFlag = true;

            friendList.setAdapter(null);
            tvFriendSearchBack.setVisibility(View.GONE);
            localFriendAdapter.setBlockFlag(1);
            friendList.setAdapter(localFriendAdapter);
        } else {
            blockFlag = false;
            friendList.setAdapter(null);
            tvFriendSearchBack.setVisibility(View.GONE);
            localFriendAdapter.setBlockFlag(0);
            friendList.setAdapter(localFriendAdapter);
        }

    }

}
