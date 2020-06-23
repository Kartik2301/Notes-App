package com.example.android.notesapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.notesapp.Adapters.ResultsAdapter;
import com.example.android.notesapp.Classes.upload;
import com.example.android.notesapp.Constants.Constants;
import com.example.android.notesapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class searchResultsActivity extends AppCompatActivity  {

    DatabaseReference mDatabaseReference;
    TextView textView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        final String search_term = getIntent().getStringExtra("key").toLowerCase();
        ListView listView = (ListView) findViewById(R.id.list_view);
        textView = (TextView) findViewById(R.id.directions1);
        textView.setText("Search Results for:\n" + search_term);
        final ArrayList<upload> data = new ArrayList<>();
        final ArrayList<upload> data1 = new ArrayList<>();
        final ResultsAdapter adapter = new ResultsAdapter(this,R.layout.item_results,data);
        listView.setAdapter(adapter);
        button = (Button) findViewById(R.id.form);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(searchResultsActivity.this,UploadActivity.class);
                startActivity(intent);
            }
        });


        mDatabaseReference = FirebaseDatabase.getInstance().getReference("entries");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data.clear();
                data1.clear();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    upload upload_ = dataSnapshot1.getValue(upload.class);
                    if(upload_.getTitle().toLowerCase().contains(search_term) || search_term.toLowerCase().contains(upload_.getTitle())){
                        if(upload_.type == Constants.IMG_UPLOAD_CATEGORY){
                            data.add(new upload(upload_.getId(),upload_.getTitle(),upload_.getDescription(),upload_.getUrl(),upload_.getType(),upload_.getLikes(),upload_.getDate()));
                            data1.add(new upload(upload_.getId(),upload_.getTitle(),upload_.getDescription(),upload_.getUrl(),upload_.getType(),upload_.getLikes(),upload_.getDate()));
                        } else {
                            data.add(new upload(upload_.getId(),upload_.title,upload_.description,Constants.pdf_icon,upload_.type,upload_.likes,upload_.date));
                            data1.add(new upload(upload_.id,upload_.title,upload_.description,upload_.url,upload_.type,upload_.likes,upload_.date));
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                upload upload_item = data1.get(i);
                if(upload_item.type == Constants.IMG_UPLOAD_CATEGORY){
                    Intent intent = new Intent(searchResultsActivity.this, img_activity.class);
                    intent.putExtra("key",upload_item);
                    startActivity(intent);
                } else {
                }
            }
        });



    }
}
