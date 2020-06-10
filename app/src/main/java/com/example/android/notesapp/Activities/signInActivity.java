package com.example.android.notesapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.android.notesapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class signInActivity extends AppCompatActivity {
    EditText password, email, username;
    ProgressDialog progressDialog;
    Button btn_up;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
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
                if(TextUtils.isEmpty(Email)){
                    email.setError("Email cannot be empty");
                }
                if(TextUtils.isEmpty(Password)){
                    password.setError("Password must have at least 6 characters");
                }
                progressDialog.setMessage("Logging you in, Please Wait...");
                progressDialog.show();
                firebaseAuth.signInWithEmailAndPassword(Email, Password)
                        .addOnCompleteListener(signInActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if(task.isSuccessful()){
                                    finish();
                                    startActivity(new Intent(signInActivity.this, HomeActivity.class));
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
