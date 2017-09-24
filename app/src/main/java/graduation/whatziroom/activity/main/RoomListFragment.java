package graduation.whatziroom.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import graduation.whatziroom.Data.RoomData;
import graduation.whatziroom.R;
import graduation.whatziroom.activity.base.BasicMethod;
import graduation.whatziroom.activity.room.RoomViewPager;
import graduation.whatziroom.network.DBSI;
import graduation.whatziroom.network.HttpNetwork;
import graduation.whatziroom.network.Params;
import graduation.whatziroom.util.ParseData;

/**
 * Created by ATIV on 2017-06-25.
 */

public class RoomListFragment extends Fragment implements BasicMethod {

    private LinearLayout layout;
    private ImageView searchBtn;
    private static ListView roomListView;
    private static RoomData roomData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layout = (LinearLayout) inflater.inflate(R.layout.room_list, container, false);

        bindView();
        setUpEvents();

        return layout;
    }

    public RoomListFragment() {

        roomData = new RoomData();
    }

    @Override
    public void setUpEvents() {

//        roomListView.setAdapter(roomData.getAdapter());

        updateRoom();

        roomListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getContext(), RoomViewPager.class);
                intent.putExtra("PKey", roomData.getRoomArrayList().get(position).getRoomPKey().toString());
                startActivity(intent);

            }
        });

        searchBtn = (ImageView) layout.findViewById(R.id.searchRoom);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                try {
                    ParseData parse = new ParseData();
                    JSONArray roomList = parse.parseJsonArray(response);
                    roomData = new RoomData();

                    for(int i=0; i < roomList.length(); i++) {
                        JSONObject jsonRoomData = new JSONObject(roomList.get(i).toString());
                        //채팅이 구현되면 Description 부분에 최근 채팅 내용을 넣어줄 예정
                        roomData.addItem(jsonRoomData.getString("PKey"), jsonRoomData.getString("Name"), jsonRoomData.getString("MaxUser"), jsonRoomData.getString("Description"));
                    }
                    roomListView.setAdapter(roomData.getAdapter());
                    roomData.getAdapter().notifyDataSetChanged();

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
        roomListView = (ListView) layout.findViewById(R.id.roomListView);
    }
}
