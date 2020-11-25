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

public class FavMoviesAdapter extends RecyclerView.Adapter<FavMoviesAdapter.Holder>{
    private ArrayList<MoviesItem> listData = new ArrayList<>();
    private Context context;
    private OnItemClickCallback onItemClickCallback;

    public ArrayList<MoviesItem> getListMovies() {
        return listData;
    }

    public FavMoviesAdapter(Context context) {
        this.context= context;
    }

    public void setListMovies(ArrayList<MoviesItem> listMovie) {

        if (listMovie.size() > 0) {
            this.listData.clear();
        }
        this.listData.addAll(listMovie);

        notifyDataSetChanged();
    }

    public void addItem(MoviesItem note) {
        this.listData.add(note);
        notifyItemInserted(listData.size() - 1);
    }

    public void updateItem(int position, MoviesItem note) {
        this.listData.set(position, note);
        notifyItemChanged(position, note);
    }

    public void removeItem(int position) {
        this.listData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,listData.size());
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_fav_movie, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int i) {
        holder.titleFavMovie.setText(listData.get(i).getTitle());
        holder.yearFavMovie.setText(listData.get(i).getReleaseDate());

        String baseUrlImage = "https://image.tmdb.org/t/p/original";
        Glide.with(context).load(baseUrlImage + listData.get(i).getPosterPath())
                .into(holder.posterFavMovie);

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

    class Holder extends RecyclerView.ViewHolder {
        TextView titleFavMovie, yearFavMovie;
        ImageView posterFavMovie;

        Holder(@NonNull View itemView) {
            super(itemView);

            titleFavMovie = itemView.findViewById(R.id.title_fav_movie);
            yearFavMovie = itemView.findViewById(R.id.year_fav_movie);
            posterFavMovie = itemView.findViewById(R.id.poster_fav_movie);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(MoviesItem data);
    }
}
