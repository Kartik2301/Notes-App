package com.example.android.notesapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.notesapp.Classes.SortingClass;
import com.example.android.notesapp.Classes.upload;
import com.example.android.notesapp.Classes.user;
import com.example.android.notesapp.Constants.Constants;
import com.example.android.notesapp.R;
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
import java.util.Collections;
import java.util.Comparator;

public class profileActivity extends AppCompatActivity {

    TextView textView, email;
    FirebaseAuth firebaseAuth;
    Button logOut;
    ImageView dp;
    TextView rank, points, lead;
    DatabaseReference mDatabaseReference;
    ArrayList<user> arrayOfUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final user user_content = (user) getIntent().getSerializableExtra("user_object");
        arrayOfUsers = new ArrayList<user>();
        points = (TextView) findViewById(R.id.points);
        points.setText(Integer.toString(Constants.points)+" ");
        firebaseAuth = FirebaseAuth.getInstance();
        lead = (TextView) findViewById(R.id.open_leader);
        lead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(profileActivity.this, LeaderBoardActivity.class));
            }
        });
        dp = (ImageView) findViewById(R.id.dp);
        Glide.with(dp)
                .load(Constants.default_img_url)
                .into(dp);
        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), Constants.IMG_UPLOAD_CATEGORY);
            }
        });
        logOut = (Button) findViewById(R.id.log_out);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(profileActivity.this,MainActivity.class));
            }
        });
        email = (TextView) findViewById(R.id.user_email);
        textView = (TextView) findViewById(R.id.your_name);
        textView.setText(Constants.username);
        String e_mail = firebaseAuth.getCurrentUser().getEmail();
        email.setText(e_mail);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.IMG_UPLOAD_CATEGORY && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                if(filePath != null){
                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setTitle("Uploading");
                    progressDialog.show();

                    final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("user_images").child(filePath.getLastPathSegment());
                    storageReference.putFile(filePath)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.dismiss();
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String url_img = uri.toString();
                                            Constants.default_img_url = url_img;
                                            Glide.with(dp)
                                                    .load(Constants.default_img_url)
                                                    .into(dp);
                                            DatabaseReference UpdateReference = FirebaseDatabase.getInstance().getReference("users").child(Constants.id);
                                            user new_user = new user(Constants.username,Constants.points, Constants.rank,url_img);
                                            UpdateReference.setValue(new_user);
                                        }
                                    });

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                    progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                                }
                            });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saved_items :
                startActivity(new Intent(profileActivity.this, SavedItems.class));
                return true;
            case R.id.notes :
                startActivity(new Intent(profileActivity.this, KeepNotes.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

class compareClass implements Comparator<user> {

    @Override
    public int compare(user a, user b) {
        return a.getPoints() - b.getPoints();
    }
}