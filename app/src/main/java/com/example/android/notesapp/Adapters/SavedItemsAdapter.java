package com.example.android.notesapp.Adapters;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.android.notesapp.Activities.display_image;
import com.example.android.notesapp.Activities.display_pdf;
import com.example.android.notesapp.Classes.saved_items;
import com.example.android.notesapp.Classes.upload;
import com.example.android.notesapp.Data.DataContract;
import com.example.android.notesapp.Data.DataDBHelper;
import com.example.android.notesapp.R;

import java.util.ArrayList;
import java.util.List;

public class SavedItemsAdapter extends  ArrayAdapter{
    ArrayList<saved_items> list = new ArrayList<>();
    private int PDF_SELECTION_CODE = 99;
    public SavedItemsAdapter(Context context, int textViewResourceId, ArrayList<saved_items> objects) {
        super(context, textViewResourceId, objects);
        list = objects;
    }
    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.item_for_save, null);
        TextView textView = v.findViewById(R.id.top);
        TextView textView1 = v.findViewById(R.id.uri);
        final String target = list.get(position).getLocation().toString();
        textView1.setText(list.get(position).getLocation().substring(0,50) + "...");
        ImageButton imageButton = (ImageButton) v.findViewById(R.id.del_id);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataDBHelper dataDBHelper = new DataDBHelper(getContext());
                SQLiteDatabase sqLiteDatabase = dataDBHelper.getWritableDatabase();
                String selection = DataContract.DataEntry.COLUMN_ENTITY_URL + "=?";
                String[] selectionArgs = {target};
                int deletedRows = sqLiteDatabase.delete(DataContract.DataEntry.TABLE_NAME, selection, selectionArgs);
                list.remove(position);
            }
        });

        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!target.contains("firebase")) {
                    Intent intent = new Intent(getContext(), display_image.class);
                    intent.putExtra("key",target);
                    getContext().startActivity(intent);
                } else {
                    Intent intent = new Intent(getContext(), display_pdf.class);
                    getContext().startActivity(intent);
                }
            }
        });
        textView.setText(list.get(position).getTopic());

        return v;


    }

}
