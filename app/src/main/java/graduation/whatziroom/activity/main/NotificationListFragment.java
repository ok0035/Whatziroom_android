package graduation.whatziroom.activity.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

import graduation.whatziroom.Data.NoticeData;
import graduation.whatziroom.R;
import graduation.whatziroom.adapter.NoticeAdapter;

import graduation.whatziroom.network.DBSI;
import graduation.whatziroom.network.HttpNetwork;
import graduation.whatziroom.network.Params;
import graduation.whatziroom.util.ParseData;


/**
 * Created by ATIV on 2017-06-25.
 */

public class NotificationListFragment extends Fragment {
    LinearLayout layout;
    // 현재 fragment가 전활될때마다
    // 선언한 NoticeData클래스가 재선언되고있다.
    // DB의 friend테이블에 status를 가져와서 읽는 것으로 바꾸면 문제 해결될 것으로 예상
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layout = (LinearLayout) inflater.inflate(R.layout.notification_list, container, false);
        final ListView listview = (ListView)layout.findViewById(R.id.noticeList);
        Button button = (Button)layout.findViewById(R.id.tempBtn);
        DBSI dbsi = new DBSI();

        Params params = new Params();
        params.add("UserPKey",dbsi.selectQuery("Select PKey From User")[0][0]);

        new HttpNetwork("GetFriendNotification.php", params.getParams(), new HttpNetwork.AsyncResponse() {
            @Override
            public void onSuccess(String response) {
                System.out.println(response);
                ParseData parseData = new ParseData();
                final ArrayList<NoticeData> arrayList = new ArrayList<>();
                try {
                    JSONArray parseArray = parseData.parseJsonArray(response);
                    for(int i = 0 ; i < parseArray.length(); i++){
                        NoticeData noticeData = new NoticeData();
                        noticeData.setUserName(parseArray.getJSONObject(i).getString("Name"));
                        noticeData.setFriendStatus(parseArray.getJSONObject(i).getString("Status"));
                        noticeData.setSrFlag(parseArray.getJSONObject(i).getString("Flag"));
                        arrayList.add(noticeData);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                NoticeAdapter noticeAdapter = new NoticeAdapter(getActivity(), arrayList);
                listview.setAdapter(noticeAdapter);
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
    
}
