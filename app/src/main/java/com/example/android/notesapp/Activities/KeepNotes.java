package com.example.android.notesapp.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.android.notesapp.Adapters.ImageAdapter;
import com.example.android.notesapp.Classes.Note;
import com.example.android.notesapp.Data.DataContract;
import com.example.android.notesapp.Data.DataDBHelper;
import com.example.android.notesapp.Data.NoteContract;
import com.example.android.notesapp.Data.NoteDBHelper;
import com.example.android.notesapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class KeepNotes extends AppCompatActivity {
    EditText editText;
    FloatingActionButton floatingActionButton;
    NoteDBHelper noteDBHelper;
    SQLiteDatabase sqLiteDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keep_notes);
        GridView gridView = (GridView) findViewById(R.id.gridview);
        noteDBHelper = new NoteDBHelper(getApplicationContext());
        sqLiteDatabase = noteDBHelper.getWritableDatabase();
        editText = (EditText) findViewById(R.id.notem);
        final ArrayList<Note> list = new ArrayList<>();
        final ImageAdapter adapter = new ImageAdapter(this,R.layout.note_item,list);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String c = editText.getText().toString().trim();
                if(c.length() == 0) {
                    return;
                }
                ContentValues contentValues = new ContentValues();
                contentValues.put(NoteContract.NoteEntry.COLUMN_NOTE_CONTENT, c);
                long newNoteId = sqLiteDatabase.insert(NoteContract.NoteEntry.TABLE_NAME, null, contentValues);
                list.add(0, new Note(c,newNoteId));
                adapter.notifyDataSetChanged();
            }
        });
        String [] projection = {
                BaseColumns._ID,
                NoteContract.NoteEntry.COLUMN_NOTE_CONTENT,
        };

        Cursor cursor = sqLiteDatabase.query(
                NoteContract.NoteEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );


        while (cursor.moveToNext()) {
            Long ID = cursor.getLong(cursor.getColumnIndexOrThrow(NoteContract.NoteEntry._ID));
            String content = cursor.getString(cursor.getColumnIndexOrThrow(NoteContract.NoteEntry.COLUMN_NOTE_CONTENT));
            list.add(0,new Note(content, ID));
        }


        list.add(new Note("Add Notes",-5L));
        list.add(new Note("Easy management",-5L));
        list.add(new Note("Easy Insert",-5L));
        list.add(new Note("Easy UI",-5L));
        list.add(new Note("Delting notes simplified",-5L));
        list.add(new Note("Updates possible",-5L));
        list.add(new Note("Get Started",-5L));
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(list.get(i).getID() != -5L) {
                    final AlertDialog builder = new AlertDialog.Builder(KeepNotes.this).create();
                    LayoutInflater layoutInflater = LayoutInflater.from(KeepNotes.this);
                    View Iview = layoutInflater.inflate(R.layout.custom_for_notes, null);
                    final EditText editText = (EditText) Iview.findViewById(R.id.content);
                    ImageButton delete = (ImageButton) Iview.findViewById(R.id.delete);
                    ImageButton done = (ImageButton) Iview.findViewById(R.id.done);
                    final int position = i;
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String new_text = editText.getText().toString();
                            sqLiteDatabase = noteDBHelper.getWritableDatabase();
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(NoteContract.NoteEntry.COLUMN_NOTE_CONTENT, new_text);
                            String selection = selection = NoteContract.NoteEntry._ID + "=?";
                            String[] selectionArgs = {String.valueOf(list.get(position).getID())};
                            int count = sqLiteDatabase.update(
                                    NoteContract.NoteEntry.TABLE_NAME,
                                    contentValues,
                                    selection,
                                    selectionArgs
                            );
                            long reqID = list.get(position).getID();
                            list.set(position, new Note(new_text, reqID));
                            adapter.notifyDataSetChanged();
                            builder.dismiss();
                        }
                    });
                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String selection = NoteContract.NoteEntry._ID + "=?";
                            String[] selectionArgs = {String.valueOf(list.get(position).getID())};
                            int deletedRows = sqLiteDatabase.delete(NoteContract.NoteEntry.TABLE_NAME, selection, selectionArgs);
                            list.remove(position);
                            builder.dismiss();
                            adapter.notifyDataSetChanged();

                        }
                    });
                    editText.setText(list.get(i).getBody());
                    builder.setView(Iview);
                    builder.show();
                }
            }
        });
    }
}
