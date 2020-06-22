package com.example.android.notesapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.notesapp.Adapters.DataAdapter;
import com.example.android.notesapp.Classes.SortingClass;
import com.example.android.notesapp.Classes.upload;
import com.example.android.notesapp.Classes.user;
import com.example.android.notesapp.Constants.Constants;
import com.example.android.notesapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    final static int PICK_PDF_CODE = 2342;

    TextView textViewStatus;
    EditText editTextFilename;
    ProgressBar progressBar;
    EditText editText;
    Button uploadPage;
    TextView logout;
    ImageView profile;
    TextView name;
    StorageReference mStorageReference;
    DatabaseReference mDatabaseReference;
    Button all, newest, rated;
    ImageButton search;
    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    ArrayList<upload> dup_list;
    ArrayList<upload> arrayOfUsers;
    DataAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        editText = (EditText) findViewById(R.id.toSearch);
        editText.setFocusableInTouchMode(true);
        name = (TextView) findViewById(R.id.your_name);
        search = (ImageButton) findViewById(R.id.search);
        all = (Button) findViewById(R.id.tomain);
        rated = (Button) findViewById(R.id.tomain1);
        newest = (Button) findViewById(R.id.tomain2);
        mStorageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        profile = (ImageView) findViewById(R.id.profile);
        dup_list = new ArrayList<>();
        logout = (TextView) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
            }
        });


        final DatabaseReference m = FirebaseDatabase.getInstance().getReference("users");
        m.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Constants.id = firebaseAuth.getUid().toString();
                user new_user = dataSnapshot.child(Constants.id).getValue(user.class);
                Constants.username = new_user.getUsername().toString();
                Constants.points = new_user.getPoints();
                Constants.rank = new_user.getRank();
                Constants.default_img_url = new_user.getImageUrl();
                Glide.with(profile)
                        .load(Constants.default_img_url).into(profile);
                name.setText(Constants.username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, profileActivity.class);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();
                //editText.getText().clear();
                if(text.length() == 0){
                    Toast.makeText(getApplicationContext(),"Please enter topic",Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(HomeActivity.this, searchResultsActivity.class);
                    intent.putExtra("key",text);
                    startActivity(intent);
                }

            }
        });


        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        all.setBackgroundResource(R.drawable.click);


        newest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackgrounds(newest,all,rated);
                SortingClass sortingClass = new SortingClass(dup_list);
                ArrayList<upload> temp = sortingClass.sortByDate();
                prepare_view(temp);
            }
        });

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackgrounds(all,rated,newest);
                prepare_view(arrayOfUsers);
            }
        });

        rated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackgrounds(rated,all,newest);
                SortingClass sortingClass = new SortingClass(dup_list);
                ArrayList<upload> temp = sortingClass.sortByLikes();
                prepare_view(temp);
            }
        });
        findViewById(R.id.upload_page).setOnClickListener(this);
        arrayOfUsers = new ArrayList<upload>();
        recyclerView = findViewById(R.id.lvItems);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        prepare_view(arrayOfUsers);
        getData();

    }

    @Override
    protected void onResume() {
        super.onResume();
        editText.clearFocus();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.upload_page :
                Intent intent = new Intent(HomeActivity.this, UploadActivity.class);
                startActivity(intent);
        }
    }

    public void setBackgrounds(Button clicked, Button non1, Button non2){
         clicked.setBackgroundResource(R.drawable.click);
         non1.setBackgroundResource(R.drawable.shape);
         non2.setBackgroundResource(R.drawable.shape);
    }

    public void getData() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("entries");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayOfUsers.clear();
                dup_list.clear();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    upload upload_ = dataSnapshot1.getValue(upload.class);
                    if(upload_.type == Constants.IMG_UPLOAD_CATEGORY){
                        arrayOfUsers.add(upload_);
                        dup_list.add(upload_);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void prepare_view(ArrayList<upload> show) {
        adapter  = new DataAdapter(show);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
}

