package graduation.whatziroom.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import graduation.whatziroom.R;
import graduation.whatziroom.activity.base.BaseActivity;
import graduation.whatziroom.activity.main.MainViewPager;
import graduation.whatziroom.network.DBSI;
import graduation.whatziroom.network.HttpNetwork;
import graduation.whatziroom.network.Params;
import graduation.whatziroom.util.ParseData;

/**
 * Created by heronation on 2017-05-22.
 */

public class LoginActivity extends BaseActivity {

    private TextView login_btn;
    private ImageView imgmypage;
    private TextView tvloginid;
    private EditText edloginid;
    private LinearLayout layoutid;
    private EditText edloginpw;
    private TextView btnlogin;
    private TextView btnguest;
    private TextView btnsignup;
    private TextView tvforget;
    private CheckBox chAutoCheckBox;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        bindView();
        setValues();
        setUpEvents();

    }

    @Override
    public void setUpEvents() {
        super.setUpEvents();
        hideActionBar();


        DBSI db = new DBSI();

        if (chAutoCheckBox.isChecked()) {
            db.query("update User set AutoLogin = 1");
        } else {
            db.query("update User set AutoLogin = 0");
        }


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Params params = new Params();

                params.add("ID", edloginid.getText().toString());
                params.add("PW", edloginpw.getText().toString());

                new HttpNetwork("Login.php", params.getParams(), new HttpNetwork.AsyncResponse() {
                    @Override
                    public void onSuccess(String response) {
                        switch (response) {

                            case "fail":

                                Toast.makeText(LoginActivity.this, "아이디 또는 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                                break;

                            default:

                                ParseData parse = new ParseData();

                                try {
                                    Log.d("res", response);
                                    String userDataString = parse.parseJsonArray(response).get(0).toString();
                                    JSONObject userData = new JSONObject(userDataString);

                                    int isAutoLogin = 0;
                                    if(chAutoCheckBox.isChecked())  isAutoLogin = 1;
                                    else isAutoLogin = 0;

                                    DBSI db = new DBSI();
                                    db.query("delete from User;");
                                    db.query("insert into User values(" + userData.getInt("PKey") + ", '" + userData.getString("Name") + "', '" + userData.getString("ID") + "', '"
                                            + userData.getString("PW") + "', '" + userData.getString("Email") + "', '" + userData.getInt("Status") + "', '"
                                            + userData.getString("Acount") + "', '" + userData.getDouble("Longitude") + "', '" + userData.getDouble("Latitude") + "', '"
                                            + userData.getString("CreatedDate") + "', '" + userData.getString("UpdatedDate") + "', '" + userData.getString("UDID") + "', " + isAutoLogin + ")");


                                    Toast.makeText(LoginActivity.this, "로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                                    Intent successIntent = new Intent(getApplicationContext(), MainViewPager.class);
                                    startActivity(successIntent);
                                    finish();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                break;

                        }
                    }

                    @Override
                    public void onFailure(String response) {

                    }

                    @Override
                    public void onPreExcute() {

                    }
                });

            }
        });

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        tvforget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "비밀번호찾기 준비중입니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void setValues() {
        super.setValues();
    }

    @Override
    public void bindView() {
        super.bindView();

        this.tvforget = (TextView) findViewById(R.id.tv_forget);
        this.btnsignup = (TextView) findViewById(R.id.btn_signup);
        this.btnlogin = (TextView) findViewById(R.id.btn_login);
        this.chAutoCheckBox = (CheckBox) findViewById(R.id.chAutoCheckBox);
        this.edloginpw = (EditText) findViewById(R.id.ed_login_pw);
        this.layoutid = (LinearLayout) findViewById(R.id.layout_id);
        this.edloginid = (EditText) findViewById(R.id.ed_login_id);
        this.tvloginid = (TextView) findViewById(R.id.tv_login_id);
        this.imgmypage = (ImageView) findViewById(R.id.img_mypage);


    }


}