package com.example.android.notesapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.android.notesapp.Classes.Note;
import com.example.android.notesapp.Classes.upload;
import com.example.android.notesapp.R;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends  ArrayAdapter{
    ArrayList<Note> list = new ArrayList<>();
    private Context mContext;

    public ImageAdapter(Context context, int textViewResourceId, ArrayList<Note> objects) {
        super(context, textViewResourceId, objects);
        list = objects;
        mContext = context;
    }
    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.note_item, null);
        TextView textView = (TextView) v.findViewById(R.id.SingleView);
        textView.setText(list.get(position).getBody());
        return v;
    }
}
