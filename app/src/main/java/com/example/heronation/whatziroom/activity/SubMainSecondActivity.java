package com.example.heronation.whatziroom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.heronation.whatziroom.R;
import com.example.heronation.whatziroom.activity.room.RoomViewPager;

/**
 * Created by ATIV on 2017-06-25.
 */

public class SubMainSecondActivity extends Fragment {
    LinearLayout layout;
    Button test;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layout = (LinearLayout) inflater.inflate(R.layout.activity_main_sub_2, container, false);

//        test = (Button) layout.findViewById(R.id.btnTest);
//        test.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), RoomViewPager.class);
//                startActivity(intent);
//            }
//        });


        return layout;
    }
    
}
