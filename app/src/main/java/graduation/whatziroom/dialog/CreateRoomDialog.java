package graduation.whatziroom.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import graduation.whatziroom.Data.ChatData;
import graduation.whatziroom.Data.RoomData;
import graduation.whatziroom.R;
import graduation.whatziroom.activity.base.BaseActivity;
import graduation.whatziroom.activity.base.BasicMethod;
import graduation.whatziroom.activity.main.MainViewPager;
import graduation.whatziroom.activity.room.RoomViewPager;
import graduation.whatziroom.network.DBSI;
import graduation.whatziroom.network.HttpNetwork;
import graduation.whatziroom.network.Params;

import static graduation.whatziroom.activity.main.MainViewPager.roomListFragment;

/**
 * Created by mapl0 on 2017-09-10.
 */

public class CreateRoomDialog extends Dialog implements BasicMethod {

    private TextView newRoomCancelBtn;
    private TextView newRoomConfirmBtn;
    private EditText edCreateRoomNmae;
    private EditText edCreateRoomMaxUser;
    private EditText edCreateRoomDesc;
    private DatabaseReference databaseReference;

    public CreateRoomDialog(@NonNull Context context, DatabaseReference dbRef) {
        super(context);
        databaseReference = dbRef;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.create_room_dialog);

        bindView();
        setValues();
        setUpEvents();
    }

    @Override
    public void setUpEvents() {
        newRoomConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Params params = new Params();
                DBSI db = new DBSI();
                params.add("PKey", db.selectQuery("select PKey from User")[0][0]);
                params.add("Name", edCreateRoomNmae.getText().toString());
                params.add("Description", edCreateRoomDesc.getText().toString());

                new HttpNetwork("CreateRoom.php", params.getParams(), new HttpNetwork.AsyncResponse() {
                    @Override
                    public void onSuccess(final String response) {

                        Toast.makeText(BaseActivity.mContext, "방 개설 완료", Toast.LENGTH_SHORT).show();
                        RoomViewPager.setRoomPKey(response);
                        roomListFragment.updateRoom(new MainViewPager.AfterUpdate() {
                            @Override
                            public void onPost(RoomData data) {

                                ChatData chat = new ChatData();
                                chat.setRoomPKey(response);
                                RoomViewPager.roomChatFragment.chatList.add(chat);

                                databaseReference.child("Chat").child(response).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Log.d("value.getChildrenCount", dataSnapshot.getValue() + "");
                                        Log.d("value.getChildrenCount", dataSnapshot.getChildren() + "");
                                        Log.d("value.getChildrenCount", dataSnapshot.getChildrenCount() + "");
                                        RoomViewPager.roomChatFragment.chatList.get(RoomViewPager.roomChatFragment.chatList.size()-1).setChatCount(Integer.parseInt(dataSnapshot.getChildrenCount() + ""));
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                databaseReference.child("Chat").child(response).addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        Log.d("add.getChildrenCount", dataSnapshot.getChildrenCount() + "");

                                        ChatData data = dataSnapshot.getValue(ChatData.class);
                                        RoomViewPager.roomChatFragment.chatList.get(RoomViewPager.roomChatFragment.chatList.size()-1).addItem(data);
                                        RoomViewPager.roomChatFragment.chatList.get(RoomViewPager.roomChatFragment.chatList.size()-1).getAdapter().notifyDataSetChanged();

                                        roomListFragment.roomListView.setAdapter(roomListFragment.roomData.getAdapter());
                                        roomListFragment.roomData.getAdapter().notifyDataSetChanged();

                                    }

                                    @Override
                                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                    }

                                    @Override
                                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                                    }

                                    @Override
                                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                        });
                        Intent intent = new Intent(BaseActivity.mContext, RoomViewPager.class);
                        BaseActivity.mContext.startActivity(intent);
                        dismiss();

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

        newRoomCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    public void setValues() {

    }

    @Override
    public void bindView() {

        this.newRoomConfirmBtn = findViewById(R.id.newRoomConfirmBtn);
        this.newRoomCancelBtn = findViewById(R.id.newRoomCancelBtn);
        this.edCreateRoomDesc = findViewById(R.id.edCreateRoomDesc);
        this.edCreateRoomNmae = findViewById(R.id.edCreateRoomNmae);

    }
}
