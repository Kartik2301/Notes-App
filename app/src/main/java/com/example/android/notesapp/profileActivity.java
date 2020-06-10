package com.example.android.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class profileActivity extends AppCompatActivity {

    TextView textView, email;
    FirebaseAuth firebaseAuth;
    Button logOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        firebaseAuth = FirebaseAuth.getInstance();
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
        textView.setText(getIntent().getStringExtra("username").toString());
        String e_mail = firebaseAuth.getCurrentUser().getEmail();
        email.setText(e_mail);
    }
}
