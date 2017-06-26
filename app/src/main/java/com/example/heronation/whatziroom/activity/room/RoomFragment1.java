package com.example.heronation.whatziroom.activity.room;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.example.heronation.whatziroom.R;

/**
 * Created by ATIV on 2017-06-25.
 */

public class RoomFragment1 extends Fragment {
    ScrollView layout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layout = (ScrollView) inflater.inflate(R.layout.activity_room_sub_1, container, false);

        return layout;
    }
    
}
