package com.example.android.notesapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.android.notesapp.Classes.upload;
import com.example.android.notesapp.Classes.user;
import com.example.android.notesapp.R;

import java.util.ArrayList;
import java.util.List;

public class LeaderAdapter extends  ArrayAdapter{
    ArrayList<user> list = new ArrayList<>();

    public LeaderAdapter(Context context, int textViewResourceId, ArrayList<user> objects) {
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
        v = inflater.inflate(R.layout.user_item, null);
        TextView textView = (TextView) v.findViewById(R.id.textView);
        TextView textView1 = (TextView) v.findViewById(R.id.ser);
        textView1.setText(Integer.toString(position+1));
        TextView textView2 = (TextView) v.findViewById(R.id.textView2);
        textView2.setText("Points: " + Integer.toString(list.get(position).getPoints()));
        ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
        textView.setText(list.get(position).getUsername());
        Glide.with(imageView)
                .load(list.get(position).getImageUrl())
                .into(imageView);
        return v;

    }
}
