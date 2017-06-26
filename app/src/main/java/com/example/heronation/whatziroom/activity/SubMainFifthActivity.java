package com.example.heronation.whatziroom.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.heronation.whatziroom.R;

/**
 * Created by ATIV on 2017-06-25.
 */

public class SubMainFifthActivity extends Fragment {
    LinearLayout layout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layout = (LinearLayout) inflater.inflate(R.layout.activity_main_sub_5, container, false);

        return layout;
    }
    
}
