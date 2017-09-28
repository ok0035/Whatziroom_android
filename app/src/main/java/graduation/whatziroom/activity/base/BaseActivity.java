package graduation.whatziroom.activity.base;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.security.MessageDigest;

import graduation.whatziroom.R;

/**
 * Created by heronation on 2017-05-22.
 */

public class BaseActivity extends AppCompatActivity {

    public static Context mContext = null;

    //메인 액션바
    public ImageView backBtn;
    public TextView titleTxt;
    public ImageView configTxt1;
    public ImageView configTxt2;

    //룸 액션바
    public TextView btnRoomExit;
    public TextView btnRoomSchedule;
    public ImageView btnRoomSetting;

    private static Resources r;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

    }

    public void setUpEvents() {
        //TODO - 이벤트 처리
        getAppKeyHash();
    }

    public void setValues() {
        //TODO  - Set 설정

    }

    public void bindView() {
        //TODO - 레이아웃 ID초기화
    }

    // 액션바 숨김
    public void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.hide();
        }
    }

    public void setMainActionBar() {

        ActionBar myActionBar = getSupportActionBar();
        myActionBar.setDisplayShowHomeEnabled(false);
        myActionBar.setDisplayHomeAsUpEnabled(false);
        myActionBar.setDisplayShowTitleEnabled(false);
        myActionBar.setDisplayShowCustomEnabled(true);
        myActionBar.setHomeButtonEnabled(true);

//        myActionBar.setHomeAsUpIndicator(R.mipmap.hambutton);

        LayoutInflater inf = LayoutInflater.from(mContext);
        View customBarView = inf.inflate(R.layout.actionbar_main, null);

        myActionBar.setCustomView(customBarView);
        myActionBar.setDisplayShowCustomEnabled(true);

        Toolbar parent = (Toolbar) customBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        getSupportActionBar().setElevation(0); // 액션바 밑에 그림자 없애는거,,


        backBtn = customBarView.findViewById(R.id.backBtn);
        titleTxt = customBarView.findViewById(R.id.titleTxt);
        configTxt1 = customBarView.findViewById(R.id.configTxt1);
        configTxt2 = customBarView.findViewById(R.id.configTxt2);

    }



    //dp값을 입력하여 px로 변환하여 반환해줌
    public static int convertDPtoPX(int size) {
        r = mContext.getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, r.getDisplayMetrics());

        return  px;

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key", something);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }

}
