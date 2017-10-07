package graduation.whatziroom.activity.room;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import graduation.whatziroom.Data.RoomUserData;
import graduation.whatziroom.R;
import graduation.whatziroom.activity.base.BaseActivity;
import graduation.whatziroom.activity.base.BasicMethod;
import graduation.whatziroom.adapter.RequestUserAdapter;
import graduation.whatziroom.adapter.RoomUserAdapter;
import graduation.whatziroom.dialog.ExitRoomDialog;
import graduation.whatziroom.network.HttpNetwork;
import graduation.whatziroom.network.Params;
import graduation.whatziroom.util.ParseData;


/**
 * Created by ATIV on 2017-06-25.
 */

public class RoomUserList extends Fragment implements BasicMethod{

    private LinearLayout parent;
    private static android.widget.ListView lvRequestUserList;
    private static android.widget.ListView lvRoomUserList;
    private android.widget.TextView tvRoomExit;
    private static RoomUserData roomUserData;
    private static RoomUserData requestUserData;
    private static RequestUserAdapter requestUserAdapter;
    private static RoomUserAdapter roomUserAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        parent = (LinearLayout) inflater.inflate(R.layout.room_user_list, container, false);

        bindView();
        setValues();
        setUpEvents();

        return parent;
    }

    @Override
    public void setUpEvents() {

        updateRequestList();
        updateRoomUserList();

        tvRoomExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ExitRoomDialog(BaseActivity.mContext).show();
            }
        });

    }

    public static void updateRequestList() {

        Params params = new Params();
        params.add("RoomPKey", RoomViewPager.getRoomPKey() + "");

        new HttpNetwork("GetRequestRoomUser.php", params.getParams(), new HttpNetwork.AsyncResponse() {
            @Override
            public void onSuccess(String response) {

                Log.d("RequestRoomUser", response);

                try {
                    ParseData parse = new ParseData();
                    JSONArray requestUserList = parse.parseJsonArray(response);
                    requestUserData = new RoomUserData();

                    for (int i = 0; i < requestUserList.length(); i++) {
                        JSONObject jsonRoomData = new JSONObject(requestUserList.get(i).toString());
                        requestUserData.addItem(Integer.parseInt(jsonRoomData.getString("UserPKey")), Integer.parseInt(jsonRoomData.getString("RoomPKey")), jsonRoomData.getString("Name"));

                        Log.d("RequestUserPKey", jsonRoomData.getString("UserPKey"));
                        Log.d("RequestUserRoomKey", jsonRoomData.getString("RoomPKey"));
                        Log.d("RequestUserName", jsonRoomData.getString("Name"));
                    }

                    requestUserAdapter = new RequestUserAdapter(RoomViewPager.mContext, requestUserData.getUserList());
                    lvRequestUserList.setAdapter(requestUserAdapter);
                    requestUserAdapter.notifyDataSetChanged();

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

    public static void updateRoomUserList() {

        Params params = new Params();
        params.add("RoomPKey", RoomViewPager.getRoomPKey() + "");

        new HttpNetwork("GetRoomUser.php", params.getParams(), new HttpNetwork.AsyncResponse() {
            @Override
            public void onSuccess(String response) {

                Log.d("RoomUser", response);

                try {
                    ParseData parse = new ParseData();
                    JSONArray roomUserList = parse.parseJsonArray(response);
                    roomUserData = new RoomUserData();

                    for (int i = 0; i < roomUserList.length(); i++) {
                        JSONObject jsonRoomData = new JSONObject(roomUserList.get(i).toString());
                        roomUserData.addItem(Integer.parseInt(jsonRoomData.getString("UserPKey")), Integer.parseInt(jsonRoomData.getString("RoomPKey")), jsonRoomData.getString("Name"));
                        Log.d("RoomUserPKey", jsonRoomData.getString("UserPKey"));
                        Log.d("RoomUserRoomKey", jsonRoomData.getString("RoomPKey"));
                        Log.d("RoomUserName", jsonRoomData.getString("Name"));
                    }

                    roomUserAdapter = new RoomUserAdapter(RoomViewPager.mContext, roomUserData.getUserList());
                    lvRoomUserList.setAdapter(roomUserAdapter);
                    roomUserAdapter.notifyDataSetChanged();

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

        this.lvRoomUserList = (ListView) parent.findViewById(R.id.lvRoomUserList);
        this.lvRequestUserList = (ListView) parent.findViewById(R.id.lvRequestUserList);
        this.tvRoomExit = (TextView) parent.findViewById(R.id.tvRoomExit);

    }
}
