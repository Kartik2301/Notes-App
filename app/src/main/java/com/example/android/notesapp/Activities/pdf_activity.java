package com.example.android.notesapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.notesapp.Adapters.CommentAdapter;
import com.example.android.notesapp.Classes.comments;
import com.example.android.notesapp.Classes.upload;
import com.example.android.notesapp.Constants.Constants;
import com.example.android.notesapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class pdf_activity extends AppCompatActivity {

    ImageButton button;
    TextView topic,desc;
    ImageButton like;
    ImageButton addComment;
    final ArrayList<comments> Com = new ArrayList<>();
    ListView l;
    EditText editText;
    upload this_item;
    DatabaseReference mDatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_activity);
        this_item = (upload) getIntent().getSerializableExtra("key");
        button = (ImageButton) findViewById(R.id.btn);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("comments");
        topic = (TextView) findViewById(R.id.topic);
        desc = (TextView) findViewById(R.id.desc);
        l = (ListView) findViewById(R.id.comments);
        topic.setText(this_item.title);
        editText = (EditText) findViewById(R.id.toSearch);

        desc.setText(this_item.description);
        like = (ImageButton) findViewById(R.id.like);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference UpdateReference = FirebaseDatabase.getInstance().getReference("entries").child(this_item.getId());
                upload upload_ = new upload(this_item.getId(), this_item.getTitle(), this_item.getDescription(), this_item.getUrl(),this_item.getType(),this_item.getLikes()+1,this_item.getDate());
                UpdateReference.setValue(upload_);
                Toast.makeText(getApplicationContext(),"Support Noted!!", Toast.LENGTH_SHORT).show();
            }
        });
        addComment = (ImageButton) findViewById(R.id.add_comment);
        final ArrayList<comments> Com = new ArrayList<>();
        final CommentAdapter commentAdapter = new CommentAdapter(this,R.layout.comment_item,Com);
        l.setAdapter(commentAdapter);

        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comments comments_ = new comments(Constants.username,editText.getText().toString(),this_item.getId());
                String id = mDatabaseReference.push().getKey();
                editText.getText().clear();
                mDatabaseReference.child(id).setValue(comments_);

            }
        });

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Com.clear();
                for(DataSnapshot d: dataSnapshot.getChildren()){
                    comments cd = d.getValue(comments.class);
                    if(cd.getId().equals(this_item.getId())){
                        Com.add(new comments(cd.getAuthor(),cd.getContent(),cd.getId()));
                    }
                }
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Com.clear();
                for(DataSnapshot d: dataSnapshot.getChildren()){
                    comments cd = d.getValue(comments.class);
                    if(cd.getId().equals(this_item.getId())){
                        Com.add(new comments(cd.getAuthor(),cd.getContent(),cd.getId()));
                    }
                }
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(this_item.url));
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.another_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {
        case R.id.add:
            displayAlert(Com);
        case R.id.share:
            shareLink();
            return(true);

    }
        return(super.onOptionsItemSelected(item));
    }
    private void displayAlert(ArrayList<comments> d){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(pdf_activity.this);
        LayoutInflater inflater = LayoutInflater.from(pdf_activity.this);
        View view = inflater.inflate(R.layout.custom,null);
        final ListView listView = view.findViewById(R.id.l_v);
        final CommentAdapter commentAdapter1 = new CommentAdapter(this,R.layout.comment_item,d);
        listView.setAdapter(commentAdapter1);
        myDialog.setView(view);
        myDialog.show();
    }
    private void shareLink() {
        Intent shareIntent =   new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT ,this_item.getTitle().toString());
        String app_url = this_item.getUrl();
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,app_url);
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }
}
