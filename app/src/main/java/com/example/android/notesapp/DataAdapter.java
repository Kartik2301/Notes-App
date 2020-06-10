package com.example.android.notesapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android.notesapp.R;
import com.example.android.notesapp.data_item;

import java.util.List;
public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder> {
    private List<data_item> moviesList;
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, year, genre;
        ImageView imageView;
        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.tvName);
            imageView = view.findViewById(R.id.head);
        }
    }
    public DataAdapter(List<data_item> moviesList) {
        this.moviesList = moviesList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        data_item movie = moviesList.get(position);
        holder.title.setText(movie.getString());
        Glide.with(holder.imageView.getContext())
                .load(movie.getImageId())
                .into(holder.imageView);
    }
    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}