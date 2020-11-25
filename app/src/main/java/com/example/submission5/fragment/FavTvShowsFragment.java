package com.example.submission5.fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.submission5.R;
import com.example.submission5.activity.DetailActivity;
import com.example.submission5.adapter.FavTvShowsAdapter;
import com.example.submission5.db.TvShowsHelper;
import com.example.submission5.model.tv.TvItem;

import java.lang.ref.WeakReference;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavTvShowsFragment extends Fragment implements LoadTvShowsCallback {
    private ProgressBar pgFavTv;
    private TvShowsHelper tvHelper;
    private FavTvShowsAdapter favoriteTvAdapter;
    private static final String EXTRA_STATE = "EXTRA_STATE";

    public FavTvShowsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return getView() != null ? getView() :
                inflater.inflate(R.layout.fragment_favorite_tv, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvFavTv = view.findViewById(R.id.rv_fav_tv);
        rvFavTv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFavTv.setHasFixedSize(true);
        pgFavTv = view.findViewById(R.id.pg_fav_tv);

        tvHelper = TvShowsHelper.getInstance(getActivity());
        try {
            tvHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        favoriteTvAdapter = new FavTvShowsAdapter(getActivity());
        rvFavTv.setAdapter(favoriteTvAdapter);

        favoriteTvAdapter.setOnItemClickCallback(new FavTvShowsAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(TvItem data) {
                showSelectedTv(data);
            }
        });

        if (savedInstanceState == null) {
            Log.d("favoritetv", "onViewCreated: saved instance kosong");
            new LoadTvAsync(tvHelper, this).execute();
        } else {
            ArrayList<TvItem> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                favoriteTvAdapter.setListTv(list);
            }
            Log.d("favoritetv", "onViewCreated: saved instance ada : " + list);
        }
    }

    private void showSelectedTv(TvItem data) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(DetailActivity.KEY_DETAIL_DATA, data);
        intent.putExtra(DetailActivity.KEY_JENIS_DATA, "tv");
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, favoriteTvAdapter.getListTv());
        Log.d("favoritetv", "onSaveInstanceState: " + favoriteTvAdapter.getListTv());
    }

    @Override
    public void preExecute() {
        new Runnable() {
            @Override
            public void run() {
                pgFavTv.setVisibility(View.VISIBLE);
            }
        };
        Log.d("favoritetv", "preExecute: masuk");
    }

    @Override
    public void postExecute(ArrayList<TvItem> tvItems) {
        pgFavTv.setVisibility(View.INVISIBLE);
        favoriteTvAdapter.setListTv(tvItems);
        Log.d("favoritetv", "postExecute: " + tvItems.toString());
    }

    private class LoadTvAsync extends AsyncTask<Void, Void, ArrayList<TvItem>> {
        private final WeakReference<TvShowsHelper> tvHelperWeakReference;
        private final WeakReference<LoadTvShowsCallback> tvCallbackWeakReference;

        LoadTvAsync(TvShowsHelper tvShowsHelper, LoadTvShowsCallback loadTvShowsCallback) {
            tvHelperWeakReference = new WeakReference<>(tvShowsHelper);
            tvCallbackWeakReference = new WeakReference<>(loadTvShowsCallback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvCallbackWeakReference.get().preExecute();
        }

        @Override
        protected ArrayList<TvItem> doInBackground(Void... voids) {
            return tvHelperWeakReference.get().getAllTv();
        }

        @Override
        protected void onPostExecute(ArrayList<TvItem> tvItems) {
            super.onPostExecute(tvItems);

            tvCallbackWeakReference.get().postExecute(tvItems);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tvHelper.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        tvHelper = TvShowsHelper.getInstance(getActivity());
        try {
            tvHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Log.d("favoritetv", "onViewCreated: saved instance kosong");
        new LoadTvAsync(tvHelper, this).execute();
    }
}
