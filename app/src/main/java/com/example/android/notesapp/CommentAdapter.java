package com.example.android.notesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends  ArrayAdapter{
    ArrayList<comments> list = new ArrayList<>();

    public CommentAdapter(Context context, int textViewResourceId, ArrayList<comments> objects) {
        super(context, textViewResourceId, objects);
        list = objects;
    }
    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.comment_item, null);
        TextView textView = (TextView) v.findViewById(R.id.author);
        TextView textView1 = (TextView) v.findViewById(R.id.content1);
        textView.setText(list.get(position).getAuthor());
        textView1.setText(list.get(position).getContent());
        return v;

    }
}
