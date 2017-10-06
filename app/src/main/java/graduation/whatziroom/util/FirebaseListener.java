package graduation.whatziroom.util;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import graduation.whatziroom.Data.ChatData;

/**
 * Created by mapl0 on 2017-10-06.
 */

public class FirebaseListener  {

    private String roomKey;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private ChatData chatData;
    private ArrayList<ChatData> chatList;

    public static FirebaseListener listener;

    public static FirebaseListener getInstance() {
        if(listener == null) {
            listener = new FirebaseListener();
        }

        return listener;
    }

    public FirebaseListener() {
        chatData = new ChatData();
    }

    public void addChatListener(String roomKey) {

        databaseReference.child("Chat").child(roomKey).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatData data = dataSnapshot.getValue(ChatData.class);
                chatData.addItem(data);
                chatData.getAdapter().notifyDataSetChanged();
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

    public ChatData getChatData() {
        return chatData;
    }

}
