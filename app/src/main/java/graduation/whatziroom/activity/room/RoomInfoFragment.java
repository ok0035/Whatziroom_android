package graduation.whatziroom.activity.room;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import graduation.whatziroom.R;


/**
 * Created by ATIV on 2017-06-25.
 */

public class RoomInfoFragment extends Fragment {
    View layout;
    private ImageView[] ivAttendee;
    private LinearLayout linAttendee;
    private String isEmpty;
    private String result = "notEmpty";

//    private ProgressDialog mProgressDialog;

    public String getIsEmpty() {
        return isEmpty;
    }

    public void setIsEmpty(String isEmpty) {
        this.isEmpty = isEmpty;
    }

    public RoomInfoFragment() {
        super();


    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        switch (getIsEmpty()) {
            case "empty":
                layout = (LinearLayout) inflater.inflate(R.layout.no_schedule, container, false);
                break;

            case "notEmpty":
                layout = (ScrollView) inflater.inflate(R.layout.room_information, container, false);
                linAttendee = (LinearLayout) layout.findViewById(R.id.linAttendee);

                break;
        }

        return layout;
    }

    //dp값을 입력하여 px로 변환하여 반환해줌
    public int convertDPtoPX(int size) {

        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, r.getDisplayMetrics());

        return px;

    }

}
