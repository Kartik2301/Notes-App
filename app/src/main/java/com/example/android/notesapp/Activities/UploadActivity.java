package com.example.android.notesapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.android.notesapp.Classes.upload;
import com.example.android.notesapp.Classes.user;
import com.example.android.notesapp.Constants.Constants;
import com.example.android.notesapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

public class UploadActivity extends AppCompatActivity {


    private Uri filePath;
    Button add_submission;
    EditText title, description;
    ImageButton pdf, img;
    int is_img_upload = 0;
    int is_pdf_upload = 0;
    String url_img = "";
    String url_pdf = "";
    DatabaseReference mDatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        pdf = (ImageButton) findViewById(R.id.pdf_add);
        img = (ImageButton) findViewById(R.id.img_add);
        title = (EditText) findViewById(R.id.topic);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("entries");
        description = (EditText) findViewById(R.id.desc);
        add_submission = (Button) findViewById(R.id.upload_page);
        add_submission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String topic = title.getText().toString();
                String desc = description.getText().toString();
                if(is_img_upload == 1){
                    String id = mDatabaseReference.push().getKey();
                    String date = DateFormat.getDateInstance().format(new Date());
                    upload item = new upload(id,topic,desc,url_img, Constants.IMG_UPLOAD_CATEGORY,0,date);
                    mDatabaseReference.child(id).setValue(item);
                } else if(is_pdf_upload == 1){
                    String id = mDatabaseReference.push().getKey();
                    String date = DateFormat.getDateInstance().format(new Date());
                    upload item = new upload(id,topic,desc,url_pdf,Constants.PDF_UPLOAD_CATEGORY,0,date);
                    mDatabaseReference.child(id).setValue(item);
                }
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
                user updated_user = new user(Constants.username,Constants.points+10,Constants.rank,Constants.default_img_url);
                databaseReference.child(Constants.id).setValue(updated_user);
                title.getText().clear();
                description.getText().clear();
                Toast.makeText(getApplicationContext(),"Thanks for your help", Toast.LENGTH_SHORT).show();
            }
        });
        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                    return;
                }

                //creating an intent for file chooser
                is_pdf_upload = 1;
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.PDF_UPLOAD_CATEGORY);

            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                is_img_upload = 1;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.IMG_UPLOAD_CATEGORY);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.IMG_UPLOAD_CATEGORY && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                if(filePath != null){
                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setTitle("Uploading");
                    progressDialog.show();

                    final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images").child(filePath.getLastPathSegment());
                    storageReference.putFile(filePath)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.dismiss();
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            url_img = uri.toString();
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
        } else if(requestCode == Constants.PDF_UPLOAD_CATEGORY && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri pdf_file = data.getData();
            if(pdf_file != null){
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading");
                progressDialog.show();
                final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("uploads").child(System.currentTimeMillis() + ".pdf");
                storageReference.putFile(pdf_file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                url_pdf = uri.toString();
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
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {
        case R.id.add:
            return(true);
        case R.id.about:
            new AlertDialog.Builder(this)
                    .setTitle("Info on adding any content")
                    .setMessage(Constants.info)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Continue with delete operation
                        }
                    })
                    .show();
            Toast.makeText(getApplicationContext(),"hello",Toast.LENGTH_SHORT).show();
            return(true);

    }
        return(super.onOptionsItemSelected(item));
    }
}

