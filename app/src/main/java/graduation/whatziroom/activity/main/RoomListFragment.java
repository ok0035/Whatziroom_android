package graduation.whatziroom.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import graduation.whatziroom.Data.RoomData;
import graduation.whatziroom.R;
import graduation.whatziroom.activity.base.BaseActivity;
import graduation.whatziroom.activity.base.BasicMethod;
import graduation.whatziroom.activity.room.RoomViewPager;
import graduation.whatziroom.dialog.RequestRoomDialog;
import graduation.whatziroom.network.HttpNetwork;
import graduation.whatziroom.network.Params;
import graduation.whatziroom.util.ParseData;

import static graduation.whatziroom.activity.main.MainViewPager.roomListFragment;

/**
 * Created by ATIV on 2017-06-25.
 */

public class RoomListFragment extends Fragment implements BasicMethod, View.OnTouchListener {

    private LinearLayout layout;
    private ImageView ivRoomSearchFlag;
    public ListView roomListView;
    public RoomData roomData;
    private RoomData roomSearchData;
    private android.widget.EditText edFindRoom;
    private android.widget.ListView roomSearchListView;
    private android.widget.ImageView searchRoom;
    private ImageView ivBtnSearch;
    private android.widget.TextView tvRoomSearchBack, tvChatCount;
    private android.widget.LinearLayout llSearchRoom;

    private ImageView btnResetRoom;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layout = (LinearLayout) inflater.inflate(R.layout.room_list, container, false);

        bindView();
        setValues();
        setUpEvents();

        return layout;
    }

    public RoomListFragment() {

        roomData = new RoomData();
    }

    @Override
    public void setUpEvents() {

//        roomListView.setAdapter(MainViewPager.roomData.getAdapter());
        Log.d("tqefef","fxcvras.........");
        roomListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                Intent intent = new Intent(getContext(), RoomViewPager.class);
                RoomViewPager.setRoomPKey(roomData.getRoomArrayList().get(position).getRoomPKey().toString());
                RoomViewPager.setRoomName(roomData.getRoomArrayList().get(position).getRoomName());
                startActivity(intent);
            }
        });

        roomSearchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("roomPKey", roomSearchData.getRoomArrayList().get(i).getRoomPKey());
                MainViewPager.getUserPKey();
                new RequestRoomDialog(getContext(), MainViewPager.getUserPKey(),
                        Integer.parseInt(roomSearchData.getRoomArrayList().get(i).getRoomPKey()),
                        roomSearchData.getRoomArrayList().get(i).getRoomName()).show();
            }
        });

        ivBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roomListView.setVisibility(View.GONE);
                llSearchRoom.setVisibility(View.VISIBLE);
                updateSearchList();
            }
        });

        ivRoomSearchFlag = layout.findViewById(R.id.ivRoomSearchFlag);
        ivRoomSearchFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (roomListView.getVisibility() == View.GONE)
                    roomListView.setVisibility(View.VISIBLE);
                edFindRoom.setText(null);
                llSearchRoom.setVisibility(View.GONE);

            }
        });

        edFindRoom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                if (text.length() > 0) {
                    btnResetRoom.setVisibility(View.VISIBLE);
                } else {
                    btnResetRoom.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        btnResetRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edFindRoom.setText(null);
                btnResetRoom.setVisibility(View.INVISIBLE);
            }
        });

    }

    public void updateRoom(final MainViewPager.AfterUpdate delegate) {

        Params params = new Params();
        params.add("UserPKey", MainViewPager.getUserPKey() + "");

        new HttpNetwork("GetRoomList.php", params.getParams(), new HttpNetwork.AsyncResponse() {
            @Override
            public void onSuccess(String response) {

                try {
                    ParseData parse = new ParseData();
                    final JSONArray roomList = parse.parseJsonArray(response);
                    roomListFragment.roomData = new RoomData();

                    for (int i = 0; i < roomList.length(); i++) {
                        JSONObject jsonRoomData = new JSONObject(roomList.get(i).toString());
                        roomListFragment.roomData.addItem(jsonRoomData.getString("PKey"), jsonRoomData.getString("Name"), jsonRoomData.getString("Description"), jsonRoomData.getString("ChatCount"), jsonRoomData.getString("FounderName"));

                        Log.d("roomPKey", jsonRoomData.getString("PKey"));
                        Log.d("Name", jsonRoomData.getString("Name"));
                        Log.d("DESC", jsonRoomData.getString("Description"));
                        Log.d("ChatCount", jsonRoomData.getString("ChatCount"));
                        Log.d("FounderName", jsonRoomData.getString("FounderName"));

                    }

                    roomListFragment.roomListView.setAdapter(roomListFragment.roomData.getAdapter());
                    roomListFragment.roomData.getAdapter().notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                delegate.onPost(roomListFragment.roomData);

            }

            @Override
            public void onFailure(String response) {

            }

            @Override
            public void onPreExcute() {

            }
        });
    }

    public void updateSearchList() {

        Params params = new Params();
        params.add("query", edFindRoom.getText().toString());

        new HttpNetwork("SearchRoomList.php", params.getParams(), new HttpNetwork.AsyncResponse() {
            @Override
            public void onSuccess(String response) {
                Log.d("re", response);

                if (response.equals("[]") || response == null) {
                    llSearchRoom.setVisibility(View.GONE);
                    Toast.makeText(BaseActivity.mContext, "검색 결과가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        ParseData parse = new ParseData();
                        JSONArray roomList = parse.parseJsonArray(response);
                        roomSearchData = new RoomData();

                        for (int i = 0; i < roomList.length(); i++) {
                            JSONObject jsonRoomData = new JSONObject(roomList.get(i).toString());
                            //다시 방정보만 뿌려주는걸로 수정, MaxUser 삭제
                            roomSearchData.addItem(jsonRoomData.getString("PKey"), jsonRoomData.getString("Name"), jsonRoomData.getString("Description"), jsonRoomData.getString("FounderName"));
                            Log.d("FounderName", jsonRoomData.getString("FounderName"));
                        }
                        roomSearchListView.setAdapter(roomSearchData.getAdapter());
                        roomSearchData.getAdapter().notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

    @Override
    public void setValues() {

    }

    @Override
    public void bindView() {
        roomListView = layout.findViewById(R.id.roomListView);
        this.searchRoom = (ImageView) layout.findViewById(R.id.ivRoomSearchFlag);
        this.roomSearchListView = (ListView) layout.findViewById(R.id.roomSearchListView);
        this.edFindRoom = (EditText) layout.findViewById(R.id.edFindRoom);
        this.ivBtnSearch = (ImageView) layout.findViewById(R.id.ivBtnSearch);
        this.tvRoomSearchBack = (TextView) layout.findViewById(R.id.tvRoomSearchBack);
        this.llSearchRoom = (LinearLayout) layout.findViewById(R.id.llRooomSearch);
        this.tvChatCount = (TextView) layout.findViewById(R.id.tvChatCount);
        this.btnResetRoom = (ImageView) layout.findViewById(R.id.btnResetRoom);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        //Log.d("TouchTest", "TOuch");
        return false;
    }



}
