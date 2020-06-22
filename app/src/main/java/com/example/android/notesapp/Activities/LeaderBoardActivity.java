package com.example.android.notesapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.ListView;

import com.example.android.notesapp.Adapters.LeaderAdapter;
import com.example.android.notesapp.Adapters.ResultsAdapter;
import com.example.android.notesapp.Classes.upload;
import com.example.android.notesapp.Classes.user;
import com.example.android.notesapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class LeaderBoardActivity extends AppCompatActivity {
    DatabaseReference mDatabaseReference;
    ArrayList<user> arrayOfUsers;
    LeaderAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        arrayOfUsers = new ArrayList<user>();
        ListView listView = (ListView) findViewById(R.id.list_view);
        final LeaderAdapter adapter = new LeaderAdapter(this,R.layout.user_item,arrayOfUsers);
        listView.setAdapter(adapter);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("users");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayOfUsers.clear();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    user upload_ = dataSnapshot1.getValue(user.class);
                    arrayOfUsers.add(upload_);
                }
                Collections.sort(arrayOfUsers, new compareClass());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
