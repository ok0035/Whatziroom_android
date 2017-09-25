package graduation.whatziroom.activity.main;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import graduation.whatziroom.R;
import graduation.whatziroom.activity.login.LoginActivity;

import static graduation.whatziroom.R.anim.anim_slide_in_left;

/**
 * Created by ATIV on 2017-06-25.
 */

public class ProfileFragment extends Fragment {
    LinearLayout layout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layout = (LinearLayout) inflater.inflate(R.layout.profile_list, container, false);

        TextView editBtn = (TextView)(layout.findViewById(R.id.updateUserInfoBtn));
        TextView signOutBtn = (TextView)(layout.findViewById(R.id.signoutBtn));

        // 내 정보 수정
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfilUpdateActivity.class);
//                Bundle bundle = ActivityOptions.makeCustomAnimation(getContext(),R.anim.anim_slide_out_right,0).toBundle();
//                startActivity(intent,bundle);
                startActivity(intent);
            }
        });

        // 로그아웃
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                Bundle bundle = ActivityOptions.makeCustomAnimation(getContext(),0,0).toBundle();
                startActivity(intent,bundle);
            }
        });

        return layout;
    }
    
}