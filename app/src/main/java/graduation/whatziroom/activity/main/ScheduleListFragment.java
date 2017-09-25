package graduation.whatziroom.activity.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import graduation.whatziroom.Data.ScheduleData;
import graduation.whatziroom.R;
import graduation.whatziroom.activity.base.BasicMethod;
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
        data.addItem("졸업 프로젝트", "용인대학교 환경과학대", "2017년 10월 13일", "D-25");

        scheduleList.setAdapter(data.getAdapter());
        data.getAdapter().notifyDataSetChanged();

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
                            Log.d("SListDate", scheduleDate.getDate() + "");

                            Date nowDate = new Date();
                            nowDate.getDate();

                            data.addItem(jsonScheduleData.getString("Name"), jsonScheduleData.getString("Place"), jsonScheduleData.getString("Date"), String.valueOf(scheduleDate.getDate() - nowDate.getDate()));

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
