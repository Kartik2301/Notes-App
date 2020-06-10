package com.example.android.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        editText = (EditText) findViewById(R.id.toSearch);
        editText.setFocusableInTouchMode(true);
        firebaseAuth = FirebaseAuth.getInstance();
        profile = (ImageView) findViewById(R.id.profile);

        logout = (TextView) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
            }
        });

        name = (TextView) findViewById(R.id.your_name);
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
        search = (ImageButton) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();
                //editText.getText().clear();
                if(text.length() == 0){
                    Toast.makeText(getApplicationContext(),"Please enter topic",Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(HomeActivity.this,searchResultsActivity.class);
                    intent.putExtra("key",text);
                    startActivity(intent);
                }

            }
        });
        mStorageReference = FirebaseStorage.getInstance().getReference();

        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        all = (Button) findViewById(R.id.tomain);
        rated = (Button) findViewById(R.id.tomain1);
        newest = (Button) findViewById(R.id.tomain2);
        all.setBackgroundResource(R.drawable.click);
        rated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackgrounds(rated,all,newest);
            }
        });

        newest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackgrounds(newest,all,rated);
            }
        });

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackgrounds(all,rated,newest);
            }
        });

        findViewById(R.id.upload_page).setOnClickListener(this);
        final ArrayList<data_item> arrayOfUsers = new ArrayList<data_item>();
        RecyclerView recyclerView = findViewById(R.id.lvItems);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        final DataAdapter adapter = new DataAdapter(arrayOfUsers);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("entries");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayOfUsers.clear();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    upload upload_ = dataSnapshot1.getValue(upload.class);
                    if(upload_.type == Constants.IMG_UPLOAD_CATEGORY){
                        arrayOfUsers.add(new data_item(upload_.title,upload_.url));
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
                Intent intent = new Intent(HomeActivity.this,UploadActivity.class);
                startActivity(intent);
        }
    }

    public void setBackgrounds(Button clicked, Button non1, Button non2){
         clicked.setBackgroundResource(R.drawable.click);
         non1.setBackgroundResource(R.drawable.shape);
         non2.setBackgroundResource(R.drawable.shape);
    }
}