package graduation.whatziroom.activity.main;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

import graduation.whatziroom.R;
import graduation.whatziroom.activity.login.LoginActivity;
import graduation.whatziroom.network.DBSI;

import static graduation.whatziroom.activity.main.MainViewPager.CheckLocationTimer;

/**
 * Created by ATIV on 2017-06-25.
 */

public class ProfileFragment extends Fragment {
    LinearLayout layout;

    private android.widget.TextView tvProfileIntro;
    private android.widget.TextView tvProfileID;
    private android.widget.TextView tvProfileName;
    private android.widget.TextView tvProfileEmail;
    private android.widget.EditText edProfileIntro;
    private android.widget.TextView updateUserInfoBtn;
    private android.widget.TextView signoutBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layout = (LinearLayout) inflater.inflate(R.layout.profile_list, container, false);

        this.signoutBtn = (TextView) layout.findViewById(R.id.signoutBtn);
        this.updateUserInfoBtn = (TextView) layout.findViewById(R.id.updateUserInfoBtn);
        this.edProfileIntro = (EditText) layout.findViewById(R.id.edProfileIntro);
        this.tvProfileEmail = (TextView) layout.findViewById(R.id.tvProfileEmail);
        this.tvProfileName = (TextView) layout.findViewById(R.id.tvProfileName);
        this.tvProfileID = (TextView) layout.findViewById(R.id.tvProfileID);
        this.tvProfileIntro = (TextView) layout.findViewById(R.id.tvProfileIntro);

        setUpEvents();

        return layout;
    }

    public void setUpEvents() {

        DBSI db = new DBSI();

        String[][] UserInfo = db.selectQuery("select ID, Name, Email, Message from User");

        tvProfileID.setText(UserInfo[0][0]);
        tvProfileName.setText(UserInfo[0][1]);
        tvProfileEmail.setText(UserInfo[0][2]);
        edProfileIntro.setText(UserInfo[0][3].equals("null") ? "" : UserInfo[0][3]);

        // 내 정보 수정
        updateUserInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfilUpdateActivity.class);
//                Bundle bundle = ActivityOptions.makeCustomAnimation(getContext(),R.anim.anim_slide_out_right,0).toBundle();
//                startActivity(intent,bundle);
                startActivity(intent);
            }
        });

        // 로그아웃
        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(CheckLocationTimer != null) {
                    CheckLocationTimer.cancel();
                    CheckLocationTimer = null;
                }


                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        DBSI db = new DBSI();
                        db.query("delete from User");
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();

                    }

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                    }

                    @Override
                    protected Void doInBackground(Void... voids) {
                        try {
                            FirebaseInstanceId.getInstance().deleteInstanceId();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute();

//                Intent intent = new Intent(getContext(), LoginActivity.class);
//                Bundle bundle = ActivityOptions.makeCustomAnimation(getContext(),0,0).toBundle();
//                startActivity(intent,bundle);

            }
        });



    }
    
}
