package com.example.submission5.fragment;

import com.example.submission5.model.tv.TvItem;

import java.util.ArrayList;

public interface LoadTvShowsCallback {
    void preExecute();

    void postExecute(ArrayList<TvItem> tvItems);
}
