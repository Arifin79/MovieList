package com.example.submission5.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.submission5.R;
import com.example.submission5.model.movie.MoviesItem;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.Holder> {
    private List<MoviesItem> listData = new ArrayList<>();
    private Context context;

    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public void setListData(List<MoviesItem> listData){
        this.listData.clear();
        this.listData.addAll(listData);
        notifyDataSetChanged();
    }

    public MoviesAdapter(Context context){
        this.context = context;
    }


    @NonNull
    @Override
    public MoviesAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MoviesAdapter.Holder holder, int i) {
        holder.titleMovie.setText(listData.get(i).getTitle());
        holder.yearMovie.setText(listData.get(i).getReleaseDate());

        String baseUrlImage = "https://image.tmdb.org/t/p/original";
        Glide.with(context).load(baseUrlImage + listData.get(i).getPosterPath())
                .into(holder.posterMovie);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listData.get(holder.getAdapterPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView titleMovie, yearMovie;
        ImageView posterMovie;

        public Holder(@NonNull View itemView) {
            super(itemView);

            titleMovie = itemView.findViewById(R.id.title_movie);
            yearMovie = itemView.findViewById(R.id.year_movie);
            posterMovie = itemView.findViewById(R.id.poster_movie);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(MoviesItem data);
    }
}
