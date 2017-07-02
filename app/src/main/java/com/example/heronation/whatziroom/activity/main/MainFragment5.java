package com.example.heronation.whatziroom.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.heronation.whatziroom.R;
import com.example.heronation.whatziroom.activity.ProfilUpdateActivity;

/**
 * Created by ATIV on 2017-06-25.
 */

public class MainFragment5 extends Fragment {
    LinearLayout layout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layout = (LinearLayout) inflater.inflate(R.layout.activity_main_sub_5, container, false);

        TextView editBtn = (TextView)(layout.findViewById(R.id.updateUserInfoBtn));

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfilUpdateActivity.class);
                startActivity(intent);
            }
        });

        return layout;
    }
    
}
