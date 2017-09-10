package com.graduation.project.whatziroom.activity.room;

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

import com.graduation.project.whatziroom.R;

/**
 * Created by ATIV on 2017-06-25.
 */

public class RoomInfoView extends Fragment {
    ScrollView layout;
    private ImageView[] ivAttendee;
    private LinearLayout linAttendee;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layout = (ScrollView) inflater.inflate(R.layout.information, container, false);
        linAttendee = (LinearLayout) layout.findViewById(R.id.linAttendee);

        ivAttendee = new ImageView[5];
         for(int i=0; i<ivAttendee.length; i++ ) {
             ivAttendee[i] = new ImageView(getContext());
             ivAttendee[i].setPadding(0,0,convertDPtoPX(10),0);
             ivAttendee[i].setImageResource(R.mipmap.ic_launcher);
             linAttendee.addView(ivAttendee[i]);
         }


        return layout;
    }

    //dp값을 입력하여 px로 변환하여 반환해줌
    public int convertDPtoPX(int size) {

        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, r.getDisplayMetrics());

        return  px;

    }
    
}
