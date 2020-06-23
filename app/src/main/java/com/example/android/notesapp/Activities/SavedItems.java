package com.example.android.notesapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.notesapp.Adapters.SavedItemsAdapter;
import com.example.android.notesapp.Classes.saved_items;
import com.example.android.notesapp.Data.DataContract;
import com.example.android.notesapp.Data.DataDBHelper;
import com.example.android.notesapp.R;

import java.util.ArrayList;

public class SavedItems extends AppCompatActivity {
    ListView listView;
    SQLiteDatabase sqLiteDatabase;
    DataDBHelper dataDBHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_items);
        listView = (ListView) findViewById(R.id.items_lv);
        dataDBHelper = new DataDBHelper(getApplicationContext());
        sqLiteDatabase = dataDBHelper.getReadableDatabase();
        String [] projection = {
                BaseColumns._ID,
                DataContract.DataEntry.COLUMN_ENTITY_URL,
                DataContract.DataEntry.COLUMN_TOPIC_NAME
        };
        Cursor cursor = sqLiteDatabase.query(
                DataContract.DataEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        ArrayList<saved_items> list = new ArrayList<>();

        SavedItemsAdapter savedItemsAdapter = new SavedItemsAdapter(getApplicationContext(),R.layout.item_for_save,list);
        listView.setAdapter(savedItemsAdapter);
        while(cursor.moveToNext()) {
            String url = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.DataEntry.COLUMN_ENTITY_URL));
            String topic = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.DataEntry.COLUMN_TOPIC_NAME));
            list.add(new saved_items(url,topic));
        }
        savedItemsAdapter.notifyDataSetChanged();
        cursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.saved_items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dell :
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Confirm Deletion");
                builder.setMessage("All saved items will be deleted!!");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int rowsDeleted = sqLiteDatabase.delete(DataContract.DataEntry.TABLE_NAME, null,null);
                        Toast.makeText(getApplicationContext(),Integer.toString(rowsDeleted) + " items deleted", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                builder.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
