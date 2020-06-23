package com.example.android.notesapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.notesapp.Adapters.CommentAdapter;
import com.example.android.notesapp.Classes.comments;
import com.example.android.notesapp.Classes.upload;
import com.example.android.notesapp.Constants.Constants;
import com.example.android.notesapp.Data.DataContract;
import com.example.android.notesapp.Data.DataDBHelper;
import com.example.android.notesapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class img_activity extends AppCompatActivity {

    ImageButton imageButton, like;
    private ProgressDialog mProgressDialog;
    private AsyncTask mMyTask;
    ImageButton addComment;
    ImageView im;
    String title1 = "";
    EditText editText;
    DatabaseReference mDatabaseReference;
    TextView tv, topic, desc;
    ListView l;
    upload this_item;
    final ArrayList<comments> Com = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_activity);
        l = (ListView) findViewById(R.id.comments);
        editText = (EditText) findViewById(R.id.toSearch);
        topic = (TextView) findViewById(R.id.topic);
        desc = (TextView) findViewById(R.id.desc);

        imageButton = (ImageButton) findViewById(R.id.btn);
        like = (ImageButton) findViewById(R.id.like);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("comments");
        addComment = (ImageButton) findViewById(R.id.add_comment);
         this_item = (upload) getIntent().getSerializableExtra("key");
        final CommentAdapter commentAdapter = new CommentAdapter(this,R.layout.comment_item,Com);
        l.setAdapter(commentAdapter);
        topic.setText(this_item.getTitle());
        desc.setText(this_item.getDescription());
        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comments comments_ = new comments(Constants.username,editText.getText().toString(),this_item.getId());
                String id = mDatabaseReference.push().getKey();
                editText.getText().clear();
                mDatabaseReference.child(id).setValue(comments_);
//                Com.add(comments_);
//                commentAdapter.notifyDataSetChanged();
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
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference UpdateReference = FirebaseDatabase.getInstance().getReference("entries").child(this_item.getId());
                upload upload_ = new upload(this_item.getId(), this_item.getTitle(), this_item.getDescription(), this_item.getUrl(),this_item.getType(),this_item.getLikes()+1,this_item.getDate());
                UpdateReference.setValue(upload_);
                Toast.makeText(getApplicationContext(),"Support Noted!!", Toast.LENGTH_SHORT).show();
            }
        });
        title1 = this_item.title;
        Glide.with(imageButton)
                .load(this_item.getUrl())
                .into(imageButton);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Details");
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        tv = (TextView) findViewById(R.id.date_up);
        tv.setText("Posted On " + this_item.date);
        mProgressDialog.setTitle("Downloading");
        mProgressDialog.setMessage("Please wait, we are downloading your image file...");
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                                mMyTask = new DownloadTask()
                        .execute(stringToURL(this_item.getUrl()));
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
            return true;
        case R.id.share:
            shareLink();
            return(true);

    }
        return(super.onOptionsItemSelected(item));
    }
    private class DownloadTask extends AsyncTask<URL,Void,Bitmap> {
        protected void onPreExecute(){
            mProgressDialog.show();
        }

        protected Bitmap doInBackground(URL...urls){
            URL url = urls[0];
            HttpURLConnection connection = null;

            try{
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);
                return bmp;

            }catch(IOException e){
                e.printStackTrace();
            }finally{
                // Disconnect the http url connection
                connection.disconnect();
            }
            return null;
        }
        protected void onPostExecute(Bitmap result){
            mProgressDialog.dismiss();

            if(result!=null){
                Uri imageInternalUri = saveImageToInternalStorage(result);
                DataDBHelper dataDBHelper = new DataDBHelper(getApplicationContext());
                SQLiteDatabase sqLiteDatabase = dataDBHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(DataContract.DataEntry.COLUMN_ENTITY_URL, imageInternalUri.toString());
                contentValues.put(DataContract.DataEntry.COLUMN_TOPIC_NAME, this_item.getTitle());
                long generated_ID = sqLiteDatabase.insert(DataContract.DataEntry.TABLE_NAME, null, contentValues);
                Intent intent = new Intent(img_activity.this,display_image.class);
                intent.putExtra("key",imageInternalUri.toString());
                startActivity(intent);
            }else {
            }
        }
    }
    protected URL stringToURL(String urlString){
        try{
            URL url = new URL(urlString);
            return url;
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        return null;
    }
    protected Uri saveImageToInternalStorage(Bitmap bitmap){
        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
        File file = wrapper.getDir("Images",MODE_PRIVATE);
        file = new File(file, title1+System.currentTimeMillis()+".jpg");

        try{
            OutputStream stream = null;
            stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            stream.flush();
            stream.close();

        }catch (IOException e) // Catch the exception
        {
            e.printStackTrace();
        }

        Uri savedImageURI = Uri.parse(file.getAbsolutePath());
        return savedImageURI;

    }
    private void displayAlert(ArrayList<comments> d){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(img_activity.this);
        LayoutInflater inflater = LayoutInflater.from(img_activity.this);
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
