package com.example.ydachating;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    ListView lvYdaChating;
    ArrayList<String> listofChating = new ArrayList<String>();
    ArrayAdapter arrayAdpt;

    String UserName;

    private DatabaseReference dbr = FirebaseDatabase.getInstance().getReference().getRoot();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        lvYdaChating = (ListView) findViewById(R.id.lvYdaChating);
        arrayAdpt = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, listofChating);
        lvYdaChating.setAdapter(arrayAdpt);


        getUserName();

        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();

                while (i.hasNext()){
                    set.add(((DataSnapshot)i.next()).getKey());
                }

                arrayAdpt.clear();
                arrayAdpt.addAll(set);
                arrayAdpt.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        lvYdaChating.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int possotion, long id) {
                Intent i = new Intent(getApplicationContext(), ChatingActivity.class);
                i.putExtra("pilih_topik",((TextView)view).getText().toString());
                i.putExtra("user_nama",UserName);
                startActivity(i);
            }
        });
    }

    private void getUserName(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText userName = new EditText(this);

        builder.setView(userName);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                UserName = userName.getText().toString();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                getUserName();
            }
        });
        builder.show();
    }
}
