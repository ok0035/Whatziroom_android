package graduation.whatziroom.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import graduation.whatziroom.Data.RoomData;
import graduation.whatziroom.R;
import graduation.whatziroom.activity.base.BasicMethod;
import graduation.whatziroom.activity.room.RoomInfoFragment;
import graduation.whatziroom.activity.room.RoomViewPager;
import graduation.whatziroom.network.DBSI;
import graduation.whatziroom.network.HttpNetwork;
import graduation.whatziroom.network.Params;
import graduation.whatziroom.util.ParseData;

/**
 * Created by ATIV on 2017-06-25.
 */

public class RoomListFragment extends Fragment implements BasicMethod, View.OnTouchListener {

    private LinearLayout layout;
    private ImageView searchBtn;
    private static ListView roomListView;
    private static RoomData roomData;
    private RoomData roomSearchData;
    private android.widget.EditText edFindRoom;
    private android.widget.ListView roomSearchListView;
    private android.widget.ImageView searchRoom;
    private ImageView ivBtnSearch;


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

        updateRoom();

        roomListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                Intent intent = new Intent(getContext(), RoomViewPager.class);
                RoomInfoFragment.setRoomPKey(roomData.getRoomArrayList().get(position).getRoomPKey().toString());
                startActivity(intent);

            }
        });

        ivBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roomListView.setVisibility(View.GONE);
                roomSearchListView.setVisibility(View.VISIBLE);
                updateSearchList();
            }
        });

        searchBtn = layout.findViewById(R.id.searchRoom);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(roomListView.getVisibility() == View.GONE) {

                    roomListView.setVisibility(View.VISIBLE);
                    roomSearchListView.setVisibility(View.GONE);

                } else {

                    roomListView.setVisibility(View.GONE);
                    roomSearchListView.setVisibility(View.VISIBLE);
                }


            }
        });

    }

    public static void updateRoom() {

        Params params = new Params();
        DBSI db = new DBSI();
        db.selectQuery("select ID from User");
        params.add("ID", db.selectQuery("select ID from User")[0][0]);

        new HttpNetwork("GetRoomList.php", params.getParams(), new HttpNetwork.AsyncResponse() {
            @Override
            public void onSuccess(String response) {

                if(response.equals("[]")) Toast.makeText(RoomViewPager.mContext, "검색 결과가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                else {

                    try {
                        ParseData parse = new ParseData();
                        JSONArray roomList = parse.parseJsonArray(response);
                        roomData = new RoomData();

                        for(int i=0; i < roomList.length(); i++) {
                            JSONObject jsonRoomData = new JSONObject(roomList.get(i).toString());
                            //채팅이 구현되면 Description 부분에 최근 채팅 내용을 넣어줄 예정
                            //룸정보만 뿌려주고 채팅이 오면 표시해주는 쪽으로 변경해야할듯
                            //검색했을때도 룸데이타를 재활용하기 위해서
                            roomData.addItem(jsonRoomData.getString("PKey"), jsonRoomData.getString("Name"), jsonRoomData.getString("MaxUser"), jsonRoomData.getString("Description"));
                        }
                        roomListView.setAdapter(roomData.getAdapter());
                        roomData.getAdapter().notifyDataSetChanged();

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

    public void updateSearchList() {

        Params params = new Params();
        params.add("query", edFindRoom.getText().toString());

        new HttpNetwork("SearchRoomList.php", params.getParams(), new HttpNetwork.AsyncResponse() {
            @Override
            public void onSuccess(String response) {
                Log.d("re", response);
                try {
                    ParseData parse = new ParseData();
                    JSONArray roomList = parse.parseJsonArray(response);
                    roomSearchData = new RoomData();

                    for(int i=0; i < roomList.length(); i++) {
                        JSONObject jsonRoomData = new JSONObject(roomList.get(i).toString());
                        //채팅이 구현되면 Description 부분에 최근 채팅 내용을 넣어줄 예정
                        roomSearchData.addItem(jsonRoomData.getString("PKey"), jsonRoomData.getString("Name"), jsonRoomData.getString("MaxUser"), jsonRoomData.getString("Description"));
                    }
                    roomSearchListView.setAdapter(roomSearchData.getAdapter());
                    roomSearchData.getAdapter().notifyDataSetChanged();

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

    @Override
    public void setValues() {

    }

    @Override
    public void bindView() {
        roomListView = layout.findViewById(R.id.roomListView);
        this.searchRoom = (ImageView) layout.findViewById(R.id.searchRoom);
        this.roomSearchListView = (ListView) layout.findViewById(R.id.roomSearchListView);
        this.edFindRoom = (EditText) layout.findViewById(R.id.edFindRoom);
        this.ivBtnSearch = (ImageView) layout.findViewById(R.id.ivBtnSearch);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        Log.d("TouchTest", "TOuch");
        return false;
    }
}
