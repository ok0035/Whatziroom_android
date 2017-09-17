package com.graduation.project.whatziroom.activity.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.graduation.project.whatziroom.Data.ScheduleData;
import com.graduation.project.whatziroom.R;
import com.graduation.project.whatziroom.activity.base.BasicMethod;
import com.graduation.project.whatziroom.network.HttpNetwork;
import com.graduation.project.whatziroom.network.Params;
import com.graduation.project.whatziroom.util.ParseData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ATIV on 2017-06-25.
 */

public class ScheduleListView extends Fragment implements BasicMethod{

    private LinearLayout layout;
    private static ScheduleData data;
    private static ListView list;

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
        data.addItem("졸업 프로젝트", "용인대학교 환경과학대", "2017년 10월 13일", "D-25");

        list.setAdapter(data.getAdapter());
        data.getAdapter().notifyDataSetChanged();

    }

    public static void updateSchedule() {

        Params params = new Params();
        new HttpNetwork("GetScheduleList.php", params.getParams(), new HttpNetwork.AsyncResponse() {
            @Override
            public void onSuccess(String response) {

                Log.d("response", response);

                if(!response.equals("[]")) {

                    try {
                        ParseData parse = new ParseData();
                        JSONArray roomList = parse.parseJsonArray(response);
                        data = new ScheduleData();

                        for(int i=0; i < roomList.length(); i++) {

                            JSONObject jsonScheduleData = new JSONObject(roomList.get(i).toString());
                            data.addItem(jsonScheduleData.getString("Name"), jsonScheduleData.getString("Place"), jsonScheduleData.getString("Date"), "5");

                        }

                        list.setAdapter(data.getAdapter());
                        data.getAdapter().notifyDataSetChanged();

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
        list = layout.findViewById(R.id.scheduleListView);
    }
}
