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

import java.util.ArrayList;

import graduation.whatziroom.Data.NoticeData;
import graduation.whatziroom.R;
import graduation.whatziroom.adapter.NoticeAdapter;

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
        ListView listview = layout.findViewById(R.id.noticeList);
        Button button = layout.findViewById(R.id.tempBtn);

        NoticeData noticeData1 = new NoticeData();
        noticeData1.setUserName("최현승");
        noticeData1.setUserText("친추 부탁드려요");
//        noticeData1.setCheckFlag(0);

        NoticeData noticeData2 = new NoticeData();
        noticeData2.setUserName("이주한");
        noticeData2.setUserText("친구 추가 신청합니다.");
//        noticeData2.setCheckFlag(2);

        NoticeData noticeData3 = new NoticeData();
        noticeData3.setUserName("KKK");
        noticeData3.setUserText("ADSF");
//        noticeData3.setCheckFlag(0);

        final ArrayList<NoticeData> arrayList = new ArrayList<>();
        arrayList.add(noticeData1);
        arrayList.add(noticeData2);
        arrayList.add(noticeData3);

        NoticeAdapter noticeAdapter = new NoticeAdapter(getActivity(), arrayList);
        listview.setAdapter(noticeAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < arrayList.size(); i++){
                    arrayList.get(i).setCheckFlag(0);
                }
            }
        });

        return layout;
    }
    
}
