package com.example.android.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signUpActivity extends AppCompatActivity {

    EditText password, email, username;
    ProgressDialog progressDialog;
    Button btn_up;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        username = (EditText) findViewById(R.id.username);
        firebaseAuth = FirebaseAuth.getInstance();
        btn_up = (Button) findViewById(R.id.btn_up);
        progressDialog = new ProgressDialog(this);
        btn_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = email.getText().toString();
                String Password = password.getText().toString();
                final String Username = username.getText().toString();
                if(TextUtils.isEmpty(Email)){
                    email.setError("Email cannot be empty");
                }
                if(TextUtils.isEmpty(Password)){
                    password.setError("Password must have at least 6 characters");
                }
                if(TextUtils.isEmpty(Username)){
                    username.setError("Username must not be empty");
                }

                progressDialog.setMessage("Registering Please Wait...");
                progressDialog.show();
                firebaseAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(signUpActivity.this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {

                        if (!task.isSuccessful()) {
                            Toast.makeText(signUpActivity.this.getApplicationContext(),
                                    "SignUp unsuccessful: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            databaseReference.child(firebaseAuth.getUid()).setValue(Username);
                            startActivity(new Intent(signUpActivity.this, HomeActivity.class));
                        }
                    }
                });
            }
        });
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
    }
}
