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

public class ResultsAdapter extends  ArrayAdapter{
    ArrayList<upload> list = new ArrayList<>();

    public ResultsAdapter(Context context, int textViewResourceId, ArrayList<upload> objects) {
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
        v = inflater.inflate(R.layout.item_results, null);
        TextView textView = (TextView) v.findViewById(R.id.textView);
        TextView textView1 = (TextView) v.findViewById(R.id.helps);
        ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
        textView.setText(list.get(position).getTitle());
        textView1.setText(Integer.toString(list.get(position).getLikes()));
        Glide.with(imageView)
                .load(list.get(position).getUrl())
                .into(imageView);
        return v;

    }
}
