package com.example.ydachating;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatingActivity extends AppCompatActivity {
    Button btnSendMsg;
    EditText etMsg;

    ListView lvDiskusi;
    ArrayList<String>listConversation = new ArrayList<String>();
    ArrayAdapter arrayAdpt;

    String UserName, pilih_topik, user_msg_key;
    private DatabaseReference dbr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chating);

        btnSendMsg = (Button) findViewById(R.id.btnSendMsg);
        etMsg = (EditText) findViewById(R.id.etMessage);

        lvDiskusi = (ListView) findViewById(R.id.lvChating);
        arrayAdpt = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listConversation);
        lvDiskusi.setAdapter(arrayAdpt);

        UserName = getIntent().getExtras().get("user_nama").toString();
        pilih_topik = getIntent().getExtras().get("pilih_topik").toString();
        setTitle("Topik : " + pilih_topik);


        dbr = FirebaseDatabase.getInstance().getReference().child(pilih_topik);

        btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> map = new HashMap<String,Object>();
                user_msg_key = dbr.push().getKey();
                dbr.updateChildren(map);

                DatabaseReference dbr2= dbr.child(user_msg_key);
                Map<String,Object> map2 = new HashMap<String, Object>();
                map2.put("msg",etMsg.getText().toString());
                map2.put("user",UserName);
                dbr2.updateChildren(map2);

            }
        });

        dbr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                updateConversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                updateConversation(dataSnapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    public void updateConversation(DataSnapshot dataSnapshot){
        String msg,user,conversation;
        Iterator i = dataSnapshot.getChildren().iterator();
        while (i.hasNext()){
            msg = (String) ((DataSnapshot)i.next()).getValue();
            user = (String) ((DataSnapshot)i.next()).getValue();

            conversation = user +": " + msg;
            arrayAdpt.insert(conversation, 0);
            arrayAdpt.notifyDataSetChanged();
        }
    }
}
