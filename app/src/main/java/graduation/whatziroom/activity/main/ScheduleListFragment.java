package graduation.whatziroom.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import graduation.whatziroom.Data.ScheduleData;
import graduation.whatziroom.R;
import graduation.whatziroom.activity.base.BasicMethod;
import graduation.whatziroom.activity.room.RoomViewPager;
import graduation.whatziroom.network.HttpNetwork;
import graduation.whatziroom.network.Params;
import graduation.whatziroom.util.ParseData;

/**
 * Created by ATIV on 2017-06-25.
 */

public class ScheduleListFragment extends Fragment implements BasicMethod {

    private LinearLayout layout;
    private static ListView scheduleList;
    private static ScheduleData data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layout = (LinearLayout) inflater.inflate(R.layout.schedule_list, container, false);

        bindView();
        setValues();
        setUpEvents();

        return layout;
    }

    @Override
    public void setUpEvents() {

        updateSchedule();

        data = new ScheduleData();

        scheduleList.setAdapter(data.getAdapter());
        data.getAdapter().notifyDataSetChanged();

        scheduleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                RoomListFragment.setRoomPKey(data.getScheduleList().get(i).getScheduleRoomPKey());

                Log.d("RoomPKeyInSchedule", RoomListFragment.getRoomPKey() + "");

                if(RoomListFragment.getRoomPKey() != 0) {

                    Intent intent = new Intent(getContext(), RoomViewPager.class);
                    startActivity(intent);

                } else Toast.makeText(getContext(), "잘못된 데이터입니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static void updateSchedule() {

        Params params = new Params();
        params.add("UserPKey", MainViewPager.getUserPKey() + "");

        new HttpNetwork("GetScheduleList.php", params.getParams(), new HttpNetwork.AsyncResponse() {
            @Override
            public void onSuccess(String response) {

                Log.d("Schedule", response);

                if (!response.equals("[]")) {

                    try {
                        ParseData parse = new ParseData();
                        JSONArray roomList = parse.parseJsonArray(response);
                        data = new ScheduleData();

                        for (int i = 0; i < roomList.length(); i++) {

                            JSONObject jsonScheduleData = new JSONObject(roomList.get(i).toString());

                            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date scheduleDate = transFormat.parse(jsonScheduleData.getString("Date"));

                            Calendar CalculateDate= Calendar.getInstance();

                            CalculateDate.setTime(scheduleDate);
                            long goalTime = CalculateDate.getTimeInMillis();

                            CalculateDate.setTime(Calendar.getInstance().getTime());
                            long nowTime = CalculateDate.getTimeInMillis();

                            long dDay = (goalTime - nowTime);

                            //밀리세컨드를 밀리초, 초, 분, 시간 으로 나눔
                            dDay = dDay / 1000 / 60 / 60 / 24;

                            Log.d("SListDate", scheduleDate.getDate() + "");

                            Date nowDate = new Date();
                            nowDate.getDate();

                            data.addItem(jsonScheduleData.getString("RoomPKey"), jsonScheduleData.getString("Name"), jsonScheduleData.getString("Place"), jsonScheduleData.getString("Date"), String.valueOf(dDay));

                        }

                        scheduleList.setAdapter(data.getAdapter());
                        data.getAdapter().notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();

                    } catch (ParseException e) {
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

        scheduleList = layout.findViewById(R.id.scheduleListView);

    }
}
