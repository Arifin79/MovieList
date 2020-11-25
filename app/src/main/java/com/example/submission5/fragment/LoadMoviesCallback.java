package com.example.submission5.fragment;

import com.example.submission5.model.movie.MoviesItem;

import java.util.ArrayList;

public interface LoadMoviesCallback {
    void preExecute();

    void postExecute(ArrayList<MoviesItem> movieItems);
}
