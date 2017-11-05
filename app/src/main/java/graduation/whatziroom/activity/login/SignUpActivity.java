package graduation.whatziroom.activity.login;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import graduation.whatziroom.R;
import graduation.whatziroom.activity.base.BaseActivity;
import graduation.whatziroom.activity.main.MainViewPager;
import graduation.whatziroom.network.DBSI;
import graduation.whatziroom.network.HttpNetwork;
import graduation.whatziroom.network.Params;
import graduation.whatziroom.util.ParseData;

public class SignUpActivity extends BaseActivity {

    private EditText edSignupID;
    private EditText edSignupName;
    private EditText edSignupEmail;
    private EditText edSignupPW;
    private EditText edSignupCheckPW;
    private TextView tvCancelSignup;
    private TextView tvSaveSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        hideActionBar();

        bindView();
        setValues();
        setUpEvents();

    }

    @Override
    public void setValues() {
        super.setValues();
    }

    @Override
    public void setUpEvents() {
        super.setUpEvents();
        tvSaveSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trySignUp();
            }
        });

        tvCancelSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void trySignUp() {

        new AsyncTask<Void, Void, Void>() {

            String fcbToken;

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Params params = new Params();

                params.add("ID", edSignupID.getText().toString());
                params.add("PW", edSignupPW.getText().toString());
                params.add("CPW", edSignupCheckPW.getText().toString());
                params.add("Name", edSignupName.getText().toString());
                params.add("Email", edSignupEmail.getText().toString());
                params.add("UUID", GetDevicesUUID(mContext));
                params.add("FirebaseToken", FirebaseInstanceId.getInstance().getToken());

//        FirebaseInstanceId.getInstance().deleteToken();
                new HttpNetwork("SignUp.php", params.getParams(), new HttpNetwork.AsyncResponse() {
                    @Override
                    public void onSuccess(String response) {

                        Log.d("res", response);

                        switch (response) {

                            case "duplicateID":
                                Toast.makeText(SignUpActivity.this, "아이디가 중복입니다.", Toast.LENGTH_SHORT).show();
                                break;

                            case "duplicateEmail":
                                Toast.makeText(SignUpActivity.this, "이메일이 중복입니다.", Toast.LENGTH_SHORT).show();
                                break;

                            case "duplicateName":
                                Toast.makeText(SignUpActivity.this, "닉네임이 중복입니다.", Toast.LENGTH_SHORT).show();
                                break;

                            default:

                                ParseData parse = new ParseData();

                                try {
                                    Log.d("res", response);
                                    String userDataString = parse.parseJsonArray(response).get(0).toString();
                                    JSONObject userData = new JSONObject(userDataString);

                                    DBSI db = new DBSI();
                                    db.query("delete from User");
                                    db.query("insert into User values(" + userData.getInt("PKey") + ", '" + userData.getString("Name") + "', '" + userData.getString("ID") + "', '"
                                            + userData.getString("PW") + "', '" + userData.getString("Email") + "', '" + userData.getInt("Status") + "', '"
                                            + userData.getString("Acount") + "', '" + userData.getDouble("Longitude") + "', '" + userData.getDouble("Latitude") + "', '"
                                            + userData.getString("CreatedDate") + "', '" + userData.getString("UpdatedDate") + "', '" + userData.getString("UUID") + "', " + 1 + ", '', '" + userData.getString("FirebaseToken") + "', 0, 0)");

                                    Toast.makeText(SignUpActivity.this, "가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                    finish();
                                    Intent intent = new Intent(getApplicationContext(), MainViewPager.class);
                                    startActivity(intent);


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

            @Override
            protected Void doInBackground(Void... voids) {
                fcbToken = FirebaseInstanceId.getInstance().getToken();
                while (fcbToken == null)//this is used to get firebase token until its null so it will save you from null pointer exeption
                {
                    fcbToken = FirebaseInstanceId.getInstance().getToken();
                }

                return null;
            }
        }.execute();
    }

    @Override
    public void bindView() {
        super.bindView();

        this.tvSaveSignup = findViewById(R.id.tvSaveSignup);
        this.tvCancelSignup = findViewById(R.id.tvCancelSignup);
        this.edSignupCheckPW = findViewById(R.id.edSignupCheckPW);
        this.edSignupPW = findViewById(R.id.edSignupPW);
        this.edSignupEmail = findViewById(R.id.edSignupEmail);
        this.edSignupName = findViewById(R.id.edSignupName);
        this.edSignupID = findViewById(R.id.edSignupID);

    }
}
