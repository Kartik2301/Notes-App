package com.example.android.notesapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android.notesapp.Classes.data_item;
import com.example.android.notesapp.Classes.upload;
import com.example.android.notesapp.R;

import java.util.List;
public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder> {
    private List<upload> moviesList;
    private onDataElementClickListener onDataElementClickListener_;


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title, year, genre;
        ImageView imageView;

        onDataElementClickListener onDataElementClickListener;
        MyViewHolder(View view, onDataElementClickListener onDataElementClickListener1) {
            super(view);
            title = view.findViewById(R.id.tvName);
            imageView = view.findViewById(R.id.head);
            view.setOnClickListener(this);
            onDataElementClickListener = onDataElementClickListener1;
        }

        @Override
        public void onClick(View view) {
            onDataElementClickListener.onDataItemClick(getAdapterPosition());
        }
    }


    public interface onDataElementClickListener {
        void onDataItemClick(int position);
    }
    public DataAdapter(List<upload> moviesList, onDataElementClickListener onDataElementClickListener12) {
        onDataElementClickListener_ = onDataElementClickListener12;
        this.moviesList = moviesList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new MyViewHolder(itemView, onDataElementClickListener_);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        upload movie = moviesList.get(position);
        holder.title.setText(movie.getTitle());
        Glide.with(holder.imageView.getContext())
                .load(movie.getUrl())
                .into(holder.imageView);
    }
    @Override
    public int getItemCount() {
        return moviesList.size();
    }

}